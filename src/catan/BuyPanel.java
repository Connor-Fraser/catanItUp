package catan;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

public class BuyPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton roadButton;
	private JButton cardButton;
	private JButton villageButton;
	private JButton cityButton;
	private CatanClient parent;
	
	private int roadNum;
	private int villageNum;
	private int cityNum;
	private int cardNum;
	
	private JLabel roadNumLabel;
	private JLabel villageNumLabel;
	private JLabel cityNumLabel;
	private JLabel cardNumLabel;
	
	//the dev card number is held locally but the different types of cards
	//and their numbers are at the server level
	
	BuyPanel(CatanClient parent){
		super();
		this.parent = parent;
		setLayout(new GridLayout(4,3));
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		roadNum = 15;
		villageNum = 5;
		cityNum = 4;
		cardNum = 25;
		
		roadNumLabel = new JLabel(": " + roadNum);
		villageNumLabel = new JLabel(": " + villageNum);
		cityNumLabel = new JLabel(": " + cityNum);
		cardNumLabel = new JLabel(": " + cardNum);
		
		roadButton = new JButton("Road");
		roadButton.setEnabled(false);
		roadButton.addActionListener(this);
		
		cardButton = new JButton("Dev Card");
		cardButton.setEnabled(false);
		cardButton.addActionListener(this);
		
		villageButton = new JButton("Village");
		villageButton.setEnabled(false);
		villageButton.addActionListener(this);
		
		cityButton = new JButton("City");
		cityButton.setEnabled(false);
		cityButton.addActionListener(this);
		
		//there are many empty JLabels used to just make space between the buttons
		add(new JLabel());
		add(new JLabel("Buy", SwingConstants.CENTER));
		add(new JLabel());
		
		JPanel panel = new JPanel();
		panel.add(roadButton);
		panel.add(roadNumLabel);
		add(panel);
		add(new JLabel());
		
		panel = new JPanel();
		panel.add(cardButton);
		panel.add(cardNumLabel);
		add(panel);
		
		panel = new JPanel();
		panel.add(villageButton);
		panel.add(villageNumLabel);
		add(panel);
		add(new JLabel());
		
		panel = new JPanel();
		panel.add(cityButton);
		panel.add(cityNumLabel);
		add(panel);
	}
	
	public void cardBought(){
		cardNum--;
		cardNumLabel.setText(": " + cardNum);
	}
	
	public void roadBought(){
		roadNum--;
		roadNumLabel.setText(": " + roadNum);
	}
	
	public void updateVillage(boolean increment){
		if(increment)
			villageNum++;
		else
			villageNum--;
		
		villageNumLabel.setText(": " + villageNum);
	}
	
	public void updateCity(boolean increment){
		if(increment)
			cityNum++;
		else
			cityNum--;
		
		cityNumLabel.setText(": " + cityNum);
	}
	
	public void playerIsActive(){
		roadButton.setEnabled(true);
		cardButton.setEnabled(true);
		villageButton.setEnabled(true);
		cityButton.setEnabled(true);
	}
	
	public void playerTurnOver(){
		roadButton.setEnabled(false);
		cardButton.setEnabled(false);
		villageButton.setEnabled(false);
		cityButton.setEnabled(false);		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == roadButton){
			parent.buildRoad();
		}
		
		else if(e.getSource() == villageButton){
			parent.buildVillage();
		}
		
		else if(e.getSource() == cityButton){
			parent.buildCity();
		}
		
		else if(e.getSource() == cardButton){
			parent.buyCard();
		}	
	}
}
