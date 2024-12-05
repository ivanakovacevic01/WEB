package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import beans.Basket;
import beans.Customer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import beans.DateSerializer;
import beans.Order;
import beans.Order.OrderStatus;
import beans.RentACarObject;
import beans.Vehicle;
import beans.Vehicle.Status;


public class OrderDAO {
	private HashMap<String, Order> Orders = new HashMap<>();
	private ArrayList<Order> allOrders = new ArrayList<Order>();
	private String filePath;
	private Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting().create();//.setDateFormat("yyyy-mm-dd").create();
	public OrderDAO() {
		
	}
	public OrderDAO(String contextPath) {
		
		String path = contextPath;

        filePath = path.concat("orders.json");
        try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Order>>(){}.getType();
            allOrders = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Order order : allOrders) {

        	Orders.put(order.getId(), order);
            
        }

	}
	
	private String generateId()
	{
		//generate 10 chars id
		 String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		 Random random = new Random();
		 StringBuilder sb = new StringBuilder(10);
		    
		 for (int i = 0; i < 10; i++) {
		     int randomIndex = random.nextInt(characters.length());
		     char randomChar = characters.charAt(randomIndex);
		     sb.append(randomChar);
		}
		       
		return sb.toString();
	}
	public ArrayList<Order> save(ArrayList<Order> orders) {
		Deserialize();
		boolean exists=true;
		ArrayList<Order> retVal = new ArrayList<Order>();
		
		for(Order order : orders)
		{
			String newId ="";
			int num=0;
			exists=true;
	        
	        while(exists)
	        {
	        	newId = generateId();
	        	num=0;
	        	for (String id : Orders.keySet()) {
	    			if(id.equals(newId))
	    			{
	    				exists=true;
	    				num++;
	    				break;
	    			}
	    		}
	        	if(num==0)
	        		exists=false;
	        	
	        }
	        order.setId(newId);
	        order.setStatus(OrderStatus.Processing);
	        order.setCancellationDate("");
	        order.setReason("");
	        allOrders.add(order);
			Orders.put(order.getId(), order);
			retVal.add(order);
		}
	
		Serialization();
		return retVal;

	}
	
	public void declineOrders(ArrayList<Order> orders) {
		Deserialize();

		
		orders.forEach(order -> {
	            String orderId = order.getId();
	            if (Orders.containsKey(orderId)) {
	                Order existingOrder = Orders.get(orderId);
	                
	                if(existingOrder.getStatus().toString().equals("Processing"))
	                	existingOrder.setStatus(OrderStatus.Declined);
	            }
	        });
		allOrders = new ArrayList<Order>();
		for(Order order : Orders.values())
			allOrders.add(order);
		
	
		Serialization();
		

	}
	public void Serialization() {
		try (FileWriter fileWriter = new FileWriter(this.filePath)) {
            gson.toJson(allOrders, fileWriter);
            System.out.println("Users successfully serialized to JSON file.");

        } catch (IOException e) {
            e.printStackTrace();
        }  
	}
	
	public void Deserialize()
	{
		allOrders = new ArrayList<Order>();
		Orders = new HashMap<String, Order>();
		
		//deserialize customers
		try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Order>>(){}.getType();
            allOrders = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Order order : allOrders) {
        	Orders.put(order.getId(), order);
            
        }
        
	}
	
	
	public Collection<Order> findAll()
	{
		Deserialize();
		return Orders.values();
	}
	public Order findById(String id)
	{
		Deserialize();
		return Orders.values().stream().filter(object -> (object.getId().equals(id))).findFirst().orElse(null);
	}
	public Order edit(Order order) {
		
		//update hashmap
		Deserialize();
		for(Order b : Orders.values())
		{
			if(b.getId().equals(order.getId()))
			{
				Orders.remove(b);
				break;
			}
		}
		
		Orders.put(order.getId(), order);
		for(Order b : allOrders)
		{
			if((b.getId().equals(order.getId())))
			{
				allOrders.remove(b);
				break;
				//tes
			}
		}
		allOrders.add(order);			
		Serialization();			
		return order;
	}
	
	public void cancel(String id)
	{
		Deserialize();
		Order forCancellation = findById(id);
		forCancellation.setStatus(OrderStatus.Canceled);
		forCancellation.setCancellationDate(LocalDate.now().toString());
		edit(forCancellation);
	}
	public Collection<Order> findByCustomer(String username)
	{
		Deserialize();
		ArrayList<Order> orders=new ArrayList<Order>();
		for(Order order:Orders.values()) {
			if(order.getCustomerUsername().equals(username)) {
				orders.add(order);
			}
		}
		return orders;

	}
	public ArrayList<String> getSuspicious()
	{
		Deserialize();
		//cancellation date time will be != "" only for cancelled reservations
		//list with suspicious usernames
				ArrayList<String> suspicious = new ArrayList<String>();
		
		//firstly, we find all cancelled orders
		ArrayList<Order> cancelledOrders = new ArrayList<Order>();
		for(Order order : Orders.values())
		{
			if(order.getStatus().toString().equals("Canceled"))
				cancelledOrders.add(order);
		}
		
		if(cancelledOrders.size()>0) {
		//now, we make map where keys are usernames and values are cancellation dates
		Map<String, List<String>> cancellationDatesByUsernames = cancelledOrders.stream()
                .collect(Collectors.groupingBy(Order::getCustomerUsername,
                        Collectors.mapping(Order::getCancellationDate, Collectors.toList())));

		//then, we sort dates in map elements
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		cancellationDatesByUsernames.forEach((username, dates) -> {
            List<LocalDate> parsedDates = dates.stream().map(dateString -> LocalDate.parse(dateString, formatter)).collect(Collectors.toList());
            
            Collections.sort(parsedDates);
            
            List<String> sortedDates = parsedDates.stream().map(date -> date.format(formatter)).collect(Collectors.toList());
            
            cancellationDatesByUsernames.put(username, sortedDates);
		
		});
		
		
		
		
		
		
		cancellationDatesByUsernames.forEach((username, dates) -> {
			
			boolean isSuspicious = false;
			for(int i=0; i<dates.size(); i++)
			{
				LocalDate startDate = LocalDate.parse(dates.get(i), formatter);
				if(i<dates.size()-5)
				{
					LocalDate endDate = LocalDate.parse(dates.get(i+5), formatter);	
					long daysNumber = Math.abs(endDate.toEpochDay() - startDate.toEpochDay());
					if(daysNumber <= 30)
					{
						isSuspicious = true;
						break;
					}
				}
				else
					break;
				
			}
			if(isSuspicious)
			{
				suspicious.add(username);
			}

		});
		
		}
		
		return suspicious;

	}
	public Collection<Vehicle> findVehicles(String id)
	{
		ArrayList<Vehicle> vehicles=new ArrayList<Vehicle>();
		for(Order order:Orders.values()) {
			if(order.getId().equals(id)) {
				vehicles=order.getVehicles();
			}
		}
		return vehicles;

	}
	public Collection<Order> findByObjectId(int id)
	{
		Deserialize();
		ArrayList<Order> orders=new ArrayList<Order>();
		for(Order order:Orders.values()) {
			if(order.getObject().getId()==id) {
				orders.add(order);
			}
		}
		return orders;
	}
	public Collection<Vehicle> findBadVehicles(String start)
	{
		Deserialize();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(start, formatter);

		ArrayList<Vehicle> vehicles=new ArrayList<Vehicle>();
		for(Order order:Orders.values()) {
			for(Vehicle v:order.getVehicles()) {
				String date=order.getStartDateTime();
				DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		        LocalDateTime dateTime1 = LocalDateTime.parse(date, formatter);
		        int result=dateTime.compareTo(dateTime1);
				if(result>0) {
					vehicles.add(v);
				}
				
			}
		}
		return vehicles;
	}
	public Collection<Order> findByOrderStatus()
	{
		Deserialize();
		ArrayList<Order> orders=new ArrayList<Order>();
		for(Order order:Orders.values()) {
			if(order.getStatus().toString().equals("Processing") || order.getStatus().toString().equals("Approved") || order.getStatus().toString().equals("Received")) {
				orders.add(order); 
			}
		}
		return orders;
	}
	public void approve(String id)
	{
		Deserialize();
		Order forApprove = findById(id);
		forApprove.setStatus(OrderStatus.Approved);
		edit(forApprove);
	}
	public void decline(String id,String reason)
	{
		Deserialize();
		Order forDeclined = findById(id);
		forDeclined.setStatus(OrderStatus.Declined);
		forDeclined.setReason(reason);
		edit(forDeclined);
	}
	public String findCustomer(String id)
	{
		Deserialize();
		Order order=findById(id);
		return order.getCustomer();
	}
	public boolean receive(String id)
	{
		Deserialize();
		Order forReceive = findById(id);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(forReceive.getStartDateTime(), formatter);
		int result=dateTime.compareTo(LocalDateTime.now());
        if(result<0 || forReceive.getStartDateTime().split("T")[0].equals(LocalDateTime.now().toString().split("T")[0])) {
			forReceive.setStatus(OrderStatus.Received);
			edit(forReceive);
			return true;
		}
		return false;
	}
	
	public void returnRental(String id)
	{
		Deserialize();
		Order forReturn = findById(id);	
		forReturn.setStatus(OrderStatus.Returned);
		edit(forReturn);
	}
	public Collection<Vehicle> getVehicleByOrderId(String id)
	{
		Deserialize();
		Order order=findById(id);
		return order.getVehicles();
	}
	public Order edit(String id, Order o) {
		
		//update hashmap
		for(Order order : Orders.values())
		{
			if(order.getId().equals(id))
			{
				Orders.remove(order);
				break;
			}
		}
		Orders.put(o.getId(), o);
		for(Order order : allOrders)
		{
			if(order.getId().equals(id))
			{
				allOrders.remove(order);
				break;
			}
		}
		allOrders.add(o);
		
		
		Serialization();
		
		return o;
	}
	public Collection<Order> getOrders(String username)
	{
		Deserialize();
		ArrayList<Order> orders=new ArrayList<Order>();
		for(Order order : Orders.values()) {
			if(order.getCustomerUsername().equals(username)) {
				orders.add(order);
			}
		}
		return orders;
	}
	public Collection<Order> editOrders(ArrayList<Order> orders) {
		
		//update hashmap
		Deserialize();
		orders.forEach(order -> {
            String orderId = order.getId();
            if (Orders.containsKey(orderId)) {
                Order existingOrder = Orders.get(orderId);
                
                existingOrder.setPrice(order.getPrice());
                existingOrder.setObject(order.getObject());
                existingOrder.setStatus(order.getStatus());
                if(existingOrder.getReason().equals(""))
                	existingOrder.setReason(order.getReason());
                existingOrder.setVehicles(order.getVehicles());
            }
        });
		allOrders = new ArrayList<Order>();
		for(Order order : Orders.values())
			allOrders.add(order);
		Serialization();
		return orders;
	}
}
