package net.whdm.blasticfantastic;
import org.jbox2d.collision.ContactID;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
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
    public World world;
    public float timeStep;
    public int velocityIterations, positionIterations;
    public Body body;
 
    public BlasticFantastic()
    {
        super("BlasticFantastic 0.1");
    }
 
    @Override
    public void init(GameContainer gc) throws SlickException {
        viewport = new Rectangle(0, 0, 1024, 600);
        runningMan1 = new Player(5, 25, 4, 12, 32, 40, "data/eri3.png", 50);
        runningMan1.getAnimation("idle").start();
        
        map1 = new TiledMap("data/2.tmx");
        
        //Create our World
        Vec2 gravity = new Vec2(0, 100);
        boolean doSleep = true;
        world = new World(gravity,doSleep);
        
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
        
        // Dynamic Body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(runningMan1.x, runningMan1.y);
        body = world.createBody(bodyDef);
        PolygonShape dynamicBox = new PolygonShape();
        //dynamicBox.setAsBox(1f, 1.25f, new Vec2(0.5f, -0.17f), 0);
        dynamicBox.setAsBox(2, 2.5f, new Vec2(2, 2.5f), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        fixtureDef.density=1;
        fixtureDef.friction=5;
        
        //fixtureDef.restitution=0.5f;
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);

        // Setup world
        timeStep = 1.0f/60.0f;
        velocityIterations = 6;
        positionIterations = 2;
        
        //Physics debug draw.
        
        //Slick2dDebugDraw sDD = new Slick2dDebugDraw(gc.getGraphics(), gc); // arg0 is the GameContainer in this case, I put this code in my init method
        //sDD.setFlags(0x0001); //Setting the debug draw flags,
        //world.setDebugDraw(sDD);
        
        
    }
 
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
    	if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
    		runningMan1.getAnimation("right").start();
    		body.setLinearVelocity(new Vec2(1.5f*delta, body.getLinearVelocity().y));
    		
    	}
    	else if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
    		runningMan1.getAnimation("left").start();
    		body.setLinearVelocity(new Vec2(-1.5f*delta, body.getLinearVelocity().y));
    	}
    	else  {
    		runningMan1.getAnimation("idle").start();
    	}
    	if (gc.getInput().isKeyPressed(Input.KEY_SPACE) && Math.round(body.getLinearVelocity().y)==0) {
    		//if(!gc.getInput().isKeyDown(Input.KEY_LEFT))
    		body.setLinearVelocity(new Vec2(body.getLinearVelocity().x, -3*delta));
    	}
    	world.step(timeStep, velocityIterations, positionIterations);
    	//System.out.println(body.getLinearVelocity().y);
    	
    	//change his location
        runningMan1.sX(body.getPosition().x);
        runningMan1.sY(body.getPosition().y);
        
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
    	//g.setClip(viewport);
    	
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