package beans;

import java.util.ArrayList;

import beans.UserType.TypeName;

public class Customer extends User {
	private ArrayList<Order> orders;
	private double collectedPoints=0;
	private Basket basket;
	private UserType type;
	private boolean removed;
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Customer(String FirstName, String LastName, String BirthDate, String Username, String Password, String Gender, ArrayList<Order> orders,boolean Blocked,boolean Removed) {

		super();
		firstName = FirstName;
		lastName = LastName;
		birthDate = BirthDate;
		username = Username;
		password = Password;
		gender = Gender;
		this.orders = orders;
		blocked = Blocked;
		this.removed = Removed;
	}
	public ArrayList<Order> getOrders() {
		return orders;
	}
	public void setRentals(ArrayList<Order> orders) {
		this.orders = orders;
	}
	public double getCollectedPoints() {
		return collectedPoints;
	}
	public void setCollectedPoints(double collectedPoints) {
		this.collectedPoints = collectedPoints;
	}
	public Basket getBasket() {
		return basket;
	}
	public void setBasket(Basket basket) {
		this.basket = basket;
	}
	public UserType getType() {
		return type;
	}
	public void setType(UserType bronze) {
		this.type = bronze;
	}
	/*public boolean IsBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}*/
	public boolean isRemoved() {
		return removed;
	}
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
	
}
