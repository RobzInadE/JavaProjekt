package net.whdm.blasticfantastic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.jbox2d.common.Vec2;

public class BFClient implements Runnable {

	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private Socket thisSocket;
	private volatile Player hisPlayer, myPlayer;
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
				Thread.sleep(30);
				outStream.flush();
				outStream.writeObject(new BFPlayerPacket(thisSocket.getInetAddress().toString(), myPlayer.getPos(), myPlayer.direction(), myPlayer.getBody().getLinearVelocity().x, myPlayer.getBody().getLinearVelocity().y));
				Object o;
				o = inStream.readObject();
				if(o instanceof BFPlayerPacket) {
					BFPlayerPacket bfp = (BFPlayerPacket) o;
					hisPlayer.setPos(bfp.getPos());
					hisPlayer.getBody().setLinearVelocity(new Vec2(bfp.vspeed(), bfp.hspeed()));
					hisPlayer.direction(bfp.direction());
				}
			} catch (ClassNotFoundException | IOException | InterruptedException e) {
				System.err.println("Couldn't read "+e.getMessage());
			}
		}
	}
}
