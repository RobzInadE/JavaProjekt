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
	private int tileWidth, tileHeight, runningTiles, idleTiles, gunTiles;
	private String spritesheet;
	private SpriteSheet sprites;
	private Animation player_running_left, player_running_right, player_idle_left, player_idle_right, player_shooting_right, player_shooting_left;
	private int delay;
	public static int IDLE_LEFT = -1, IDLE_RIGHT = -2, RUNNING_LEFT = 1, RUNNING_RIGHT = 2, SHOOT_LEFT = 11, SHOOT_RIGHT = 22;
	private int lastDirection = IDLE_LEFT;
	private volatile Body body;
	private volatile BodyDef bodyDef;
	private volatile FixtureDef fixtureDef;
	public volatile boolean isJumping, isFiring = false;
	
	Player(float x, float y, int idleTiles, int runningTiles, int gunTiles, int tileWidth, int tileHeight, String spritesheet, int delay) throws SlickException {
		this.x = x;
		this.y = y;
		this.runningTiles = runningTiles;
		this.idleTiles = idleTiles;
		this.spritesheet = spritesheet;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.gunTiles = gunTiles;
		this.delay = delay;
		this.sprites = new SpriteSheet(this.spritesheet,this.tileWidth,this.tileHeight);
		
		this.player_idle_left = new Animation();
		this.player_idle_right = new Animation();
		this.player_running_right = new Animation();
		this.player_running_left = new Animation();
		this.player_shooting_right = new Animation();
		this.player_shooting_left = new Animation();
		
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
		
		/*
		 * Jag kan inte få animationen för att skjuta, att spelas en gång och sen gå tillbaka till föregående animation på ett bra sätt
		 * så jag utelämnar det. Här byggs animationerna upp iaf.
		 */
		
		//Create gun animation right
		for(int frame=0;frame<this.gunTiles;frame++) {
			player_shooting_right.addFrame(sprites.getSprite(frame,3), 30);
		}
		
		//Create gun animation left (Mirrored.)
		for(int frame=this.gunTiles-1;frame>0;frame--) {
			player_shooting_left.addFrame(sprites.getSprite(frame,4), 30);
		}
		
		//Will only play once.
		player_shooting_left.setLooping(false);
		player_shooting_right.setLooping(false);
		
		// Dynamic Body (Phys world)
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(this.x, this.y);
        this.body = BlasticFantastic.world.createBody(bodyDef);
        PolygonShape dynamicBox = new PolygonShape();
        dynamicBox.setAsBox(this.tileWidth/16, this.tileHeight/16, new Vec2(this.tileWidth/16, this.tileHeight/16), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        fixtureDef.density=1;
        fixtureDef.friction=5;
        
        this.body.createFixture(fixtureDef);
        this.body.setFixedRotation(true);
		
	}
	public Animation getAnimation(int state) {
		//Get proper animation
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
		else if(state==SHOOT_LEFT) {
			return this.player_shooting_left;
		}
		else if(state==SHOOT_RIGHT) {
			return this.player_shooting_right;
		}
		//Invalid input? Return empty animation.
		else {
			return new Animation();
		}
	}
	//Booleans
	public boolean isFiring() {
		return this.isFiring;
	}
	
	public void isFiring(boolean x) {
		this.isFiring = x;
	}
	
	public boolean isJumping() {
		return this.isJumping;
	}
	
	public void isJumping(boolean x) {
		this.isJumping = x;
	}
	//Get direction
	public int direction() {
		return this.lastDirection;
	}
	
	//Set direction
	public void direction(int x) {
		lastDirection = x;
	}
	
	//Get physics body of this object
	public Body getBody() {
		return this.body;
	}
	
	//Get the physics definitions for this object
	public FixtureDef getFixture() {
		return fixtureDef;
	}
	
	//Set objects coordinates to physicsworld coordinates on each frameupdate
	public void updateLoc() {
		x = this.body.getPosition().x;
		y = this.body.getPosition().y;
	}
	
	//Get xpos
	public float getX() {
		return x;
	}
	
	//Get ypos
	public float getY() {
		return y;
	}
	
	//Get  both x and y pos
	public Vec2 getPos() {
		return new Vec2(x, y);
	}
	
	//Set both x and y pos.
	public void setPos(Vec2 v) {
		this.x = v.x;
		this.y = v.y;
		this.body.setTransform(v, this.body.getAngle());
	}
	
	
}
