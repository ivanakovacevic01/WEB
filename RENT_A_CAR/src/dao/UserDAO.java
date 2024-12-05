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

import beans.Administrator;
import beans.Customer;
import beans.DateDeserializer;

import beans.DateSerializer;


import beans.DateSerializer;
import beans.User;
import beans.Vehicle;
public class UserDAO {
	private HashMap<String, User> Users = new HashMap<>();
	private ArrayList<User> allUsers = new ArrayList<User>();
	private String filePathCustomers;
	private String filePathAdmins;
	private String filePathManagers;
	private Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting().create();//.setDateFormat("yyyy-mm-dd").create();
	
	public UserDAO() {
		
	}
	public UserDAO(String contextPath) {
		String path=contextPath;
		
        filePathCustomers = path.concat("customers.json");
        filePathAdmins = path.concat("admins.json");
        filePathManagers = path.concat("managers.json");
        
        //mapa cuva sve Usere, lista ne!
        
        
        //deserialize customers
        try (FileReader fileReader = new FileReader(this.filePathCustomers)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            allUsers = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(User user : allUsers) {
        	Users.put(user.getUsername(), user);
            
        }
        
        //deserialize admins
        try (FileReader fileReader = new FileReader(this.filePathAdmins)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            allUsers = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }
        for(User user : allUsers) {
        	Users.put(user.getUsername(), user);
            
        }
        
        
      //deserialize managers
        try (FileReader fileReader = new FileReader(this.filePathManagers)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            allUsers = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }
        for(User user : allUsers) {
        	Users.put(user.getUsername(), user);
            
        }
       

	}
	

	public void Deserialize()
	{
		allUsers = new ArrayList<User>();
		Users = new HashMap<String, User>();
		
		//deserialize customers
		try (FileReader fileReader = new FileReader(this.filePathCustomers)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            allUsers = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(User user : allUsers) {
        	Users.put(user.getUsername(), user);
            
        }
        
        //deserialize admins
        try (FileReader fileReader = new FileReader(this.filePathAdmins)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            allUsers = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }
        for(User user : allUsers) {
        	Users.put(user.getUsername(), user);
            
        }
        
        
      //deserialize managers
        try (FileReader fileReader = new FileReader(this.filePathManagers)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            allUsers = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }
        for(User user : allUsers) {
        	Users.put(user.getUsername(), user);
            
        }
	}
	
	public boolean usernameExists(String username)
	{
		Deserialize();
		return Users.values().stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null) != null;
	}
	
	public boolean usernameExistsForEditing(String username, String oldUsername)
	{
		Deserialize();
		if(username.equals(oldUsername))
			return false;
		return Users.values().stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null) != null;
	}
	
	public String findRole(String username)
	{
		Deserialize();
		return Users.values().stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null).getRole().toString();
	}
	public Boolean UserExists(String username,String password)
	{
		Deserialize();
		return Users.values().stream().filter(user -> (user.getUsername().equals(username) && user.getPassword().equals(password))).findFirst().orElse(null)!=null;
	}
	
	public User isLogged()
	{
		Deserialize();
		return Users.values().stream().filter(user -> (user.isLoggedIn())).findFirst().orElse(null);
		
	}
	public Collection<User> findAll()
	{
		Deserialize();
		return Users.values();
	}

	public boolean userBlocked(String username) {
		Deserialize();
		for(User user : Users.values()) {
			if(user.getUsername().equals(username)) {
				return user.isBlocked();
			}
		}
		return false;
	}
	public User getByUsername(String username)
	{
		Deserialize();
		return Users.values().stream().filter(user -> (user.getUsername().equals(username))).findFirst().orElse(null);	
		
	}

}
