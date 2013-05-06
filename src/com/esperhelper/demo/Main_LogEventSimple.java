package com.esperhelper.demo;

import java.io.File;

import com.esperhelper.event.log.Expressions;
import com.esperhelper.event.log.LogEvent;
import com.esperhelper.event.log.listener.ErrorListener;
import com.esperhelper.event.log.listener.InfoListener;
import com.esperhelper.event.log.listener.SystemListener;
import com.esperhelper.event.log.listener.WarningListener;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

public class Main_LogEventSimple extends Thread {

	static EPServiceProvider epService = null;
	
	public static void main(String[] args) {

		String testFileN = args[0];
		File testFile = new File(testFileN);

		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.esperhelper.bean");
		epService = EPServiceProviderManager.getDefaultProvider(config);

		loadCfg2();
		sendEvents2(testFile);
		
	}

	static void loadCfg2 () {
		
		// send info message to info listener
		EPStatement stmtInfo = epService.getEPAdministrator().createEPL(Expressions.EPL_INFO);
		InfoListener infoListener = new InfoListener();
		stmtInfo.addListener(infoListener);


		// send error message to error listener
		EPStatement stmtErr = epService.getEPAdministrator().createEPL(Expressions.EPL_ERROR);
		ErrorListener errorListener = new ErrorListener();
		stmtErr.addListener(errorListener);


		// send warning message to warning listener
		EPStatement stmtwarn = epService.getEPAdministrator().createEPL(Expressions.EPL_WARN);
		WarningListener warnListener = new WarningListener();
		stmtwarn.addListener(warnListener);

		// System Listener for absence of info message in last 5 mins
		EPStatement stmtSystem = epService.getEPAdministrator().createEPL(Expressions.EPL_ABSENT);
		SystemListener systemListener = new SystemListener();
		stmtSystem.addListener(systemListener);

		// stream containing count of messages in the past 1 min
		epService.getEPAdministrator().createEPL(Expressions.EPL_SUMMARY_STRM);

		// System error - decrease in avg num of messages in given time
		EPStatement stmtFallOff = epService.getEPAdministrator().createEPL(Expressions.EPL_FALLOFF);
		stmtFallOff.addListener(systemListener);

		// System Listener for error messages matching given criteria
		EPStatement stmtError = epService.getEPAdministrator().createEPL(Expressions.EPL_OPERATORS);
		stmtError.addListener(systemListener);

		// System Listener for warning messages and if message occurring twice in 2 sec
		EPStatement stmtWarn = epService.getEPAdministrator().createEPL(Expressions.EPL_REOCCUR);
		stmtWarn.addListener(systemListener);
	}
	
	static void sendEvents2 (File testFile) {
		
		//		long sleepTime = 2*1*1000; //60=2 min, 5=10seconds
		long sleepTime = 7000;  
		int iterations = 20;
		int count = 0;
		int firstSleepIteration = 0;
		while (count < iterations) {

			System.out.println("ITERATION: " + ++count);

			// We send the following N events in every iteration

			LogEvent event = null;
			event = new LogEvent("info","info message",3,"0011234","xabc");
			epService.getEPRuntime().sendEvent(event);

			event = new LogEvent("error","error message",3,"0311234","abc");
			epService.getEPRuntime().sendEvent(event);

			event = new LogEvent("fatal","fatal message",1,"0111234","xabc");
			epService.getEPRuntime().sendEvent(event);

			event = new LogEvent("error","error message",7,"0311234","xabc");
			epService.getEPRuntime().sendEvent(event);

			event = new LogEvent("warn","warning message",3,"0311234","xabc");
			epService.getEPRuntime().sendEvent(event);


			if(count >= firstSleepIteration) {
				try
				{
					long sleepValue = sleepTime;

					if (count == 2) {
						sleepValue *= 2;
					}

					if (count == 4) {
						sleepValue *= 4;
					}

					System.out.println ("Going to sleep for: " + sleepValue);

					Thread.sleep(sleepValue); 
				}
				catch(Exception e) {

				}
			}


		}
	}



}
