package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Administrator;
import beans.Customer;
import beans.User;
import dao.AdminDAO;
import dao.CustomerDAO;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


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


@Path("/adminService")
public class AdminService {
	@Context
	ServletContext ctx;
	
	public AdminService() {
		
	}
	
	
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	@PostConstruct
	public void init() {
		// Ovaj objekat se instanciravise puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("adminDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("adminDAO", new AdminDAO(contextPath));
		}
	}
	
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Administrator> findAll() {
		AdminDAO dao = (AdminDAO) ctx.getAttribute("adminDAO");
		for(Administrator u: dao.findAll())
			System.out.println(u.getFirstName());
		return dao.findAll();
	}
	
	
	@GET
	@Path("/userExists/{username}/{password}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean exists(@PathParam("username") String username,@PathParam("password") String password) {
		AdminDAO dao = (AdminDAO) ctx.getAttribute("adminDAO");
		return dao.UserExists(username, password);
	}
	
	@GET
	@Path("/getUserByUsername/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Administrator getByUsername(@PathParam("username") String username) {
		AdminDAO dao = (AdminDAO) ctx.getAttribute("adminDAO");
		return dao.getByUsername(username);
	}
	
	
	@PUT
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Administrator edit(@PathParam("username") String oldUsername,Administrator user) {
		AdminDAO dao = (AdminDAO) ctx.getAttribute("adminDAO");
		
		//backend validation
		if(user.getFirstName().equals("") || user.getLastName().equals("") || user.getUsername().equals("") || user.getGender().equals("") || user.getBirthDate()==null)
			return null;
		
		if(!Pattern.matches("[a-zA-Z]*", user.getFirstName()) || !Pattern.matches("[a-zA-Z]*", user.getLastName()))
			return null;
		return dao.edit(oldUsername, user);
	}
	

	@PUT
	@Path("/logOutAll")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void logOut() {
		AdminDAO dao = (AdminDAO) ctx.getAttribute("adminDAO");
		
	   dao.logOutAll();
	}
	
		
}
