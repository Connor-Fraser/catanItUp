package catan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * This class represents the instance of a player for the server to send and receive
 * messages from. This class is what will listen for messages from the players
 * and pass them on to the server for handling
 */


public class ServerPlayer extends Thread {

	private Socket socket;
	private int playerNum;
	private  String playerName;
	private boolean activePlayer;
	private boolean isReady;
	private BufferedReader in;
    private PrintWriter out;
	private String IO;
    
	ServerPlayer(Socket socket, int playerNum){
		this.socket = socket;
		this.playerNum = playerNum;
		this.activePlayer = false;
		this.isReady = false;
		IO = "";
		
		//set up communication with the player and store their name
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			playerName = in.readLine();
		} catch (IOException e) {
			CatanServer.log("problem setting up player socket");
		}
	}
	
	
	public void run() {
		
		//while the user has not left the game, listen for inputs and pass them on to the server
		while(!IO.startsWith("QUIT")){
			try {
				IO = in.readLine();	
				CatanServer.updateAllPlayers(IO);
				
					if(IO.startsWith("READY"))
						isReady = true;
			
			} catch (IOException e) {
				CatanServer.log("problem reading player " + (playerNum+1) + " input");
				IO = "QUIT " + playerNum;
				CatanServer.updateAllPlayers(IO);
				try {
					socket.close();
				} catch (IOException e1) {}
			}	
		}
	}

	//pass a message to the player
	public void updatePlayer(String message) {
		out.println(message);
	}
	
	//return the player's name
	public String getPlayerName(){
		return playerName;
	}
	
	//return player ready status
	public boolean isReady(){
		return isReady;
	}
	
	//set new player number after the game has been started
	public void setPlayerNum(int i){
		playerNum = i;
	}

}
