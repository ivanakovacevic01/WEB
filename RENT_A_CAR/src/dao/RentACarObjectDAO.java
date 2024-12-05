package dao;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import beans.RentACarObject;
import beans.RentACarObject.State;

import java.io.File;

import java.io.FileReader;

import java.io.IOException;

import java.lang.reflect.Type;

import java.io.FileWriter;

import java.util.ArrayList;

import java.util.Collection;

import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;




import beans.DateDeserializer;

import beans.DateSerializer;
import beans.Manager;
import beans.Customer;
import beans.DateSerializer;



public class RentACarObjectDAO {
	private HashMap<Integer, RentACarObject> Objects = new HashMap<>();
	private ArrayList<RentACarObject> allObjects = new ArrayList<RentACarObject>();
	private String filePath;
	private Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting().create();//.setDateFormat("yyyy-mm-dd").create();
	public RentACarObjectDAO() {
		
	}
	public RentACarObjectDAO(String contextPath) {
		
		String path = contextPath;

        filePath = path.concat("objects.json");
        try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<RentACarObject>>(){}.getType();
            allObjects = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(RentACarObject object : allObjects) {

            Objects.put(object.getId(), object);
            
        }

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
		allObjects = new ArrayList<RentACarObject>();
		Objects = new HashMap<Integer, RentACarObject>();
		
		//deserialize objects
		try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<RentACarObject>>(){}.getType();
            allObjects = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(RentACarObject object : allObjects) {
        	//set state to opened or closed depend.on datetime.now and start-end time
			LocalTime currentTime = LocalTime.now(); 
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	        LocalTime startTime = LocalTime.parse(object.getStartTime(), formatter);
	        LocalTime endTime = LocalTime.parse(object.getEndTime(), formatter);
	        if(currentTime.isAfter(startTime) && currentTime.isBefore(endTime))	///////////provjeriti treba li ovo cuvati uopste
	        	object.setState(State.Opened);
	        else
	        	object.setState(State.Closed);
        	Objects.put(object.getId(), object);
            
        }
        
	}
	
	
	public Collection<RentACarObject> findAll()
	{
		Deserialize();
		allObjects.sort((objekat1, objekat2) -> {
		    if (objekat1.getState() == State.Opened && objekat2.getState() == State.Closed) {
		        return -1;
		    } else if (objekat1.getState() ==  State.Closed && objekat2.getState() == State.Opened) {
		        return 1; 
		    } else {
		        return 0; 
		    }
		});

		
		return allObjects;
	}
	public RentACarObject edit(RentACarObject object) {
			
			//update hashmap
			Deserialize();
			for(RentACarObject rent : Objects.values())
			{
				if(rent.getId() == object.getId())
				{
					Objects.remove(rent);
					break;
				}
			}
			Objects.put(object.getId(), object);
			for(RentACarObject rent : allObjects)
			{
				if(rent.getId()==object.getId())
				{
					allObjects.remove(rent);
					break;
				}
			}
			allObjects.add(object);			
			Serialization();			
			return object;
	}
	
	public RentACarObject getById(int id)
	{
		return Objects.values().stream().filter(object -> (object.getId() == id)).findFirst().orElse(null);
	}
	
	public RentACarObject GetSelectedObject(int id)
	{
		Deserialize();
		for(RentACarObject object : Objects.values()) {
			if(object.getId() == id) 
				return object;
        }
		return null;
	}
	public RentACarObject save(RentACarObject object) {
		Deserialize();
		Integer maxId = -1;
		for (int id : Objects.keySet()) {
			int idNum =id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		object.setId(maxId);
		object.setRemoved(false);
		allObjects.add(object);
		Objects.put(object.getId(), object);
		Serialization();
		
	return object;
	}
}
