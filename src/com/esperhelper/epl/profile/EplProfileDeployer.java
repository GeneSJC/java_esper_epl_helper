package com.esperhelper.epl.profile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.esperhelper.util.ReadTextFile;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;

/**
 * An EPL RunProfile consists of:
 * - Specifying the Event package name 
 * - INSERT EPLs
 * - SELECT EPLs
 * - Specifying the SELECT listeners
 * 
 * @author Gene
 *
 * Example file:

 */
public class EplProfileDeployer {

	private EPServiceProvider epService = null;

	/**
	 * 1) Reads in the EPL data
	 * 2) Uses the EPL data to initial the Esper service
	 * 3) Runs an LogFileMonitor on the log file path, which publishes all entries to the Esper stream 
	 */
	public void run(File aFile) {

		EplProfile eplProfile = EplProfileDeployer.readEplFile(aFile);

		this.run(eplProfile);
	}

	public void run(EplProfile eplProfile) {		 

		this.epService = EplProfileDeployer.initEPLsAndListeners(eplProfile); // pass in the EPL config info to set up Esper
	}
	
	// ===================================
	// STATIC HELPER METHODS
	// ===================================
	
	public static EPServiceProvider initEPLsAndListeners(EplProfile eplProfile) {

		String eventPackageName = eplProfile.getEventPackageName();		
		List<String> esperServiceOnlyEPLs = eplProfile.getEsperServiceOnlyEPLs();
		Map<String, List<String> > mapListenerToEPL = eplProfile.getMapListenerToEPL();

		EPServiceProvider epService = null;

		// Create ESPER Service		
		Configuration config = new Configuration();
		config.addEventTypeAutoName(eventPackageName);
		epService = EPServiceProviderManager.getDefaultProvider(config);

		EPAdministrator esperAdmin = epService.getEPAdministrator();

		// Attach all non-Select EPLs (eg, INSERT INTO & CREATE WINDOW) 
		for (String curEPL : esperServiceOnlyEPLs) {

			System.out.println ("curEPL = " + curEPL);
			
			esperAdmin.createEPL(curEPL);
		}

		// Attach all Select EPLs and their corresponding listeners
		EplProfileDeployer.addListeners(mapListenerToEPL, esperAdmin);

		return epService;
	}

	/**
	 * This version assumes one listener instance can handle multiple epls.
	 * 
	 * @param mapListenerToEPL key=listenerName, value = list of assoc'd EPL queries
	 * @param esperAdmin
	 */
	public static void addListeners (Map<String, List<String> > mapListenerToEPL,
													EPAdministrator esperAdmin ) {

		// We'll keep a map of our listener objects so as not to create duplicates
		Map<String, UpdateListener> listenerNameToClassMap = new HashMap<String, UpdateListener>();

		for (String curListenerClassName : mapListenerToEPL.keySet() ) {

			UpdateListener curListener = null;

			try {
				
					// 1) get existing or create the instance of the listener for the specified name
				curListener = listenerNameToClassMap.get(curListenerClassName);
				if (curListener == null) {
					curListener = (UpdateListener)( Class.forName( curListenerClassName).newInstance() );
					listenerNameToClassMap.put(curListenerClassName, curListener);
				}

					// 2) for each EPL in the listener's list, associate with the listener
				List<String> selectEplList = mapListenerToEPL.get(curListenerClassName);
				for (String selectEPL : selectEplList ) {
					// if we make it this far, we assume curListener is not NULL
					EPStatement selectEPLStmt = esperAdmin.createEPL(selectEPL);
					selectEPLStmt.addListener(curListener);					
				}
			}
			catch (Exception e) {

				if (curListener == null) {
					System.out.println ("Make sure this class in your ClassPath: " 
							+ curListenerClassName);
				}
				
				e.printStackTrace();
			}

		}


	}

	private static EplProfile readEplFile(File aFile ) {		

		String contents = ReadTextFile.getContentsWithLineSeparators(aFile);

		StringTokenizer st = new StringTokenizer(contents, ";");

		EplProfile eplProfile = new EplProfile();

		int idx = 0;
		int counter = 0;
		while (st.hasMoreElements()) {

			String line = st.nextToken().trim();

			if (line.length() == 0) 
				continue;

			if (line.startsWith("#")) // comment
				continue; 
				
			counter++;

			if ( counter == 1) {

				eplProfile.setEventPackageName(line);

				System.out.println ("\n >> Event Package Name: ");
			}
			else if ( (idx = line.indexOf(":=")) > -1) {
				
				try {
					String listener = line.substring(0, idx );
					String epl = line.substring(idx+2 );

					eplProfile.addEPL(listener.trim(), epl.trim());

				}
				catch (Exception e) {
					System.out.println ("ERROR.  Expecting 'listener' line entry.  eg, <listener_class>:=<SELECT epl query>" +
							", ex: " + e.getMessage());

				}

				System.out.println ("\n >> EPL for Listener: ");
			}
			else {

				eplProfile.addEPL(line);

				System.out.println ("\n >> EPL for Esper Service Only: ");
			}

			System.out.println (
					//					"cur line = " + 
					line);
		}

		System.out.println ("done:\n" + eplProfile);

		return eplProfile;
	}

	public EPServiceProvider getEpService() {
		return epService;
	}
	
}
