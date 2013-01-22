package net.whdm.blasticfantastic;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.jbox2d.common.Vec2;

/*
 * This class grabes the inputstream of our serverside socket made from our connected client, and listens to objects.
 * If it got a new object that we like, we update either positions or create new bullets.
 */

public class BFClientReceiver implements Runnable{

	private ObjectInputStream in;
	private Player hisPlayer;
	public BFClientReceiver(ObjectInputStream in, Player hisPlayer) {
		this.in = in;
		this.hisPlayer = hisPlayer;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while(true) {
			Object o;
			try {
				//Read if possible, and update.
				o = in.readObject();
				if(o instanceof BFPlayerPacket) {
					BFPlayerPacket bfp = (BFPlayerPacket) o;
					hisPlayer.setPos(bfp.getPos());
					hisPlayer.getBody().setLinearVelocity(new Vec2(bfp.vspeed(), bfp.hspeed()));
					hisPlayer.direction(bfp.direction());
				}
				else if(o instanceof BFBulletPacket) {
					BFBulletPacket bp = (BFBulletPacket) o;
					BlasticFantastic.addBullet(bp.getX(), bp.getY(), bp.getXSpeed(), bp.getYSpeed());
				}
				o = null;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
