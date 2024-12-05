package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import beans.Address;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import beans.DateSerializer;
import beans.Location;
import beans.RentACarObject;
import beans.User;

public class AddressDAO {
	private HashMap<Integer, Address> Objects = new HashMap<>();
	private ArrayList<Address> allObjects = new ArrayList<Address>();
	private String filePath;
	private Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting().create();//.setDateFormat("yyyy-mm-dd").create();
	public AddressDAO() {
		
	}
	public AddressDAO(String contextPath) {
		
		String path = contextPath;

        filePath = path.concat("addresses.json");
        try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Address>>(){}.getType();
            allObjects = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Address object : allObjects) {

            Objects.put(object.getId(), object);
            
        }

	}
	public Address save(Address address) {
		Deserialize();
		Integer maxId = -1;
		for (int id : Objects.keySet()) {
			int idNum =id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		address.setId(maxId);
		allObjects.add(address);
		Objects.put(address.getId(), address);
		Serialization();
		
		return address;
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
		allObjects = new ArrayList<Address>();
		Objects = new HashMap<Integer, Address>();
		
		//deserialize customers
		try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Address>>(){}.getType();
            allObjects = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Address address : allObjects) {
        	Objects.put(address.getId(), address);
            
        }
        
	}
	
	
	public Collection<Address> findAll()
	{
		Deserialize();
		return Objects.values();
	}

}
