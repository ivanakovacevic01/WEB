package beans;

public class Comment {
	public enum CommentStatus{
		Processing,
		Approved,
		Declined
	}
	private Customer customer;
	private RentACarObject object;
	private String text;
	private int grade;
	private String orderId;
	private CommentStatus status;
	private boolean removed;
	

	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Comment(String orderId, Customer customer, RentACarObject object, String text, int grade, CommentStatus status, boolean removed) {
		super();
		this.customer = customer;
		this.object = object;
		this.text = text;
		this.grade = grade;
		this.orderId = orderId;
		this.status = status;
		this.removed = removed;
	}
	
	public boolean isRemoved() {
		return removed;
	}
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
	public CommentStatus getStatus() {
		return status;
	}
	public void setStatus(CommentStatus status) {
		this.status = status;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public RentACarObject getObject() {
		return object;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String id) {
		this.orderId = id;
	}
	public void setObject(RentACarObject object) {
		this.object = object;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	

}
