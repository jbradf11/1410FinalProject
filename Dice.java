package finalProject;

import java.util.Random;

public class Dice {
	
	private static Random r = new Random();
	
	private Dice() {}
	
	static int Roll() {
		
		return r.nextInt(6) +1;
		
	}
	
	public static void main(String[] args) {
		
		for(int i = 0;i<10;i++)
			System.out.println(Dice.Roll());
		
	}
	
	
	
	
	

}
