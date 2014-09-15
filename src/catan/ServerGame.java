package catan;

import java.util.List;

public class ServerGame implements Runnable{
/*
 * This class is an instance of a game running on the server. It will handle the game-specific
 * messages that players send in a given game.
 */
	
	
	private List<ServerPlayer> playerList;
	
	
	ServerGame(List<ServerPlayer> playerList){
		this.playerList = playerList;
		
		//accurately reset the playerNums
		for(int i = 0; i<playerList.size(); i++){
			playerList.get(i).setPlayerNum(i);
		}
	}


	public void run() {
		initiateGUI();
		
	}
	
	private void initiateGUI(){
		//notify player names and nums for OpponentPanels
		updateAllPlayers("FINAL_PLAYER_LIST");
		
		for(int i = 0; i < playerList.size(); i++){
			String message = "PLAYER " + i + " " + playerList.get(i).getPlayerName();
			updateAllPlayers(message);
		}
		updateAllPlayers("END_FINAL_PLAYER_LIST");
	
	}

	void updateAllPlayers(String message){
		//TODO: handle player quit's at some point
		
		
		//send incoming message to every player in the game
		for(ServerPlayer player: playerList)
			if(player != null)
				player.updatePlayer(message);
		
	}
}
