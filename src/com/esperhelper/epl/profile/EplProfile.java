package com.esperhelper.epl.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Bean for storing 3 types of EPL considerations:
 * 	- Event package name
 * 	- Non-SELECT EPL statements
 * - SELECT EPL statements + listener (only 1 listener as of 10/18) 
 * 
 * @author Gene
 *
 */
public class EplProfile {

	private String eventPackageName;
	
	private List<String> esperServiceOnlyEPLs = new ArrayList<String>();
	
	private Map<String, List<String>> mapListenerToEPL = new HashMap<String, List<String>>();
	
	public void addEPL (String epl) {
		
		esperServiceOnlyEPLs.add(epl);
	}

	public void addEPL (String listener, String epl) {
		
		List<String> eplList = mapListenerToEPL.get(listener);
		
		if (eplList == null) {
			eplList = new ArrayList<String>();
			mapListenerToEPL.put(listener, eplList);
		}
		
		eplList.add(epl);
	}

	public String getEventPackageName() {
		return eventPackageName;
	}

	public void setEventPackageName(String eventPackageName) {
		this.eventPackageName = eventPackageName;
	}

	public List<String> getEsperServiceOnlyEPLs() {
		return esperServiceOnlyEPLs;
	}

	public Map<String, List<String> > getMapListenerToEPL() {
		return mapListenerToEPL;
	}

	@Override
	public String toString() {
		return "ProfileEPL [" 
				+ "\n\n eventPackageName=" + eventPackageName
				+ "\n\n esperServiceOnlyEPLs=" + esperServiceOnlyEPLs
				+ "\n\n mapListenerToEPL=" + mapListenerToEPL + "]";
	}
	
	
	
}
