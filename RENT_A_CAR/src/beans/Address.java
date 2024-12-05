package beans;

public class Address {
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String street;
	private String number;
	private String city;
	private String country;
	private String zipCode;
	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Address(String street, String number, String city, String country, String zipCode) {
		super();
		this.street = street;
		this.number = number;
		this.city = city;
		this.country = country;
		this.zipCode = zipCode;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	
}
