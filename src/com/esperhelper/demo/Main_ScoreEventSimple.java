package com.esperhelper.demo;

import com.esperhelper.event.score.AllScoresListener;
import com.esperhelper.event.score.ScoreEvent;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

public class Main_ScoreEventSimple {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.esperhelper.event.score");
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);
				
		String expression = "select avg(score) from ScoreEvent.win:time(30 sec)";
		EPStatement statement = epService.getEPAdministrator().createEPL(expression);

		AllScoresListener listener = new AllScoresListener();
		statement.addListener(listener);
		
		ScoreEvent event = null;
		
		event = new ScoreEvent("field_goal", 3);
		epService.getEPRuntime().sendEvent(event);
	
		event = new ScoreEvent("touchdown", 7);
		epService.getEPRuntime().sendEvent(event);

		event = new ScoreEvent("free_throw", 1);
		epService.getEPRuntime().sendEvent(event);

	}

}
