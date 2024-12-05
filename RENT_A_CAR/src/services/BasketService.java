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

import beans.Address;
import beans.Administrator;
import beans.Basket;
import beans.Customer;
import beans.Location;
import beans.Manager;
import beans.RentACarObject;
import beans.Vehicle;
import dao.VehicleDAO;
import dao.BasketDAO;
import dao.CustomerDAO;
import dao.ManagerDAO;
import dao.RentACarObjectDAO;
import dao.UserDAO;

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


@Path("/basketService")
public class BasketService {
	@Context
	ServletContext ctx;
	
	public BasketService() {
		
	}
	
	
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	@PostConstruct
	public void init() {
		// Ovaj objekat se instanciravise puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("basketDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("basketDAO", new BasketDAO(contextPath));
			//ctx.setAttribute("vehicleDAO", new VehicleDAO(contextPath));
		}
	}
	
	@POST
	@Path("/addBasket")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Basket add(Basket basket) {
		BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
		VehicleDAO vehicleDao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		return dao.save(basket);
		//return null;
	}
	@GET
	@Path("/getVehicleByBasket/{basketId}/{vehicleId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vehicle getVehicleByBasketId(@PathParam("basketId") int basketId, @PathParam("vehicleId") int vehicleId) {
		BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
		return dao.getVehicleByBasket(basketId, vehicleId);
	}
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Basket> findAll() {
		BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
		return dao.findAll();
	}
	@GET
	@Path("/usernameExists/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean usernameExists(@PathParam("username") String username) {
		BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
		return dao.UserExists(username);
	}
	@GET
	@Path("/getByUser/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Basket getByUser(@PathParam("username") String username) {
		BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
		CustomerDAO customerDao=(CustomerDAO) ctx.getAttribute("customerDAO");
		Customer customer=customerDao.findByUsername(username);
		return dao.getByUser(customer);
	}
	@GET
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Basket findById(@PathParam("id") int id) {
		BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
		return dao.findById(id);
	}

	@PUT
	@Path("/edit/{basketId}/{id}/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Basket editBasket(@PathParam("basketId") int basketId,@PathParam("id") int id,@PathParam("username") String username) {
		//RentACarObjectDAO dao = (RentACarObjectDAO) ctx.getAttribute("objectDAO");
		VehicleDAO vehicleDao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
	//	ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("managerDAO");
		Vehicle addedVehicle = vehicleDao.getById(id);
		//RentACarObject objectForEdit = dao.getById(objectId);
		BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
		Basket basket=dao.findById(basketId);
		CustomerDAO customerDao=(CustomerDAO) ctx.getAttribute("customerDAO");
		Customer customer=customerDao.findByUsername(username);
		Basket validBasket=null;
		if(basket.getVehicles()==null)
		{
			ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
			vehicles.add(addedVehicle);
			basket.setVehicles(vehicles);
			basket.setPrice(addedVehicle.getPrice());
			basket.setUsername(username);
			validBasket=dao.edit(basket);
		}
		else
		{
			int quantity=0;
			ArrayList<Vehicle> vehicles = basket.getVehicles();
			for(Vehicle v:vehicles) {
				if(v.getId()==addedVehicle.getId()) {
					quantity=v.getQuantity();
				}
			}
			for(Vehicle v:vehicles) {
				if(v.getId()==addedVehicle.getId()) {
					v.setQuantity(quantity+1);
				}
			}
			addedVehicle.setQuantity(quantity+1);
			if(quantity==0)
				vehicles.add(addedVehicle);
			basket.setVehicles(vehicles);
			basket.setPrice(basket.getPrice()+addedVehicle.getPrice());
			basket.setUsername(username);
			validBasket=dao.edit(basket);
		}
		return validBasket;
	}
	
	@PUT
	@Path("/removeVehicle/{basketId}/{id}/{username}/{quantity}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Basket removeVehicleFromBasket(@PathParam("basketId") int basketId,@PathParam("id") int id,@PathParam("username") String username, @PathParam("quantity") int quantity) {
		VehicleDAO vehicleDao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		Vehicle removingVehicle = vehicleDao.getById(id);
		BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
		Basket basket=dao.findById(basketId);
		Basket validBasket=null;
		
		//removing vehicle from basket; remove method (for arraylist) not working so using this way
		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		for(Vehicle vehicle : basket.getVehicles())
		{
			if(vehicle.getId()!=id)
				vehicles.add(vehicle);
		}
		
		basket.setVehicles(vehicles);
		double totalPrice = 0;
		for(Vehicle vehicle : basket.getVehicles())
		{
			totalPrice += (vehicle.getPrice()*vehicle.getQuantity());
		}
		basket.setPrice(totalPrice);
		validBasket=dao.edit(basket);
		
		return validBasket;
	}
	
	@PUT
	@Path("/changeQuantity/{basketId}/{id}/{username}/{quantity}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Basket changeVehiclesQnautityInBasket(@PathParam("basketId") int basketId,@PathParam("id") int id,@PathParam("username") String username, @PathParam("quantity") int quantity) {
		VehicleDAO vehicleDao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		Vehicle removingVehicle = vehicleDao.getById(id);
		BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
		Basket basket=dao.findById(basketId);
		Basket validBasket=null;
		
		//backend validation for quantity (>=1)
		if(quantity<1)
			return null;
		
		//edit quantity
		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>(basket.getVehicles());
		for(Vehicle vehicle : vehicles)
		{
			if(vehicle.getId()==id) {
				vehicle.setQuantity(quantity);
				break;
			}
				
		}
		
		basket.setVehicles(vehicles);
		double totalPrice = 0;
		for(Vehicle vehicle : basket.getVehicles())
		{
			totalPrice += (vehicle.getPrice()*vehicle.getQuantity());
		}
		basket.setPrice(totalPrice);
		validBasket=dao.edit(basket);
		
		return validBasket;
	}
	
	@DELETE
	@Path("/{basketId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteBasket(@PathParam("basketId") int basketId)
	{
		BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
		dao.delete(basketId);
	}
	@PUT
	@Path("/editBasket")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Basket edit(Basket basket) {
		BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
		
		
		return dao.edit( basket);
	}
	

}
