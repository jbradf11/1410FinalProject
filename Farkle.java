package finalProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Farkle {
	
	private int score;
	private int roundScore;
	private int[] dice;
	private int[] activeDice;
	private int[] keptDice;
	private boolean farkle;
	
	public Farkle() {
		score = 0;
		roundScore = 0;
		dice = new int[6];
		activeDice = new int[] {1,1,1,1,1,1};
		keptDice = new int[] {0,0,0,0,0,0};
		farkle = false;
	}
	
	public int[] getDice() {
		return dice;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getRoundScore() {
		return roundScore;
	}
	
	public boolean getFarkle() {
		return farkle;
	}
	
	private void setKept(int [] a) {
		for(int i = 0;i<keptDice.length;i++)
			if(a[i] == 1) keptDice[i] = 1;
		activeDice = invertMe(keptDice);
	}
	
	public int[] Roll(int[] r) {
		if(Arrays.equals(r, new int[] {0,0,0,0,0,0})) {
			activeDice = new int[] {1,1,1,1,1,1};
			keptDice = new int[] {0,0,0,0,0,0};
			return RollAll();
		}
		for(int i = 0; i<r.length;i++)
			if(r[i]==1) 
				dice[i] = Dice.Roll();
		int[] f = trimMe(r);
		testPrint();
		System.out.println("Farkle: "+testFarkle(f));
		return dice;
	}
	
	public int[] RollAll() {
		for(int i = 0;i<dice.length;i++)
			dice[i] = Dice.Roll();
		Arrays.sort(dice);
		testPrint();
		System.out.println("Farkle: "+testFarkle(dice));
		return dice;
	}
	
	int[] trimMe(int[] r) {
		int count = 0;
		for(int a: r) {
			if(a == 1) count++;
		}
		int[] r2 = new int[count];
		count = 0;
		for(int i = 0;i<r.length;i++) {
			if(r[i]==1) { 
				r2[count] = dice[i];
				count++;
			}
		}
		return r2;
	}
	
	int[] invertMe(int[] r) {
		int[] r2 = new int[r.length];
		for(int i = 0;i<r.length;i++) {
			if(r[i]==0) r2[i] = 1;
			else r2[i]=0;
		}
		return r2;
	}
	
	public int testScore(List<Integer> b) {
		int[] returnMe = new int[b.size()];
		for(int i = 0; i<b.size(); i++) {
			returnMe[i] = b.get(i);
		}
		return testScore(returnMe);
	}
	
	public int testScore(int[] a) {
		
		Arrays.sort(a);
		
		List<Integer> b = new ArrayList<>();
		
		for(int c:a)
			b.add(c);
		
		int test = 0;
		int temp = 0;
		
		switch(a.length){
		
			case 6: if(Arrays.equals(a, new int[] {1,2,3,4,5,6})) 				//Straight is worth 3000
						return 3000;
					else if(Arrays.equals(a, new int[] {1,1,1,1,1,1})) 			//Unique case - two 3 of a kinds of 1's worth more than 3 pair
						return 2000; 
					else if(a[0]==a[1] && a[2]==a[3] && a[4]==a[5]) 			//3 pair worth 1500
						return 1500;
					else 
						for(int i = 0;i<4;i++)
							if(a[i] == a[i+1] && a[i] == a[i+2]) {
								test += threeOfKind(a[i]);
								b.remove(i);b.remove(i);b.remove(i);
								temp = testScore(b);
								if(temp==-1) return -1;
								return test+=temp;
							};
						break;
			case 5:for(int i = 0;i<3;i++) {
						if(a[i] == a[i+1] && a[i] == a[i+2]) {
							test += threeOfKind(a[i]);
							b.remove(i);b.remove(i);b.remove(i);
							temp = testScore(b);
							if(temp==-1) return -1;
							return test+=temp;
						}
					}
					break;
			case 4: for(int i = 0;i<2;i++) {
						if(a[i] == a[i+1] && a[i] == a[i+2]) {
							test += threeOfKind(a[i]);
							b.remove(i);b.remove(i);b.remove(i);
							temp = testScore(b);
							if(temp==-1) return -1;
							return test+=temp;
						}
					}
					if(Arrays.equals(a, new int[] {1,1,5,5})) 
						return 300;
					else return -1;
					
			case 3:if(a[0] == a[1] && a[0] == a[2]) 
						return test += threeOfKind(a[0]);
					else if(a[0]==1 && a[1] == 5 && a[2]==5) 
						return test+=200;	
					else if(a[0]== 1 && a[1] == 1 && a[2] == 5) 
						return test+=250;
					else return -1;	
			
			case 2: if(a[0]==1 &&a [1]==1) return test+=200;
					else if(a[0]==1&&a[1]==5) return test+=150;
					else if(a[0]==5&a[1]==5) return test+=100;
					else return -1;
			
			case 1: if(a[0]==1) test += 100;
					else if(a[0]==5) test += 50;
					else return -1;
					return test;
					
			case 0: return 0;
					
			default: return -1;
		
		}
					
		return -1;
		
	}

	public boolean testFarkle(int[] a) {
		Arrays.sort(a);
		for(int b:a) {
			if(b == 1 || b == 5)
				return false;
		}
		for(int i = 0;i<a.length-2;i++) {
			if(a[i]== a[i+1]&&a[i]==a[i+2])
				return false;
		}
		if(a.length == 6) {
			if(a[0]==a[1]&&a[2]==a[3]&&a[4]==a[5])
				return false;
		}
		farkle = true;
		return farkle;
	}
			
	private int threeOfKind(int a) {
		
		if(a == 1) return 1000;
		else return a*100;	
	}
		
	private void resetRound() {
		if(farkle) roundScore = 0;
		activeDice = new int[]{1,1,1,1,1,1};
		keptDice = new int[]{0,0,0,0,0,0};
		score+= roundScore;
		roundScore = 0;
		farkle = false;
		return;
	}
	
	public void testPrint() {
		System.out.print("[");
		for(int i = 0;i<dice.length-1;i++) {
			System.out.print(dice[i]+", ");
		}
		System.out.print(dice[dice.length-1]+"]");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		
		Farkle one = new Farkle();
		Scanner in = new Scanner(System.in);
	
		while(one.score < 5000) {
			//new Round
			System.out.println("New Round - Total Score: " + one.getScore());
			System.out.println("----------");
			one.RollAll();
			
			//Round loop;
			while(!one.getFarkle()) 
			{
				List a = new ArrayList();
				
				System.out.print("Keep: ");
					String keepMe = in.nextLine();
					String[] values = keepMe.split(" ");
					int[] temp = new int[values.length];
					
				for(int i = 0;i<values.length;i++) {
					temp[i] = Integer.parseInt(values[i]);
					if(temp[i]==1) a.add(one.dice[i]);
				}
				
				one.setKept(temp);
				one.roundScore += one.testScore(a);
				one.testPrint(); System.out.print("Round Score: "+one.getRoundScore());
				
				System.out.println("\nRoll again?(y or n): ");
				if(in.nextLine().equals("y")) 
				{
					one.Roll(one.activeDice);
					continue;
				}
				
				else break;
			}
			
			//round exit	
			if(one.getFarkle()) {
				one.resetRound();
				System.out.println("Round Over! - Farkle!");
			}
			else {
				System.out.println("Round Score Submitted: " + one.getRoundScore());
				one.resetRound();
			}
		}
		//print final score and exit
		System.out.println("Final Score: "+ one.getScore());
		in.close();
	}
}
