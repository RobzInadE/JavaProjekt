package net.whdm.blasticfantastic;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class BFServer {

	private ServerSocket thisServer;
	
	public BFServer(boolean online) {
		try {
			System.out.println("Server starting");
			thisServer = new ServerSocket(7777);
			new BFListener(online, thisServer);
			System.out.println("Server online");
			
		} catch (IOException e) {
			System.err.println("Can't start server"+e.getMessage());
		}
	}
}
