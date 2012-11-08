package net.whdm.blasticfantastic;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;
 
public class BlasticFantastic extends BasicGame {
 
    Image plane = null;
    Image land = null;
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
    public Vec2 position;
 
    public BlasticFantastic()
    {
        super("BlasticFantastic 0.1");
    }
 
    @Override
    public void init(GameContainer gc) throws SlickException {
        plane = new Image("data/plane.png");
        land = new Image("data/land.jpg");
        viewport = new Rectangle(0, 0, 800, 600);
        runningMan1 = new Player(5, 12.5f, 4, 12, 32, 40, "data/eri3.png", 50);
        runningMan1.getAnimation("idle").start();
        
        map1 = new TiledMap("data/1.tmx");
        
        //Create our World
        Vec2 gravity = new Vec2(0, 100);
        boolean doSleep = true;
        world = new World(gravity,doSleep);
        
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(5, 27);
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(10, 1);
        groundBody.createFixture(groundBox, 0);

        // Dynamic Body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(runningMan1.x, runningMan1.y);
        body = world.createBody(bodyDef);
        PolygonShape dynamicBox = new PolygonShape();
        dynamicBox.setAsBox(2, 2.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        fixtureDef.density=1;
        fixtureDef.friction=0.5f;
        //fixtureDef.restitution=0.5f;
        body.createFixture(fixtureDef);

        // Setup world
        timeStep = 1.0f/60.0f;
        velocityIterations = 6;
        positionIterations = 2;
        
        position = body.getPosition();

    }
 
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
    	if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
    		runningMan1.getAnimation("right").start();
    		body.setLinearVelocity(new Vec2(5, 0));
    	}
    	else if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
    		runningMan1.getAnimation("left").start();
    	}
    	else  {
    		runningMan1.getAnimation("idle").start();
    	}
    	world.step(timeStep, velocityIterations, positionIterations);
    	position = body.getPosition();
    	System.out.println(body.getLinearVelocity().y);
        runningMan1.sX(position.x);
        runningMan1.sY(position.y);
    }
 
    public void render(GameContainer gc, Graphics g) throws SlickException {
    	g.setColor(new Color(60, 66, 86));
    	g.fillRect(0,0,4800,4800); //Background
    	g.setClip(viewport);
    	map1.render(0, 0);
    	if(runningMan1.currentDirection()==0) {
    		//Player is idle, animate idle.
    		g.drawAnimation(runningMan1.getAnimation("idle"), runningMan1.x*16, runningMan1.y*16);
    	}
    	else if(runningMan1.currentDirection()==1) {
    		//Player is running left, animate left.
    		g.drawAnimation(runningMan1.getAnimation("left"), runningMan1.x*16, runningMan1.y*16);
    	}
    	else if(runningMan1.currentDirection()==3) {
    		//Player is running right, animate right.
    		g.drawAnimation(runningMan1.getAnimation("right"), runningMan1.x*16, runningMan1.y*16);
    	}
    	//System.out.println(world.getGravity().y);
    	
    	

 
    }
    
    
    public static void main(String[] args) throws SlickException {
         AppGameContainer app =
			new AppGameContainer( new BlasticFantastic() );
         app.setVSync(true);
         app.setDisplayMode(800, 600, false);
         app.start();
    }
}