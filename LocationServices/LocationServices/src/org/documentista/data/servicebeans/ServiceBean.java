package org.documentista.data.servicebeans;

import java.io.Serializable;
import java.lang.reflect.Field;

public abstract class ServiceBean implements Serializable {
	
	private static final String JSON_START = "{ \"serviceBean\" : [ ";
	private static final String JSON_END = " ] }";
	private static final String QUOTE ="\"";
	private static final String JSON_VALUE_SEPARATOR = ":";
	private static final String JSON_FIELD_SEPARATOR = ",";
	
	public String toJSON() throws IllegalArgumentException, IllegalAccessException {
		StringBuilder json = new StringBuilder(JSON_START);
		Field[] fields = getClass().getFields();
		int i = 1;
		for (Field field : fields) {
			
			json.append(QUOTE);
			json.append(field.getName());
			json.append(QUOTE);
			json.append(JSON_VALUE_SEPARATOR);
			json.append(QUOTE);
			//Note: Right now this will only work for fields with a "non-primitive" type (?)
			Object fieldValue = field.get(this);
			if (fieldValue != null) {
				json.append(fieldValue);
			}
			else {
				json.append(" ");
			}
			json.append(QUOTE);
			if (i < fields.length) {
				json.append(JSON_FIELD_SEPARATOR);
			}
			i++;
			
		}
		
		json.append(JSON_END);
		
		return json.toString();
	}

}
