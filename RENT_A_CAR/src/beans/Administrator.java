package beans;

public class Administrator extends User {
	//private boolean blocked;
	public Administrator() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Administrator(String FirstName, String LastName, String BirthDate, String Username, String Password, String Gender, boolean Blocked) {
		super();
		firstName = FirstName;
		lastName = LastName;
		birthDate = BirthDate;
		username = Username;
		password = Password;
		gender = Gender;
		blocked = Blocked;
	}
	/*public boolean IsBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}*/

}
