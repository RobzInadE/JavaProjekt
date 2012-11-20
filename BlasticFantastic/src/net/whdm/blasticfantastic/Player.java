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

public class Player extends BodyDef{
	public float x;
	public float y;
	private int idleTiles;
	private int runningTiles;
	private int tileWidth;
	private int tileHeight;
	private String spritesheet;
	private SpriteSheet sprites;
	private Animation player_running_left, player_running_right, player_idle;
	private int delay;
	private int direction;
	private Body body;
	
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
		
		this.player_idle = new Animation();
		this.player_running_right = new Animation();
		this.player_running_left = new Animation();
		
		//Create idle animation
		for (int frame=0;frame<idleTiles;frame++) {
			player_idle.addFrame(sprites.getSprite(frame,0), delay);
		}
		//Loop back and forth
		player_idle.setPingPong(true);
		
		//Create running right
		for (int frame=0;frame<runningTiles;frame++) {
			player_running_right.addFrame(sprites.getSprite(frame,1), delay);
		}
		
		//Create running left (Mirrored.)
		for (int frame=runningTiles-1;frame>0;frame--) {
			player_running_left.addFrame(sprites.getSprite(frame,2), delay);
		}
		
		// Dynamic Body
        BodyDef bodyDef = new BodyDef();
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
	public Animation getAnimation(String state) {
		if(state=="right") {
			this.direction = 3; 
			return this.player_running_right;
		}
		else if(state=="idle") {
			this.direction = 0;
			return this.player_idle;
		}
		else if(state=="left") {
			this.direction = 1;
			return this.player_running_left;
		}
		else {
			this.direction = 0;
			return new Animation();
		}
	}
	
	public Body getBody() {
		return this.body;
	}
	public void updateLoc() {
		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;
	}
	public int currentDirection() {
		return this.direction;
	}
	
	
}
