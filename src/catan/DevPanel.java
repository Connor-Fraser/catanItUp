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

public class DevPanel extends JPanel implements ActionListener{

	/*note: order of dev card appearance on this panel is
		[0] Knight
		[1] Road Building
		[2] Year of Plenty
		[3] Monopoly
		[4] Victory
	
	*
	*	NOTE: look into possibly making these represented by an Enum
	*/
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	//containing arrays for interactive/changing parts of the panel
	private int [] cardNums; 
	private JLabel [] cardNumLabels;
	private JButton [] playCardButtons;
	private CatanClient parent;
	
	DevPanel(CatanClient parent){
		super();
		this.parent = parent;
		setLayout(new GridLayout(5,3));
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		//initialize container arrays, NOTE: there is only four buttons because you cannot play victory cards
		cardNums = new int[]{0,0,0,0,0};
		cardNumLabels = new JLabel[5];
		playCardButtons = new JButton[4];
		
		//initialize the panel's buttons
		for(int i=0; i<4; i++){
			playCardButtons[i] = new JButton("Play");
			playCardButtons[i].setEnabled(false);
			playCardButtons[i].addActionListener(this);
		}
		
		//initialize the panel's labels
		for(int i=0; i<5; i++){
			cardNumLabels[i] = new JLabel("Num: 0");
			cardNumLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
		}
		//create the labels that display the different types of dev cards (not used after initialized)
		JLabel [] nameLabels = new JLabel[5];
		nameLabels[0] = new JLabel("Knight");
		nameLabels[1] = new JLabel("Road Building");
		nameLabels[2] = new JLabel("Year of Plenty");
		nameLabels[3] = new JLabel("Monopoly");
		nameLabels[4] = new JLabel("Victory");
	
		//add everything to the panel
		for(int i=0; i<5; i++){
			add(nameLabels[i]);
			add(cardNumLabels[i]);
			
			if(i<4)
				add(playCardButtons[i]);
		}
		
		
	}
	
	//change card count values after they have been bought, or played NOTE: the boolean is used to differentiate 
	//between incrementing or de-incrementing the card count
	public void incrementNum(int cardInt, boolean increment){
			if(increment){
				cardNums[cardInt]++;
				playerIsActive();
				cardNumLabels[cardInt].setText("Num: "+ cardNums[cardInt]);
			}
			else{
				cardNums[cardInt]--;
				playerIsActive();
				cardNumLabels[cardInt].setText("Num: "+ cardNums[cardInt]);
			}
	}
	
	//called when it is players turn to enable the ability to play any cards they might have
	//this function is also called whenever a card is played or bought to account for any changes of what cards are playable
	public void playerIsActive(){
		for(int i=0; i<5; i++){
			if(cardNums[i] > 0)
				playCardButtons[i].setEnabled(true);
			else
				playCardButtons[i].setEnabled(false);
		}
	}

	//called when the player's turn ends to disable the ability to play cards when it is not allowed
	public void playerTurnOver(){
		for(JButton button: playCardButtons)
			button.setEnabled(false);
	}
	
	//determine which card is being played and call the appropriate CatanClient function
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == playCardButtons[0])
			parent.knightPlayed();
			
		if(e.getSource() == playCardButtons[1])
			parent.roadBuildPlayed();
			
		if(e.getSource() == playCardButtons[2])
			parent.yearPlentyPlayed();
			
		if(e.getSource() == playCardButtons[3])
			parent.monopolyPlayed();
	}

}
