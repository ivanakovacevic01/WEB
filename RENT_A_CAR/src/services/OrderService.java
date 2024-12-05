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
import beans.Basket;
import beans.Customer;
import beans.Location;
import beans.Manager;
import beans.Order;
import beans.RentACarObject;
import beans.Vehicle;
import beans.Order.OrderStatus;
import dao.VehicleDAO;
import dao.BasketDAO;
import dao.CustomerDAO;
import dao.ManagerDAO;
import dao.OrderDAO;
import dao.RentACarObjectDAO;
import dao.UserDAO;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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


@Path("/orderService")
public class OrderService {
	@Context
	ServletContext ctx;
	
	public OrderService() {
		
	}
	
	
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	@PostConstruct
	public void init() {
		// Ovaj objekat se instanciravise puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("orderDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("orderDAO", new OrderDAO(contextPath));
			ctx.setAttribute("objectDAO", new RentACarObjectDAO(contextPath));
			ctx.setAttribute("vehicleDAO", new VehicleDAO(contextPath));
			ctx.setAttribute("customerDAO", new CustomerDAO(contextPath));
			
		}
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Order> add(ArrayList<Order> orders) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		//VehicleDAO vehicleDao = (VehicleDAO) ctx.getAttribute("vehicleDAO");
		RentACarObjectDAO objectDao = (RentACarObjectDAO) ctx.getAttribute("objectDAO");
		for(Order order : orders)
		{
			if(order.getVehicles()==null || order.getStartDateTime().equals("") || order.getDuration()<0 || order.getPrice()<0 || order.getCustomer().equals(""))
				return null;
			order.setObject(objectDao.GetSelectedObject(order.getObjectId()));
			if(order.getObject()==null)
				return null;
			
		}
		
		return dao.save(orders);

	}
	
	@PUT
	@Path("/declineForBlockedUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Order> declineOrders(ArrayList<Order> orders) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		
		if(orders!=null)
		{
			dao.declineOrders(orders);
			return orders;
		}
		return null;
		
		

	}
	
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Order> findAll() {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.findAll();
	}
	@GET
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Order findById(@PathParam("id") String id) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.findById(id);
	}
	@GET
	@Path("/getOrdersByObjectId/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Order> findByObjectId(@PathParam("id") int id) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.findByObjectId(id);
	}
	@GET
	@Path("/getByUsername/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Order> findByCustomer(@PathParam("username") String username) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.findByCustomer(username);
	}
	
	@GET
	@Path("/getSuspiciousCustomers")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Customer> getSuspicious()
	{
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		CustomerDAO customerDao=(CustomerDAO) ctx.getAttribute("customerDAO");
		ArrayList<String> suspiciousUsernames = dao.getSuspicious();
		ArrayList<Customer> suspiciousCustomers = new ArrayList<Customer>();
		for(String suspiciousUsername : suspiciousUsernames)
		{
			Customer customer = customerDao.findByUsername(suspiciousUsername);
			if(customer!=null)
				suspiciousCustomers.add(customer);
		}
		
		return suspiciousCustomers;
		
	}
	@GET
	@Path("/getVehiclesByOrderId/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Vehicle> findVehicles(@PathParam("id") String id) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.findVehicles(id);	
	}
	
	@PUT
	@Path("/cancel/{id}/{status}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean cancelRental(@PathParam("id") String id, @PathParam("status") String status) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		if(status.equals(OrderStatus.Processing.toString()))
		{
			dao.cancel(id);
			return true;
		}
		return false;
	}
	
	@PUT
	@Path("/approve/{id}/{status}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean approveRental(@PathParam("id") String id, @PathParam("status") String status) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		if(status.equals(OrderStatus.Processing.toString()))
		{
			dao.approve(id);
			return true;
		}
		return false;
	}
	@PUT
	@Path("/decline/{id}/{status}/{reason}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean declineRental(@PathParam("id") String id, @PathParam("status") String status,@PathParam("reason") String reason) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		if(status.equals(OrderStatus.Processing.toString()))
		{
			dao.decline(id,reason);
			return true;
		}
		return false;
	}
	@GET
	@Path("/getOrdersByStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Order> findByStatus() {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.findByOrderStatus();	
	}
	@GET
	@Path("/getCustomer/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer findCustomer(@PathParam("id") String id) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		String customer=dao.findCustomer(id);
		String firstName=customer.split(" ")[0];
		CustomerDAO customerDao=(CustomerDAO) ctx.getAttribute("customerDAO");
		for(Customer c : customerDao.findAll()) {
			if(c.getFirstName().equals(firstName)) {
				return c;
			}
		}
		return null;
	}
	@PUT
	@Path("/receive/{id}/{status}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean receiveRental(@PathParam("id") String id, @PathParam("status") String status) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		if(status.equals(OrderStatus.Approved.toString()))
		{
			return dao.receive(id);
		}
		return false;
	}
	@PUT
	@Path("/return/{id}/{status}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean returnRental(@PathParam("id") String id, @PathParam("status") String status) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		if(status.equals(OrderStatus.Received.toString()))
		{
			dao.returnRental(id);
			return true;
		}
		return false;
	}
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Order edit(@PathParam("id") String id,Order order) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.edit(id, order);
	}
	
	
	
	@GET
	@Path("/getObject/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentACarObject findObject(@PathParam("id") String id) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		Order order=findById(id);
		return order.getObject();
	}
	@GET
	@Path("/getProcessingOrdersByUsername/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Order> getOrders(@PathParam("username") String username) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		CustomerDAO customerDao = (CustomerDAO) ctx.getAttribute("customerDAO");
		Customer customer=customerDao.findByUsername(username);
		return dao.getOrders(username);
	}
	@PUT
	@Path("/editOrders")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Order> editOrders(ArrayList<Order> orders) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDAO");
		return dao.editOrders(orders);
	}

}
