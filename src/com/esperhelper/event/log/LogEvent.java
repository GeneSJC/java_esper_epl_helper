package com.esperhelper.event.log;


public class LogEvent {
	
	public String type;	
	public String message;
	public int priority;
	public String errorCode;
	public String client_co;
	
	public LogEvent(String type, String message, int priority, String errorCode, String client_co) {
		this.type = type;
		this.message = message;
		this.priority = priority;
		this.errorCode = errorCode;
		this.client_co = client_co;
	}
	/**
	 * @return the level
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param level the level to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the client_co
	 */
	public String getClient_co() {
		return client_co;
	}
	/**
	 * @param clientCo the client_co to set
	 */
	public void setClient_co(String clientCo) {
		client_co = clientCo;
	}
	
	
	

}
