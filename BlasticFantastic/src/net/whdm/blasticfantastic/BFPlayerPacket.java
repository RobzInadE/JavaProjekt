package net.whdm.blasticfantastic;

import java.io.Serializable;

import org.jbox2d.common.Vec2;

public class BFPlayerPacket implements Serializable{

	private static final long serialVersionUID = -569804796318003059L;
	
	private float vspeed, hspeed;
	private Vec2 pos;
	private int direction;
	private String who;
	public BFPlayerPacket(String who, Vec2 pos, int direction, float vspeed, float hspeed) {
		this.who = who;
		this.direction = direction;
		this.pos = pos;
		this.vspeed = vspeed;
		this.hspeed = hspeed;
	}
	public String who() {
		return who;
	}
	public int direction() {
		return direction;
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public float vspeed() {
		return vspeed;
	}
	
	public float hspeed() {
		return hspeed;
	}
	
}
