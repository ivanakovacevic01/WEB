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

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;




import beans.DateDeserializer;

import beans.DateSerializer;
import beans.Manager;
import beans.Order;
import beans.Administrator;
import beans.Customer;
import beans.DateSerializer;
import beans.User;
import beans.Order.OrderStatus;

enum Gender1 {
	Male,
	Female
}
enum Role1 {
	Customer,
	Admin,
	Manager
}
public class AdminDAO {
	private HashMap<String, Administrator> Admins = new HashMap<>();
	private ArrayList<Administrator> allAdmins = new ArrayList<Administrator>();
	private String filePath;
	private Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting().create();//.setDateFormat("yyyy-mm-dd").create();
	public AdminDAO() {
		
	}
	public AdminDAO(String contextPath) {
		
		String path = contextPath;

        filePath = path.concat("admins.json");
        try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Administrator>>(){}.getType();
            allAdmins = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Administrator user : allAdmins) {

        	Admins.put(user.getUsername(), user);

        }

	}
	public Administrator edit(String oldUsername, Administrator user) {
		
		//update hashmap
		Deserialize();
		for(Administrator customer : Admins.values())
		{
			if(customer.getUsername().equals(oldUsername))
			{
				Admins.remove(customer);
				break;
			}
		}
		Admins.put(user.getUsername(), user);
		for(Administrator customer : allAdmins)
		{
			if(customer.getUsername().equals(oldUsername))
			{
				allAdmins.remove(customer);
				break;
			}
		}
		allAdmins.add(user);
		
		
		Serialization();
		
		return user;
	}
	public void Serialization() {
		try (FileWriter fileWriter = new FileWriter(this.filePath)) {
            gson.toJson(allAdmins, fileWriter);
            System.out.println("Users successfully serialized to JSON file.");

        } catch (IOException e) {
            e.printStackTrace();
        }  
	}
	
	public void Deserialize()
	{
		allAdmins = new ArrayList<Administrator>();
		Admins = new HashMap<String, Administrator>();
		
		//deserialize customers
		try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Administrator>>(){}.getType();
            allAdmins = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Administrator admin : allAdmins) {
        	Admins.put(admin.getUsername(), admin);
            
        }
        
        
	}
	public void logOutAll()
	{
		Deserialize();
		ArrayList<Administrator> loggedOutAdmins = new ArrayList<Administrator>();
		for(Administrator admin : Admins.values())
		{
			admin.setLoggedIn(false);
			loggedOutAdmins.add(admin);
		}
		allAdmins = loggedOutAdmins;
		Admins = new HashMap<String, Administrator>();
		for(Administrator admin : allAdmins)
		{
			Admins.put(admin.getUsername(), admin);
		}
		
		Serialization();
	}
	
	
	public Administrator findByUsername(String username)
	{
		Deserialize();
		return Admins.values().stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
	}
	public Boolean UserExists(String username,String password)
	{
		Deserialize();
		return Admins.values().stream().filter(user -> (user.getUsername().equals(username) && user.getPassword().equals(password))).findFirst().orElse(null)!=null;
	}

	public Collection<Administrator> findAll()
	{
		Deserialize();
		return Admins.values();
	}
	
	public Administrator getByUsername(String username)
	{
		return Admins.values().stream().filter(user -> (user.getUsername().equals(username))).findFirst().orElse(null);	
		
	} //za sta ce ova funkcija?
}
