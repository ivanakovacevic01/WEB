package beans;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;


public class Order {
	public enum OrderStatus{
		Processing,
		Approved,
		Received,	
		Returned,
		Declined,
		Canceled
	}
	private String id;	//da li provejra za 10 karaktera da ide pri pravljenju objekta
	private ArrayList<Vehicle> vehicles;
	private RentACarObject object;
	private int objectId;
	private String startDateTime;
	private double duration;
	private double price;
	private String customer;	//provjeriti treba li samo ime i prezime ili citav user?
	private OrderStatus status;
	private String reason;
	private String customerUsername;
	private String cancellationDate;
	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Order(String id, ArrayList<Vehicle> vehicles, RentACarObject object, String startDateTime,
			double duration, double price, String customer, OrderStatus status, String cancellationDate) {
		super();
		this.id = id;
		this.vehicles = vehicles;
		this.object = object;
		this.startDateTime = startDateTime;
		this.duration = duration;
		this.price = price;
		this.customer = customer;
		this.status = status;
		this.cancellationDate = cancellationDate;
	}
	public String getCancellationDate() {
		return cancellationDate;
	}
	public void setCancellationDate(String cancellationDate) {
		this.cancellationDate = cancellationDate;
	}
	public String getId() {
		return id;
	}
	public int getObjectId() {
		return objectId;
	}
	public String getCustomerUsername() {
		return customerUsername;
	}
	public void setCustomerUsername(String customerUsername) {
		this.customerUsername = customerUsername;
	}
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}
	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}
	public RentACarObject getObject() {
		return object;
	}
	public void setObject(RentACarObject object) {
		this.object = object;
	}
	public String getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(String dateTime) {
		this.startDateTime = dateTime;
	}
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	

}
