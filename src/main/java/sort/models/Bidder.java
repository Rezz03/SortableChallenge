package sort.models;

public class Bidder {
	String name;
	double adjustment;
	public Bidder(String aName, double adjust) {
		name = aName;
		adjustment = adjust;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getAdjustment() {
		return adjustment;
	}
	public void setAdjustment(double adjustment) {
		this.adjustment = adjustment;
	}
	
	
}
