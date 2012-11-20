package net.whdm.blasticfantastic;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Bullet implements ContactListener {
	private Image bulletImg;
	public float x,y,rx;
	private Body body;
	private int d;
	
	public Bullet(float x, float y, int direction) throws SlickException {
		switch(direction) {
		case 1: //Left
			this.rx = 0;
			this.d = -500;
			break;
		case 3: //Right
			this.rx = 4f;
			this.d = 500;
			break;
		default:
			this.rx = 4f;
			this.d = 500;
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
        this.body.setFixedRotation(true);
        this.body.setLinearVelocity(new Vec2(this.d, 0));
	}
	
	public Image getImg() {
		return this.bulletImg;
	}
	public void updateLoc() {
		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;
	}

	@Override
	public void beginContact(Contact arg0) {
		
		
	}

	@Override
	public void endContact(Contact arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
