package org.documentista.services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.documentista.data.providers.SQLDataProvider;
import org.documentista.data.servicebeans.Location;
import org.documentista.data.servicebeans.ServiceBeanList;

/**
 * Servlet implementation class DinerServices
 */
public class DinerServices extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String GET_DINERS_QUERY = "CALL GetDinersWithinRadius(?, ?, ?);";
	private static final String INSERT_DINER_QUERY = "INSERT INTO diners (DINER_NAME, STREET, CITY, STATE, ZIP, WEB_SITE, LATITUDE, LONGITUDE) values (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String DINER_NAME = "DINER_NAME";
	private static final String DINER_STREET = "STREET";
	private static final String DINER_CITY = "CITY";
	private static final String DINER_STATE = "STATE";
	private static final String DINER_ZIP = "ZIP";
	private static final String DINER_WEB_SITE = "WEB_SITE";
	private static final String DINER_LATITUDE = "LATITUDE";
	private static final String DINER_LONGITUDE = "LONGITUDE";
	
	private String JNDI_DATASOURCE = "jdbc/DinerDB";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DinerServices() {
        super();
        //set JNDI_DATASOURCE based on env. context.xml will have two datasources defined.
        String env = System.getProperty("serverEnv");
        if (env.equals("local")) {
        	JNDI_DATASOURCE = "jdbc/DinerDB_local";
        }
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SQLDataProvider sqlProvider = new SQLDataProvider();
		Connection connection;
		List<String> parms = new ArrayList<String>();
		populateParms(parms, request);
		try {
			
			connection = sqlProvider.getConnection(JNDI_DATASOURCE);
			
			ResultSet results = sqlProvider.getData(connection, GET_DINERS_QUERY, parms);
			
			//create ServiceBeanList for results
			ServiceBeanList serviceBeans = getServiceBeanCollection(results);
			String jsonResults = serviceBeans.toJSON();
			
			//now write json string to response
			response.getWriter().write(jsonResults);

		} catch (NamingException e) {
			response.getWriter().write(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			response.getWriter().write(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			response.getWriter().write(e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			response.getWriter().write(e.getLocalizedMessage());
		}
		
		
	}
	/*
	 * Retrieves form inputs from request and populates a list of parameters to
	 * be used in the "get" SQL query.
	 */
	private void populateParms(List<String> parms, HttpServletRequest request) {
		parms.add(request.getParameter("latitude"));
		parms.add(request.getParameter("longitude"));
		parms.add(request.getParameter("radius"));
		
	}
	
	/*
	 * Retrieves form inputs from request and populates a list of parameters to
	 * be used in the "post" SQL query.
	 */
	private void populatePostParms(List<String> parms, HttpServletRequest request) {
		parms.add(request.getParameter("dinerName"));
		parms.add(request.getParameter("street"));
		parms.add(request.getParameter("city"));
		parms.add(request.getParameter("state"));
		parms.add(request.getParameter("zip"));
		parms.add(request.getParameter("webSite"));
		parms.add(request.getParameter("latitude"));
		parms.add(request.getParameter("longitude"));	
	}

	private ServiceBeanList getServiceBeanCollection(ResultSet results) throws SQLException {
		ServiceBeanList serviceBeans = new ServiceBeanList();
		
		while (results.next()) {
			Location diner = new Location();
			diner.setName(results.getString(DINER_NAME));
			diner.setStreet(results.getString(DINER_STREET));
			diner.setCity(results.getString(DINER_CITY));
			diner.setState(results.getString(DINER_STATE));
			diner.setZip(results.getString(DINER_ZIP));
			diner.setWebSite(results.getString(DINER_WEB_SITE));
			diner.setLatitude(String.valueOf(results.getDouble(DINER_LATITUDE)));
			diner.setLongitude(String.valueOf(results.getDouble(DINER_LONGITUDE)));
			
			serviceBeans.add(diner);
			
		}
		
		return serviceBeans;
	}

	/**
	 * Allows for the insertion of diner data into the DB via POST
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = null;
		List<String> parms = new ArrayList<String>();
		populatePostParms(parms, request);
		
		SQLDataProvider sqlProvider = new SQLDataProvider();
		Connection connection;
		
		try {
			
			connection = sqlProvider.getConnection(JNDI_DATASOURCE);
			sqlProvider.updateData(connection, INSERT_DINER_QUERY, parms);
			message = "Thanks!!";
		}
		catch(NamingException ne) {
			message = ne.getLocalizedMessage();
		} catch (SQLException e) {
			message = e.getLocalizedMessage();
		}
		
		response.getWriter().write(message);
		
	}
	
	private List getDinerData() {
		List dinerData = new ArrayList();
		return dinerData;
	};

}
