package net.whdm.blasticfantastic;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

/*
 * This class implements a keylistener so we can enter chatinput.
 */

public class BFKeyListener implements KeyListener{

	@Override
	public void inputEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputStarted() {
		// TODO Auto-generated method stub
		
	}

	//Accept input.
	@Override
	public boolean isAcceptingInput() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setInput(Input arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(int kId, char kChar) {
		if(BlasticFantastic.chatUp) {
			if(kId==Input.KEY_BACK && BlasticFantastic.chatMessage.length()>0 && BlasticFantastic.chatMessage!=null){
				BlasticFantastic.chatMessage = BlasticFantastic.chatMessage.substring(0, BlasticFantastic.chatMessage.length()-1);
			}
			else if(BlasticFantastic.chatMessage.length()<20) {
				BlasticFantastic.chatMessage = BlasticFantastic.chatMessage+kChar;
			}
		}
	}

	@Override
	public void keyReleased(int arg0, char arg1) {
		// TODO Auto-generated method stub
		
	}

}
