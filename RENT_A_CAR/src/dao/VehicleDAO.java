package dao;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import beans.Vehicle;
import beans.Vehicle.Status;
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
import beans.Order;
import beans.RentACarObject;
import beans.Customer;
import beans.DateSerializer;



public class VehicleDAO {
	private HashMap<Integer, Vehicle> Vehicles = new HashMap<>();
	private ArrayList<Vehicle> allVehicles = new ArrayList<Vehicle>();
	private String filePath;
	private Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting().create();//.setDateFormat("yyyy-mm-dd").create();
	public VehicleDAO() {
		
	}
	public VehicleDAO(String contextPath) {
		
		String path = contextPath;

        filePath = path.concat("vehicles.json");
        try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Vehicle>>(){}.getType();
            allVehicles = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Vehicle vehicle : allVehicles) {
            Vehicles.put(vehicle.getId(), vehicle);
            
        }

	}
	
	public void Serialization() {
		try (FileWriter fileWriter = new FileWriter(this.filePath)) {
            gson.toJson(allVehicles, fileWriter);
            System.out.println("Vehicles successfully serialized to JSON file.");

        } catch (IOException e) {
            e.printStackTrace();
        }  
	}
	public void Deserialize()
	{
		allVehicles = new ArrayList<Vehicle>();
		Vehicles = new HashMap<Integer, Vehicle>();
		
		//deserialize objects
		try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Vehicle>>(){}.getType();
            allVehicles = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Vehicle object : allVehicles) {
        	
	        Vehicles.put(object.getId(), object);
            
        }
        
	}
	public Vehicle getById(int id) 
	{
		Deserialize();
		return Vehicles.values().stream().filter(vehicle -> ((vehicle.getId() == id) && (vehicle.isRemoved()==false))).findFirst().orElse(null);
	}
	
	public Collection<Vehicle> findAll()
	{
		Deserialize();
		//return (Collection<Vehicle>)allVehicles.stream().filter(v -> (v.isRemoved()==false));
		ArrayList<Vehicle> vehicles=new ArrayList<Vehicle>();
		for(Vehicle v : allVehicles) {
			if(!v.isRemoved()) {
				vehicles.add(v);
			}
		}
		return vehicles;
	}
	public Collection<Vehicle> findAllByInputParameters(String start, String end)
	{
		Deserialize();
		ArrayList<Vehicle> vehicles=new ArrayList<Vehicle>();
		for(Vehicle vehicle : allVehicles) {
			if(!vehicle.isRemoved() && vehicle.getStatus()!=null && vehicle.getStatus().toString().equals("Available"))
				vehicles.add(vehicle);
		}
		return vehicles;
		
	}
	public Vehicle delete(int id) {
		
		//update hashmap
		Vehicle vehicleToDelete = getById(id);
		Deserialize();
		vehicleToDelete.setRemoved(true);
		for(Vehicle vehicle : Vehicles.values())
		{
			if(vehicle.getId() == vehicleToDelete.getId())
			{
				Vehicles.remove(vehicle);
				break;
			}
		}
		Vehicles.put(vehicleToDelete.getId(), vehicleToDelete);
		for(Vehicle vehicle : allVehicles)
		{
			if(vehicle.getId()==vehicleToDelete.getId())
			{
				allVehicles.remove(vehicle);
				break;
			}
		}
		allVehicles.add(vehicleToDelete);
		
		
		Serialization();
		return vehicleToDelete;
	} 
	
	public Vehicle save(Vehicle vehicle) {
		Deserialize();
		Integer maxId = -1;
		for (int id : Vehicles.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		vehicle.setId(maxId);
		vehicle.setStatus(Status.Available);
		vehicle.setRemoved(false);
		vehicle.setQuantity(1);
		allVehicles.add(vehicle);
		Vehicles.put(vehicle.getId(), vehicle);
		Serialization();
		
		return vehicle;
	}
	public Vehicle findById(int id) {
		for(Vehicle v:Vehicles.values()) {
			if(v.getId()==id) {
				return v;
			}
		}
		return null;
	}
	public Vehicle edit(int id, Vehicle v) {
			
			//update hashmap
			for(Vehicle vehicle : Vehicles.values())
			{
				if(vehicle.getId()==id)
				{
					Vehicles.remove(vehicle);
					break;
				}
			}
			Vehicles.put(v.getId(), v);
			for(Vehicle vehicle : allVehicles)
			{
				if(vehicle.getId()==id)
				{
					allVehicles.remove(vehicle);
					break;
				}
			}
			allVehicles.add(v);
			
			
			Serialization();
			
			return v;
		}
	public void editVehicles(ArrayList<Vehicle> vehicles) {
		
		//update hashmap
		Deserialize();
		ArrayList<Vehicle> newV=new ArrayList<Vehicle>();
		Collection<Vehicle> pomocna=Vehicles.values();
		for(Vehicle vehicle : pomocna)
		{
			for(Vehicle v : vehicles) {
				if(vehicle.getId()==v.getId())
				{
					newV.add(vehicle);
					Vehicles.remove(vehicle);
					//Vehicles.values().stream().filter(null)
				}
			}
			
		}
		for(Vehicle v : newV) {
			Vehicles.put(v.getId(), v);
		}
		//Vehicles.put(v.getId(), v);
		newV.clear();
		Collection<Vehicle> pomocna1=Vehicles.values();
		for(Vehicle vehicle : pomocna1)
		{
			for(Vehicle v : vehicles) {
				if(vehicle.getId()==v.getId())
				{
					newV.add(vehicle);
					allVehicles=(ArrayList<Vehicle>) allVehicles.stream().filter(v1 -> v1.getId()==v.getId());
				}
			}
			
		}
		for(Vehicle v : newV) {
			allVehicles.add(v);
		}		
		Serialization();
		
		//return v;
	}
	public Collection<Vehicle> findRemovedVehicles()
	{
		Deserialize();
		ArrayList<Vehicle> vehicles=new ArrayList<Vehicle>();
		for(Vehicle vehicle : allVehicles) {
			if(vehicle.isRemoved())
				vehicles.add(vehicle);
		}
		return vehicles;
		
	}
	public Collection<Vehicle> editVehiclesList(ArrayList<Vehicle> vehicles) {
		
		//update hashmap
		Deserialize();
		vehicles.forEach(vehicle -> {
            int id = vehicle.getId();
            if (Vehicles.containsKey(id)) {
                Vehicle existingVehicle = Vehicles.get(id);
                existingVehicle.setRemoved(vehicle.isRemoved());
            }
        });
		allVehicles = new ArrayList<Vehicle>();
		for(Vehicle vehicle : Vehicles.values())
			allVehicles.add(vehicle);
		Serialization();
		return vehicles;
	}
}
