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
import beans.UserType;
import dao.AdminDAO;
import dao.CustomerDAO;
import dao.UserTypeDAO;

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


@Path("/typeService")
public class UserTypeService {
	@Context
	ServletContext ctx;
	
	public UserTypeService() {
		
	}
	
	
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	@PostConstruct
	public void init() {
		// Ovaj objekat se instanciravise puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("typeDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("typeDAO", new UserTypeDAO(contextPath));
		}
	}
	
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<UserType> findAll() {
		UserTypeDAO dao = (UserTypeDAO) ctx.getAttribute("typeDAO");
		return dao.findAll();
	}
	@GET
	@Path("/getBronze")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserType findBronze() {
		UserTypeDAO dao = (UserTypeDAO) ctx.getAttribute("typeDAO");
		return dao.findBronze();
	}
	
}
