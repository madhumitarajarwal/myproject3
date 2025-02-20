package in.co.rays.project_3.dto;

import java.util.Date;

public class PurchaseDTO extends BaseDTO{
	private long quantity;
	private int product;
	private Date orderDate;
	private long totalCost;
	

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public int getProduct() {
		return product;
	}

	public void setProduct(int product) {
		this.product = product;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public long getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(long totalCost) {
		this.totalCost = totalCost;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
