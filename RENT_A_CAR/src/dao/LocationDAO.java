package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import beans.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import beans.Address;
import beans.Customer;
import beans.DateSerializer;
import beans.RentACarObject;
import beans.User;

public class LocationDAO {
	private HashMap<Integer, Location> Objects = new HashMap<>();
	private ArrayList<Location> allObjects = new ArrayList<Location>();
	private String filePath;
	private Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting().create();//.setDateFormat("yyyy-mm-dd").create();
	public LocationDAO() {
		
	}
	public LocationDAO(String contextPath) {
		
		String path = contextPath;

        filePath = path.concat("locations.json");
        try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Location>>(){}.getType();
            allObjects = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Location object : allObjects) {

            Objects.put(object.getId(), object);
            
        }

	}
	
	public Location save(Location location) {
		Deserialize();
		Integer maxId = -1;
		for (int id : Objects.keySet()) {
			int idNum =id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		location.setId(maxId);
		allObjects.add(location);
		Objects.put(location.getId(), location);
		Serialization();
		
		return location;
	}
	
	public void Serialization() {
		try (FileWriter fileWriter = new FileWriter(this.filePath)) {
            gson.toJson(allObjects, fileWriter);
            System.out.println("Users successfully serialized to JSON file.");

        } catch (IOException e) {
            e.printStackTrace();
        }  
	}
	
	public void Deserialize()
	{
		allObjects = new ArrayList<Location>();
		Objects = new HashMap<Integer, Location>();
		
		//deserialize customers
		try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Location>>(){}.getType();
            allObjects = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Location location : allObjects) {
        	Objects.put(location.getId(), location);
            
        }
        
	}
	
	
	public Collection<Location> findAll()
	{
		Deserialize();
		return Objects.values();
	}

}
