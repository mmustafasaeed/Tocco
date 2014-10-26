
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

public class Arrows extends ShapePanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Polygon origin;

	public Arrows(int[] xpoints, int[] ypoints) {  // Constructor
		super();
		
		origin = new Polygon(xpoints, ypoints, 5);
		Rectangle r = origin.getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Set the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Set the position of this panel
	}
	
	@Override
	public int isResize(int x, int y) {
		
		if (new Rectangle(origin.xpoints[0]-5, origin.ypoints[0]-5, 10, 10).contains(x, y))
			return 0;
		if (new Rectangle(origin.xpoints[1]-5, origin.ypoints[1]-5, 10, 10).contains(x, y))
			return 1;
		if (Line2D.ptSegDist(origin.xpoints[0], origin.ypoints[0], origin.xpoints[1], origin.ypoints[1], x, y) < 5)
			return -1;
		
		return -2;
	}
	
	@Override
	public void moveTo(int deltaX, int deltaY) {
		
		origin.translate(deltaX, deltaY);
		this.setLocation(this.getX() + deltaX, this.getY() + deltaY);
	}
	
	@Override
	public void reSize(int j, int deltaX, int deltaY) {
		
		if (j == 0) {
			origin.xpoints[0] += deltaX;
			origin.ypoints[0] += deltaY;
		}
		
		if (j == 1) {
			origin.xpoints[1] += deltaX;
			origin.ypoints[1] += deltaY;
			origin.xpoints[3] += deltaX;
			origin.ypoints[3] += deltaY;
		}
		
		double angle;
		float x1 = origin.xpoints[0];
		float y1 = origin.ypoints[0];
		float x2 = origin.xpoints[1];
		float y2 = origin.ypoints[1];
		
		//double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) / 5;
		//if (d > 12)
		double	d = 12;
		
		if (x1 != x2)
			angle = Math.abs(Math.atan((y1 - y2) / (x1 - x2)));
		else
			angle = Math.PI * 0.5;
		
		float dx1 = (float) (d * Math.cos(angle - Math.PI / 6));
		float dy1 = (float) (d * Math.sin(angle - Math.PI / 6));
		float dx2 = (float) (d * Math.sin(Math.PI / 3 - angle));
		float dy2 = (float) (d * Math.cos(Math.PI / 3 - angle));
		
		if (x1 >= x2) {
			origin.xpoints[2] = Math.round(x2 + dx1);
			origin.xpoints[4] = Math.round(x2 + dx2);
			if (y1 < y2) {
				origin.ypoints[2] = Math.round(y2 - dy1);
				origin.ypoints[4] = Math.round(y2 - dy2);
			}
			else {
				origin.ypoints[2] = Math.round(y2 + dy1);
				origin.ypoints[4] = Math.round(y2 + dy2);
			}
		}
		
		else if (x1 < x2) {
			origin.xpoints[2] = Math.round(x2 - dx1);
			origin.xpoints[4] = Math.round(x2 - dx2);
			if (y1 < y2) {
				origin.ypoints[2] = Math.round(y2 - dy1);
				origin.ypoints[4] = Math.round(y2 - dy2);
			}
			else {
				origin.ypoints[2] = Math.round(y2 + dy1);
				origin.ypoints[4] = Math.round(y2 + dy2);
			}
		}
		
		Rectangle r = new Polygon(origin.xpoints, origin.ypoints, origin.npoints).getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Get the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Get the position of this panel
	}
	
	@Override
	public boolean addText() {
		
		return false;
	}
	
	@Override
	public void paint(Graphics g) {		
		super.paint(g);
		
		int[] xpoints = new int[5];  // Get the shape in this panel
		int[] ypoints = new int[5];
		for (int i = 0; i < 5; i++) {
			xpoints[i] = origin.xpoints[i] - this.getX();
			ypoints[i] = origin.ypoints[i] - this.getY();
		}
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		
		g2d.setColor(drawcolor);  // Draw the shape in this panel
		g2d.drawPolyline(xpoints, ypoints, 5);
		
		if (seleted) {
			g2d.setStroke(new BasicStroke(1.0f));
			g2d.setColor(Color.BLACK);
			g2d.drawOval(xpoints[0]-3, ypoints[0]-3, 6, 6);
			g2d.drawOval(xpoints[1]-3, ypoints[1]-3, 6, 6);
			g2d.setColor(Color.GREEN);
			g2d.fillOval(xpoints[0]-2, ypoints[0]-2, 5, 5);
			g2d.fillOval(xpoints[1]-2, ypoints[1]-2, 5, 5);
		}
	}

	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return null;
	}
}