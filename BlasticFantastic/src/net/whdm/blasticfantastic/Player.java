package net.whdm.blasticfantastic;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Player {
	private volatile float x;
	private volatile float y;
	private int idleTiles;
	private int runningTiles;
	private int tileWidth;
	private int tileHeight;
	private String spritesheet;
	private SpriteSheet sprites;
	private Animation player_running_left, player_running_right, player_idle_left, player_idle_right;
	private int delay;
	public static int IDLE_LEFT = -1, IDLE_RIGHT = -2, RUNNING_LEFT = 1, RUNNING_RIGHT = 2;
	private int lastDirection = IDLE_LEFT;
	private volatile Body body;
	private volatile BodyDef bodyDef;
	
	Player(float x, float y, int idleTiles, int runningTiles, int tileWidth, int tileHeight, String spritesheet, int delay) throws SlickException {
		this.x = x;
		this.y = y;
		this.runningTiles = runningTiles;
		this.idleTiles = idleTiles;
		this.spritesheet = spritesheet;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.delay = delay;
		this.sprites = new SpriteSheet(this.spritesheet,this.tileWidth,this.tileHeight);
		
		this.player_idle_left = new Animation();
		this.player_idle_right = new Animation();
		this.player_running_right = new Animation();
		this.player_running_left = new Animation();
		
		//Create idle animation
		for (int frame=0;frame<this.idleTiles;frame++) {
			player_idle_right.addFrame(sprites.getSprite(frame,0), this.delay);
		}
		for (int frame=4;frame<this.idleTiles*2;frame++) {
			player_idle_left.addFrame(sprites.getSprite(frame,0), this.delay);
		}
		//Loop back and forth
		player_idle_left.setPingPong(true);
		player_idle_right.setPingPong(true);
		
		//Create running right
		for (int frame=0;frame<this.runningTiles;frame++) {
			player_running_right.addFrame(sprites.getSprite(frame,1), this.delay);
		}
		
		//Create running left (Mirrored.)
		for (int frame=this.runningTiles-1;frame>0;frame--) {
			player_running_left.addFrame(sprites.getSprite(frame,2), this.delay);
		}
		
		// Dynamic Body
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(this.x, this.y);
        this.body = BlasticFantastic.world.createBody(bodyDef);
        PolygonShape dynamicBox = new PolygonShape();
        dynamicBox.setAsBox(this.tileWidth/16, this.tileHeight/16, new Vec2(this.tileWidth/16, this.tileHeight/16), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        fixtureDef.density=1;
        fixtureDef.friction=5;
        
        this.body.createFixture(fixtureDef);
        this.body.setFixedRotation(true);
		
	}
	public Animation getAnimation(int state) {
		if(state==RUNNING_RIGHT) {
			return this.player_running_right;
		}
		else if(state==RUNNING_LEFT) {
			return this.player_running_left;
		}
		
		else if(state==IDLE_RIGHT) {
			return this.player_idle_right;
		}
		else if(state==IDLE_LEFT) {
			return this.player_idle_left;
		}
		
		else {
			return new Animation();
		}
	}
	
	public int direction() {
		return this.lastDirection;
	}
	public void direction(int x) {
		lastDirection = x;
	}
	
	public Body getBody() {
		return this.body;
	}
	public void updateLoc() {
		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	
	public Vec2 getPos() {
		return new Vec2(x, y);
	}
	public void setPos(Vec2 v) {
		this.x = v.x;
		this.y = v.y;
		this.bodyDef.position.set(v);
	}
	
	
}
