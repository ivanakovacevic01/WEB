package services;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Administrator;
import beans.Customer;
import beans.Manager;
import beans.User;
import dao.AdminDAO;
import dao.CustomerDAO;
import dao.ManagerDAO;
import dao.UserDAO;

@Path("/allUsersService")
public class UserService {
	@Context
	ServletContext ctx;
	
	public UserService() {
		
	}
	
	
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	@PostConstruct
	public void init() {
		// Ovaj objekat se instanciravise puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("userDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
			ctx.setAttribute("customerDAO", new CustomerDAO(contextPath));
			ctx.setAttribute("managerDAO", new ManagerDAO(contextPath));
			ctx.setAttribute("adminDAO", new AdminDAO(contextPath));
		}
	}
	
	@GET
	@Path("/isLoggedIn")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User isLogged() {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.isLogged();
	}
	
	
	@GET
	@Path("/userExists/{username}/{password}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean exists(@PathParam("username") String username,@PathParam("password") String password) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		CustomerDAO customerDao = (CustomerDAO) ctx.getAttribute("customerDAO");
		ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("managerDAO");
		AdminDAO adminDao = (AdminDAO) ctx.getAttribute("adminDAO");
		
		Collection<Customer> allCustomers = customerDao.findAll();
		Collection<Manager> allManagers = managerDao.findAll();
		Collection<Administrator> allAdmins = adminDao.findAll();
		
		
		
		
		if(allCustomers.stream().filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password) && !user.isRemoved()).findFirst().orElse(null)!=null)
			return true;
		if(allManagers.stream().filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password) && !user.isRemoved()).findFirst().orElse(null)!=null)
			return true;
		if(allAdmins.stream().filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password)).findFirst().orElse(null)!=null)
			return true;
		return false;
		
		
	}
	
	@GET
	@Path("/usernameExistsForEditing1/{username}/{oldUsername}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean usernameExistsForEditing(@PathParam("username") String username, @PathParam("oldUsername") String oldUsername) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		CustomerDAO customerDao = (CustomerDAO) ctx.getAttribute("customerDAO");
		ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("managerDAO");
		AdminDAO adminDao = (AdminDAO) ctx.getAttribute("adminDAO");
		
		Collection<Customer> allCustomers = customerDao.findAll();
		Collection<Manager> allManagers = managerDao.findAll();
		Collection<Administrator> allAdmins = adminDao.findAll();
		
		if(username.equals(oldUsername))
			return false;
		
		if(allCustomers.stream().filter(user -> user.getUsername().equals(username) && !user.isRemoved()).findFirst().orElse(null) != null)
			return true;
		if(allManagers.stream().filter(user -> user.getUsername().equals(username) && !user.isRemoved()).findFirst().orElse(null) != null)
			return true;
		if(allAdmins.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null) != null)
			return true;
		
		return false;
		
		
	}
	

	@GET
	@Path("/usernameExists1/{username}")	//for registration
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean usernameExists(@PathParam("username") String username) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		CustomerDAO customerDao = (CustomerDAO) ctx.getAttribute("customerDAO");
		ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("managerDAO");
		AdminDAO adminDao = (AdminDAO) ctx.getAttribute("adminDAO");
		
		Collection<Customer> allCustomers = customerDao.findAll();
		Collection<Manager> allManagers = managerDao.findAll();
		Collection<Administrator> allAdmins = adminDao.findAll();
		
		if(allCustomers.stream().filter(user -> user.getUsername().equals(username) && !user.isRemoved()).findFirst().orElse(null) != null)
			return true;
		if(allManagers.stream().filter(user -> user.getUsername().equals(username) && !user.isRemoved()).findFirst().orElse(null) != null)
			return true;
		if(allAdmins.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null) != null)
			return true;
		return false;

	}
	
	@GET
	@Path("/findUser/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String findRole(@PathParam("username") String username) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.findRole(username);
	}
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> findAllUsers() {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		CustomerDAO customerDao = (CustomerDAO) ctx.getAttribute("customerDAO");
		ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("managerDAO");
		AdminDAO adminDao = (AdminDAO) ctx.getAttribute("adminDAO");
		
		ArrayList<User> allUsers = new ArrayList<User>();
		
		ArrayList<Customer> allCustomers = new ArrayList<Customer>();
		ArrayList<Manager> allManagers = new ArrayList<Manager>();

		
		List<Customer> tryCustomers = customerDao.findAll().stream().filter(user -> !user.isRemoved()).toList();
		List<Manager> tryManagers = managerDao.findAll().stream().filter(user -> !user.isRemoved()).toList();

		
		if(tryCustomers!=null)
			allUsers.addAll(tryCustomers);
		if(tryManagers!=null)
			allUsers.addAll(tryManagers);
		if(adminDao.findAll()!=null)
			allUsers.addAll(adminDao.findAll());

		return allUsers;
	}
	@GET
	@Path("/isBlocked/{username}")	//for registration
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean userBlocked(@PathParam("username") String username) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.userBlocked(username);
	}
	@GET
	@Path("/getUserByUsername/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User getByUsername(@PathParam("username") String username) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.getByUsername(username);
	}

}
