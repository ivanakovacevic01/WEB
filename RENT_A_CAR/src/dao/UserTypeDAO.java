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
import beans.UserType;
import beans.Vehicle;
public class UserTypeDAO {
	private HashMap<Integer, UserType> Types = new HashMap<>();
	private ArrayList<UserType> allTypes = new ArrayList<UserType>();
	private String filePath;
	private Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting().create();//.setDateFormat("yyyy-mm-dd").create();
	
	public UserTypeDAO() {
		
	}
	public UserTypeDAO(String contextPath) {
		String path = contextPath;

        filePath = path.concat("types.json");
        try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<UserType>>(){}.getType();
            allTypes = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(UserType type : allTypes) {

        	Types.put(type.getId(), type);

        }
	}
	public void Serialization() {
		try (FileWriter fileWriter = new FileWriter(this.filePath)) {
            gson.toJson(allTypes, fileWriter);
            System.out.println("Users successfully serialized to JSON file.");

        } catch (IOException e) {
            e.printStackTrace();
        }  
	}
	public Collection<UserType> findAll()
	{
		return Types.values();
	}
	public UserType findBronze()
	{
		for(UserType type : Types.values()) {
			if(type.getTypename().toString().equals("Bronze")) {
				return type;
			}
		}
		return null;
	}


}
