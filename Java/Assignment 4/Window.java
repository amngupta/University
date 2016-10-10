import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
/**
 * The main class Window contains the entire code for the Card Game
 **/
public class Window {
	JFrame frame = new JFrame();
	JPanel UserPanel = new JPanel();
	JLabel Card_1 = new JLabel("");
	JLabel Card_2 = new JLabel("");
	JLabel Card_3 = new JLabel("");
	JPanel ButtonsPanel = new JPanel();
	JPanel panel = new JPanel();
	JButton replace1 = new JButton("Replace Card 1");
	JButton replace2 = new JButton("Replace Card 2");
	JButton replace3 = new JButton("Replace Card 3");
	JPanel panel_4 = new JPanel();
	JPanel panel_6 = new JPanel();
	JTextField betAmount = new JTextField();
	JLabel lblNewLabel = new JLabel("Bet: $");
	JButton btnStart = new JButton("Start");
	JButton btnResult = new JButton("Result");
	JPanel panel_5 = new JPanel();
	JLabel Message1 = new JLabel("");
	JLabel Card_6 = new JLabel("");
	JPanel panel_7 = new JPanel();
	JLabel Message2 = new JLabel("");
	JLabel Card_5 = new JLabel("");
	JPanel DealerPanel = new JPanel();
	JLabel Card_4 = new JLabel("");
	public static int Money = 100;
	public static ArrayList<Integer> Cards = new ArrayList<>();
	public static ArrayList<Integer> Cards_Deck = new ArrayList<>();
	public static int CardCC = 0;
	public static int Bet = 0;
	public static int GameC = 0;
	public static boolean checker = true;

