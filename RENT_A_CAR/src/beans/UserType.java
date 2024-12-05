package beans;


public class UserType {
	public enum TypeName {
		Gold, 
		Bronze,
		Silvern
	}
	private TypeName typename;
	private double percent;
	private double pointsNumber;
	private int id;
	public UserType() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserType(TypeName typename, double percent, double pointsNumber) {
		super();
		this.typename = typename;
		this.percent = percent;
		this.pointsNumber = pointsNumber;
	}
	public TypeName getTypename() {
		return typename;
	}
	public void setTypename(TypeName typename) {
		this.typename = typename;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public double getPointsNumber() {
		return pointsNumber;
	}
	public void setPointsNumber(double pointsNumber) {
		this.pointsNumber = pointsNumber;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
