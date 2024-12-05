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
import beans.Location;
import beans.RentACarObject;
import dao.AddressDAO;

import dao.RentACarObjectDAO;

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


@Path("/addressService")
public class AddressService {
	@Context
	ServletContext ctx;
	
	public AddressService() {
		
	}
	
	
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	@PostConstruct
	public void init() {
		// Ovaj objekat se instanciravise puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("addressDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("addressDAO", new AddressDAO(contextPath));
		}
	}
	
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Address> findAll() {
		AddressDAO dao = (AddressDAO) ctx.getAttribute("addressDAO");
		return dao.findAll();
	}

		
}
