package catan;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CatanServer extends JFrame implements ActionListener{



	/**
	 * This class is the server application used to connect and facilitate games of 
	 * Settlers of Catan and communication between players on different computers. 
	 */
	
	static private List<ServerPlayer> playerList;
	static private ServerSocket listener;	
	static private JTextArea logArea;
	static private int playerCount;
	private boolean flag1;
	private boolean flag2;
	private static final long serialVersionUID = 1L;
	private static final int PORT = 8901;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		
		//create the server
		CatanServer cServer = new CatanServer();
		cServer.initServerGUI();
		
		//accept players until all players (a minimum of two) send the ready signal
		//then start the game. Messages are passed between users in the mean time
		
		//create an instance of the monitor thread, to check for player readiness and player number
		Thread t = cServer.spawnReadyMonitor();
		t.start();
		
		//connect players as required, or simply wait until all players are ready by waiting for flag sets from the monitor thread
		while(cServer.flag2){
			Thread.sleep(1000);
			
			while(cServer.flag1){
				
				Thread.sleep(1000);
				cServer.connectPlayers();
			}
		}
		
		//create a new player list with all the nulls removed, and a randomized order for playing in
		List<ServerPlayer> newPlayerList = Collections.synchronizedList(new ArrayList<ServerPlayer>());
		for(ServerPlayer player: playerList)
			if(player != null){
				newPlayerList.add(player);
			}
		Collections.shuffle(newPlayerList);
		playerList = newPlayerList;
		
		
		//notify players of game start 
		updateAllPlayers("MESSAGE All players ready. The game will now begin.");
		Thread.sleep(1000);
		updateAllPlayers("MESSAGE 3...");
		Thread.sleep(1000);
		updateAllPlayers("MESSAGE 2..");
		Thread.sleep(1000);
		updateAllPlayers("MESSAGE 1.");
		Thread.sleep(1000);
		updateAllPlayers("MESSAGE CATAN!");
		Thread.sleep(1000);
		
		log("The game has now begun");
		Thread game = new Thread(new ServerGame(playerList));
		updateAllPlayers("GAME_START");
		game.start();
	}

	CatanServer(){
		super("Catan Server");
		//set the connection flags to true so the server may accept players
		flag1 = true;
		flag2 = true;
		
		
		//start count of players and initialize player array
		playerCount = 0;
		playerList = Collections.synchronizedList(new ArrayList<ServerPlayer>());
		
		//create ServerSocket
		try {
			listener = new ServerSocket(PORT);
		} catch (IOException e) {
			CatanServer.log("Problem in creating Server Socket");
			e.printStackTrace();
		}
	}
	
	private void initServerGUI(){
		//create server pane
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(300,300);
		setLocationRelativeTo(null);
		setResizable(false);
		
		//add close button and log field
		JButton button = new JButton("Close Server");
		button.addActionListener(this);
		logArea = new JTextArea();
		logArea.setLineWrap(false);
		logArea.setEditable(false);
		JScrollPane scroller = new JScrollPane(logArea);
		
		
		add(button, BorderLayout.PAGE_END);
		add(scroller, BorderLayout.CENTER);	

		setVisible(true);
		log("Server has been constructed");
	}

	//close the server upon close button press
	public void actionPerformed(ActionEvent arg0) {
		try {
			listener.close();
		} catch (IOException e) {}
		System.exit(0);
		
		//TODO: free other resources as needed
	}
	
	//function called from the main thread to connect players to the server
	private void connectPlayers(){
		try {
			Socket socket = listener.accept();
			
			//server accepts a new client, and if the the number of players is less then four (flag1)
			//update all players of the new player joining and add them to the game
			
			if(flag1){
				
				ServerPlayer player = new ServerPlayer(socket, playerCount);
				playerList.add(player);
				playerCount++;
				player.start();
				updatePlayerList();
			}
			
			//otherwise just close the new socket which is likely just a fake player
			//from the monitor thread to exit the blocking call
			else
				socket.close();
	
		} catch (IOException e) {
			CatanServer.log("Error connecting player " + playerCount);
		}
		
		
		
		
	}
	
	//add server log text to the server GUI
	static void log(String logText){
		logArea.append(logText + "\n");
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}
	
	//take the input from one player and send it to all players
	//as well as checking for a quit message removing the quit player
    //and updating all players of the new player list
	
	static void updateAllPlayers(String message){
		
		//check for a quit and update players and the player list if so
		if(message.contains("QUIT")){
			int playerNum = Integer.parseInt(message.substring(5, message.length())); 
			String deadPlayerName = playerList.get(playerNum).getPlayerName();
			playerList.set(playerNum, null);
			playerCount--;
			log("Player " + (playerNum+1) + "has been removed");
			message = "QUIT " + deadPlayerName;
			updatePlayerList();
		}
		
		//send incoming message to every player in the game
		for(ServerPlayer player: playerList)
			if(player != null)
				player.updatePlayer(message);
		
	}

	//refresh everyone's player list
	private static void updatePlayerList() {
		updateAllPlayers("NEW_PLAYER_LIST");
		for(ServerPlayer player: playerList)
			if(player != null){
				String name = player.getPlayerName();
				updateAllPlayers("PLAYER_NAME " + name);
			}
	}

	
	//create an instance of a ready Monitor Thread for the main function
	public Thread spawnReadyMonitor(){
		return new Thread(new ReadyMonitor());
	}
	
	private class ReadyMonitor implements Runnable{

		public void run() {
			
			//run the thread until the game starts
			boolean monitorFlag = true;
			
			while(monitorFlag){
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			
				//if the player count has just reached 4, spawn a fake player to exit
				//the server's blocking call for it
				if(flag1 && playerCount >= 4){
					flag1 = false;
					
					try {
						Socket acceptBreak = new Socket("localhost", PORT);
						acceptBreak.close();
					} catch (IOException e) {
						log("Problem creating acceptBreak");
					}
				}
				
				//if the player count is less than four, continue accepting players as normal
				else if(playerCount < 4) {
					flag1 = true;
				}
				
				//if its been greater than 4 for a while, just continue to not accept players
				else if (playerCount >= 4){
					flag1 = false;
				}
					
				//check how many of the current players are ready
				int readyCount = 0;
				for(ServerPlayer player: playerList){
					if(player != null){
						if(player.isReady())
							readyCount++;
					}
				}
				
				//if all players are ready and there are at least two players, turn off the flags to
				//stop accepting palyers and start the game
				if(readyCount == playerCount && readyCount >= 2){
					flag2 = false;
					flag1 = false;
					
					//break any current server.accept() calls and move on to start the game
					try {
						Socket acceptBreak = new Socket("localhost", PORT);
						acceptBreak.close();
					} catch (IOException e) {
						log("Problem creating acceptBreak");
					}
					
					//end the thread
					monitorFlag = false;
				}
				
			}		
		}
	}	
}
