package sort.models;

import org.json.simple.JSONObject;

public class Bid {
	String bidder;
	Long bid;
	String unit;
	
	
	
	public Bid(String bidder, Long bid, String unit) {
		this.bidder = bidder;
		this.bid = bid;
		this.unit = unit;
	}
	public String getBidder() {
		return bidder;
	}
	public void setBidder(String bidder) {
		this.bidder = bidder;
	}
	public Long getBid() {
		return bid;
	}
	public void setBid(Long bid) {
		this.bid = bid;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public JSONObject toJsonObject() {
		JSONObject bidObj = new JSONObject();
		bidObj.put("bidder", bidder);
		bidObj.put("bid", bid);
		bidObj.put("unit", unit);
		return bidObj;
	}
	
}
