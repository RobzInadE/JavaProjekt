package net.whdm.blasticfantastic;


import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class BFContactListener implements ContactListener {

	@Override
	public void beginContact(Contact c) {
		Bullet toRemove = null;
		for(Bullet s : BlasticFantastic.bulletList) {
			if(s.getBody()==c.getFixtureB().getBody()) {
				toRemove = s;
			}
		}
		BlasticFantastic.bulletList.remove(toRemove);
	}

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
