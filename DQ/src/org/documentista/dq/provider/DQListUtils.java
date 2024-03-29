package org.documentista.dq.provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.documentista.utils.data.USStates;



public class DQListUtils {

	private static final String DQ_URL = "http://www.dairyqueen.com/us-en/stores/me/";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Connection conn = null;

       
		DQList dqList = new DQList();
		
		for (USStates us_state : USStates.values()) {
			String dqURL = "http://www.dairyqueen.com/us-en/stores/" + us_state + "/";
			List<DQ> dairyQueens = dqList.getDairyQueens(DQ_URL);
			for (DQ dq : dairyQueens) {
				String name = dq.getName();
				String street = dq.getStreetAddr();
				String city = dq.getCity();
				String state = dq.getState();
				String zip = dq.getZip();
				String webSite = dq.getUrl();
				String telno = dq.getTelno();
				Double longitude = 0.0;
				Double latitude = 0.0;
				if(dq.getLongitude() != null && !dq.getLongitude().equals("")) {
					longitude = Double.valueOf(dq.getLongitude());
				}
				if (dq.getLatitude() != null && !dq.getLatitude().equals("")) {
					latitude = Double.valueOf(dq.getLatitude());
				}
				
				insertDQ(name, street, city, state, zip, webSite, telno, longitude, latitude);
			}
		}
		
		
	}

	public static void insertDQ(String name, String street, String city,
			String state, String zip, String webSite, String telno, Double longitude,
			Double latitude) {
		String insertStatement = "INSERT INTO DQ.DAIRY_QUEENS (DQ_NAME, STREET, CITY, STATE, ZIP, WEB_SITE, TELNO, LATITUDE, LONGITUDE) values (\"" + name +"\", \"" + street + "\", \"" + city + "\", \"" + state + "\", \"" + zip + "\", \"" + webSite + "\", \"" + telno + "\", \"" + longitude + "\", \"" + latitude + "\")";
		
		Connection conn = null;

        try
        {
            String userName = "root";
            String password = "hon360da";
//        	String userName = "tgdolanc_tom";
//            String password = "hondacl360";
//            String url = "jdbc:mysql://tgdolan.com:3306/tgdolanc_diners";
           String url = "jdbc:mysql://localhost:3306/DQ";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);
            Statement stmt = conn.createStatement();
            stmt.execute(insertStatement);
            
        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server " + e.getLocalizedMessage());
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                }                
                catch (Exception e) { /* ignore close errors */ }
            }
        }
    }

		


}
