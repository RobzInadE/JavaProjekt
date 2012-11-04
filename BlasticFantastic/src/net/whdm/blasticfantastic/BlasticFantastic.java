package net.whdm.blasticfantastic;
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
 
public class BlasticFantastic extends BasicGame{
 
    Image plane = null;
    Image land = null;
    Rectangle viewport;
    //TiledMap map1;
    int x = 100;
    int y = 100;
    int scale = 1;
 
    public BlasticFantastic()
    {
        super("BlasticFantastic 0.1");
    }
 
    @Override
    public void init(GameContainer gc) throws SlickException {
        plane = new Image("data/plane.png");
        land = new Image("data/land.jpg");
        viewport = new Rectangle(0, 0, 800, 600);
        //map1 = new TiledMap("data/test.tmx");
    }
 
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
    	
    }
 
    public void render(GameContainer gc, Graphics g) throws SlickException {
    	g.setColor(new Color(72, 152, 72));
    	g.fillRect(0,0,4800,4800); //Background
    	//map1.render(0,0);
        //land.draw(0, 0);
 
        //plane.draw(x, y, scale);
 
    }
 
    public static void main(String[] args) throws SlickException {
         AppGameContainer app =
			new AppGameContainer( new BlasticFantastic() );
         app.setVSync(true);
         app.setDisplayMode(800, 600, false);
         app.start();
    }
}