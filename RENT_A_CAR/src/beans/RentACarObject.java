package beans;

import java.awt.Image;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;


public class RentACarObject {
	public enum State {
		Opened,
		Closed
	}
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String name;
	private ArrayList<Vehicle> vehicles;	//vozila u ponudi
	private String startTime;
	private String endTime;
	private State state;
	private Location location;
	private String logo;
	private double grade;
	public String locationS;
	private boolean removed;
	
	public RentACarObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RentACarObject(String name, ArrayList<Vehicle> vehicles, String startTime, String endTime, State state, Location location,
			String logo, double grade, boolean Removed) {
		super();
		this.name = name;
		//this.vehicles = vehicles;
		this.startTime = startTime;
		this.endTime = endTime;
		this.state = state;
		this.location = location;
		setLocationS(location.getAddress().getStreet()+" "+location.getAddress().getNumber()+", " +location.getAddress().getCity());
		this.logo = logo;
		this.grade = grade;
		this.removed=Removed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public State getState() {
		return state;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
		setLocationS(location.getAddress().getStreet()+" "+location.getAddress().getNumber()+", " +location.getAddress().getCity());
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}
	
	public String getLocationS() {
		return locationS;
	}
	public void setLocationS(String locationS) {
		this.locationS = locationS;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
	
	
}
