package net.whdm.blasticfantastic;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/*
 * This class listens for new client connection requests. Adds them to an arraylist.
 */

public class BFListener implements Runnable{

	boolean on;
	private volatile ArrayList<BFClientConnection> clients;
	private ServerSocket s;
	public BFListener(boolean online, ServerSocket s) {
		this.on = online;
		clients = new ArrayList<BFClientConnection>();
		this.s = s;
		new Thread(this).start();
	}
	public ArrayList<BFClientConnection> getClients() {
		return clients;
	}
	
	public void run() {
		do {
			try {
				clients.add(new BFClientConnection(s.accept(), this));
				System.out.println("ACCEPTED!");
			} catch (IOException e) {
				System.err.println("Can't accept client...");
			}
		}while(on);
		System.out.println("Server is not listening for new connections");
	}
}
