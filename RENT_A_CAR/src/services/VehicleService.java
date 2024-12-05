package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Administrator;
import beans.Customer;
import beans.Order;
import beans.Vehicle;
import beans.RentACarObject;
import dao.AdminDAO;
import dao.CustomerDAO;
import dao.OrderDAO;
import dao.RentACarObjectDAO;
import dao.VehicleDAO;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
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


@Path("/vehicleService")
public class VehicleService {
	@Context
	ServletContext ctx;
	
	public VehicleService() {
		
	}
	
	
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	@PostConstruct
	public void init() {
		// Ovaj objekat se instanciravise puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("vehicleDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("vehicleDAO", new VehicleDAO(contextPath));
		}
	}
	
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Vehicle> findAll() {
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.findAll();
	}
	
	/*@GET
	@Path("/showVehicles/{startDate}/{endDate}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Vehicle> findAllByInputParameters(@PathParam("startDate") String startDate,@PathParam("endDate") String endDate) {
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.findAllByInputParameters(startDate,endDate);
	} */
	@GET
	@Path("/showVehicles/{start}/{end}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Vehicle> findAllByInputParameters(@PathParam("start") String start,@PathParam("end") String end) {
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		//OrderDAO orderDao=(OrderDAO) ctx.getAttribute("orderDAO");
		Collection<Vehicle> vehicles=dao.findAllByInputParameters(start, end);
		//Collection<Vehicle> reservedVehicles=orderDao.findAllVehicles();
		
		return vehicles;
	} 
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vehicle delete(@PathParam("id") int id) {
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.delete(id);
	}
	
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vehicle create(Vehicle vehicle) {
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		
		//backend validation
		if(vehicle.getBrand().equals("") || vehicle.getConsumption()<0 || vehicle.getDoorsNumber()<0 || vehicle.getFuelType().toString().equals("") || vehicle.getImage().equals("") || vehicle.getKind().toString().equals("") || vehicle.getModel().equals("") || vehicle.getPersonNumber()<0 || vehicle.getPrice()<0 || vehicle.getType().equals(""))
			return null;
		
		Vehicle validVehicle = dao.save(vehicle);
		
		return validVehicle;
	}
	@GET
	@Path("/getVehicle/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vehicle findById(@PathParam("id") int id) {
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.findById(id);
	}
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vehicle edit(@PathParam("id") int id,Vehicle vehicle) {
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		
		//backend validation
		if(vehicle.getBrand().equals("") || vehicle.getConsumption()<0 || vehicle.getDoorsNumber()<0 || vehicle.getFuelType().toString().equals("") || vehicle.getImage().equals("") || vehicle.getKind().toString().equals("") || vehicle.getModel().equals("") || vehicle.getPersonNumber()<0 || vehicle.getPrice()<0 || vehicle.getType().equals(""))
			return null;
		return dao.edit(id, vehicle);
	}
	@PUT
	@Path("/editVehicles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void editVehicles(ArrayList<Vehicle> vehicles) {
		//OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		//VehicleDAO vehicleDao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		VehicleDAO vehicleDao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		
		
		
		vehicleDao.editVehicles(vehicles);

	}
	@GET
	@Path("/getRemovedVehicles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Vehicle> findRemovedVehicles() {
		VehicleDAO dao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.findRemovedVehicles();
	}
	@PUT
	@Path("/newEditVehicles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Vehicle> editVehiclesList(ArrayList<Vehicle> vehicles) {
		//OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		//VehicleDAO vehicleDao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		VehicleDAO vehicleDao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		
		
		
		return vehicleDao.editVehiclesList(vehicles);

	}
}
