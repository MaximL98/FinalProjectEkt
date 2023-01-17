package common;

import java.io.Serializable;

// BOTH server and client will have an object of this type, which will be used to pass messages!
/*
 * SCCP = Server-Client Communication Protocol:
 * This class describes an object (message) to be sent between server and client (using OCS framework).
 * An object of the type SCCP will be sent from client and interpreted by server, and vice-versa, using an enum (message type).
 * After the type is known, the recipient will perform the necessary action and respond (if needed)
 * 
 */

public class SCCP implements Serializable{
	/**
	 * default f
	 */
	private static final long serialVersionUID = 1L;
	private ServerClientRequestTypes requestType;
	private Object messageSent;
	
	public SCCP() {
		
	}
	
	public SCCP(ServerClientRequestTypes requestType, Object messageSent) {
		this.requestType = requestType;
		this.messageSent = messageSent;
	}

	public ServerClientRequestTypes getRequestType() {
		return requestType;
	}

	public void setRequestType(ServerClientRequestTypes requestType) {
		this.requestType = requestType;
	}

	public Object getMessageSent() {
		return messageSent;
	}

	public void setMessageSent(Object messageSent) {
		this.messageSent = messageSent;
	}
	
	@Override
	public String toString() {
		return requestType.name() + ", " + messageSent.toString();
	}
	
}
