

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.IOException;

public class Polygons extends ShapePanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Polygon origin;
	boolean state;
	
	
	public Polygons(int[] xpoints, int[] ypoints, int npoints, boolean b) {
		super();
		
		origin = new Polygon(xpoints, ypoints, npoints);
		state=b;
		
		for(int i=0; i<xpoints.length; i++)
		System.out.println(xpoints[i] +" : "+ ypoints[i]);
		
		Rectangle r = origin.getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Set the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Set the position of this panel
	}
	
	@Override
	public int isResize(int x, int y) {
		for (int i = 0; i < origin.npoints; i++) {
			if (new Rectangle(origin.xpoints[i]-5, origin.ypoints[i]-5, 10, 10).contains(x, y))
				return i;
		}
		
		return -1;
	}
	
	public Polygon getPolygon()
	{
		return origin;
	}
	
	@Override
	public void moveTo(int deltaX, int deltaY) {
		
		origin.translate(deltaX, deltaY);
		this.setLocation(this.getX() + deltaX, this.getY() + deltaY);
	}

	@Override
	public void reSize(int j, int deltaX, int deltaY) {
		
		origin.xpoints[j] += deltaX;
		origin.ypoints[j] += deltaY;
		
		Rectangle r = new Polygon(origin.xpoints, origin.ypoints, origin.npoints).getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Get the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Get the position of this panel
		
		repaint();
	}
	
	@Override
	public boolean addText() {

		return false;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Polygon Draw = new Polygon();  // Get the shape in this panel
		for (int i = 0; i < origin.npoints; i++) {
			Draw.addPoint(origin.xpoints[i] - this.getX(), origin.ypoints[i] - this.getY());
		}
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		
		if (drawcolor != null) {
			g2d.setStroke(new BasicStroke(1.5f));
			g2d.setColor(drawcolor);  // Draw the shape in this panel
			g2d.draw(Draw);
		}
		
		if (fillcolor != null) {
			g2d.setColor(fillcolor);
			g2d.fillPolygon(Draw.xpoints, Draw.ypoints, Draw.npoints);
		}
		
		if (seleted) {
			g2d.setStroke(new BasicStroke(1.0f));
			for (int i = 0; i < origin.npoints; i++) {
				g2d.setColor(Color.BLACK);
				g2d.drawOval(Draw.xpoints[i]-3, Draw.ypoints[i]-3, 6, 6);
				g2d.setColor(Color.GREEN);
				g2d.fillOval(Draw.xpoints[i]-2, Draw.ypoints[i]-2, 5, 5);
			}
		}
	}

	public boolean getState() {
		
		return state;
	}
	
	public String getXML() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("\n<shape>");

		int count = origin.npoints;
		if(count<=4 ) {
			buffer.append("\n<name value=\"triangle\" />\n<points>");
			count = 3;
		} else {
			buffer.append("\n<name value=\"polygon\" />\n<points>");
		}
		
		for(int i=0; i<count; i++) {
			buffer.append("\n<point x=\""+origin.xpoints[i]+"\" y=\""+origin.ypoints[i]+"\" />");
		}
		buffer.append("\n</points>" +
					"\n<constraints>" +
					"\n<static value=\""+state+"\" />" +
					"\n</constraints>" +
					"\n</shape>");
		return buffer.toString();
	}
}
