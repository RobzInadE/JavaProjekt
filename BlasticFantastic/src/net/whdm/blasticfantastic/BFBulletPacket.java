package net.whdm.blasticfantastic;

import java.io.Serializable;

public class BFBulletPacket implements Serializable {

	private static final long serialVersionUID = -8207340276464401201L;
	private float x,y;
	private float xSpeed, ySpeed;
	public BFBulletPacket(float x, float y, float xSpeed, float ySpeed) {
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return this.x;
	}
	public float getY() {
		return this.y;
	}
	public float getXSpeed() {
		return this.xSpeed;
	}
	public float getYSpeed() {
		return this.ySpeed;
	}
	
}
