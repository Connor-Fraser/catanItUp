package catan;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

class OpponentPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private	int longestRoadNum;
	private	int largestArmyNum;
	private	int resourceNum;
	private	int devCardNum;
	private	int playerNum;
	
	private	String name;
	private	JLabel nameLabel, cardLabel, longestLabel;
	private	JButton tradeButton;
	private CatanClient parent;

	OpponentPanel(String name, int playerNum, CatanClient parent){
		super(new FlowLayout());
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));	
		
		this.parent = parent;
		this.name = name;
		this.playerNum = playerNum;
		resourceNum = 0;
		devCardNum = 0;
		longestRoadNum = 0;
		largestArmyNum = 0;	
		
		nameLabel = new JLabel(name);
		nameLabel.setOpaque(true);
		nameLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), new EmptyBorder(5, 5, 5, 5)));
		
		if(playerNum == 0)
			nameLabel.setBackground(CatanClient.PLAYER_0_COLOR);
		else if(playerNum == 1)
			nameLabel.setBackground(CatanClient.PLAYER_1_COLOR);
		else if(playerNum == 2)
			nameLabel.setBackground(CatanClient.PLAYER_2_COLOR);
		else if(playerNum == 3)
			nameLabel.setBackground(CatanClient.PLAYER_3_COLOR);
		
		add(nameLabel);

		cardLabel = new JLabel();		
		setCardLabelText();
		add(cardLabel);

		longestLabel = new JLabel();
		setLongestLabelText();
		add(longestLabel);

		tradeButton = new JButton("Trade");
		tradeButton.addActionListener(this);
		tradeButton.setEnabled(false);
		add(tradeButton);
	}
	
	public String getPlayerName(){
		return name;
	}
	
	public int getPlayerNum(){
		return playerNum;
	}

	public void actionPerformed(ActionEvent e){
		parent.tradeInitiate(playerNum);
	}

	public void playerIsActive(){
		tradeButton.setEnabled(true);
	}

	public void playerTurnOver(){
		tradeButton.setEnabled(false);
	}

	public void setCardLabelText(){
		cardLabel.setText("<html>Resources: " + resourceNum + "<br>" + "Dev Cards: " + devCardNum + "</html>");
	}

	public void setLongestLabelText(){
		longestLabel.setText("<html>Longest Road: " + longestRoadNum + "<br>" + "largest Army: " + largestArmyNum + "</html>");
	}

	public void updateLongestRoadNum(int n){
		longestRoadNum = n;
		setLongestLabelText();
	}

	public void updateLargestArmyNum(int n){
		largestArmyNum = n;
		setLongestLabelText();
	}

	public void updateResourceNum(int n){
		resourceNum = n;
		setCardLabelText();
	}

	public void updateDevCardNum(int n){
		devCardNum = n;
		setCardLabelText();
	}
}
