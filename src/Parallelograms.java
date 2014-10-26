

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class Parallelograms extends ShapePanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Polygon origin;
	
	public Parallelograms (int[] xpoints, int[] ypoints) {
		super();
		
		origin = new Polygon(xpoints, ypoints, 4);
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
		if (new Rectangle(origin.xpoints[2]-5, origin.ypoints[2]-5, 10, 10).contains(x, y))
			return 2;
		if (new Rectangle(origin.xpoints[3]-5, origin.ypoints[3]-5, 10, 10).contains(x, y))
			return 3;
		
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
		
		if (j != 3) {
			origin.xpoints[j+1] += deltaX;
			origin.ypoints[j+1] += deltaY;
		}
		else {
			origin.xpoints[0] += deltaX;
			origin.ypoints[0] += deltaY;
		}
		
		Rectangle r = new Polygon(origin.xpoints, origin.ypoints, origin.npoints).getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Set the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Set the position of this panel
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
//		for (int i = 0; i < Draw.npoints; i++) {
//			int x = (int) Draw.getBounds().getCenterX();
//			int y = (int) Draw.getBounds().getCenterY();
//			if (Draw.ypoints[i] < y) {
//				Draw.ypoints[i] += 1;
//				if (Draw.xpoints[i] < x)
//					Draw.xpoints[i] += 1;
//				else if (Draw.xpoints[i] > x)
//					Draw.xpoints[i] -= 1;
//			}
//			else if (Draw.ypoints[i] > y) {
//				Draw.ypoints[i] -= 1;
//				if (Draw.xpoints[i] > x)
//					Draw.xpoints[i] -= 1;
//				else if (Draw.xpoints[i] < x)
//					Draw.xpoints[i] += 1;
//			}
//			else {
//				if (Draw.xpoints[i] < x)
//					Draw.xpoints[i] += 1;
//				else if (Draw.xpoints[i] > x)
//					Draw.xpoints[i] -= 1;
//			}
//		}
			g2d.fillPolygon(Draw.xpoints, Draw.ypoints, Draw.npoints);
		}
		
		if (seleted) {
			g2d.setStroke(new BasicStroke(1.0f));
			g2d.setColor(Color.BLACK);
			g2d.drawOval(Draw.xpoints[0]-3, Draw.ypoints[0]-3, 6, 6);
			g2d.drawOval(Draw.xpoints[1]-3, Draw.ypoints[1]-3, 6, 6);
			g2d.drawOval(Draw.xpoints[2]-3, Draw.ypoints[2]-3, 6, 6);
			g2d.drawOval(Draw.xpoints[3]-3, Draw.ypoints[3]-3, 6, 6);
			g2d.setColor(Color.GREEN);
			g2d.fillOval(Draw.xpoints[0]-2, Draw.ypoints[0]-2, 5, 5);
			g2d.fillOval(Draw.xpoints[1]-2, Draw.ypoints[1]-2, 5, 5);
			g2d.fillOval(Draw.xpoints[2]-2, Draw.ypoints[2]-2, 5, 5);
			g2d.fillOval(Draw.xpoints[3]-2, Draw.ypoints[3]-2, 5, 5);
		}
	}

	@Override
	public String getXML() {
StringBuffer buffer = new StringBuffer();
		
		buffer.append("\n<shape>");

		int count = origin.npoints;
			buffer.append("\n<name value=\"polygon\" />\n<points>");
		
		for(int i=0; i<count; i++) {
			buffer.append("\n<point x=\""+origin.xpoints[i]+"\" y=\""+origin.ypoints[i]+"\" />");
		}
		buffer.append("\n</points>" +
					"\n<constraints>" +
					"\n<static value=\"true\" />" +
					"\n</constraints>" +
					"\n</shape>");
		return buffer.toString();
	}
}
