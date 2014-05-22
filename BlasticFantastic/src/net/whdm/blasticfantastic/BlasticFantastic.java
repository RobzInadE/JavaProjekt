package net.whdm.blasticfantastic;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
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
 


/*
 * This is our main game class. This class handles the graphics throuh 1 method: render(), it also calls update() on each render
 * to update various things.
 * 
 * We have 2 constructors, one for single player mode and one for multiplayer.
 */



public class BlasticFantastic extends BasicGame {
	
	//Lots of variables.
    Rectangle viewport;
    public volatile Player player1;
    public volatile Player player2;
    public volatile static Player myPlayer;
	public volatile static Player hisPlayer;
    private TiledMap map1;
    int x = 100;
    int y = 100;
    int scale = 1;
    public int displayList;
    public volatile static World world;
    public float timeStep;
    public int velocityIterations, positionIterations;
    public static volatile ArrayList<Bullet> bulletList;
    public static Random random = new Random(System.currentTimeMillis());
    public volatile static boolean chatUp = false;
    public static String chatMessage = "";
    private BFKeyListener keylistener = new BFKeyListener();
    public static volatile boolean removeBullets = true;
    
    private Image hpbar;
    
    //Server variables,
    private String status;
    private String host = "localhost";
    private int port = 50001;
    private boolean multiplayer = false;
    private BFClient thisClient;
    private boolean isHosting;
    
    //2 different Arraylist for bullets.
    private volatile static ArrayList<Body> bulletsToRemove;
    private volatile static ArrayList<Float[]> bulletsToAdd;
    
    //Constructor 1, created when clicking SinglePlayer mode.
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
    
    //Constructor 2, created when playing multiplayer.
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
    
    
 
    //This method runs when the game is launching.
    @Override
    public void init(GameContainer gc) throws SlickException {

    	bulletsToRemove = new ArrayList<Body>();
    	bulletsToAdd = new ArrayList<Float[]>();
    	bulletList = new ArrayList<Bullet>();
    	
    	//Set camera coordinates and size.
        viewport = new Rectangle(0, 0, 1024, 600);
        
        //Load our map. (Made in a program called TILED)
        map1 = new TiledMap("data/2.tmx");
        
        
        //Create our World, enter a gravity (100 default, bullets have a gravity variable of this*0.05)
        Vec2 gravity = new Vec2(0, 100);
        world = new World(gravity);
        
        //Create 2 players.
        //Player 1
        player1 = new Player(5, 180, 4, 12, 11, 40, 40, "data/temp2.png", 50);
        player1.getAnimation(Player.IDLE_LEFT).start();
        
        //Player 2
        player2 = new Player(15, 180, 4, 12, 11, 40, 40, "data/temp2.png", 50);
        player2.getAnimation(Player.IDLE_LEFT).start();
        
        hpbar = new Image("data/healthbar.png");
        
        //If we want multiplayer, connect to the server.
        if(multiplayer) {
        	//Connecting to someone
        	isHosting = false;
        	myPlayer = player2;
        	hisPlayer = player1;
        	thisClient = new BFClient(myPlayer, hisPlayer, host, port);
        	status = "Connected to server";
        }
        else {
        	//Connecting to ourselfs
        	isHosting = true;
        	myPlayer = player1;
        	hisPlayer = player2;
        	new BFServer(true);
        	thisClient = new BFClient(myPlayer, hisPlayer, host, port);
        	status = "Local server online";
        }
        
        //Set player2's friction to only about half of ours, this will reduce "lag" and simulate running better.
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
        

        //Setup physics world
        timeStep = 1.0f/60.0f;
        velocityIterations = 6;
        positionIterations = 2;
        
        //Add a new collision listener to our world.
        world.setContactListener(new BFContactListener());
        
        //Add Keylistener to our GameContainer.
        gc.getInput().addKeyListener(keylistener);
        
        //Physics debug draw. (DEPRECATED. I'm using JBox2d-2.3.0 SNAPSHOT, this debugdraw hasn't been updated for it yet)
        
        //Slick2dDebugDraw sDD = new Slick2dDebugDraw(gc.getGraphics(), gc);
        //sDD.setFlags(0x0001); //Setting the debug draw flags, draw polygons, no joints.
        //world.setDebugDraw(sDD);
    }
 
    
    //This method is called 60 times a second. (VSync)
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {

    	hisPlayer.setTimer(true, delta);
    	myPlayer.setTimer(true, delta);
    	
    	if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
    		if(chatUp) {
    			//Flush and send chatmessage.
    			if(chatMessage.trim().length()>0 && chatMessage.trim()!=null) {
    				if(chatMessage.trim().substring(0, 1).equals("/") && chatMessage.trim().length()>2 && isHosting) {
    					//Command found
    					manageCommand(chatMessage.trim().substring(1));
    				}
    				else {    					
    					myPlayer.setChatMessage(chatMessage);
    					myPlayer.setTimer(false, 0);
    					try {
    						this.thisClient.outStream.flush();
    						this.thisClient.outStream.writeObject(new ChatMessage(chatMessage.trim()));
    						chatUp = false;
    					} catch (IOException e) {
    						System.err.println("Can't send chatmessage");
    						chatUp = false;
    					}
    				}
    			}
    			chatUp = false;
    			chatMessage = "";
    		}
    		else chatUp = true;
    		chatMessage = "";
    	}
    	
