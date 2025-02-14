package in.co.rays.project_3.dto;
import java.util.Date;

public class OrderDTO {

	private String productName;
	private Date orderDate;
	private long quantity;	
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	private String customer;

	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
}
