package rev1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Farkle {
	
	private int score;
	private int roundScore;
	private int[] dice;
	private boolean[] activeDice;
	private boolean farkle;
	
	public Farkle() {
		score = 0;
		roundScore = 0;
		dice = new int[]{1,2,3,4,5,6};
		activeDice = new boolean[] {true,true,true,true,true,true};
		farkle = false;
	}
	
	public int[] getDice() {
		return dice;
	}
	
	public int getScore() {
		return score;
	}
	
//	public void setRoundScore(int[] a){
//		for(int i =0;i<6;i++) {
//			if(a[i]==1) activeDice[i] = 0;
//		}
//		
//	}

	public int getRoundScore() {
		return roundScore;
	}
	
	public boolean getFarkle() {
		return farkle;
	}

	public void keep(boolean[] a) {
		roundScore+= testScore(a);
		setActiveDice(a);
	}
	
	void setActiveDice(boolean [] a) {
		for(int i = 0;i<6;i++)
			if(a[i]) activeDice[i] = false;
	}
	
	public int[] roll(boolean[] r) {
		if(Arrays.equals(r, new boolean[] {false,false,false,false,false,false})) {
			activeDice = new boolean[] {true,true,true,true,true,true};
			return rollAll();
		}
		int count = 0;
		for(int i = 0;i<6;i++)
			if(r[i]  && activeDice[i]) count++;
		
		int[] test = new int[count];
		count = 0;
		for(int i = 0; i<r.length;i++)
			if(r[i] && activeDice[i]) { 
				dice[i] = Dice.roll();
				test[count] = dice[i];
				count++;
			}
		//int[] f = trimMe(r);
		if(testFarkle(test)) return null;//new int[] {-1,-1,-1,-1,-1,-1};
		return dice;
	}
	
	public int[] rollAll() {
		for(int i = 0;i<6;i++)
			dice[i] = Dice.roll();
		Arrays.sort(dice);
		if(testFarkle(dice)) return null; //new int[] {-1,-1,-1,-1,-1,-1};
		return dice;
	}
	
//	int[] trimMe(int[] r) {
//		int count = 0;
//		for(int a: r) {
//			if(a == 1) count++;
//		}
//		int[] r2 = new int[count];
//		count = 0;
//		for(int i = 0;i<r.length;i++) {
//			if(r[i]==1) { 
//				r2[count] = dice[i];
//				count++;
//			}
//		}
//		return r2;
//	}
	
//	int[] invertMe(int[] r) {
//		int[] r2 = new int[r.length];
//		for(int i = 0;i<r.length;i++) {
//			if(r[i]==0) r2[i] = 1;
//			else r2[i]=0;
//		}
//		return r2;
	//}
	
	public int testScore(boolean[] a){
		List<Integer> b = new ArrayList<>();
		for(int i = 0;i<6;i++) 
			if(a[i]) b.add(dice[i]);
		
		return testScore(b);
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
					
			case 0: return -1;
					
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
		
	void resetRound() {
		if(farkle) roundScore = 0;
		activeDice = new boolean[] {true,true,true,true,true,true};
		score+= roundScore;
		roundScore = 0;
		farkle = false;
		return;
	}
	
//	void testPrint() {
//		System.out.print("[");
//		for(int i = 0;i<dice.length-1;i++) {
//			System.out.print(dice[i]+", ");
//		}
//		System.out.print(dice[dice.length-1]+"]");
//	}
	
	public static void main(String[] args) {
		Farkle test = new Farkle();
		test.roll(new boolean[] {true,true, true, false,false,false});
		System.out.println(Arrays.toString(test.getDice()));
		test.keep(new boolean[] {false,false,false,false,true,false});
		System.out.println(test.getRoundScore());
		System.out.println(Arrays.toString(test.activeDice));
		
	}
//test Method no longer fully functional	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static void main(String[] args) {
//		
//		Farkle one = new Farkle();
//		Scanner in = new Scanner(System.in);
//	
//		while(one.score < 5000) {
//			//new Round
//			System.out.println("New Round - Total Score: " + one.getScore());
//			System.out.println("----------");
//			one.RollAll();
//			
//			//Round loop;
//			while(!one.getFarkle()) 
//			{
//				List a = new ArrayList();
//				
//				System.out.print("Keep: ");
//					String keepMe = in.nextLine();
//					String[] values = keepMe.split(" ");
//					int[] temp = new int[values.length];
//					
//				for(int i = 0;i<values.length;i++) {
//					temp[i] = Integer.parseInt(values[i]);
//					if(temp[i]==1) a.add(one.dice[i]);
//				}
//				
//				one.setActiveDice(temp);
//				one.roundScore += one.testScore(a);
//				one.testPrint(); System.out.print("Round Score: "+one.getRoundScore());
//				
//				System.out.println("\nRoll again?(y or n): ");
//				if(in.nextLine().equals("y")) 
//				{
//					one.Roll(one.activeDice);
//					continue;
//				}
//				
//				else break;
//			}
//			
//			//round exit	
//			if(one.getFarkle()) {
//				one.resetRound();
//				System.out.println("Round Over! - Farkle!");
//			}
//			else {
//				System.out.println("Round Score Submitted: " + one.getRoundScore());
//				one.resetRound();
//			}
//		}
//		//print final score and exit
//		System.out.println("Final Score: "+ one.getScore());
//		in.close();
//	}
}
