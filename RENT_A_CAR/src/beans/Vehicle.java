package beans;

import java.awt.Image;

public class Vehicle {
	public enum Kind {
		Manual,
		Automatic
	}
	public enum FuelType {
		Diesel,
		Petrol,
		Hybrid,
		Electric
	}
	public enum Status {
		Available,
		Rented
	}
	private String brand;
	private String model;
	private double price;
	private String type;	//string?
	private int objectId;
	private Kind kind;
	private FuelType fuelType;
	private double consumption;	//potrosnja
	private int doorsNumber;
	private int personNumber;
	private String description;
	private String image; //mozda treba ipak prebaciti u image al to vidjeti jos 
	private Status status;
	private int id;
	private int quantity;
	private boolean removed;
	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public Vehicle() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Vehicle(String brand, String model, double price, String type, int objectId, Kind kind,
			FuelType fuelType, double consumption, int doorsNumber, int personNumber, String description, String image,
			Status status) {
		super();
		this.brand = brand;
		this.model = model;
		this.price = price;
		this.type = type;
		this.objectId = objectId;
		this.kind = kind;
		this.fuelType = fuelType;
		this.consumption = consumption;
		this.doorsNumber = doorsNumber;
		this.personNumber = personNumber;
		this.description = description;
		this.image = image;
		this.status = status;
	}
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getObjectId() {
		return objectId;
	}
	public void setObject(int objectId) {
		this.objectId = objectId;
	}
	public Kind getKind() {
		return kind;
	}
	public void setKind(Kind kind) {
		this.kind = kind;
	}
	public FuelType getFuelType() {
		return fuelType;
	}
	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}
	public double getConsumption() {
		return consumption;
	}
	public void setConsumption(double consumption) {
		this.consumption = consumption;
	}
	public int getDoorsNumber() {
		return doorsNumber;
	}
	public void setDoorsNumber(int doorsNumber) {
		this.doorsNumber = doorsNumber;
	}
	public int getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(int personNumber) {
		this.personNumber = personNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	
}
