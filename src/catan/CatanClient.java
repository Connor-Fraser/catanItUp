package catan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;


public class CatanClient {

	
	/**
	 * This class is the client application used to connect and facilitate games of 
	 * Settlers of Catan and communication between players on different computers. 
	 * The client is also responsible for the creation of the game interface the players will use
	 */
	
	//constants representing which player number gets which colour
	public static final Color PLAYER_0_COLOR = Color.RED;
	public static final Color PLAYER_1_COLOR = Color.CYAN;
	public static final Color PLAYER_2_COLOR = Color.GREEN;
	public static final Color PLAYER_3_COLOR = Color.YELLOW;
	
	private static int PORT = 8901;
	private String name;
	private String playerLabelText;
	private Socket connection;
	private BufferedReader in;
    private PrintWriter out;
    private JButton readyButton;
    private JScrollPane scroller;
    private JLabel  playerLabel;
    private JTextArea playerText;
    private JTextField playerMessage;
    private JFrame lobbyFrame;
    private JFrame gameFrame;
    private List<OpponentPanel> opponentPanels;
    private DevPanel devPanel;
    private InfoPanel infoPanel;
    private BuyPanel buyPanel;
    private GameBoardPanel gameBoardPanel;
    private Color playerColor;
    public static final int BOARD_WIDTH = 1100;
    public static final int BOARD_HEIGHT = 700;
	
	public static void main(String[] args) {
		
		CatanClient player = new CatanClient();
		player.initLobby();
		String input = "";
		
		while(!input.equals("GAME_START")){            
		
			try {
				input = player.in.readLine();
				player.handleLobbyInput(input);
			
			
			} catch (IOException e) {
				JOptionPane.showMessageDialog(new JFrame(), "There was a problem connecting to the server.\nEither it is unavailable or you inputted the incorrect IP Address");
				System.exit(1);
			}
		}
		
		//create game frame
		player.initGameGUI();
		
		//temporary code to see if messages are handled correctly still
		while(true){
			try {
				input = player.in.readLine();
				player.handleLobbyInput(input);
			} catch (IOException e) {}
			
		}
	}
	
	//handles incoming messages from the server
	private void handleLobbyInput(String input) {
		
		//print a message to the text field
		if(input.startsWith("MESSAGE"))
			updateText(input.substring(8, input.length()));
		
		//add new player's name to the player list
		else if(input.startsWith("PLAYER_NAME"))
			changePlayerLabel(input.substring(12, input.length()));
		
		//notify the player of another player becoming ready
		else if(input.startsWith("READY")){
			updateText(input.substring(6, input.length()) + " is now ready");
		}
		
		//reset the player list after someone has quit
		else if(input.startsWith("NEW_PLAYER_LIST")){
			playerLabelText = "<html>Players:<br><br>";
			playerLabel.setText(playerLabelText + "</html>");
		}
		
		//notify user of a player quitting
		else if(input.startsWith("QUIT"))
			updateText("Player " + input.substring(5, input.length()) + " has quit");
	}
	
	private void handleGameInitInput(String input){
		
		if(input.startsWith("PLAYER")){
			String playerName = input.substring(9, input.length());
			int i = Integer.parseInt(input.substring(7,8));
			
			generateOpponent(playerName, i);
		}
	}

	CatanClient() {
		
		//Obtain the Server IP Address and the player's name and submit player name	
		boolean flag = true;
		String serverAddress = "";
		String playerName = "";
				
		//get Server IP
		while(flag){
			serverAddress = JOptionPane.showInputDialog(new JFrame(), "Enter IP Address of the Server:", "Server IP", JOptionPane.PLAIN_MESSAGE);
			
			if(serverAddress == null)
				System.exit(0);
			else if(serverAddress.length() < 1)
				continue;
			else
				flag = false;
		}
		try {
			this.connection = new Socket(serverAddress, PORT); 
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			out = new PrintWriter(connection.getOutputStream(), true);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "There was a problem connecting to the server.\nEither it is unavailable or you inputted the incorrect IP Address");
				System.exit(1);
		} 
				
				
		//get Player Name
		flag = true;
		while(flag){
			playerName = JOptionPane.showInputDialog(new JFrame(),"Enter your Name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
			
			if(playerName == null)
				System.exit(0);
			else if(playerName.length() < 1)
				continue;
			else
				flag = false;
				}
		
		this.name = playerName;
		
		//send the player's name to the server
		out.println(name);
	}
	
