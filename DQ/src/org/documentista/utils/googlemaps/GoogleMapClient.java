package org.documentista.utils.googlemaps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.android.maps.GeoPoint;

public class GoogleMapClient {
	private static final String GOOGLE_MAPS_URI = "http://maps.googleapis.com";
	private static final String GOOGLE_GEOCODE_URI = GOOGLE_MAPS_URI + "/maps/api/geocode/";
	private String output = "json";
	//private String address = "342 Goodwin Rd., Eliot, ME";
	//private String address;
	private String sensor = "&sensor=false";
	
//	public GeoPoint getLatLong(String address) {
//		
//		String[] latLong;
//		try {
//			String encodedAddress = URLEncoder.encode(address, "UTF-8");
//			latLong = executeHttpGet(encodedAddress);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		GeoPoint geoPoint = new GeoPoint(latLong[0], latLong[1]);
//		return geoPoint;
//	}

//	public String[] executeHttpGet(String encodedAddress) throws Exception {
//		
//		try {
//			HttpClient googleMapClient = new DefaultHttpClient();
//			
//			String serviceCallURI = GOOGLE_GEOCODE_URI + output + "?address=" + encodedAddress + sensor;
//			
//			HttpGet method = new HttpGet(serviceCallURI);
//			HttpResponse response = googleMapClient.execute(method);
//			InputStream googleResponse = response.getEntity().getContent();
//			String googleJSON = convertStreamToString(googleResponse);
//			
//			JSONObject jsonObj = (JSONObject) new JSONObject(googleJSON);
//			JSONArray results = jsonObj.getJSONArray("results");
//
//			String lat = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
//			String lng = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
//			String[] latLong = {lat, lng};	
//			return latLong;
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
	
	public String[] getDinerLatLong(String street, String city, String state) {
		BufferedReader in = null;
		String[] latLong = {"",""};
		try {
			HttpClient googleMapClient = new DefaultHttpClient();
			//HttpGet request = new HttpGet();
			//request.setURI(new URI(GOOGLE_MAPS_URI));
			String address = street + ", " + city + ", " + state;
			String encodedAddress = URLEncoder.encode(address, "UTF-8");
			String serviceCallURI = GOOGLE_GEOCODE_URI + output + "?address=" + encodedAddress + sensor;
			
			//String signedURI = UrlSigner.signURL(serviceCallURI);
			HttpGet method = new HttpGet(serviceCallURI);
			HttpResponse response = googleMapClient.execute(method);
			
			InputStream googleResponse = response.getEntity().getContent();
			String googleJSON = convertStreamToString(googleResponse);
			
			JSONObject jsonObj = (JSONObject) new JSONObject(googleJSON);
			JSONArray results = jsonObj.getJSONArray("results");
			if (results.length() > 0) {
			String lat = ((Double) results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat")).toString();
			String lng = ((Double) results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng")).toString();
			latLong[0] = lat;	
			latLong[1] = lng;
			}
			
			
		}
		catch (org.json.JSONException jsone) {
			//TODO: logging
			System.out.println("Bad geo lookup for : " + city + ", " + state);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return latLong;
	}
	
	/*
	 * Convert InputStream from URL to String. 
	 */
	private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
