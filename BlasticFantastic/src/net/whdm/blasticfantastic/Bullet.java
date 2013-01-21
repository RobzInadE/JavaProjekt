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

public class Bullet {
	private Image bulletImg;
	public float x,y,rx = 0;
	private Body body;
	private int d, mx, my;
	public Vector<Long> tempInfo;
	private float xSpeed, ySpeed;
	
	public Bullet(float x, float y, int mouseX, int mouseY) throws SlickException {
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
		
		this.bulletImg = new Image("data/bullet.png");
		// Dynamic Body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        //Set init pos to x+(horizontal offset), y = y+vertical offset to not interfer with the characters physic body.
        bodyDef.position.set(this.x+rx, this.y+1.75f);
        bodyDef.bullet = true;
        this.body = BlasticFantastic.world.createBody(bodyDef);
        CircleShape dynamicBox = new CircleShape();
        dynamicBox.m_radius = 0.125f;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        
        this.body.createFixture(fixtureDef);
        this.body.setUserData(tempInfo);
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
	public void updateLoc() {
		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;
	}
	
}
