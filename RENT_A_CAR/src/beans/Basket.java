package beans;

import java.util.ArrayList;

public class Basket {
	private ArrayList<Vehicle> vehicles;
	private String username;
	private double price;
	private int id;
	public Basket() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Basket(ArrayList<Vehicle> vehicles, String username, double price) {
		super();
		this.vehicles = vehicles;
		this.username = username;
		this.price = price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}
	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
