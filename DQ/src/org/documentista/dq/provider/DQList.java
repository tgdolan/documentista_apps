package org.documentista.dq.provider;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.documentista.utils.googlemaps.GoogleMapClient;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.xml.sax.SAXException;


public class DQList {
	
	public static void main(String[] args) {
		DQList dqList = new DQList();
		List<DQ> dqs = dqList.getDairyQueens(URL);
		System.out.println("I found " + dqs.size() + " DQs.");
	}
	
//private static final String DDD_URL = "http://www.foodnetwork.com/shows/guys-diners-drive-ins-and-dives/index.html";
	private static final String URL = "http://www.dairyqueen.com/us-en/stores/me/";
	//private static final String NODE_PATH = "//container/";
	
	public List<DQ> getDairyQueens(String siteURL) {
		ArrayList<DQ> dairyQueens = new ArrayList<DQ>();
		
		try {
			dairyQueens = parseSiteForDiners(siteURL);
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (DQ dq : dairyQueens) {
			
				setLatLong(dq);


		}
		
		return dairyQueens;
	}

	private ArrayList<DQ> parseSiteForDiners(String dinerSiteURL) throws XPatherException, ParserConfigurationException,SAXException, IOException, XPatherException {
		ArrayList<DQ> diners = new ArrayList<DQ>();
		
		// this is where the HtmlCleaner comes in, I initialize it here
	        HtmlCleaner cleaner = new HtmlCleaner();
	        CleanerProperties props = cleaner.getProperties();
	        props.setAllowHtmlInsideAttributes(true);
	        props.setAllowMultiWordAttributes(true);
	        props.setRecognizeUnicodeChars(true);
	        props.setOmitComments(true);
	 
	        // open a connection to the desired URL
	        URL url = new URL(dinerSiteURL);
	        URLConnection conn = url.openConnection();
	 
	        //use the cleaner to "clean" the HTML and return it as a TagNode object
	        TagNode node = cleaner.clean(new InputStreamReader(conn.getInputStream()));
	        
	        // once the HTML is cleaned, then you can run your XPATH expressions on the node, which will then return an array of TagNode objects (these are returned as Objects but get casted below)
	       // Object[] diner_nodes = node.evaluateXPath("//div[@class='body-text']/P");
	        Object[] diner_nodes = node.evaluateXPath("//ul[@id='results']/li/div[@class='vcard']");
	        
	        for (Object dinerNode : diner_nodes) {
	        	try {
	        		DQ diner = new DQ((TagNode)dinerNode);
	        		if (diner.getName() != null) {
	        			diners.add(diner);
	        		}
	        	}
	        	catch (Exception e) {
	        		e.printStackTrace();
	        	}
	        	
	        }

		return diners;
	}
	
	public void setLatLong(DQ dq) {
		String street = dq.getStreetAddr();
		String city = dq.getCity();
		String state = dq.getState();
		
		if (validateInputs(street, city, state)) {
			GoogleMapClient googleClient = new GoogleMapClient();
			String[] latLong = googleClient.getDinerLatLong(street, city, state);
			dq.setLatitude(latLong[0]);
			dq.setLongitude(latLong[1]);
		}
		
	}
	
	private boolean validateInputs(String street, String city, String state) {
		
		if (street == null || street.equals(""))
			return false;
		
		if (city == null || city.equals(""))
			return false;
		
		if (state == null || state.equals(""))
			return false;
		
		return true;
	}
	

}
