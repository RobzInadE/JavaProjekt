package net.whdm.blasticfantastic;

import java.io.Serializable;

/*
 * Very simple Admin command packet. Serializable.
 */

public class BFAdminCommand implements Serializable {

	public static int IWANTPEACE0 = -1, IWANTPEACE1 = 1; 
	private int id;
	public BFAdminCommand(int commandId) {
		this.id = commandId;
	}
	
	public int getCommand() {
		return this.id;
	}

}
