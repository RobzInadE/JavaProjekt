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
	public float x,y,rx;
	private Body body;
	private int d;
	public Vector<Long> tempInfo;
	
	public Bullet(float x, float y, int direction) throws SlickException {
		switch(direction) {
		case 1: case -1: //Left
			this.rx = -1f;
			this.d = -750;
			break;
		default:
			this.rx = 5f;
			this.d = 750;
			break;		
		}
		this.x = x;
		this.y = y;
		this.bulletImg = new Image("data/bullet.png");
		// Dynamic Body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
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
        this.body.setGravityScale(0);
        this.body.setLinearVelocity(new Vec2(this.d, 0));
        
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
