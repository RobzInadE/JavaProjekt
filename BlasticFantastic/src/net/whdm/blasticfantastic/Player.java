package net.whdm.blasticfantastic;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.jbox2d.dynamics.BodyDef;

public class Player {
	private float posX;
	private float posY;
	private String image;
	
	private BodyType bodyType;
	
	public Player(float posX, float posY, BodyType bodyType, String image) {
		this.posX = posX;
		this.posY = posY;
		this.bodyType = bodyType;
		this.image = image;
	}
	
	public Image create() throws SlickException {
		Image player = new Image(this.image);
		
		BodyDef bd = new BodyDef();
		bd.type = bodyType;
		bd.position.set(posX, posY);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = new CircleShape();
		fd.shape.m_radius = (player.getHeight() / 2);
		fd.density = 1;
		fd.friction = 0.3f;
		fd.restitution = 0.6f;
		
		Body body = Utils.world.createBody(bd);
		body.createFixture(fd);
		
		return player;
	}
}
