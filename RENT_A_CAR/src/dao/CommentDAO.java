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
import beans.Comment;
import beans.Customer;
import beans.Comment.CommentStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import beans.DateSerializer;
import beans.Location;
import beans.Order;
import beans.RentACarObject;
import beans.User;
import beans.Order.OrderStatus;

public class CommentDAO {
	private HashMap<String, Comment> Objects = new HashMap<>();
	private ArrayList<Comment> allObjects = new ArrayList<Comment>();
	private String filePath;
	private Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting().create();//.setDateFormat("yyyy-mm-dd").create();
	public CommentDAO() {
		
	}
	public CommentDAO(String contextPath) {
		
		String path = contextPath;

        filePath = path.concat("comments.json");
        try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Comment>>(){}.getType();
            allObjects = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Comment object : allObjects) {

            Objects.put(object.getOrderId(), object);
            
        }

	}
	public Comment save(Comment comment) {
		Deserialize();
		
		//comment will have id from it's order
		allObjects.add(comment);
		Objects.put(comment.getOrderId(), comment);
		Serialization();
		
		return comment;
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
		allObjects = new ArrayList<Comment>();
		Objects = new HashMap<String, Comment>();
		
		//deserialize customers
		try (FileReader fileReader = new FileReader(this.filePath)) {
            Type userListType = new TypeToken<ArrayList<Comment>>(){}.getType();
            allObjects = gson.fromJson(fileReader, userListType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Comment comment : allObjects) {
        	Objects.put(comment.getOrderId(), comment);
            
        }
        
	}
	
	public void declineComments(String username)
	{
		Deserialize();
		Collection<Comment> comments = findByUsername(username);
		
		if(comments!=null) {
		 comments.forEach(comment -> {
	            String orderId = comment.getOrderId();	// = comment id
	            if (Objects.containsKey(orderId)) {
	                Comment existingComment = Objects.get(orderId);
	                
	                if(existingComment.getStatus().toString().equals("Processing"))
	                	existingComment.setStatus(CommentStatus.Declined);
	            }
	        });
		allObjects = new ArrayList<Comment>();
		for(Comment comment : Objects.values())
			allObjects.add(comment);
		}
	
		Serialization();
	}
	public Comment findById(String id)
	{
		Deserialize();
		return Objects.values().stream().filter(comment -> comment.getOrderId().equals(id) && !comment.isRemoved()).findFirst().orElse(null);
	}
	
	public Collection<Comment> findByObjectId(int id)
	{
		Deserialize();
		ArrayList<Comment> retVal = new ArrayList<Comment>();
		for(Comment comment : Objects.values())
		{
			if(comment.getObject().getId()==id && comment.getStatus().toString().equals("Approved"))
				retVal.add(comment);
		}
		return retVal;
	}
	
	public Collection<Comment> findByUsername(String username)
	{
		Deserialize();
		ArrayList<Comment> retVal = new ArrayList<Comment>();
		for(Comment comment : Objects.values())
		{
			if(comment.getCustomer().getUsername().equals(username))
				retVal.add(comment);
		}
		return retVal;
	}
	
	public Collection<Comment> findAll()
	{
		Deserialize();
		return Objects.values();
	}
	public Comment edit(String id,Comment comment) {
		
		//update hashmap
		Deserialize();
		for(Comment b : Objects.values())
		{
			if(b.getOrderId().equals(comment.getOrderId()))
			{
				Objects.remove(b);
				break;
			}
		}
		
		Objects.put(comment.getOrderId(), comment);
		for(Comment b : allObjects)
		{
			if(b.getOrderId().equals(comment.getOrderId()))
			{
				allObjects.remove(b);
				break;
			}
		}
		allObjects.add(comment);			
		Serialization();			
		return comment;
	}
	public Collection<Comment> findByCustomer(String username)
	{
		Deserialize();
		ArrayList<Comment> comments=new ArrayList<Comment>();
		for(Comment comment : findAll()) {
			if(comment.getCustomer().getUsername().equals(username)) {
				comments.add(comment);
			}
		}
		return comments;
	}
	
	//returns collection od approved comments (updated with removed field)
	public Collection<Comment> getUpdatedApprovedComments(Comment removed) {
		
		//update hashmap
		Deserialize();
		for(Comment comment : Objects.values())
		{
			if(comment.getOrderId().equals(removed.getOrderId()))
			{
				Objects.remove(comment);
				break;
			}
		}
		Objects.put(removed.getOrderId(), removed);
		for(Comment comment : allObjects)
		{
			if(comment.getOrderId().equals(removed.getOrderId()))
			{
				allObjects.remove(comment);
				break;
			}
		}
		allObjects.add(removed);
		
		
		Serialization();
		
		return findByObjectId(removed.getObject().getId());
	}

}
