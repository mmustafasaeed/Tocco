


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class Polylines extends ShapePanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Polygon origin;
	
	// Constructor
	public Polylines(int[] xpoints, int[] ypoints, int npoints) {
		super();
		
		origin = new Polygon(xpoints, ypoints, npoints);
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
		
		int[] xpoints = new int[origin.npoints];  // Get the shape in this panel
		int[] ypoints = new int[origin.npoints];
		for (int i = 0; i < origin.npoints; i++) {
			xpoints[i] = origin.xpoints[i] - this.getX();
			ypoints[i] = origin.ypoints[i] - this.getY();
		}
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		
		g2d.setColor(drawcolor);  // Draw the shape in this panel
		g2d.drawPolyline(xpoints, ypoints, xpoints.length);
		
		if (seleted) {
			g2d.setStroke(new BasicStroke(1.0f));
			for (int i = 0; i < origin.npoints; i++) {
				g2d.setColor(Color.BLACK);
				g2d.drawOval(xpoints[i]-3, ypoints[i]-3, 6, 6);
				g2d.setColor(Color.GREEN);
				g2d.fillOval(xpoints[i]-2, ypoints[i]-2, 5, 5);
			}
		}
	}

	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return " ";
	}
}
