package net.whdm.blasticfantastic;

import java.io.Serializable;

public class BFPlayerPacket implements Serializable{

	private static final long serialVersionUID = -569804796318003059L;
	
	private float xpos, ypos, vspeed, hspeed;
	private int direction;
	private String who;
	public BFPlayerPacket(String who, float xpos, float ypos, int direction, float vspeed, float hspeed) {
		this.who = who;
		this.direction = direction;
		this.xpos = xpos;
		this.ypos = ypos;
		this.vspeed = vspeed;
		this.hspeed = hspeed;
	}
	public String who() {
		return who;
	}
	public int direction() {
		return direction;
	}
	public float xpos() {
		return xpos;
	}
	public float ypos() {
		return ypos;
	}
	
	public float vspeed() {
		return vspeed;
	}
	
	public float hspeed() {
		return hspeed;
	}
	
}
