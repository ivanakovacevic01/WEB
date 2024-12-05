package beans;
public class User{
	protected String firstName;
	protected String lastName;
	protected String birthDate;
	protected String username;
	protected String password;
	protected String gender;
	protected String role;
	protected boolean blocked;
	protected boolean loggedIn;
	//////////////////////////////////////////////sva iznajmnljivanja, nova klasa??????????????
	//fale nam tip kupca, korpa itd 
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String firstName, String lastName, String birthDate, String username, String password, String gender, boolean blocked, boolean loggedIn) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.blocked=blocked;
		this.loggedIn = loggedIn;
	}
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setRole(String role)
	{
		this.role = role;
	}
	public String getRole()
	{
		return this.role;
	}
	public boolean isBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	
}
