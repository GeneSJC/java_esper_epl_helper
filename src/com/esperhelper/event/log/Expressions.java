package com.esperhelper.event.log;

public class Expressions {
	
	// filters message on type and routes message to type listener
	public static final String EPL_INFO = "select irstream * from com.esperhelper.epl.bean.LogEvent(type = 'info').win:length(1)";
	public static final String EPL_ERROR = "select irstream * from com.esperhelper.epl.bean.LogEvent(type = 'error').win:length(1)";
	public static final String EPL_WARN = "select irstream * from com.esperhelper.epl.bean.LogEvent(type = 'warn').win:length(1)";
	
	// reports absence of an expected message type for 60 secs
	// checks every 2 minutes, was an event sent in the last 60 secs for co 'abc'
	
	public static final String EPL_ABSENT = "select 'system offline of client abc' as SystemError from pattern [every " +
			"timer:interval (4 sec) -> (timer:interval(5 sec) and not com.esperhelper.epl.bean.LogEvent(client_co = 'xabc'))] output first every 10 sec";

	// checks every 10 minutes, was an event sent in the last 60 secs.
//	public static final String EPL_ABSENT = "select 'system offline of client abc' as SystemError from pattern [every " +
//	"timer:interval (60 sec) -> (timer:interval(65 sec) and not com.esperhelper.epl.bean.LogEvent(client_co = 'abc'))] output first every 10 min";
		
	// rate of different message types per 1 minute
	// Every time you render an Event, the "class type" of that event goes to a stream specifically for that class type.
	public static final String EPL_SUMMARY_STRM = "insert into CountPerType " +
			"select client_co, type, count(*) as countPerType from com.esperhelper.epl.bean.LogEvent.win:time_batch(1 minutes)" +
			" group by type output all every 1 minutes";
	
		
	// detects fall- off a particular message. say number of info type message reduced to 75% over last 5 mins
	// in 5 mins, is the # of messages reduced to 75% of what there was before?
	// so on each iteration, we check the a) the current value of countPerType & b) the avg value of countPerType for the specified window
	// to create this scenario, a) ... 
	public static final String EPL_FALLOFF = "select client_co, type, 'from EPL_FALLOFF' as epls_ops_expression , avg(countPerType) as avgCnt, countPerType as Cnt" +
			" from CountPerType.win:time(30 sec) where (type = 'info' and client_co = 'xabc') group by type having countPerType > avg(countPerType) * .75";
	
	 	
	//  operators priority between 1 and 3 (or >= and <=)  and message type in ('error', 'fatal') and client co != 'abc' 
	public static final String EPL_OPERATORS = "select type, message, priority, client_co, 'from EPL_OPERATORS' as epls_ops_expression from com.esperhelper.epl.bean.LogEvent where type in ('error','fatal') and " +
			"priority between 1 and 3 and client_co != 'xabc'";
	
	// reports same message occurring 5 times within the interval of 10 secs for same client co
	public static final String EPL_REOCCUR = "select 'Client xabc recieved same message 2 times in 30 sec' " +
			"as MessageRepeating from pattern [ every  [2] com.esperhelper.epl.bean.LogEvent(client_co = 'xabc' and type='warn') where timer:within(30 sec)]";
	

}
