package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import beans.Basket;
import beans.Customer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import beans.DateSerializer;
import beans.RentACarObject;
import beans.Vehicle;


public class BasketDAO {
	private HashMap<Integer, Basket> Baskets = new HashMap<>();
	private ArrayList<Basket> allBaskets = new ArrayList<Basket>();
	private String filePath;
	private Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting().create();//.setDateFormat("yyyy-mm-dd").create();
	public BasketDAO() {
		
	}
	public BasketDAO(String contextPath) {
		
		String path = contextPath;

        filePath = path.concat("basket.json");
        try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Basket>>(){}.getType();
            allBaskets = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Basket basket : allBaskets) {

            Baskets.put(basket.getId(), basket);
            
        }

	}
	public Basket save(Basket basket) {
		Deserialize();
		Integer maxId = -1;
		for (int id : Baskets.keySet()) {
			int idNum =id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		basket.setId(maxId);
		allBaskets.add(basket);
		Baskets.put(basket.getId(), basket);
		Serialization();
		
		return basket;
	}
	public void Serialization() {
		try (FileWriter fileWriter = new FileWriter(this.filePath)) {
            gson.toJson(allBaskets, fileWriter);
            System.out.println("Users successfully serialized to JSON file.");

        } catch (IOException e) {
            e.printStackTrace();
        }  
	}
	
	public void Deserialize()
	{
		allBaskets = new ArrayList<Basket>();
		Baskets = new HashMap<Integer, Basket>();
		
		//deserialize customers
		try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Basket>>(){}.getType();
            allBaskets = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Basket basket : allBaskets) {
        	Baskets.put(basket.getId(), basket);
            
        }
        
	}
	
	
	public Collection<Basket> findAll()
	{
		Deserialize();
		return Baskets.values();
	}
	public Basket findById(int id)
	{
		return Baskets.values().stream().filter(object -> (object.getId() == id)).findFirst().orElse(null);
	}
	public Basket edit(Basket basket) {
		
		//update hashmap
		Deserialize();
		for(Basket b : Baskets.values())
		{
			if(b.getId() == basket.getId())
			{
				Baskets.remove(b);
				break;
			}
		}
		Baskets.put(basket.getId(), basket);
		for(Basket b : allBaskets)
		{
			if(b.getId() == basket.getId())
			{
				allBaskets.remove(b);
				break;
			}
		}
		allBaskets.add(basket);			
		Serialization();			
		return basket;
}
	public Boolean UserExists(String username)
	{
		Deserialize();
		return Baskets.values().stream().filter(basket -> (basket.getUsername().equals(username))).findFirst().orElse(null)!=null;
	}
	public Basket getByUser(Customer customer) {
		
		Deserialize();
		if(customer==null)
			return null;
		return Baskets.values().stream().filter(object -> (object.getUsername().equals(customer.getUsername()))).findFirst().orElse(null);
		
	}
	public Vehicle getVehicleByBasket(int basketId, int vehicleId) {
		
		Deserialize();
		for(Basket basket : Baskets.values())
		{
			if(basket.getId()==basketId)
			{
				if(basket.getVehicles()!=null)
				{
					for(Vehicle vehicle : basket.getVehicles())
					{
						if(vehicle.getId() == vehicleId)
							return vehicle;
					}
				}
				else
					return null;
				
			}
		}
		return null;
		
	}
	public void delete(int basketId)
	{
		Deserialize();
		for(Basket b : Baskets.values())
		{
			if(b.getId() == basketId)
			{
				Baskets.remove(b);
				break;
			}
		}
		
		for(Basket b : allBaskets)
		{
			if(b.getId() == basketId)
			{
				allBaskets.remove(b);
				break;
			}
		}
					
		Serialization();
		
		
	}
	/*public Collection<Basket> editBaskets(ArrayList<Basket> baskets) {
		
		//update hashmap
		/*Deserialize();
		baskets.forEach(basket -> {
            int basketId = basket.getId();
            if (Baskets.containsKey(basket)) {
                Basket existingBasket = Baskets.get(basket);
                
                existingBasket.setPrice(basket.getPrice());
                existingBasket.setVehicles(basket.getVehicles());
                
            }
        });
		allBaskets = new ArrayList<Basket>();
		for(Basket basket : Baskets.values())
			allBaskets.add(basket);
		Serialization();
		return baskets;
	}*/


}
