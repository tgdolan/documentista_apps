package org.documentista.dq.provider;

import java.util.List;

import org.htmlcleaner.ContentToken;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class DQ {
	//TODO: Create abstract base class "Location" from which this and others can extend
	private String DQ_BASE_URL = "http://www.dairyqueen.com";
	private String url;
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getStreetAddr() {
		return streetAddr;
	}

	public void setStreetAddr(String streetAddr) {
		this.streetAddr = streetAddr;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTelno() {
		return telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
	}

	private String zip;
	private String name;
	public void setName(String name) {
		this.name = name;
	}

	private String streetAddr;
	private String city;
	private String state;
	private String telno;
	private String latitude;
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	private String longitude;
	//no arg constructor currently used by JUnit test
	public DQ () {};
	
	/*
	 * TagNode coming in consists of this:
	 * <div class="vcard">
											<div class="fn n org">
												<span class="given-name"><a href="/us-en/store-details/20/">DAIRY QUEEN BRAZIER</a></span>
											</div>
											<div class="adr">
												<div class="street-div">174 STATE RD</div>
												<span class="locality">KITTERY</span>,
												<span class="region">ME</span>

												<span class="postal-code">03904-1536</span>
											</div>
											<div>
												<span class="tel"></span>
											</div>
										</div>
	 */
	public DQ(TagNode node) {
		//parse p into diner bits
		List<Object> dinerInfoBits = node.getChildren();
		Object[] nameNodes = null;
		Object[] addrNodes = null;
		Object[] telNodes = null;
		try {
			nameNodes = node.evaluateXPath("//span[@class='given-name']");
			TagNode nameNode = (TagNode) nameNodes[0];
			this.name = nameNode.getText().toString();
			this.url = DQ_BASE_URL + nameNode.findElementByName("a", false).getAttributeByName("href");
			
			addrNodes = node.evaluateXPath("//div[@class='adr']");
			TagNode addrNode = (TagNode) addrNodes[0];
			this.streetAddr = addrNode.findElementByAttValue("class", "street-div", false, false).getText().toString();
			this.city = addrNode.findElementByAttValue("class", "locality", false, false).getText().toString();
			this.state = addrNode.findElementByAttValue("class", "region", false, false).getText().toString();
			this.zip = addrNode.findElementByAttValue("class", "postal-code", false, false).getText().toString();
			
			telNodes = node.evaluateXPath("//div/span[@class='tel']");
			TagNode telNode = (TagNode) telNodes[0];
			this.telno = telNode.getText().toString();
			
		} catch (XPatherException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}


	public String getName() {
		return name;
	}
	
}