    	if(myPlayer.getHealth()<0) {
    		myPlayer.reset();
    	}
    	//Can only move if chat is closed.
    	if(!chatUp) {
    		//Did we press "D"?
    		if (gc.getInput().isKeyDown(Input.KEY_D)) {
    			//Run right, set direction, get proper animation (start it as well), and set a velocity
    			myPlayer.direction(Player.RUNNING_RIGHT);
    			myPlayer.getAnimation(Player.RUNNING_RIGHT).start();
    			//"Push" our player with a force of 25.5 in x-direction.
    			//NOTE: This will also enable you to "wallclimb". Haven't figured out how to solve it yet, so... we'll call it a feature.
    			myPlayer.getBody().setLinearVelocity(new Vec2(25.5f, myPlayer.getBody().getLinearVelocity().y));
    			
    		}
    		//Did we press "A"?
    		else if (gc.getInput().isKeyDown(Input.KEY_A)) {
    			//Run left, set direction, get proper animation (start it as well), and set a velocity
    			myPlayer.direction(Player.RUNNING_LEFT);
    			myPlayer.getAnimation(Player.RUNNING_LEFT).start();
    			//"Push" our player with a force of -25.5 in x-direction. (Left)
    			//NOTE: This will also enable you to "wallclimb". Haven't figured out how to solve it yet, so... we'll call it a feature.
    			myPlayer.getBody().setLinearVelocity(new Vec2(-25.5f, myPlayer.getBody().getLinearVelocity().y));
    		}
    		//Did we press... nothing?
    		else  {
    			//We're not running anywhere atm, play idle animation.
    			if(myPlayer.direction()==Player.IDLE_RIGHT || myPlayer.direction()==Player.RUNNING_RIGHT) {
    				myPlayer.direction(Player.IDLE_RIGHT);
    			}
    			else {
    				myPlayer.direction(Player.IDLE_LEFT);
    			}
    		}
    		//We want to JUMP!!
    		if (gc.getInput().isKeyPressed(Input.KEY_SPACE) && Math.round(myPlayer.getBody().getLinearVelocity().y)==0) {
    			myPlayer.getBody().setLinearVelocity(new Vec2(myPlayer.getBody().getLinearVelocity().x, -60));
    			
    		}
    		
    		//We want to SHOOOOOT!
    		if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
    			//Fire new bullet (if we're facing the right way)!
    			if((
    				//IF we're facing left, and mouseclick was on left side of the screen
    				(myPlayer.direction()==Player.RUNNING_LEFT || myPlayer.direction()==Player.IDLE_LEFT || myPlayer.direction()==Player.SHOOT_LEFT) && gc.getInput().getAbsoluteMouseX()<(gc.getWidth()/2)) ||
    				//OR if we're facing right, and mouseclick was on right side of the screen
    				(myPlayer.direction()==Player.RUNNING_RIGHT || myPlayer.direction()==Player.IDLE_RIGHT || myPlayer.direction()==Player.SHOOT_RIGHT) && gc.getInput().getAbsoluteMouseX()>(gc.getWidth()/2)) {
    				//Create the bullet. And add it to our arraylist.
    				Bullet theBullet = new Bullet(myPlayer.getX(), myPlayer.getY(), gc.getInput().getAbsoluteMouseX(), gc.getInput().getAbsoluteMouseY());
    				bulletList.add(theBullet);
    				try {
    					//Flush and send to server.
    					thisClient.outStream.flush();
    					thisClient.outStream.writeObject(new BFBulletPacket(theBullet.x, theBullet.y, theBullet.xSpeed, theBullet.ySpeed));
    				} catch (IOException e) {
    					System.err.println("Error sending bullet");
    				}
    			}
    		}
    	}
    	
    	//Step phys world each update
    	world.step(timeStep, velocityIterations, positionIterations);
    	
    	//Any bullets marked for removal?
    	if(bulletsToRemove.size()>0) {
    		for(Body b : bulletsToRemove) {
    			//Destroy physics object of this bullet.
    			world.destroyBody(b);
    		}
    		//Clear the whole arraylist.
    		bulletsToRemove.clear();
    	}
    	
    	//Any bullets to ADD?
    	if(bulletsToAdd.size()>0) {
    		for(Float[] f : bulletsToAdd) {
    			//Add bullet to our arraylist from 4 floats we got from the server.
    			bulletList.add(new Bullet(f[0], f[1], f[2], f[3]));
    		}
    		//Clear the whole arraylist
    		bulletsToAdd.clear();
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
        
    }
    public static Player getMyPlayer() {
    	return myPlayer;
    }
    
    //Add bullet, this basically puts it into an arraylist.
    public static void addBullet(float x, float y, float xspeed, float yspeed) {
    	Float[] t = new Float[4];
    	t[0] = x;
    	t[1] = y;
    	t[2] = xspeed;
    	t[3] = yspeed;
    	bulletsToAdd.add(t);
    }
    
    //Remove bullet, this basically adds it to an arraylist.
    public static void removeBody(Body obj, Body bullet) {
    	if(obj==myPlayer.getBody()) {
    		//We've been hit.
    		myPlayer.takeDamage(0.09f);
    	}
    	bulletsToRemove.add(bullet);
    }
    
    public boolean isHosting() {
    	return this.isHosting;
    }
    
    public void manageCommand(String command) {
    	if(command!=null && command.length()>0) {
    		//Switch-case with strings forbidden pre JDK 1.7
    		Integer id = null;
    		if(command.equals("iwantpeace 1")) {
				this.removeBullets = false;
				status = "Peacemode on";
				id = 1;
    		}
    		else if(command.equals("iwantpeace 0")) {
    			this.removeBullets = true;
    			status = "Local server online";
    			id = -1;
    		}
    		if(id!=null) {
    			try {
					thisClient.outStream.flush();
					thisClient.outStream.writeObject(new BFAdminCommand(id));
				} catch (IOException e) {
					System.err.println("ERROR sending admin command");
				}
    		}
    	}
    }
 
    public void render(GameContainer gc, Graphics g) throws SlickException {
    	//Make the camera (viewport) "Point" at the right stuff.
    	g.translate(512-viewport.getX(), 300-viewport.getY());
    	//Draw world from tmx file.
    	map1.render(0, 0);
    	
    	//"CLIP" an area of 1280x720 to improve performance. This will cause it to only render what's visible on my screen.
    	g.setClip(0, 0, 1280, 720);
    	
    	//Draw animations for our 2 players.
    	g.drawAnimation(player1.getAnimation(player1.direction()), player1.getX()*8, player1.getY()*8);
    	g.drawAnimation(player2.getAnimation(player2.direction()), player2.getX()*8, player2.getY()*8);
    	
    	//Draw all the bullets.
	    for(Bullet b : bulletList) {
	    	b.getImg().draw(b.x*8, b.y*8);
	    }
	    
	    
	    //DEPRECATED
    	//world.drawDebugData();
	    
	    //Draw a status & chat string.
	    g.setColor(Color.white);
	    g.drawString(status, viewport.getX()+300, viewport.getY()+250);
	    if(chatUp) {
	    	g.setColor(Color.white);
	    	g.drawString("> "+chatMessage+"_", viewport.getX()-500, viewport.getY()+250);
	    }
	    //Draw chatmessages if there are any, for a short period of time only.
	    if(myPlayer.getTimer()<2000) {
	    	myPlayer.getImg().draw(myPlayer.getX()*8-75, myPlayer.getY()*8-60);
	    	g.setColor(Color.black);
	    	g.drawString(myPlayer.getChatMessage(), myPlayer.getX()*8-67, myPlayer.getY()*8-50);
	    }
	    if(hisPlayer.getTimer()<2000) {
	    	hisPlayer.getImg().draw(hisPlayer.getX()*8-75, hisPlayer.getY()*8-60);
	    	g.setColor(Color.black);
	    	g.drawString(hisPlayer.getChatMessage(), hisPlayer.getX()*8-67, hisPlayer.getY()*8-50);
	    }
    	
	    //Draw hp bar
	    g.setColor(new Color(0, 195, 0));
	    Rectangle hpb = new Rectangle(viewport.getX()-436, viewport.getY()-289, 198*myPlayer.getHealth(), 26);
	    g.fill(hpb);
	    //Hpbar background img.
	    g.drawImage(hpbar, viewport.getX()-450, viewport.getY()-295);
 
    }
    
}
