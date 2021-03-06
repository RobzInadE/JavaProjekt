package net.whdm.blasticfantastic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * This class will handle the connections on the server.
 * It will read objects from our client and send them through to the others players.
 */

public class BFClientConnection implements Runnable{
	
	private Socket thisConnection;
	private volatile ObjectOutputStream serverOut;
	private volatile ObjectInputStream serverIn;
	private BFListener thisBfl;
	private String who;
	public BFClientConnection(Socket s, BFListener bfl) {
		thisConnection = s;
		thisBfl = bfl;
		this.who = s.getRemoteSocketAddress().toString();
		try {
			serverOut = new ObjectOutputStream(thisConnection.getOutputStream());
			serverIn = new ObjectInputStream(thisConnection.getInputStream());
		} catch (IOException e) {
			System.err.println("ServerConnection can't establish input/output stream");
		}
		new Thread(this).start();
	}
	
	public void run() {
		while(true) {
			try {
				//Read incoming object, write it to all other users (we exclude our selfs)
				Object o = this.serverIn.readObject();
				for(BFClientConnection bfc : thisBfl.getClients()) {
					if(who!=bfc.who) {
						//No seriously... We don't want to send to ourselfs, this just makes everything messy.
						bfc.serverOut.flush();
						bfc.serverOut.writeObject(o);
					}
				}
				
			} catch (IOException e) {
				System.err.println(e.getMessage()+" 11111");
			}
			catch(ClassNotFoundException e) {
				System.err.println(e.getMessage()+" 22222");
			}
		}
	}

}
