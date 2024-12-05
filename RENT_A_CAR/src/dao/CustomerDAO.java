package dao;

import java.lang.reflect.Type;




import java.io.File;

import java.io.FileReader;

import java.io.IOException;

import java.lang.reflect.Type;

import java.io.FileWriter;

import java.util.ArrayList;

import java.util.Collection;

import java.util.Date;

import java.util.HashMap;




import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;




import beans.DateDeserializer;

import beans.DateSerializer;
import beans.Manager;
import beans.Order;
import beans.Order.OrderStatus;
import beans.UserType;
import beans.UserType.TypeName;
import beans.Basket;
import beans.Comment;
import beans.Customer;
import beans.DateSerializer;

enum Gender {
	Male,
	Female
}
enum Role {
	Customer,
	Admin,
	Manager
}

public class CustomerDAO {
	private HashMap<String, Customer> Users = new HashMap<>();
	private ArrayList<Customer> allUsers = new ArrayList<Customer>();
	private String filePath;
	private Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting().create();//.setDateFormat("yyyy-mm-dd").create();
	public CustomerDAO() {
		
	}
	public CustomerDAO(String contextPath) {
		
		String path = contextPath;

        filePath = path.concat("customers.json");
        try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Customer>>(){}.getType();
            allUsers = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Customer user : allUsers) {

            Users.put(user.getUsername(), user);
            
        }

	}
	
	public void Serialization() {
		try (FileWriter fileWriter = new FileWriter(this.filePath)) {
            gson.toJson(allUsers, fileWriter);
            System.out.println("Users successfully serialized to JSON file.");

        } catch (IOException e) {
            e.printStackTrace();
        }  
	}
	
	public void Deserialize()
	{
		allUsers = new ArrayList<Customer>();
		Users = new HashMap<String, Customer>();
		
		//deserialize customers
		try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Customer>>(){}.getType();
            allUsers = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Customer customer : allUsers) {
        	Users.put(customer.getUsername(), customer);
            
        }
        
        
	}
	public Customer save(Customer user) {
		user.setRole(Role.Customer.toString());
		user.setCollectedPoints(0);
		user.setLoggedIn(false);
		allUsers.add(user);
		Users.put(user.getUsername(), user);
		Serialization();
		
		
	return user;
	}
	
	public void logOutAll()
	{
		Deserialize();
		ArrayList<Customer> loggedOutCustomers = new ArrayList<Customer>();
		for(Customer customer : Users.values())
		{
			customer.setLoggedIn(false);
			loggedOutCustomers.add(customer);
		}
		allUsers = loggedOutCustomers;
		Users = new HashMap<String, Customer>();
		for(Customer customer : allUsers)
		{
			Users.put(customer.getUsername(), customer);
		}
		
		Serialization();
	}
	
	public Customer edit(String oldUsername, Customer user) {
		
		//update hashmap
		Deserialize();
		for(Customer customer : Users.values())
		{
			if(customer.getUsername().equals(oldUsername))
			{
				Users.remove(customer);
				break;
			}
		}
		Users.put(user.getUsername(), user);
		for(Customer customer : allUsers)
		{
			if(customer.getUsername().equals(oldUsername))
			{
				allUsers.remove(customer);
				break;
			}
		}
		allUsers.add(user);
		
		
		Serialization();
		
		return user;
	}
	
	
	public Customer findByUsername(String username)
	{
		return Users.values().stream().filter(user -> user.getUsername().equals(username) && !user.isRemoved()).findFirst().orElse(null);
	}
	public Boolean UserExists(String username,String password)
	{
		return Users.values().stream().filter(user -> (user.getUsername().equals(username) && user.getPassword().equals(password))).findFirst().orElse(null)!=null;
	}
	public Basket basketExists(String username)
	{
		for(Customer customer : Users.values()) {
			if(customer.getUsername().equals(username)) {
				return customer.getBasket(); 
			}
		}
		return null;
	}
	public Collection<Customer> findAll()
	{
		return Users.values();
	}
	public Customer getByUsername(String username)
	{
		return Users.values().stream().filter(user -> (user.getUsername().equals(username))).findFirst().orElse(null);	
		
	}
	public void block(String username)
	{
		Customer customer = findByUsername(username);
		customer.setBlocked(true);
		customer=edit(username,customer);
	}
	public void remove(String username)
	{
		Customer customer = findByUsername(username);
		customer.setRemoved(true);
		customer=edit(username,customer);
	}
}
