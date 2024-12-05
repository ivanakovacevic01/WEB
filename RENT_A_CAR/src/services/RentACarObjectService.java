package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Address;
import beans.Administrator;
import beans.Customer;
import beans.Location;
import beans.Manager;
import beans.RentACarObject;
import beans.RentACarObject.State;
import beans.Vehicle;
import dao.AddressDAO;
import dao.AdminDAO;
import dao.CustomerDAO;
import dao.LocationDAO;
import dao.ManagerDAO;
import dao.RentACarObjectDAO;
import dao.VehicleDAO;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/objectsService")
public class RentACarObjectService {
	@Context
	ServletContext ctx;
	
	public RentACarObjectService() {
		
	}
	
	
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	@PostConstruct
	public void init() {
		// Ovaj objekat se instanciravise puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("objectDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("objectDAO", new RentACarObjectDAO(contextPath));
			ctx.setAttribute("locationDAO", new LocationDAO(contextPath));
			ctx.setAttribute("addressDAO", new AddressDAO(contextPath));
			ctx.setAttribute("managerDAO", new ManagerDAO(contextPath));
			ctx.setAttribute("vehicleDAO", new VehicleDAO(contextPath));
		}
	}
	
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<RentACarObject> findAll() {
		RentACarObjectDAO dao = (RentACarObjectDAO) ctx.getAttribute("objectDAO");
		return dao.findAll();
	}
	
	
	@GET
	@Path("/getObject/{objectId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentACarObject GetSelectedObject(@PathParam("objectId") int id) {
		RentACarObjectDAO dao = (RentACarObjectDAO) ctx.getAttribute("objectDAO");
		return dao.GetSelectedObject(id);
	}
	

	
	@PUT
	@Path("/addVehicle/{objectId}/{vehicleId}/{managerUsername}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentACarObject addNewVehicle(@PathParam("objectId") int objectId, @PathParam("vehicleId") int vehicleId, @PathParam("managerUsername") String managerUsername) {
		RentACarObjectDAO dao = (RentACarObjectDAO) ctx.getAttribute("objectDAO");
		VehicleDAO vehicleDao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("managerDAO");
		Vehicle addedVehicle = vehicleDao.getById(vehicleId);
		RentACarObject objectForEdit = dao.getById(objectId);
		if(objectForEdit.getVehicles()==null)
		{
			ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
			vehicles.add(addedVehicle);
			objectForEdit.setVehicles(vehicles);
		}
		else
		{
			ArrayList<Vehicle> vehicles = objectForEdit.getVehicles();
			vehicles.add(addedVehicle);
			objectForEdit.setVehicles(vehicles);
		}
		
		RentACarObject validObject = dao.edit(objectForEdit);
		Manager managerToEdit = managerDao.getByUsername(managerUsername);
		managerToEdit.setObject(validObject);
		managerDao.edit(managerUsername, managerToEdit);
		return validObject;
	}
	@PUT
	@Path("/removeVehicle/{objectId}/{vehicleId}/{managerUsername}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentACarObject removeVehicleFromObjects(@PathParam("objectId") int objectId, @PathParam("vehicleId") int vehicleId, @PathParam("managerUsername") String managerUsername) {
		RentACarObjectDAO dao = (RentACarObjectDAO) ctx.getAttribute("objectDAO");
		VehicleDAO vehicleDao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("managerDAO");
		
		RentACarObject objectForEdit = dao.getById(objectId);
		ArrayList<Vehicle> vehicles = objectForEdit.getVehicles();
		for(Vehicle vehicle : vehicles)
		{	
			if(vehicle.getId()==vehicleId)
			{
				vehicle.setRemoved(true);
				break;
			}			
		}
		objectForEdit.setVehicles(vehicles);		
		RentACarObject validObject = dao.edit(objectForEdit);	//with removed vehicle
		Manager managerToEdit = managerDao.getByUsername(managerUsername);
		managerToEdit.setObject(validObject);
		managerDao.edit(managerUsername, managerToEdit);
		return validObject;
	}
	@PUT
	@Path("/editVehicle/{objectId}/{vehicleId}/{managerUsername}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentACarObject editVehicle(@PathParam("objectId") int objectId, @PathParam("vehicleId") int vehicleId, @PathParam("managerUsername") String managerUsername,Vehicle vehicle) {
		RentACarObjectDAO dao = (RentACarObjectDAO) ctx.getAttribute("objectDAO");
		VehicleDAO vehicleDao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("managerDAO");
		Vehicle editedVehicle = vehicleDao.getById(vehicleId);
		RentACarObject objectForEdit = dao.getById(objectId);
		ArrayList<Vehicle> vehicles = objectForEdit.getVehicles();
		
		for(Vehicle v : vehicles)
		{	
			if(v.getId()==vehicleId)
			{
				v.setConsumption(vehicle.getConsumption());
				v.setBrand(vehicle.getBrand());
				v.setDescription(vehicle.getDescription());
				v.setDoorsNumber(vehicle.getDoorsNumber());
				v.setFuelType(vehicle.getFuelType());
				v.setImage(vehicle.getImage());
				v.setKind(vehicle.getKind());
				v.setModel(vehicle.getModel());
				v.setPersonNumber(vehicle.getPersonNumber());
				v.setPrice(vehicle.getPrice());
				v.setType(vehicle.getType());
				break;
			}			
		}
		objectForEdit.setVehicles(vehicles);
		
		RentACarObject validObject = dao.edit(objectForEdit);
		Manager managerToEdit = managerDao.getByUsername(managerUsername);
		managerToEdit.setObject(validObject);
		managerDao.edit(managerUsername, managerToEdit);
		return validObject;
	}
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentACarObject create(Manager manager) {
		RentACarObjectDAO dao = (RentACarObjectDAO) ctx.getAttribute("objectDAO");
		AddressDAO addressDao = (AddressDAO) ctx.getAttribute("addressDAO");
		LocationDAO locationDao = (LocationDAO) ctx.getAttribute("locationDAO");
		ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("managerDAO");
		
		//backend validation
		if(!isManagerValid(manager) || !isObjectValid(manager.getObject()))
			return null;
		
		//location - to has address which has valid id
		Address validAddress = addressDao.save(manager.getObject().getLocation().getAddress());
		Location validLocation = manager.getObject().getLocation();
		validLocation.setAddress(validAddress);
		validLocation = locationDao.save(validLocation);
		RentACarObject validObject = manager.getObject();
		validObject.setLocation(validLocation);
		
		//set state to opened or closed depend.on datetime.now and start-end time
				LocalTime currentTime = LocalTime.now(); 
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		        LocalTime startTime = LocalTime.parse(validObject.getStartTime(), formatter);
		        LocalTime endTime = LocalTime.parse(validObject.getEndTime(), formatter);
		        if(currentTime.isAfter(startTime) && currentTime.isBefore(endTime))	///////////provjeriti treba li ovo cuvati uopste
		        	validObject.setState(State.Opened);
		        else
		        	validObject.setState(State.Closed);
		        
		validObject = dao.save(validObject);
		        
		manager.setObject(validObject);
		
		//if manager is made right now
		if(managerDao.findByUsername(manager.getUsername())==null)
			managerDao.save(manager);
		else
			managerDao.edit(manager.getUsername(), manager);
			
		
		return validObject;
	}
	private boolean isManagerValid(Manager manager)
	{
		return manager.getBirthDate()!="" && manager.getFirstName()!="" && manager.getLastName()!="" && manager.getGender()!="" && manager.getPassword()!="" && manager.getUsername()!="" && manager.getObject()!=null && Pattern.matches("[a-zA-Z]*", manager.getFirstName()) && Pattern.matches("[a-zA-Z]*", manager.getLastName());
	} 
	private boolean isAddressValid(Address address)
	{
		return address!=null && address.getCountry()!="" && address.getCity()!="" && address.getZipCode()!="" && Pattern.matches("[0-9]{5,5}", address.getZipCode()) && address.getStreet()!="" && address.getNumber()!="";
	}
	private boolean isLocationValid(Location location)
	{
		return location!=null && isAddressValid(location.getAddress()) && location.getLongitude()>=0 && location.getLatitude()>=0 && location.getImage()!="";
	}
	private boolean isObjectValid(RentACarObject object)
	{
		return isLocationValid(object.getLocation()) && object!=null && object.getName()!="" && object.getStartTime()!="" && object.getEndTime()!="" && object.getLogo()!="";
	}
	@PUT
	@Path("/{objectId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentACarObject edit(@PathParam("objectId") int objectId,RentACarObject object) {
		RentACarObjectDAO dao = (RentACarObjectDAO) ctx.getAttribute("objectDAO");
		return dao.edit(object);
	}
	
}
