package catan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

/*
 * This class represents the game board area of play
 */

public class GameBoardPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private static final int ROAD_LENGTH = 40;
	private Drawable selectedPiece;
	private Building [] buildings;
	
	GameBoardPanel(){
		super();
		this.selectedPiece = null;
		buildings = new Building[54];
	}
	
	public void initialize(){
		int width = getWidth();
		int height = getHeight();
		Point middle = new Point(width/2, height/2);
		
	}
	
	public void paintComponent(Graphics g) {
	    int width = getWidth();
	    int height = getHeight();
	    g.setColor(Color.BLACK);
	    g.drawRect(5,5, width-10, height-10);
	    
	  }
	
}
