package net.whdm.blasticfantastic;
import java.util.ArrayList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;
 
public class BlasticFantastic extends BasicGame {
 
    Rectangle viewport;
    Player runningMan1;
    Animation runningMan1anim;
    TiledMap map1;
    int x = 100;
    int y = 100;
    int scale = 1;
    public int displayList;
    public static World world;
    public float timeStep;
    public int velocityIterations, positionIterations;
    private ArrayList<Bullet> bulletList;
 
    public BlasticFantastic()
    {
        super("BlasticFantastic 0.1");
    }
 
    @Override
    public void init(GameContainer gc) throws SlickException {
        viewport = new Rectangle(0, 0, 1024, 600);        
        map1 = new TiledMap("data/2.tmx");
        
        this.bulletList = new ArrayList<Bullet>();
        
        //Create our World
        Vec2 gravity = new Vec2(0, 100);
        world = new World(gravity);
        
        //Player 1
        runningMan1 = new Player(5, 25, 4, 12, 32, 40, "data/eri3.png", 50);
        runningMan1.getAnimation("idle").start();
        
        //Get collision layer from map
        int collisionLayerIndex = map1.getLayerIndex("collision");

        //Create our collision blocks in the physics engine.
        for(int row=0; row<map1.getWidth(); row++) {
        	for(int col=0; col<map1.getHeight(); col++) {
        		if(map1.getTileId(row, col, collisionLayerIndex)==1) {
        			BodyDef groundBodyDef = new BodyDef();
        			groundBodyDef.position.set(row*2, col*2);
        			Body groundBody = world.createBody(groundBodyDef);
        			PolygonShape groundBox = new PolygonShape();
        			groundBox.setAsBox(1, 1, new Vec2(1, 1), 0);
        			groundBody.createFixture(groundBox, 0);
        		}
        	}
        }
        
        //System.out.println(tiles);

        // Setup world
        timeStep = 1.0f/60.0f;
        velocityIterations = 6;
        positionIterations = 2;
        
        world.setContactListener(new BFContactListener());
        
        
        //Physics debug draw.
        
        //Slick2dDebugDraw sDD = new Slick2dDebugDraw(gc.getGraphics(), gc);
        //sDD.setFlags(0x0001); //Setting the debug draw flags,
        //world.setDebugDraw(sDD);
        
        
    }
 
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
    	if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
    		runningMan1.getAnimation("right").start();
    		runningMan1.getBody().setLinearVelocity(new Vec2(1.5f*delta, runningMan1.getBody().getLinearVelocity().y));
    		
    	}
    	else if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
    		runningMan1.getAnimation("left").start();
    		runningMan1.getBody().setLinearVelocity(new Vec2(-1.5f*delta, runningMan1.getBody().getLinearVelocity().y));
    	}
    	else  {
    		runningMan1.getAnimation("idle").start();
    	}
    	if (gc.getInput().isKeyPressed(Input.KEY_SPACE) && Math.round(runningMan1.getBody().getLinearVelocity().y)==0) {
    		//if(!gc.getInput().isKeyDown(Input.KEY_LEFT))
    		runningMan1.getBody().setLinearVelocity(new Vec2(runningMan1.getBody().getLinearVelocity().x, -3*delta));
    	}
    	
    	if(gc.getInput().isKeyPressed(Input.KEY_RETURN)) {
    		this.bulletList.add(new Bullet(runningMan1.x, runningMan1.y, runningMan1.currentDirection()));
    	}
    	
    	world.step(timeStep, velocityIterations, positionIterations);
    	//bulletWorld.step(timeStep, velocityIterations, positionIterations);
    	//System.out.println(body.getLinearVelocity().y);
    	
    	//change his location
        runningMan1.updateLoc();
        for(Bullet b : this.bulletList) {
        	b.updateLoc();
        }
        
        //change camera location
        viewport.setX((runningMan1.x*8));
        viewport.setY((runningMan1.y*8));
        //System.out.println(runningMan1.y);
        
        
    }
 
    public void render(GameContainer gc, Graphics g) throws SlickException {
    	//g.setColor(new Color(60, 66, 86));
    	//g.fillRect(0,0,1280,720); //Background
    	
    	g.translate(512-viewport.getX(), 300-viewport.getY());
    	map1.render(0, 0);
    	g.setClip(0, 0, 1280, 720);
    	
    	if(runningMan1.currentDirection()==0) {
    		//Player is idle, animate idle.
    		g.drawAnimation(runningMan1.getAnimation("idle"), runningMan1.x*8, runningMan1.y*8);
    	}
    	else if(runningMan1.currentDirection()==1) {
    		//Player is running left, animate left.
    		g.drawAnimation(runningMan1.getAnimation("left"), runningMan1.x*8, runningMan1.y*8);
    	}
    	else if(runningMan1.currentDirection()==3) {
    		//Player is running right, animate right.
    		g.drawAnimation(runningMan1.getAnimation("right"), runningMan1.x*8, runningMan1.y*8);
    	}
    	//System.out.println(body.getPosition().y);
	    for(Bullet b : this.bulletList) {
	    	b.getImg().draw(b.x*8, b.y*8);
	    }
    	//world.drawDebugData();
    	
 
    }
    
    
    public static void main(String[] args) throws SlickException {
         AppGameContainer app =
			new AppGameContainer( new BlasticFantastic() );
         app.setVSync(true);
         app.setDisplayMode(1024, 600, false);
         app.start();
    }
}