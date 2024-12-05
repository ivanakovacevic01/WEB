package beans;

public class Manager extends User {
	private RentACarObject object;


	private boolean removed;


	public Manager() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Manager(String firstName, String lastName, String birthDate, String username, String password,
			String gender, RentACarObject object, boolean blocked, boolean loggedIn, boolean Removed) {
		super(firstName, lastName, birthDate, username, password, gender, blocked, loggedIn);
		this.object=object;
		this.removed=Removed;
	}
	

	public RentACarObject getObject() {
		return object;
	}

	public void setObject(RentACarObject object) {
		this.object = object;
	}
	public boolean isRemoved() {
		return removed;
	}
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
}
