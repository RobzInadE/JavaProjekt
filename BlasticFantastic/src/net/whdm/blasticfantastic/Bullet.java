package net.whdm.blasticfantastic;


import java.util.Vector;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/*
 * This is the class for each Bullet. You either create a completely new bullet, or create one from the
 * input given.
 * NOTE: This can't be created in a seperate thread from the update/render thread, since DX needs to be bound to ONE thread.
 */

public class Bullet {
	private Image bulletImg;
	public float x,y,rx = 0;
	private Body body;
	private int mx, my;
	public Vector<Long> tempInfo;
	public float xSpeed, ySpeed;
	public boolean isMine;
	
	//Primary constructor, creates bullet based on mouse points and location. Calculates what xSpeed and ySpeed it should have based on
	//Mouse position when clicked.
	public Bullet(float x, float y, int mouseX, int mouseY) throws SlickException {
		this.isMine = true;
		this.x = x;
		this.y = y;
		this.mx = mouseX;
		this.my = mouseY;
		
		float startingSpeed = 750;
		double radian = Math.atan2(my-320, mx-512);

		rx = -1f;
		if(mx>512) rx = 5f;
		
		xSpeed = (float) (startingSpeed * Math.cos(radian));
		ySpeed = (float) (startingSpeed * Math.sin(radian));
		
		//Set init pos to x+(horizontal offset), y = y+vertical offset to not interfer with the characters physic body.
		this.x = this.x+rx;
		this.y = this.y+1.75f;
		this.bulletImg = new Image("data/bullet.png");
		createPhys();
        
	}
	
	//Secondary constructor, for creating bullet from received network packet. This also sets isMine to false, meaning we can collide with it.
	public Bullet(float x, float y, float xs, float ys) throws SlickException {
		this.isMine = false;
		this.x = x;
		this.y = y;
		this.xSpeed = xs;
		this.ySpeed = ys;
		createPhys();
		this.bulletImg = new Image("data/bullet.png");
		
	}
	
	//Create the physics-world object attatched to graphics object.
	public void createPhys() {
		// Dynamic Body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);
        bodyDef.bullet = true;
        this.body = BlasticFantastic.world.createBody(bodyDef);
        CircleShape dynamicBox = new CircleShape();
        dynamicBox.m_radius = 0.125f;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        this.body.createFixture(fixtureDef);
        this.body.setFixedRotation(true);
        this.body.setGravityScale(0.05f);
        this.body.setLinearVelocity(new Vec2(xSpeed, ySpeed));
	}
	
	public Body getBody() {
		return this.body;
	}
	public Image getImg() {
		return this.bulletImg;
	}
	
	//Run this (runs each update()) to synchronize physics object with visible object.
	public void updateLoc() {
		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;
	}
	
}
