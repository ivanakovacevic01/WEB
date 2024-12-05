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
import beans.Comment;
import beans.Customer;
import beans.Location;
import beans.RentACarObject;
import beans.Vehicle;
import dao.AddressDAO;
import dao.CommentDAO;
import dao.CustomerDAO;
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


@Path("/commentService")
public class CommentService {
	@Context
	ServletContext ctx;
	
	public CommentService() {
		
	}
	
	
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	@PostConstruct
	public void init() {
		// Ovaj objekat se instanciravise puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("commentDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("commentDAO", new CommentDAO(contextPath));
			ctx.setAttribute("customerDAO", new CustomerDAO(contextPath));
		}
	}
	
	
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> findAll() {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.findAll();
	}
	
	@GET
	@Path("/getByOrderId/{orderId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Comment getById(@PathParam("orderId") String orderId) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.findById(orderId);
	}
	
	@POST
	@Path("/ifReturned/{status}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Comment addComment(@PathParam("status") String status, Comment comment) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		if(comment.getOrderId().equals("") || comment.getText().equals("") || comment.getObject()==null || comment.getCustomer()==null || comment.getGrade()<1 || comment.getGrade()>5 || comment.getStatus().toString().equals("") || !status.equals("Returned"))
			return null;
			
		return dao.save(comment);
	}
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Comment edit(@PathParam("id") String id,Comment comment) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		
		//backend validation
		
		return dao.edit(id, comment);
	}

	@GET
	@Path("/getByCustomer/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> getByCustomer(@PathParam("username") String username) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		
		return dao.findByCustomer(username);
	}
	
	@GET
	@Path("/getApprovedCommentsByObjectId/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> getApprovedByObject(@PathParam("id") int id) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		
		return dao.findByObjectId(id);
	}

	
	@PUT
	@Path("/declingForBlockedUser/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void declineForBlockedUser(@PathParam("username") String username) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");	
		dao.declineComments(username);
	}
	
	@PUT
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> editComment(Comment comment) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");	
		return dao.getUpdatedApprovedComments(comment);
	}
	
	
}
