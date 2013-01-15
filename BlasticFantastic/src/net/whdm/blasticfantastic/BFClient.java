package net.whdm.blasticfantastic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class BFClient implements Runnable {

	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private Socket thisSocket;
	private Player thisPlayer;
	public BFClient(Player p, String server, int port) {
		try {
			System.out.println("Connecting");
			thisSocket = new Socket(server, port);
			System.out.println("Connected!");
		} catch (IOException e) {
			System.err.println("Can't connect!");
		}
		try {
			outStream = new ObjectOutputStream(thisSocket.getOutputStream());
			inStream = new ObjectInputStream(thisSocket.getInputStream());
			thisPlayer = p;
			new Thread(this).start();
		} catch (IOException e) {
			System.err.println("Can't initiate input/output streams to socket");
		}
	}
	
	public Socket getSocket() {
		return thisSocket;
	}
	
	private ObjectOutputStream toServer() {
		return outStream;
	}
	
	private ObjectInputStream fromServer() {
		return inStream;
	}

	@Override
	public void run() {
		//Thread
		while(true) {
			try {
				Thread.sleep(100);
				outStream.flush();
				outStream.writeObject(new BFPlayerPacket(thisSocket.getInetAddress().toString(), thisPlayer.x, thisPlayer.y, thisPlayer.direction(), thisPlayer.getBody().getLinearVelocity().x, thisPlayer.getBody().getLinearVelocity().y));
				Object o;
				o = inStream.readObject();
				if(o instanceof BFPlayerPacket) {
					BFPlayerPacket bfp = (BFPlayerPacket) o;
					thisPlayer.x = bfp.xpos();
					thisPlayer.y = bfp.ypos();
				}
			} catch (ClassNotFoundException | IOException | InterruptedException e) {
				System.err.println("Couldn't read "+e.getMessage());
			}
		}
	}
}
