package sort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import sort.models.Bid;
import sort.models.Bidder;
import sort.models.Site;

/**
 * Main class to run auction application
 * @author Perez
 *
 */
public class AuctionRunner {

	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		HashMap<String, Site> siteMap = null;
		HashMap<String, Bidder> bidderMap = null;
		 try {
			 //Read config and populate sitemap and biddermap
			 JSONObject config = (JSONObject) parser.parse(new FileReader("/auction/config.json")); 
			 siteMap = getSiteMap((JSONArray) config.get("sites"));
			 bidderMap = getBidderMap((JSONArray) config.get("bidders"));
			 
			 //populate auctionmap with sites and their highest bid per unit
			 HashMap<String, HashMap<String, Bid>> auctionMap = new HashMap<String, HashMap<String, Bid>>();
			 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			 Iterator<JSONObject> auctionIter = ((JSONArray) parser.parse(br)).iterator();
			 while(auctionIter.hasNext()) {
				 JSONObject auction = auctionIter.next();
				 HashSet<String> auctionUnits = getAuctionUnits((JSONArray)auction.get("units"));
				 if(siteMap.containsKey((String)auction.get("site"))){
					 Site currSite = siteMap.get((String)auction.get("site"));
					 HashMap<String, Bid> unitBids = auctionMap.getOrDefault((String)auction.get("site"), new HashMap<String, Bid>());
					 JSONArray bidsArr = (JSONArray) auction.get("bids");
					 Iterator<JSONObject> bidIter = bidsArr.iterator();
					 while(bidIter.hasNext()) {
						 JSONObject currBidObj = bidIter.next();
						 Bid currBid = new Bid((String)currBidObj.get("bidder"), (Long)currBidObj.get("bid"), (String)currBidObj.get("unit"));
						 double adjustedCurrBid = currBid.getBid() + (currBid.getBid() * bidderMap.get(currBid.getBidder()).getAdjustment());
						 if(currSite.isValidBidder(currBid.getBidder()) && auctionUnits.contains(currBid.getUnit()) && adjustedCurrBid > currSite.getFloor()) {
							 if(unitBids.containsKey(currBid.getUnit())) {								 
								 Bid hiBid = unitBids.get(currBid.getUnit());
								 double adjustedHiBid = hiBid.getBid() + (hiBid.getBid() * bidderMap.get(hiBid.getBidder()).getAdjustment());								 
								 if(adjustedHiBid < adjustedCurrBid ) {
									 unitBids.put(currBid.getUnit(), currBid);
								 }
							 } else {
								 unitBids.put(currBid.getUnit(), currBid);
							 }
						 }
					 }
					 auctionMap.put((String)auction.get("site"), unitBids);
				 }				 
			 }
			 
			 writeOutput(auctionMap);
			 
			 
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}
	
	private static HashMap<String, Site> getSiteMap(JSONArray siteList){
		HashMap<String, Site> siteMap = new HashMap<String, Site>();
		Iterator<JSONObject> siteIter = siteList.iterator();
		 while(siteIter.hasNext()) {
			 JSONObject site = siteIter.next();
			 Site newSite = new Site((String)site.get("name"), (Long)site.get("floor"));
			 JSONArray bidderList = (JSONArray) site.get("bidders");
			 Iterator<String> bidderIter = bidderList.iterator();
			 while(bidderIter.hasNext()) {
				 newSite.addValidBidder((String)bidderIter.next());
			 }
			 siteMap.put((String)site.get("name"), newSite);
		 }
		 return siteMap;
	}
	
	private static HashMap<String, Bidder> getBidderMap(JSONArray bidderList){
		HashMap<String, Bidder> bidderMap = new HashMap<String, Bidder>();
		Iterator<JSONObject> bidderIter = bidderList.iterator();
		 while(bidderIter.hasNext()) {
			 JSONObject bidder = bidderIter.next();
			 Bidder newBidder = new Bidder((String)bidder.get("name"), ((Number)bidder.get("adjustment")).doubleValue());
			 
			 bidderMap.put((String)bidder.get("name"), newBidder);
		 }
		 return bidderMap;
	}
	
	private static HashSet<String> getAuctionUnits(JSONArray units){
		HashSet<String> unitSet = new HashSet<String>();
		for(int i = 0; i < units.size(); i++)
			unitSet.add((String)units.get(i));
		return unitSet;
	}
	
	private static void writeOutput(HashMap<String, HashMap<String, Bid>> auctions) {
		JSONArray siteBids = new JSONArray();
		Iterator<String> auctionMapIter = auctions.keySet().iterator();
		while(auctionMapIter.hasNext()) {
			 HashMap<String, Bid> unitBidMap = auctions.get(auctionMapIter.next());
			 Iterator<Bid> unitBidIter = unitBidMap.values().iterator();
			 JSONArray unitBids = new JSONArray();
			 while(unitBidIter.hasNext()) {
				 Bid curr = unitBidIter.next();
				 unitBids.add(curr.toJsonObject());
			 }
			 siteBids.add(unitBids);
	    }
		System.out.println(siteBids.toJSONString());
	}

}
