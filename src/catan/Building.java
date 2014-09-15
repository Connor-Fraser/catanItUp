package catan;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Building extends Polygon implements Drawable {

	/*
	 * This class represents either a city or village or location where one can be built
	 * In essence it is a vertice of the game's hexagons.
	 * 
	 * It's color and border change depending on its state
	 */
	
	private static final long serialVersionUID = 1L;
	private boolean village;
	private boolean city;
	private boolean isSelected;
	private Color color;
	private Color borderColor;
	private Point center;
	private GameBoardPanel parent;
	private static final int DIAMETER = 10;
	private static final int BORDER_SIZE = 3;
	//note: Hexes will contain the resource data a building get.
	
	Building(GameBoardPanel parent, Point p){
		super(new int[]{p.x-(DIAMETER/2), p.x-(DIAMETER/2), p.x+(DIAMETER/2), p.x+(DIAMETER/2)}, new int[]{p.y+(DIAMETER/2), p.y-(DIAMETER/2), p.y+(DIAMETER/2), p.y-(DIAMETER/2)}, 4);
		this.parent = parent;
		this.center = p;
		this.color = Color.WHITE;
		this.borderColor = Color.BLACK;
		city = false;
		village = false;
		isSelected = false;
	}
	
	
	//testing function
	public static void main(String[] args) {

	}

	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		int newX = center.x - (DIAMETER+(2*BORDER_SIZE))/ 2;
	    int newY = center.y - (DIAMETER+(2*BORDER_SIZE))/ 2;
		
		g2d.setColor(isSelected ? Color.RED : borderColor);
		g2d.fillOval(newX, newY, DIAMETER+(2*BORDER_SIZE), DIAMETER+(2*BORDER_SIZE));
		
		newX = center.x - DIAMETER/ 2;
	    newY = center.y - DIAMETER/ 2;
		
		g2d.setColor(color);
	    g2d.fillOval(newX, newY, DIAMETER, DIAMETER);    
	}
	
	
	public void unselected() {
		isSelected = false;
	}
}
