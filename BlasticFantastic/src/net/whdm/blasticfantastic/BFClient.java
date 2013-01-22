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
				Object o;
				o = inStream.readObject();
				if(o instanceof BFPlayerPacket) {
					BFPlayerPacket bfp = (BFPlayerPacket) o;
					hisPlayer.setPos(bfp.getPos());
					hisPlayer.getBody().setLinearVelocity(new Vec2(bfp.vspeed(), bfp.hspeed()));
					hisPlayer.direction(bfp.direction());
				}
				else if(o instanceof BFBulletPacket) {
					BFBulletPacket bp = (BFBulletPacket) o;
					BlasticFantastic.upcomingBullet = true;
					BlasticFantastic.upcbcoords = new Vec2(bp.getX(), bp.getY());
					BlasticFantastic.upcbspeed = new Vec2(bp.getXSpeed(), bp.getYSpeed());
				}
			} catch (ClassNotFoundException e) {
				System.err.println("Couldn't read "+e.getMessage());
			} catch (InterruptedException e) {
				System.err.println("Couldn't read "+e.getMessage());
			} catch (IOException e) {
				
				System.err.println("Can't send/write to socket!");
			}
		}
	}
}