	//generate the lobby's GUI
	private void initLobby(){
		
		//construct the pre-game lobby for the players
		lobbyFrame = new JFrame("Catan Lobby: " + name);
		lobbyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lobbyFrame.setSize(500,400);
		lobbyFrame.setResizable(false);
		
		//add the ready button
		readyButton = new JButton("Ready");
		readyButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				    readyButton.setEnabled(false);
				    out.println("READY " + name);
				  } 
				});
		lobbyFrame.add(readyButton, BorderLayout.PAGE_START);
		
		//add the existing player panel
		playerLabelText = "<html>Players:<br><br>";
		playerLabel = new JLabel(playerLabelText + "</html>");
		Dimension d = playerLabel.getPreferredSize();
		playerLabel.setPreferredSize(new Dimension(d.width+50,d.height));
		playerLabel.setVerticalAlignment(SwingConstants.TOP);
		playerLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		lobbyFrame.add(playerLabel, BorderLayout.LINE_START);
		
		//add the message Area
		playerText = new JTextArea();
		playerText.setLineWrap(true);
		playerText.setEditable(false);
		scroller = new JScrollPane(playerText);
		scroller.setBorder(BorderFactory.createLineBorder(Color.black));
		lobbyFrame.add(scroller, BorderLayout.CENTER);
		
		//add the player text input
		JPanel inputPanel = new JPanel();
		inputPanel.add(new JLabel ("Enter Text Here:"), BorderLayout.LINE_START);
		playerMessage = new JTextField("");
		playerMessage.setEditable(true);
		playerMessage.setFocusable(true);
		playerMessage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				//take the input from the message field and send it to the server to print to the other players
				String message = "MESSAGE " + name + ": " + playerMessage.getText() + "\n";
				playerMessage.setText("");
				out.println(message);
			}
		
		});
	            
		playerMessage.setBorder(BorderFactory.createLineBorder(Color.black));
		inputPanel.add(playerMessage, BorderLayout.CENTER);
		
		d = playerMessage.getPreferredSize();
		playerMessage.setPreferredSize(new Dimension(d.width+350,d.height));
		
		lobbyFrame.add(inputPanel, BorderLayout.PAGE_END);
		
		
		//display the lobby
		lobbyFrame.setLocationRelativeTo(null);
		lobbyFrame.setVisible(true);
	}
	
	private void initGameGUI(){
		//remove the lobby GUI
		lobbyFrame.setVisible(false);
		lobbyFrame.dispose();
		
		//create the new frame
		gameFrame = new JFrame("Catan it UP");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setSize(BOARD_WIDTH,BOARD_HEIGHT);
		gameFrame.setResizable(false);
		gameFrame.setLocationRelativeTo(null);
		
		//create the message area of the game frame using the lobby's pieces
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		//inputPanel.add(new JLabel ("Enter Text Here:"));
		messagePanel.add(playerMessage, BorderLayout.PAGE_END);
		messagePanel.add(scroller, BorderLayout.CENTER);
		//messagePanel.add(inputPanel, BorderLayout.PAGE_END);
		messagePanel.setPreferredSize(new Dimension(BOARD_WIDTH*1/5, BOARD_HEIGHT*2/5));
		gameFrame.add(messagePanel, BorderLayout.LINE_END);
		
		//generate panel that holds information for opponents
		JPanel opponentPanel = new JPanel();
		opponentPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		opponentPanels = Collections.synchronizedList(new ArrayList<OpponentPanel>());
		
		//call input handler to collect other player input and add to the opponentPanels List
		try {
			String input = in.readLine();
			while(!input.equals("FINAL_PLAYER_LIST")){
				input = in.readLine();
			}
			while(!input.equals("END_FINAL_PLAYER_LIST")){
				input = in.readLine();
				handleGameInitInput(input);
			}
		} catch (IOException e) {}
		
		//add the individual OpponentPanels to their container and add it to the gameFrame
		for(OpponentPanel panel: opponentPanels){
			if(panel != null)
				opponentPanel.add(panel);
		}
		gameFrame.add(opponentPanel, BorderLayout.PAGE_START);
		
		//create the player panel that will hold their information and actions for the game
		JPanel playerPanel = new JPanel();
		
		//create the Buying Panel that will handle the purchasing actions for the player
		buyPanel = new BuyPanel(this);
		
		//create the player info panel that holds relevant information for the player and allows them to roll
		infoPanel = new InfoPanel(this);
		
		//create the Dev Card Panel which will handle all the player's dev card info and actions
		devPanel = new DevPanel(this);
		
		playerPanel.add(buyPanel);
		playerPanel.add(infoPanel);
		playerPanel.add(devPanel);
		gameFrame.add(playerPanel, BorderLayout.PAGE_END);
		
		//game board initialization
		gameBoardPanel = new GameBoardPanel();
		gameFrame.add(gameBoardPanel, BorderLayout.CENTER);
		gameFrame.setVisible(true);
		gameBoardPanel.initialize();
		
	}
	
	//updates the label that holds player names by adding a new line
	private void changePlayerLabel(String newPlayer){
		playerLabelText += newPlayer + "<br>";
		playerLabel.setText(playerLabelText + "</html>");
	}
	
	//updates the message field for the players
	private void updateText(String newText){
		playerText.append(newText + "\n");
		JScrollBar vertical = scroller.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum() );
	}

	//creating the opponent pannels/assinging correct playerNum after game starts
	private void generateOpponent(String playerName, int playerNum){
		if(name.equals(playerName)){
			opponentPanels.add(null);
			
			//set the player's color
			if(playerNum == 0)
				playerColor = PLAYER_0_COLOR;
			
			if(playerNum == 1)
				playerColor = PLAYER_1_COLOR;
			
			if(playerNum == 2)
				playerColor = PLAYER_2_COLOR;
			
			if(playerNum == 3)
				playerColor = PLAYER_3_COLOR;
				
		}
		else{
			opponentPanels.add(new OpponentPanel(playerName, playerNum, this));
		}
	}
	
	public Color getColor(){
		return playerColor;
	}
	
	//trading (duh)
	public void tradeInitiate(int playerNum) {
		// TODO Auto-generated method stub
		
	}

	public void knightPlayed() {
		// TODO Auto-generated method stub
		
	}

	public void roadBuildPlayed() {
		// TODO Auto-generated method stub
		
	}
	
	public void yearPlentyPlayed() {
		// TODO Auto-generated method stub
	}
	
	public void monopolyPlayed() {
		// TODO Auto-generated method stub
	}

	public void convertResources() {
		// TODO Auto-generated method stub
		
	}

	public void roll() {
		// TODO Auto-generated method stub
		
	}

	public void buildRoad() {
		// TODO Auto-generated method stub
		
	}

	public void buildVillage() {
		// TODO Auto-generated method stub
		
	}

	public void buildCity() {
		// TODO Auto-generated method stub
		
	}

	public void buyCard() {
		// TODO Auto-generated method stub
		
	}

}