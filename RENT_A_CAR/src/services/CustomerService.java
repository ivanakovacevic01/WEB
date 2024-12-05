package services;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Customer;
import beans.Basket;
import beans.RentACarObject;
import beans.Vehicle;
import beans.Order.OrderStatus;
import dao.CustomerDAO;
import dao.ManagerDAO;
import dao.OrderDAO;
import dao.RentACarObjectDAO;
import dao.VehicleDAO;
import dao.AdminDAO;
import dao.BasketDAO;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Customer;
import dao.CustomerDAO;


@Path("/userService")
public class CustomerService {
	@Context
	ServletContext ctx;
	
	public CustomerService() {
		
	}
	
	
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	@PostConstruct
	public void init() {
		// Ovaj objekat se instanciravise puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("customerDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("customerDAO", new CustomerDAO(contextPath));
		}
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer registrate(Customer user) {
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		
		
		//backend validation
		if(user.getFirstName().equals("") || user.getLastName().equals("") || user.getUsername().equals("") || user.getPassword().equals("") || user.getGender().equals("") || user.getBirthDate()==null)
			return null;
		
		if(dao.findByUsername(user.getUsername())!=null || !Pattern.matches("[a-zA-Z]*", user.getFirstName()) || !Pattern.matches("[a-zA-Z]*", user.getLastName())) //validacija za datum, i treba li za password npr duzina ili sta god!!!!!!
			return null;
		return dao.save(user);
	}
	
	@PUT
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer edit(@PathParam("username") String oldUsername,Customer user) {
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		
		//backend validation
		if(user.getFirstName().equals("") || user.getLastName().equals("") || user.getUsername().equals("") || user.getGender().equals("") || user.getBirthDate()==null)
			return null;
		
		if(!Pattern.matches("[a-zA-Z]*", user.getFirstName()) || !Pattern.matches("[a-zA-Z]*", user.getLastName())) //validacija za datum, i treba li za password npr duzina ili sta god!!!!!!
			return null;
		return dao.edit(oldUsername, user);
	}
	
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Customer> findAll() {
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		for(Customer u: dao.findAll())
			System.out.println(u.getFirstName());
		return dao.findAll();
	}
	
	@GET
	@Path("/userExists/{username}/{password}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean exists(@PathParam("username") String username,@PathParam("password") String password) {
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		return dao.UserExists(username, password);
	}
	
	@GET
	@Path("/getUserByUsername/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getByUsername(@PathParam("username") String username) {
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		return dao.getByUsername(username);
	}
	@PUT
	@Path("/block/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void block(@PathParam("username") String username) {
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		dao.block(username);
	}
	@PUT
	@Path("/remove/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void remove(@PathParam("username") String username) {
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		dao.remove(username);
	}
	
	@PUT
	@Path("/logOutAll")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void logOut() {
		CustomerDAO dao = (CustomerDAO) ctx.getAttribute("customerDAO");
		
	   dao.logOutAll();
	}
}