	/**
	 * The Window Constructor is used to initialize the Swing UI and all of it's elements.
	 */
	public Window() {
		JPanel Menu_Panel = new JPanel();
		JMenuBar menuBar = new JMenuBar();
		JMenu mnNewMenu = new JMenu("Game");
		JMenuItem newGame = new JMenuItem("New Game");

		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.getContentPane().add(Menu_Panel);
		Menu_Panel.add(menuBar);
		menuBar.add(mnNewMenu);
		mnNewMenu.add(newGame);
		frame.getContentPane().add(UserPanel);
		UserPanel.setLayout(new GridLayout(0, 3, 0, 0));
		Card_1.setHorizontalAlignment(SwingConstants.CENTER);
		Card_1.setIcon(new ImageIcon(Window.class.getResource("/Images/card_back.gif")));
		UserPanel.add(Card_1);
		Card_2.setHorizontalAlignment(SwingConstants.CENTER);
		UserPanel.add(Card_2);
		Card_2.setIcon(new ImageIcon(Window.class.getResource("/Images/card_back.gif")));
		Card_3.setHorizontalAlignment(SwingConstants.CENTER);
		Card_3.setIcon(new ImageIcon(Window.class.getResource("/Images/card_back.gif")));
		UserPanel.add(Card_3);
		frame.getContentPane().add(ButtonsPanel);
		ButtonsPanel.setLayout(new GridLayout(4, 0, 0, 0));
		ButtonsPanel.add(panel);
		panel.setLayout(new GridLayout(0, 3, 0, 0));
		panel.add(replace1);
		newGame.addActionListener(new Restart());
		replace1.addActionListener(new Replace());
		panel.add(replace2);
		replace2.addActionListener(new Replace());
		panel.add(replace3);
		replace3.addActionListener(new Replace());
		ButtonsPanel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 3, 0, 0));
		panel_4.add(panel_6);
		panel_6.add(lblNewLabel);
		panel_6.add(betAmount);
		betAmount.setColumns(10);
		panel_4.add(btnStart);
		btnStart.addActionListener(new StartListener());
		panel_4.add(btnResult);
		btnResult.addActionListener(new ResultListener());
		ButtonsPanel.add(panel_5);
		panel_5.add(Message1);
		ButtonsPanel.add(panel_7);
		panel_7.add(Message2);
		frame.getContentPane().add(DealerPanel);
		DealerPanel.setLayout(new GridLayout(0, 3, 0, 0));
		Card_4.setHorizontalAlignment(SwingConstants.CENTER);
		Card_4.setIcon(new ImageIcon(Window.class.getResource("/Images/card_back.gif")));
		DealerPanel.add(Card_4);
		Card_5.setHorizontalAlignment(SwingConstants.CENTER);
		DealerPanel.add(Card_5);
		Card_5.setIcon(new ImageIcon(Window.class.getResource("/Images/card_back.gif")));
		Card_6.setHorizontalAlignment(SwingConstants.CENTER);
		Card_6.setIcon(new ImageIcon(Window.class.getResource("/Images/card_back.gif")));
		DealerPanel.add(Card_6);
		frame.setVisible(true);
	}
	/**
	 * The Restart method is used to initiate a New Game and dispose off the frame of the last game
	 */
	public class Restart implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			frame.dispose();
			Window window = new Window();

		}
	}

	/**
	 * The StartListener method is called when the Start JButton is clicked.
	 * If the user has played 5 games, it freezes the game.
	 * If the user clicks on it again without placing his bet, it displays the user a message to place a bet.
	 * Otherwise, it calls on the Starter() method which displays the cards.
	 */
	public class StartListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (GameC < 5 && checker == true)
				Starter();
			else if (GameC <5 && checker == false)
			{
				Message2.setText("Finish your game first!");
			}
			else{
				checker = false;
				Message2.setText("Maximum 5 games at a Time");
				btnResult.setEnabled(false);
				btnStart.setEnabled(false);
			}
			}
	}

	/**
	 * The Starter method displays the money left with the user and the user's cards.
	 */
	public void Starter() {
			checker = false;
			GameC++;
			CardCC = 0;
			Message1.setText("The remaining money is $" + Money);
			Message2.setText("");
			Cards = Randomize();
			String cardName_1 = "/Images/card_" + Cards.get(0) + ".gif";
			ImageIcon Img_1 = new ImageIcon(Window.class.getResource(cardName_1));
			Card_1.setIcon(Img_1);
			String cardName_2 = "/Images/card_" + Cards.get(1) + ".gif";
			ImageIcon Img_2 = new ImageIcon(Window.class.getResource(cardName_2));
			Card_2.setIcon(Img_2);
			String cardName_3 = "/Images/card_" + Cards.get(2) + ".gif";
			ImageIcon Img_3 = new ImageIcon(Window.class.getResource(cardName_3));
			Card_3.setIcon(Img_3);
			Card_4.setIcon(new ImageIcon(Window.class.getResource("/Images/card_back.gif")));
			Card_5.setIcon(new ImageIcon(Window.class.getResource("/Images/card_back.gif")));
			Card_6.setIcon(new ImageIcon(Window.class.getResource("/Images/card_back.gif")));
			replace1.setEnabled(true);
			replace2.setEnabled(true);
			replace3.setEnabled(true);
		}

	/**
	 * The ResultListener is called when the Result JButton is clicked on.
	 * It checks who won the game and depending on that calls the Dealer_Wins or User_Wins methods
	 * It is also used to display the dealer's cards.
	 */
	public class ResultListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				replace1.setEnabled(false);
				replace2.setEnabled(false);
				replace3.setEnabled(false);
				checker = true;
				try {
					Bet = Integer.parseInt(betAmount.getText());
					if (Bet > Money)
						throw new NullPointerException();
					Money -= Bet;
					String cardName = "/Images/card_" + Cards.get(3) + ".gif";
					ImageIcon Img_1 = new ImageIcon(Window.class.getResource(cardName));
					Card_4.setIcon(Img_1);
					cardName = "/Images/card_" + Cards.get(4) + ".gif";
					ImageIcon Img_2 = new ImageIcon(Window.class.getResource(cardName));
					Card_5.setIcon(Img_2);
					cardName = "/Images/card_" + Cards.get(5) + ".gif";
					ImageIcon Img_3 = new ImageIcon(Window.class.getResource(cardName));
					Card_6.setIcon(Img_3);
					int sum_u = 0;
					int special_u = 0;
					int sum_d = 0;
					int special_d = 0;
					for (int i = 0; i < 3; i++) {
						int x = Cards.get(i);
						if (x >= 50)
							special_u++;
						else {
							if (x % 10 == 0)
								sum_u += 10;
							else
								sum_u += x % 10;
						}
					}
					sum_u = sum_u % 10;
					//System.out.println("U"+sum_u);
					for (int i = 3; i < 6; i++) {
						int x = Cards.get(i);
						if (x >= 50)
							special_d++;
						else {
							if (x % 10 == 0)
								sum_d += 10;
							else
								sum_d += x % 10;
						}
					}
					sum_d = sum_d % 10;
					//System.out.println("D"+sum_d);
					if (special_d > special_u) {
						Dealer_Wins();
					} else if (special_u > special_d) {
						User_Wins();
					} else if (sum_u > sum_d) {
						User_Wins();
					} else if (sum_d > sum_u) {
						Dealer_Wins();
					} else {
						Dealer_Wins();
					}

				} catch (Exception except) {
					System.out.println("LLUN");
					Message2.setText("Input Bet First!");
				}
			}
		}

	/**
	 * The Dealer_Wins method is called when the dealer wins
	 */
	public void Dealer_Wins() {
			Message2.setText("The Dealer Wins");
			Message1.setText("The remaining money is $" + Money);
		}
	/**
	 * The User_Wins method is called when the user wins, it adds the bet money into the user's account.
	 */
	public void User_Wins() {
			Message2.setText("YOU WIN!");
			Money += (2 * Bet);
			Message1.setText("The remaining money is $" + Money);
		}

	/**
	 * The Replace method is used to Replace the user's card and can only be called twice in a round.
	 */
	public class Replace implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				CardCC++;
				if (CardCC > 1) {
					replace1.setEnabled(false);
					replace2.setEnabled(false);
					replace3.setEnabled(false);
				}
				Random generator = new Random();
				int x = generator.nextInt(52) + 10;
				while (Cards.contains(x)) {
					x = generator.nextInt(52) + 10;
				}
				switch (e.getActionCommand()) {
					case "Replace Card 1": {
						Cards.set(0, x);
						String cardName_1 = "/Images/card_" + Cards.get(0) + ".gif";
						ImageIcon Img_1 = new ImageIcon(Window.class.getResource(cardName_1));
						Card_1.setIcon(Img_1);
						break;
					}
					case "Replace Card 2": {
						Cards.set(1, x);
						String cardName_1 = "/Images/card_" + Cards.get(1) + ".gif";
						ImageIcon Img_1 = new ImageIcon(Window.class.getResource(cardName_1));
						Card_2.setIcon(Img_1);
						break;
					}
					case "Replace Card 3": {
						Cards.set(2, x);
						String cardName_1 = "/Images/card_" + Cards.get(2) + ".gif";
						ImageIcon Img_1 = new ImageIcon(Window.class.getResource(cardName_1));
						Card_3.setIcon(Img_1);
						break;
					}
				}
			}
		}

	/**
	 * The Randomize method is used to generate a set of cards randomly from the deck of 52 cards for a round.
	 * @return an ArrayList containing the index of 6 cards for the round.
	 */
	public static ArrayList<Integer> Randomize() {
			Random generator = new Random();
			Cards.removeAll(Cards);
			while (Cards.size() < 6) {
				int x = generator.nextInt(52) + 10;
				if (Cards_Deck.contains(x) || Cards.contains(x)) {
					Randomize();
				} else {
					//System.out.println(x);
					Cards.add(x);
				}
			}
			Cards_Deck.addAll(Cards);
			return Cards;
		}

	/**
	 * The main method initializes a new Window() object.
	 * @param args null
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Window window = new Window();
	}
}



