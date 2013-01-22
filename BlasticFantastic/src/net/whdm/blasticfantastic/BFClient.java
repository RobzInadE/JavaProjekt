package net.whdm.blasticfantastic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.jbox2d.common.Vec2;


/*
 * Class for handling the client-side connection and socket when playing multiplayer and so forth.
 * This class sends updates each 50ms to all connected clients.
 */
public class BFClient implements Runnable {

	public ObjectOutputStream outStream;
	public ObjectInputStream inStream;
	private Socket thisSocket;
	private volatile Player hisPlayer, myPlayer;
	
	//Construct, also specify the 2 players.
	public BFClient(Player p1, Player p2, String server, int port) {
		try {
			System.out.println("Connecting to "+server+" port "+port);
			thisSocket = new Socket(server, port);
			System.out.println("Connected!");
		} catch (IOException e) {
			System.err.println("Can't connect! " + e.getMessage());
		}
		try {
			outStream = new ObjectOutputStream(thisSocket.getOutputStream());
			inStream = new ObjectInputStream(thisSocket.getInputStream());
			myPlayer = p1;
			hisPlayer = p2;
			new BFClientReceiver(inStream, hisPlayer);
			new Thread(this).start();
		} catch (IOException e) {
			System.err.println("Can't initiate input/output streams to socket");
		}
	}
	
	public Socket getSocket() {
		return thisSocket;
	}

	@Override
	public void run() {
		//Thread
		while(true) {
			try {
				Thread.sleep(50);
				outStream.flush();
				outStream.writeObject(new BFPlayerPacket(thisSocket.getInetAddress().toString(), myPlayer.getPos(), myPlayer.direction(), myPlayer.getBody().getLinearVelocity().x, myPlayer.getBody().getLinearVelocity().y));
			} catch (InterruptedException e) {
				System.err.println("Couldn't read "+e.getMessage());
			} catch (IOException e) {
				System.err.println("Can't send/write to socket!");
			}
		}
	}
}
