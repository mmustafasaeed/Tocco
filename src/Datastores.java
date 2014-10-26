
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class Datastores extends ShapePanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Polygon origin;
	
	public Datastores(int[] xpoints, int[] ypoints) {
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
		
		switch(j) {
		case 0:
			origin.xpoints[3] += deltaX;
			origin.ypoints[1] += deltaY;
			break;
		case 1:
			origin.ypoints[0] += deltaY;
			origin.xpoints[2] += deltaX;
			break;
		case 2:
			origin.xpoints[1] += deltaX;
			origin.ypoints[3] += deltaY;
			break;
		case 3:
			origin.ypoints[2] += deltaY;
			origin.xpoints[0] += deltaX;
			break;
		}
		
		Rectangle r = new Polygon(origin.xpoints, origin.ypoints, origin.npoints).getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Set the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Set the position of this panel
		
		if (scrollpane != null) {
	        textpane.setSize(r.width-1, r.height-1);
	        scrollpane.setSize(r.width-1, r.height-1);
	        scrollpane.setLocation(r.x-this.getX()+1, r.y-this.getY()+1);
		}
	}
	
	@Override
	public boolean addText() {
		super.addText();
		
		Rectangle r = new Polygon(origin.xpoints, origin.ypoints, origin.npoints).getBounds();
        textpane.setSize(r.width-1, r.height-1);
        scrollpane.setSize(r.width-1, r.height-1);
        scrollpane.setLocation(r.x-this.getX()+1, r.y-this.getY()+1);
        
        return true;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		int[] xpoints = new int[4];  // Get the shape in this panel
		int[] ypoints = new int[4];
		for (int i = 0; i < origin.npoints; i++) {
			xpoints[i] = origin.xpoints[i] - this.getX();
			ypoints[i] = origin.ypoints[i] - this.getY();
		}
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		
		g2d.setColor(drawcolor);  // Draw the shape in this panel
		g2d.drawPolyline(xpoints, ypoints, 4);
		if (fillcolor != null)
			if (scrollpane != null)
				scrollpane.setBackground(fillcolor);
		else {
			if (scrollpane != null) {
				scrollpane.setOpaque(false);
				scrollpane.repaint();
			}
		}
		
		if (seleted) {
			g2d.setStroke(new BasicStroke(1.0f));
			g2d.setColor(Color.BLACK);
			g2d.drawOval(xpoints[0]-3, ypoints[0]-3, 6, 6);
			g2d.drawOval(xpoints[1]-3, ypoints[1]-3, 6, 6);
			g2d.drawOval(xpoints[2]-3, ypoints[2]-3, 6, 6);
			g2d.drawOval(xpoints[3]-3, ypoints[3]-3, 6, 6);
			g2d.setColor(Color.GREEN);
			g2d.fillOval(xpoints[0]-2, ypoints[0]-2, 5, 5);
			g2d.fillOval(xpoints[1]-2, ypoints[1]-2, 5, 5);
			g2d.fillOval(xpoints[2]-2, ypoints[2]-2, 5, 5);
			g2d.fillOval(xpoints[3]-2, ypoints[3]-2, 5, 5);
		}
	}

	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return "";
	}
}
