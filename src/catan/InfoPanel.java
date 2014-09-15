package catan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class InfoPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private CatanClient parent;
	
	private int points;
	private JLabel pointsLabel;
	
	private JButton resourceConvert;
	
	private JButton rollButton;
	private JLabel rollLabel1;
	private JLabel rollLabel2;
	
	private JLabel armyLabel;
	private JLabel roadLabel;
	private int armySize;
	private int roadSize;
	
	
	/*
	 * resources go by the convention of:
	 * 
	 * [0] Wood			(205,133,63)
	 * [1] Brick		(220,20,60)
	 * [2] Stone		(119,136,153)
	 * [3] Wheat		(255,215,0)
	 * [4] Sheep		(50,205,50)
	 * 
	 * NOTE: look into possibly doing this via ENUM
	 */
	
	public static final Color WOOD_COLOR = new Color(205,133,63);
	public static final Color BRICK_COLOR = new Color(220,20,60);
	public static final Color STONE_COLOR = new Color(119,136,153);
	public static final Color WHEAT_COLOR = new Color(255,215,0);
	public static final Color SHEEP_COLOR = new Color(50,205,50);
	
	private int[] resourceNums;
	private JLabel[] resourceLabels;
	
	InfoPanel(CatanClient parent){
		super();
		this.parent = parent;
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		setLayout(new BorderLayout());
		
		//initializing and adding the parts of the panel
		points = 0;
		armySize = 0;
		roadSize = 0;
		resourceNums = new int[]{0,0,0,0,0};
		
		//top part of the player Panel
		JPanel topPanel = new JPanel();
		pointsLabel = new JLabel("Points: " + points);
		pointsLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), new EmptyBorder(5, 5, 5, 5))); 
		pointsLabel.setBackground(parent.getColor());
		pointsLabel.setOpaque(true);
		rollButton = new JButton("Roll:");
		rollButton.setEnabled(false);
		rollButton.addActionListener(this);
		rollLabel1 = new JLabel("      ");
		rollLabel2 = new JLabel("      ");
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		rollLabel1.setBorder(border);
		rollLabel2.setBorder(border);
		rollLabel1.setOpaque(true);
		rollLabel2.setOpaque(true);
		rollLabel1.setBackground(Color.WHITE);
		rollLabel2.setBackground(Color.WHITE);
		topPanel.add(pointsLabel);
		topPanel.add(new JLabel("       "));
		topPanel.add(rollButton);
		topPanel.add(rollLabel1);
		topPanel.add(rollLabel2);
		add(topPanel, BorderLayout.PAGE_START);
		
		//middle part of the panel
		JPanel middlePanel = new JPanel();
		resourceConvert = new JButton("<html>Trade In<br>Resources</html>");
		resourceConvert.setEnabled(false);
		resourceConvert.addActionListener(this);
		resourceLabels = new JLabel[5];
		resourceLabels[0] = new JLabel("<html>Wood " + "<br>" + resourceNums[0] + "</html>", JLabel.CENTER);
		resourceLabels[1] = new JLabel("<html>Brick " + "<br>" + resourceNums[1] + "</html>", JLabel.CENTER);
		resourceLabels[2] = new JLabel("<html>Stone " + "<br>" + resourceNums[2] + "</html>", JLabel.CENTER);
		resourceLabels[3] = new JLabel("<html>Wheat " + "<br>" + resourceNums[3] + "</html>", JLabel.CENTER);
		resourceLabels[4] = new JLabel("<html>Sheep " + "<br>" + resourceNums[4] + "</html>", JLabel.CENTER);
		resourceLabels[0].setBackground(WOOD_COLOR);
		resourceLabels[1].setBackground(BRICK_COLOR);
		resourceLabels[2].setBackground(STONE_COLOR);
		resourceLabels[3].setBackground(SHEEP_COLOR);
		resourceLabels[4].setBackground(WHEAT_COLOR);
		middlePanel.add(resourceConvert);
		for (int i = 0; i<5; i++){
			resourceLabels[i].setOpaque(true);
			resourceLabels[i].setVerticalAlignment(SwingConstants.CENTER);
			Dimension d = resourceLabels[i].getPreferredSize();
			resourceLabels[i].setPreferredSize(new Dimension(d.width+10,d.height+25));
			middlePanel.add(resourceLabels[i]);
		}
		add(middlePanel, BorderLayout.CENTER);
		
		//bottom part of the panel
		JPanel bottomPanel = new JPanel();
		armyLabel = new JLabel("Largest Army: " + armySize);
		roadLabel = new JLabel("Longest Road: " + roadSize);
		bottomPanel.add(armyLabel);
		bottomPanel.add(new JLabel("          "));
		bottomPanel.add(roadLabel);
		add(bottomPanel, BorderLayout.SOUTH);
		
	}

	public void setPoints(boolean increment){
		if(increment)
			points++;
		else
			points--;
		
		pointsLabel.setText("Points: " + points);		
	}
	
	public void setRoll(int roll1, int roll2){
		rollLabel1.setText(""+roll1);
		rollLabel2.setText(""+roll2);
	}
	
	public void addToArmy(){
		armySize++;
		armyLabel.setText("Largest Army: " + armySize);
	}
	
	public void addToRoad(){
		roadSize++;
		armyLabel.setText("Longest Road: " + roadSize);
	}
	
	public void updateResource(int resourceIndex, boolean increment, int amount){
		
		int delta = (increment ? amount : -amount);
		String type;
		if(resourceIndex == 0)
			type = "Wood ";
		else if(resourceIndex == 1)
			type = "Brick ";
		else if(resourceIndex == 2)
			type = "Stone ";
		else if(resourceIndex == 3)
			type = "Wheat ";
		else
			type = "Sheep ";
		
		resourceNums[resourceIndex] += delta;
		resourceLabels[resourceIndex].setText("<html>" + type + "<br>" +resourceNums[resourceIndex]+"</html>");
		
	}
	
	public void playerIsActive(){
		resourceConvert.setEnabled(true);
		rollButton.setEnabled(true);
	}

	public void playerTurnOver(){
		resourceConvert.setEnabled(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == resourceConvert)
			parent.convertResources();
		
		if(e.getSource() == rollButton){
			rollButton.setEnabled(false);
			parent.roll();
		}
	}
}
