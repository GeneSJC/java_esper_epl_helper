package com.esperhelper.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class BaseListener implements UpdateListener {

	protected String label = "BaseListener";
	
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		
		echoProps(newEvents);
	}

	public void echoProps (EventBean[] newEvents) {

		System.out.println("\n -- ENTER BaseListener for : " + label+ "::   ");
		
		EventBean event = newEvents[0];	
		String[] props = event.getEventType().getPropertyNames();
		
		Arrays.sort(props);
		
		for(int i = 0; i < props.length; i++) {			
	 
			Object propValue = event.get(props[i]);

			String output = getPropValueString(propValue);
									
			System.out.print(" :: " + props[i]+" -> "+ output +"");
			
			if ( i < props.length - 1 ) {
	    		System.out.print("\n");
	    	}
	    }
		
	    System.out.println("");

	}

	@SuppressWarnings("unchecked")
	private String getPropValueString(Object propValue) {
		
		String output = null;
		
		if ( propValue instanceof HashMap) {

			output = getMapString(propValue);
		}
		else {
			output = propValue.toString();
		}

		return output;
	}

	@SuppressWarnings("unchecked")
	private String getMapString (Object propValue) {

		Map<String, Object> mapPropValue = (HashMap<String, Object>) propValue;
		
		StringBuffer sb = new StringBuffer();
		sb.append ("  ");
		Set<String> fieldNames = mapPropValue.keySet();
	    List<String> list = new ArrayList<String>(fieldNames);
	    Collections.sort(list);
	    
		int counter = 0;
		for (String curField : list) {
			counter++;
			Object theVal = mapPropValue.get(curField);
			sb.append( curField + " = " + theVal);
			if (counter < fieldNames.size() ) {
				sb.append(",\t");
			}
		}
					
		return sb.toString();		
	}
}
