package net.whdm.blasticfantastic;


import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;


/*
 * This implements a ContactListender found in JBox2d, we handle all collisions here.
 */

public class BFContactListener implements ContactListener {

	//Contact happend.
	@Override
	public void beginContact(Contact c) {
		Bullet toRemove = null;
		for(Bullet s : BlasticFantastic.bulletList) {
			if(s.getBody()==c.getFixtureB().getBody()) {
				//Found colliding bullet
				//Remove it's body from phys world
				if(BlasticFantastic.removeBullets) {
					BlasticFantastic.removeBody(c.getFixtureA().getBody(), s.getBody());
					toRemove = s;
				}
			}
		}
		//And remove graphical version of the bullet in question.
		if(toRemove != null) BlasticFantastic.bulletList.remove(toRemove);
	}
	
	//We won't be using these 3 methods.

	@Override
	public void endContact(Contact c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact c, ContactImpulse ci) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact c, Manifold m) {
		// TODO Auto-generated method stub

	}

}
