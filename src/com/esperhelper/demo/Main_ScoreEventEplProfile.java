package com.esperhelper.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.esperhelper.epl.profile.EplProfileDeployer;
import com.esperhelper.event.score.ScoreEvent;
import com.espertech.esper.client.EPServiceProvider;

public class Main_ScoreEventEplProfile {
	
	public static void main (String... args) throws Exception {

		String eplPath = ".\\input\\config1.epl.txt";

		if (args.length > 0) {
			eplPath = args[0];			
		}

		test1(eplPath);
	}

	public static void test1 (String eplPath) {
		
		EplProfileDeployer profileDeploy = new EplProfileDeployer();

		File aFile = new File(eplPath);
		
		profileDeploy.run(aFile);

		
		List<Object> esperEvents = getEsperEvents();

		sendEsperEvents(esperEvents, profileDeploy.getEpService() ); 
	}
	
	public static void sendEsperEvents (List<Object> esperEvents, EPServiceProvider epService) {
		
		for (Object event : esperEvents) {

			System.out.println("\n >> SENDING EVENT >> to esper = " + event);
			epService.getEPRuntime().sendEvent(event);
		}
	}


	public static List<Object> getEsperEvents () {
		
		int [] scoreValues = { 2, 4, 3  };
		
		List<Object> retList = new ArrayList<Object>();
		
		ScoreEvent event = null;
		
		for (int curVal : scoreValues) {

			event = new ScoreEvent("field_goal", curVal);
			retList.add(event);
		}
		
		return retList;
	}

}
