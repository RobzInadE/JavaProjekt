package net.whdm.blasticfantastic;

import java.io.Serializable;

/*
 * Serializable class that will hold a text and a username/IP. Which will then be sent over to server and distributed.
 */

public class ChatMessage implements Serializable{
	
	private static final long serialVersionUID = -5966919026842665953L;

	private String t;
	public ChatMessage(String t) {
		this.t = t;
	}
	
	public String getMessage() {
		return this.t;
	}

}
