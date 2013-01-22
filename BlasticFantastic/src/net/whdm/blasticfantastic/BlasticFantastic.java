package net.whdm.blasticfantastic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
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
    public volatile Player player1;
    public volatile Player player2;
    public volatile Player myPlayer, hisPlayer;
    Animation player1anim;
    TiledMap map1;
    int x = 100;
    int y = 100;
    int scale = 1;
    public int displayList;
    public volatile static World world;
    public float timeStep;
    public int velocityIterations, positionIterations;
    public static volatile ArrayList<Bullet> bulletList;
    public static Random random = new Random(System.currentTimeMillis());
    private String status;
    private String host = "localhost";
    private int port = 50001;
    private boolean multiplayer = false;
    public volatile static boolean upcomingBullet;
    public volatile static Vec2 upcbcoords = null;
    public volatile static Vec2 upcbspeed = null;
    
    private volatile static ArrayList<Body> bulletsToRemove;
    
    private BFClient thisClient;
    
    public BlasticFantastic()
    {
        super("BlasticFantastic 0.1");
        AppGameContainer app;
		try {
			app = new AppGameContainer(this);
			app.setDisplayMode(1024, 600, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
     
    }
    public BlasticFantastic(String host, int port) {
    	super("BlasticFantastic 0.1 MP");
    	multiplayer = true;
    	this.host = host;
    	this.port = port;
    	AppGameContainer app;
		try {
			app = new AppGameContainer(this);
			app.setDisplayMode(1024, 600, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
    }
    
    
 
    @Override
    public void init(GameContainer gc) throws SlickException {

    	bulletsToRemove = new ArrayList<Body>();
        viewport = new Rectangle(0, 0, 1024, 600);        
        map1 = new TiledMap("data/2.tmx");
        
        bulletList = new ArrayList<Bullet>();
        
        //Create our World
        Vec2 gravity = new Vec2(0, 100);
        world = new World(gravity);
        
        //Setup server
        
        
        //Player 1
        player1 = new Player(5, 180, 4, 12, 11, 40, 40, "data/temp2.png", 50);
        player1.getAnimation(Player.IDLE_LEFT).start();
        
        player2 = new Player(15, 180, 4, 12, 11, 40, 40, "data/temp2.png", 50);
        player2.getAnimation(Player.IDLE_LEFT).start();
        if(multiplayer) {
        	//Connecting to someone
        	myPlayer = player2;
        	hisPlayer = player1;
        	thisClient = new BFClient(myPlayer, hisPlayer, host, port);
        	status = "Connected to server";
        }
        else {
        	//Connecting to ourselfs
        	myPlayer = player1;
        	hisPlayer = player2;
        	new BFServer(true);
        	thisClient = new BFClient(myPlayer, hisPlayer, host, port);
        	status = "Local server online";
        }
        
        FixtureDef fd = hisPlayer.getFixture();
        fd.friction = 2;
        hisPlayer.getBody().createFixture(fd);
        
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
    
    public Player getMyPlayer() {
    	return myPlayer;
    }
 
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {

    	if (gc.getInput().isKeyDown(Input.KEY_D)) {
    		//Run right, set direction, get proper animation (start it aswell), and set a velocity
    		myPlayer.direction(Player.RUNNING_RIGHT);
    		myPlayer.getAnimation(Player.RUNNING_RIGHT).start();
    		myPlayer.getBody().setLinearVelocity(new Vec2(25.5f, myPlayer.getBody().getLinearVelocity().y));
    		
    	}
    	else if (gc.getInput().isKeyDown(Input.KEY_A)) {
    		//Run left, set direction, get proper animation (start it aswell), and set a velocity
    		myPlayer.direction(Player.RUNNING_LEFT);
    		myPlayer.getAnimation(Player.RUNNING_LEFT).start();
    		myPlayer.getBody().setLinearVelocity(new Vec2(-25.5f, myPlayer.getBody().getLinearVelocity().y));
    	}
    	else  {
    		//We're not running anywhere atm, play idle animation.
    		if(myPlayer.direction()==Player.IDLE_RIGHT || myPlayer.direction()==Player.RUNNING_RIGHT) {
    			myPlayer.direction(Player.IDLE_RIGHT);
    		}
    		else {
    			myPlayer.direction(Player.IDLE_LEFT);
    		}
    	}
    	if (gc.getInput().isKeyPressed(Input.KEY_SPACE) && Math.round(myPlayer.getBody().getLinearVelocity().y)==0) {
    		//myPlayer.isJumping(true);
    		myPlayer.getBody().setLinearVelocity(new Vec2(myPlayer.getBody().getLinearVelocity().x, -60));
    		
    	}
    	
    	if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
    		//Fire new bullet (if we're facing the right way)!
    		if((
    		//IF we're facing left, and mouseclick was on left side of the screen
    		(myPlayer.direction()==1 || myPlayer.direction()==-1 || myPlayer.direction()==11) && gc.getInput().getAbsoluteMouseX()<(gc.getWidth()/2)) ||
    		//OR if we're facing right, and mouseclick was on right side of the screen
    		(myPlayer.direction()==2 || myPlayer.direction()==-2 || myPlayer.direction()==22) && gc.getInput().getAbsoluteMouseX()>(gc.getWidth()/2)) {
    			Bullet theBullet = new Bullet(myPlayer.getX(), myPlayer.getY(), gc.getInput().getAbsoluteMouseX(), gc.getInput().getAbsoluteMouseY());
    			bulletList.add(theBullet);
    			try {
    				thisClient.outStream.flush();
					thisClient.outStream.writeObject(new BFBulletPacket(theBullet.x, theBullet.y, theBullet.xSpeed, theBullet.ySpeed));
				} catch (IOException e) {
					System.err.println("Error sending bullet");
				}
    		}
    	}
    	
    	//Add new bullet received from network.
    	if(upcbcoords!=null) {
    		bulletList.add(new Bullet(upcbcoords.x, upcbcoords.y, upcbspeed.x, upcbspeed.y));
    		upcbcoords = null;
    		upcbspeed = null;
    	}
    	
    	//Step phys world each update
    	world.step(timeStep, velocityIterations, positionIterations);
    	
    	if(bulletsToRemove.size()>0) {
    		System.out.println("New bullet to remove");
    		for(Body b : bulletsToRemove) {
    			world.destroyBody(b);
    		}
    		bulletsToRemove.clear();
    	}
    	
    	//change his/my location and/or bullets.
        player1.updateLoc();
        player2.updateLoc();
        for(Bullet b : bulletList) {
        	b.updateLoc();
        }
        
        //change camera location
        viewport.setX((myPlayer.getX()*8));
        viewport.setY((myPlayer.getY()*8));
        
        //System.out.println(myPlayer.isFiring());
        
    }
    
    public static void removeBody(Body s) {
    	bulletsToRemove.add(s);
    }
 
    public void render(GameContainer gc, Graphics g) throws SlickException {
    	//g.setColor(new Color(60, 66, 86));
    	//g.fillRect(0,0,1280,720); //Background
    	
    	g.translate(512-viewport.getX(), 300-viewport.getY());
    	map1.render(0, 0);
    	g.setClip(0, 0, 1280, 720);
    	
    	g.drawAnimation(player1.getAnimation(player1.direction()), player1.getX()*8, player1.getY()*8);
    	g.drawAnimation(player2.getAnimation(player2.direction()), player2.getX()*8, player2.getY()*8);
    	
	    for(Bullet b : bulletList) {
	    	b.getImg().draw(b.x*8, b.y*8);
	    }
    	//world.drawDebugData();
	    
	    g.drawString(status, viewport.getX()+300, viewport.getY()+250);
    	
 
    }
    
}
