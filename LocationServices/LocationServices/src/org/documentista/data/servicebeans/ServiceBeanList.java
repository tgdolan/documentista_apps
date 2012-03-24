package org.documentista.data.servicebeans;

import java.util.ArrayList;

import java.util.Iterator;

public class ServiceBeanList extends ArrayList<ServiceBean> {
	
	private static final String JSON_START = "{";
	private static final String JSON_END = "}";
	private static final String QUOTE ="\"";
	private static final String JSON_VALUE_SEPARATOR = ":";
	private static final String JSON_ITEM_SEPARATOR = ",";
	private static final String JSON_COLLECTION_START = "[";
	private static final String JSON_COLLECTION_END = "]";
	
	public String toJSON() throws IllegalArgumentException, IllegalAccessException {
		
		StringBuilder json = new StringBuilder(JSON_START);
		json.append(QUOTE);
		json.append("serviceBeans");
		json.append(QUOTE);
		json.append(JSON_VALUE_SEPARATOR);
		json.append(JSON_COLLECTION_START);
		
		Iterator<ServiceBean> itemIter = iterator();
		int i = 1;
		while (itemIter.hasNext()) {
			json.append(itemIter.next().toJSON());
			if (i < size()) {
				json.append(JSON_ITEM_SEPARATOR);
			}
			i++;
		}
		
		json.append(JSON_COLLECTION_END);
		
		json.append(JSON_END);
		
		return json.toString();
	}

}
