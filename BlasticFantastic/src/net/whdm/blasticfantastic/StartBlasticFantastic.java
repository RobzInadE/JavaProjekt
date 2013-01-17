package net.whdm.blasticfantastic;

import java.util.Scanner;

public class StartBlasticFantastic {
	
	public static void main(String[] args) {
		System.out.println("Welcome to the launcher! 1 for singleplayer, 2 for multiplayer");
		Scanner in = new Scanner(System.in);
		
		int a = in.nextInt();
		in.nextLine();
		switch(a) {
		case 1: {
			new BlasticFantastic();
			break;
		}
		case 2: {
			System.out.println("Enter host:");
			String b = in.nextLine();
			System.out.println("Enter port:");
			int c = in.nextInt();
			new BlasticFantastic(b, c);
			break;
		}
		default: {
			System.out.println("Wrong selection. Exiting.");
			break; 
		}
		
		}
	}

}
