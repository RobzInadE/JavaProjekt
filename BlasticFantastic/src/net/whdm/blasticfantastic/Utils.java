package net.whdm.blasticfantastic;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Utils {
	//Create a JBox2D world. 
    public static final World world = new World(new Vec2(0.0f, -10.0f), true);
     
    //Screen width and height
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
     
    //Ball radius in pixel
    public static final int BALL_SIZE = 8;
     
    //Total number of balls
    public final static int NO_OF_BALLS = 400; 
     
    //This method adds a ground to the screen. 
    public static void addGround(float width, float height){
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width,height);
         
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
 
        BodyDef bd = new BodyDef();
        bd.position= new Vec2(0.0f,-10f);
 
        world.createBody(bd).createFixture(fd);
    }
     
    //This method creates a walls. 
    public static void addWall(float posX, float posY, float width, float height){
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width,height);
         
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 1.0f;
        fd.friction = 0.3f;    
 
        BodyDef bd = new BodyDef();
        bd.position.set(posX, posY);
         
        Utils.world.createBody(bd).createFixture(fd);
    }
  
}
