package beans;

public class Location {

	private int id;
	private double latitude;	//sirina
	private double longitude;   //duzina
	private Address address;
	private String image;
	public Location() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Location(double latitude, double longitude, Address address,String image) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.image=image;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
}
