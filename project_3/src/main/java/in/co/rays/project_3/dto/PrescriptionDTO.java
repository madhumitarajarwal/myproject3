package in.co.rays.project_3.dto;

import java.util.Date;

public class PrescriptionDTO extends BaseDTO{
	private String name;
	private int decease;
	private int capacity;
	private Date dob;
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDecease() {
		return decease;
	}

	public void setDecease(int decease) {
		this.decease = decease;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
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
