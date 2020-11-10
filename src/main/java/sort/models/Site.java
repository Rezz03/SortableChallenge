package sort.models;

import java.util.HashSet;
import java.util.List;

/**
 * Site with valid bidders and a bidding floor
 * @author Perez
 *
 */
public class Site {
	String name;
	HashSet<String> bidders;
	double floor;
	
	public Site(String aName, double aFloor) {
		name = aName;
		floor = aFloor;
		bidders = new HashSet<String>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getFloor() {
		return floor;
	}
	public void setFloor(double floor) {
		this.floor = floor;
	}	
	
	public void addValidBidder(String bidName) {
		bidders.add(bidName);
	}
	
	public boolean isValidBidder(String bidName) {
		return bidders.contains(bidName);
	}
	
}
