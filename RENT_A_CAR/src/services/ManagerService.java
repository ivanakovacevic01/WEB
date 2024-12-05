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
import beans.Manager;
import beans.RentACarObject;
import dao.AdminDAO;
import dao.CustomerDAO;
import dao.ManagerDAO;
import dao.RentACarObjectDAO;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;



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


@Path("/managerService")
public class ManagerService {
	@Context
	ServletContext ctx;
	
	public ManagerService() {
		
	}
	
	
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	@PostConstruct
	public void init() {
		// Ovaj objekat se instanciravise puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("managerDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("managerDAO", new ManagerDAO(contextPath));
		}
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Manager registrate(Manager user) {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		
		
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
	public Manager edit(@PathParam("username") String oldUsername,Manager user) {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		
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
	public Collection<Manager> findAll() {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		for(Manager u: dao.findAll())
			System.out.println(u.getFirstName());
		return dao.findAll();
	}
	
	@GET
	@Path("/userExists/{username}/{password}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean exists(@PathParam("username") String username,@PathParam("password") String password) {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		return dao.UserExists(username, password);
	}
	
	@PUT
	@Path("/logOutAll")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void logOut() {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		
	   dao.logOutAll();
	}
	
	
	
	@GET
	@Path("/getUserByUsername/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Manager getByUsername(@PathParam("username") String username) {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		return dao.getByUsername(username);
	}
	
	@GET
	@Path("/getByManager/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentACarObject getObjectByManager(@PathParam("username") String username) {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		return dao.getByManager(username);
	}
	
	@GET
	@Path("/getManagersWithoutRentObject")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Manager> getAllWithoutRentObject() {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		if(dao.getAllWithoutRentObject().isEmpty())
			return null;
		return dao.getAllWithoutRentObject();
	}
	
	@GET
	@Path("/getManagerByObjectsId/{objectId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Manager getManager(@PathParam("objectId") int id) {
		ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("managerDAO");
		return managerDao.getManagerByObjectId(id);
	}
	
	@PUT
	@Path("/block/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void block(@PathParam("username") String username) {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		dao.block(username);
	}
	
	@PUT
	@Path("/remove/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void remove(@PathParam("username") String username) {
		ManagerDAO dao = (ManagerDAO) ctx.getAttribute("managerDAO");
		dao.remove(username);
	}
		
}
