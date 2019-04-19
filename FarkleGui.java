package rev3;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLayeredPane;
import javax.swing.JTextPane;
import java.awt.Color;

public class FarkleGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel_8;
	//icons for each die
	private ImageIcon die1 = new ImageIcon("dice/die-1.png");
	private ImageIcon die2 = new ImageIcon("dice/die-2.png");
	private ImageIcon die3 = new ImageIcon("dice/die-3.png");
	private ImageIcon die4 = new ImageIcon("dice/die-4.png");
	private ImageIcon die5 = new ImageIcon("dice/die-5.png");
	private ImageIcon die6 = new ImageIcon("dice/die-6.png");
	//tracks active check boxes
	private boolean[] check = new boolean[6];
	//this array makes it easy to put die icons onto labels
	private JLabel[]dieLabel = new JLabel[6];
	//will be replaced by an array
	private Farkle player1 = new Farkle();
	//temporary output field - will probably be replaced
	private JTextField txtWelcomeToFarkle;
	//makes it easy to reset CheckBoxes
	private JCheckBox[] boxes = new JCheckBox[6];
	//action buttons
	private JButton btnSubmitDice;
	private JButton btnRollDice;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				try {
					FarkleGui frame = new FarkleGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FarkleGui() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
		panel_8 = new JPanel();
		contentPane.add(panel_8, BorderLayout.WEST);
		panel_8.setLayout(new GridLayout(0, 2, 0, 0));
		
		diePanels(panel_8);
		
		btnRollDice = new JButton("Roll Dice");
		panel_8.add(btnRollDice);
		btnRollDice.addActionListener(new ActionListener() {
			String firstRound = "Roll Dice";
			String midRoll = "Keep Dice and Roll Again";
			String newRound = "Roll Dice - new round";

			public void actionPerformed(ActionEvent e) {
				
				//clears 
				clearSelectedBoxes();
				
				txtWelcomeToFarkle.setText(" Select the Dice you would like to keep:");
				
				if(btnRollDice.getText().equals(newRound)) {
					boolean[] b = Arrays.copyOf(player1.getActiveDice(),6);
					
					for(int i = 0;i<6;i++) 
						if(!boxes[i].isSelected())
							b[i]=false;
					player1.keep(b);
					clearAllBoxes();
				}
				
				//Check button text and if it is a new round roll all and enable
				if(btnRollDice.getText().equals(firstRound) || btnRollDice.getText().equals(newRound)){
					System.out.println("Initial Round Roll");
					if(player1.rollAll()!=null) {
						txtWelcomeToFarkle.setText("Round Score: " + player1.getRoundScore() + " Select the Dice you would like to keep:");
						for(int i =0;i<6;i++) {
							dieLabel[i].setIcon(setDice(player1,i));
						}	
						enableAllBoxes();
						btnRollDice.setText(midRoll);
						btnRollDice.setEnabled(false);
						btnSubmitDice.setEnabled(false);
						btnSubmitDice.setText("Keep Dice and End Round");
					}else {
						for(int i =0;i<6;i++) {
							dieLabel[i].setIcon(setDice(player1,i));
						}
					}
					return;
				}
				
				boolean[] b = Arrays.copyOf(player1.getActiveDice(),6);
				
				for(int i = 0;i<6;i++) 
					if(!boxes[i].isSelected())
						b[i]=false;
				
				//System.out.println("Before Keep: " + Arrays.toString(player1.getActiveDice()) + player1.getRoundScore());
				player1.keep(b);
				//System.out.println("After Keep: " + Arrays.toString(player1.getActiveDice())+ player1.getRoundScore());

				if(player1.roll(player1.getActiveDice())!=null) {
					txtWelcomeToFarkle.setText("Round Score: " + player1.getRoundScore() + " Select the Dice you would like to keep:");
					for(int i =0;i<6;i++) {
						dieLabel[i].setIcon(setDice(player1,i));
					}
					
					for(int i = 0;i<6;i++) 
						if(boxes[i].isSelected()) {
							boxes[i].setEnabled(false);
							boxes[i].setSelected(false);
						}
					
				}else {
					for(int i =0;i<6;i++) 
						dieLabel[i].setIcon(setDice(player1,i));
					zilch();
					disableAllBoxes();
					//forward turn to next player
				}

			}					
		});
		
		btnSubmitDice = new JButton("");
		btnSubmitDice.setEnabled(false);
		btnSubmitDice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean[] b = new boolean[6];
				for(int i = 0;i<6;i++) {
					if(boxes[i].isSelected()) {
						b[i] = true;
						boxes[i].setEnabled(false);
					}
				}
				player1.keep(b);
				System.out.println(Arrays.toString(player1.getActiveDice()));
			}
		});
		panel_8.add(btnSubmitDice);
		
		txtWelcomeToFarkle = new JTextField();
		txtWelcomeToFarkle.setText("Welcome to Farkle");
		contentPane.add(txtWelcomeToFarkle, BorderLayout.NORTH);
		txtWelcomeToFarkle.setColumns(10);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(0,0,459,596);
		contentPane.add(layeredPane, BorderLayout.CENTER);
		
		JLabel label = new JLabel("");
		//layeredPane.setLayer(label, 1);
		label.setIcon(new ImageIcon("dice/farkel.png"));
		label.setBounds(0, 0, 459, 596);
		layeredPane.add(label);
		
		JTextPane txtpnTesting = new JTextPane();
		txtpnTesting.setBackground(Color.BLUE);
		layeredPane.setLayer(txtpnTesting, 1);
		txtpnTesting.setText("testing");
		txtpnTesting.setBounds(28, 114, 95, 325);
		layeredPane.add(txtpnTesting);
		
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Color.BLUE);
		layeredPane.setLayer(textPane, 2);
		textPane.setBounds(130, 114, 95, 325);
		layeredPane.add(textPane);
		
		JTextPane textPane_1 = new JTextPane();
		textPane_1.setBackground(Color.BLUE);
		layeredPane.setLayer(textPane_1, 3);
		textPane_1.setBounds(232, 114, 95, 325);
		layeredPane.add(textPane_1);
		
		JTextPane textPane_2 = new JTextPane();
		textPane_2.setBackground(Color.BLUE);
		layeredPane.setLayer(textPane_2, 4);
		textPane_2.setBounds(336, 114, 95, 325);
		layeredPane.add(textPane_2);
				
	}

	private void diePanels(JPanel panel) {
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3);
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4);
		
		JPanel panel_5 = new JPanel();
		panel.add(panel_5);
		
		JPanel panel_6 = new JPanel();
		panel.add(panel_6);
		
		JPanel panel_7 = new JPanel();
		panel.add(panel_7);
		
		JCheckBox KeepDice1 = new JCheckBox("Keep Dice");
		boxes[0] = KeepDice1;
		KeepDice1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				boolean[] b = Arrays.copyOf(player1.getActiveDice(), 6);
				System.out.println(Arrays.toString(b) + "\n" + Arrays.toString(check));
				if(KeepDice1.isSelected()&& b[0]) check[0] = true;
				else check[0] = false;
				
				System.out.println(Arrays.toString(check));
				if(player1.testScore(check)!=-1) {
					txtWelcomeToFarkle.setText("Round Score: "+player1.getRoundScore()+": Potential Score for selection: "+player1.testScore(check));
					btnSubmitDice.setEnabled(true);
					btnRollDice.setEnabled(true);
					checkReroll();
				}
				else {
					txtWelcomeToFarkle.setText("Round Score: "+player1.getRoundScore()+": Non-Scoring combination");
					btnSubmitDice.setEnabled(false);
					btnRollDice.setEnabled(false);
				}
			}
		});
		
		JLabel Dice1 = new JLabel("");
		dieLabel[0] = Dice1;
		panel_2.add(Dice1);
		Dice1.setVerticalAlignment(SwingConstants.TOP);
		Dice1.setIcon(die1);
		Dice1.setHorizontalAlignment(SwingConstants.LEFT);
		panel_2.add(KeepDice1);
		
		JCheckBox KeepDice2 = new JCheckBox("Keep Dice");
		boxes[1] = KeepDice2;
		KeepDice2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean[] b = Arrays.copyOf(player1.getActiveDice(), 6);
				System.out.println(Arrays.toString(b) + "\n" + Arrays.toString(check));
				if(KeepDice2.isSelected()&& b[1]) check[1] = true;
				else check[1] = false;
				System.out.println(Arrays.toString(check));
				if(player1.testScore(check)!=-1) {
					txtWelcomeToFarkle.setText("Round Score: "+player1.getRoundScore()+": Potential Score for selection: "+player1.testScore(check));
					btnSubmitDice.setEnabled(true);
					btnRollDice.setEnabled(true);
					checkReroll();
				}
				else {
					txtWelcomeToFarkle.setText("Round Score: "+player1.getRoundScore()+": Non-Scoring combination");
					btnSubmitDice.setEnabled(false);
					btnRollDice.setEnabled(false);
				}
			}
		});
		
		JLabel Dice2 = new JLabel("");
		dieLabel[1] = Dice2;
		panel_3.add(Dice2);
		Dice2.setVerticalAlignment(SwingConstants.TOP);
		Dice2.setIcon(die2);
		Dice2.setHorizontalAlignment(SwingConstants.CENTER);	
		panel_3.add(KeepDice2);
		
		JCheckBox KeepDice3 = new JCheckBox("Keep Dice");
		boxes[2] = KeepDice3;
		KeepDice3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean[] b = Arrays.copyOf(player1.getActiveDice(), 6);
				System.out.println(Arrays.toString(b) + "\n" + Arrays.toString(check));
				if(KeepDice3.isSelected()&& b[2]) check[2] = true;
				else check[2] = false;
				System.out.println(Arrays.toString(check));
				if(player1.testScore(check)!=-1) {
					txtWelcomeToFarkle.setText("Round Score: "+player1.getRoundScore()+": Potential Score for selection: "+player1.testScore(check));
					btnSubmitDice.setEnabled(true);
					btnRollDice.setEnabled(true);
					checkReroll();
				}
				else {
					txtWelcomeToFarkle.setText("Round Score: "+player1.getRoundScore()+": Non-Scoring combination");
					btnSubmitDice.setEnabled(false);
					btnRollDice.setEnabled(false);
				}
			}
		});
		
		JLabel Dice3 = new JLabel("");
		panel_4.add(Dice3);
		dieLabel[2] = Dice3;
		Dice3.setIcon(die3);
		Dice3.setHorizontalAlignment(SwingConstants.CENTER);
		panel_4.add(KeepDice3);
		
		JCheckBox KeepDice4 = new JCheckBox("Keep Dice");
		boxes[3] = KeepDice4;
		KeepDice4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean[] b = Arrays.copyOf(player1.getActiveDice(), 6);
				System.out.println(Arrays.toString(b) + "\n" + Arrays.toString(check));
				if(KeepDice4.isSelected()&& b[3]) check[3] = true;
				else check[3] = false;
				System.out.println(Arrays.toString(check));
				if(player1.testScore(check)!=-1) {
					txtWelcomeToFarkle.setText("Round Score: "+player1.getRoundScore()+": Potential Score for selection: "+player1.testScore(check));
					btnSubmitDice.setEnabled(true);
					btnRollDice.setEnabled(true);
					checkReroll();
				}
				else {
					txtWelcomeToFarkle.setText("Round Score: "+player1.getRoundScore()+": Non-Scoring combination");
					btnSubmitDice.setEnabled(false);
					btnRollDice.setEnabled(false);
				}
			}
		});
		
		JLabel Dice4 = new JLabel("");
		panel_5.add(Dice4);
		dieLabel[3] = Dice4;
		Dice4.setIcon(die4);
		Dice4.setHorizontalAlignment(SwingConstants.CENTER);		
		panel_5.add(KeepDice4);
		
		JCheckBox KeepDice5 = new JCheckBox("Keep Dice");
		boxes[4] = KeepDice5;
		KeepDice5.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				boolean[] b = Arrays.copyOf(player1.getActiveDice(), 6);
				System.out.println(Arrays.toString(b) + "\n" + Arrays.toString(check));
				if(KeepDice5.isSelected()&& b[4]) check[4] = true;
				else check[4] = false;
				System.out.println(Arrays.toString(check));
				if(player1.testScore(check)!=-1) {
					txtWelcomeToFarkle.setText("Round Score: "+player1.getRoundScore()+ ": Potential Score for selection: "+player1.testScore(check));
					btnSubmitDice.setEnabled(true);
					btnRollDice.setEnabled(true);
					checkReroll();
				}
				else {
					txtWelcomeToFarkle.setText("Round Score: "+player1.getRoundScore()+": Non-Scoring combination selected");
					btnSubmitDice.setEnabled(false);
					btnRollDice.setEnabled(false);
				}
			}
		});
		
			JLabel Dice5 = new JLabel("");
			dieLabel[4] = Dice5;
			panel_6.add(Dice5);
			Dice5.setIcon(die5);
			Dice5.setHorizontalAlignment(SwingConstants.CENTER);
		panel_6.add(KeepDice5);
		
		JCheckBox KeepDice6 = new JCheckBox("Keep Dice");
		boxes[5] = KeepDice6;
		KeepDice6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean[] b = Arrays.copyOf(player1.getActiveDice(), 6);
				System.out.println("Active Dice: "+Arrays.toString(b) + "\nChecked before: "  + Arrays.toString(check));
				if(KeepDice6.isSelected()&& b[5]) check[5] = true;
				else check[5] = false;
				System.out.println("Checked After: "+Arrays.toString(check));
				if(player1.testScore(check)!=-1) {
					txtWelcomeToFarkle.setText("Round Score: "+player1.getRoundScore()+": Potential Score for selection: "+player1.testScore(check));
					btnSubmitDice.setEnabled(true);
					btnRollDice.setEnabled(true);
					checkReroll();
				}
				else {
					txtWelcomeToFarkle.setText("Round Score: "+player1.getRoundScore()+": Non-Scoring combination");
					btnSubmitDice.setEnabled(false);
					btnRollDice.setEnabled(false);
				}
			}
		});
		
		JLabel Dice6 = new JLabel("");
		dieLabel[5] = Dice6;
		panel_7.add(Dice6);
		Dice6.setIcon(die6);
		Dice6.setHorizontalAlignment(SwingConstants.CENTER);
		panel_7.add(KeepDice6);
		
	}
	
	//if round ends with a non-scoring roll no points are scored and round is over
	private void zilch() {
		txtWelcomeToFarkle.setText("Player 1 FARKLES: Round Over: "+player1.getRoundScore()+ "points lost");
		player1.resetRound();
		clearAllBoxes();
		disableAllBoxes();
		btnRollDice.setText("Roll Dice");
		btnSubmitDice.setText("");
		btnSubmitDice.setEnabled(false);
		
	}
	
	private void disableAllBoxes() {
		for(JCheckBox a: boxes) 
			a.setEnabled(false);
	}
	
	private void enableAllBoxes() {
		for(JCheckBox a:boxes) {
			a.setEnabled(true);
			a.setSelected(false);
		}
	}
	
	private void clearSelectedBoxes() {
		for(int i = 0; i<6;i++) {
			check[i] = false;
		}
	}
	
	private void clearAllBoxes() {
		for(int i = 0;i<6;i++) {
			boxes[i].setSelected(false);
			check[i] = false;
		}
	}
	
	
	
	private void checkReroll() {
		for(int i = 0;i<6;i++) {
			if(boxes[i].isEnabled() && !check[i]) {
				return;
			}
		}
		btnRollDice.setText("Roll Dice - new round");
	}
	
	private ImageIcon setDice(Farkle a, int i) {
	int[] b = a.getDice();

		switch(b[i]) {
			case 1: return die1;
			case 2: return die2;
			case 3: return die3;
			case 4: return die4;
			case 5: return die5;
			case 6: return die6;
			default: throw new UnsupportedOperationException();
			//default: System.out.println(Arrays.toString(b)); return null;
		}
	
	}
}