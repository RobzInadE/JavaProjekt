package net.whdm.blasticfantastic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BFClientConnection implements Runnable{
	
	private Socket thisConnection;
	private ObjectOutputStream serverOut;
	private ObjectInputStream serverIn;
	private BFListener thisBfl;
	private String who;
	public BFClientConnection(Socket s, BFListener bfl) {
		thisConnection = s;
		thisBfl = bfl;
		this.who = s.getRemoteSocketAddress().toString();
		try {
			serverOut = new ObjectOutputStream(s.getOutputStream());
			serverIn = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			System.err.println("ServerConnection can't establish input/output stream");
		}
		new Thread(this).start();
	}
	
	public void run() {
		while(true) {
			try {
				Object o = serverIn.readObject();
				if(o instanceof BFPlayerPacket) {
					for(BFClientConnection bfc : thisBfl.getClients()) {
						if(who!=bfc.who)bfc.serverOut.writeObject(o);
					}
				}
				//System.out.println("Heartbeat");
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Whoops");
			}
		}
	}

}
