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
import beans.Location;
import beans.Manager;
import beans.Order;
import beans.RentACarObject;
import beans.User;
import beans.Administrator;
import beans.Customer;
import beans.DateSerializer;

public class ManagerDAO {
	private HashMap<String, Manager> Managers = new HashMap<>();
	private ArrayList<Manager> allManagers = new ArrayList<Manager>();
	private String filePath;
	private Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting().create();//.setDateFormat("yyyy-mm-dd").create();
	public ManagerDAO() {
		
	}
	public ManagerDAO(String contextPath) {
		
		String path = contextPath;

        filePath = path.concat("managers.json");
        try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Manager>>(){}.getType();
            allManagers = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Manager manager : allManagers) {

        	Managers.put(manager.getUsername(), manager);
            
        }

	}
	
	public void Serialization() {
		try (FileWriter fileWriter = new FileWriter(this.filePath)) {
            gson.toJson(allManagers, fileWriter);
            System.out.println("Users successfully serialized to JSON file.");

        } catch (IOException e) {
            e.printStackTrace();
        }  
	}
	public Manager save(Manager manager) {
		Deserialize();
		manager.setRole(Role.Manager.toString());
		manager.setLoggedIn(false);
		allManagers.add(manager);
		Managers.put(manager.getUsername(), manager);
		Serialization();
		
		
	return manager;
	}
	
	public void Deserialize()
	{
		allManagers = new ArrayList<Manager>();
		Managers = new HashMap<String, Manager>();
		
		//deserialize customers
		try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Manager>>(){}.getType();
            allManagers = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Manager manager : allManagers) {
        	Managers.put(manager.getUsername(), manager);
            
        }
        
        
	}
	
	public void logOutAll()
	{
		Deserialize();
		ArrayList<Manager> loggedOutManagers = new ArrayList<Manager>();
		for(Manager manager : Managers.values())
		{
			manager.setLoggedIn(false);
			loggedOutManagers.add(manager);
		}
		allManagers = loggedOutManagers;
		Managers = new HashMap<String, Manager>();
		for(Manager manager : allManagers)
		{
			Managers.put(manager.getUsername(), manager);
		}
		
		Serialization();
	}
	
	
	public Manager edit(String oldUsername, Manager user) {
		
		//update hashmap
		Deserialize();
		for(Manager customer : Managers.values())
		{
			if(customer.getUsername().equals(oldUsername))
			{
				Managers.remove(customer);
				break;
			}
		}
		Managers.put(user.getUsername(), user);
		for(Manager customer : allManagers)
		{
			if(customer.getUsername().equals(oldUsername))
			{
				allManagers.remove(customer);
				break;
			}
		}
		allManagers.add(user);
		
		
		Serialization();
		
		return user;
	}
	
	
	public Manager findByUsername(String username)
	{
		Deserialize();
		return Managers.values().stream().filter(user -> user.getUsername().equals(username) && !user.isRemoved()).findFirst().orElse(null);
	}
	public Boolean UserExists(String username,String password)
	{
		Deserialize();
		return Managers.values().stream().filter(user -> (user.getUsername().equals(username) && user.getPassword().equals(password))).findFirst().orElse(null)!=null;
	}

	public Collection<Manager> findAll()
	{
		Deserialize();
		return Managers.values();
	}
	public Manager getByUsername(String username)
	{
		Deserialize();
		return Managers.values().stream().filter(user -> (user.getUsername().equals(username))).findFirst().orElse(null);	
		
	}
	public RentACarObject getByManager(String username)
	{
		Deserialize();
		Manager retVal = Managers.values().stream().filter(user -> (user.getUsername().equals(username))).findFirst().orElse(null);	
		return retVal.getObject();	
	}
	public Collection<Manager> getAllWithoutRentObject()
	{
		Deserialize();
		ArrayList<Manager> freeManagers = new ArrayList<Manager>();
		for(Manager m : Managers.values())
		{
			if(m.getObject()==null)
				freeManagers.add(m);
				
		}
		return freeManagers;
	}
	public void block(String username)
	{
		Deserialize();
		Manager manager = findByUsername(username);
		manager.setBlocked(true);
		manager=edit(username,manager);
	}
	public void remove(String username)
	{
		Deserialize();
		Manager manager = findByUsername(username);
		manager.setRemoved(true);
		manager=edit(username,manager);
	}
	
	public Manager getManagerByObjectId(int objectId)
	{
		Deserialize();
		return Managers.values().stream().filter(manager -> (manager.getObject()!=null && manager.getObject().getId()==objectId)).findFirst().orElse(null);	
	}
	
}
