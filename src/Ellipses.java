

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;


public class Ellipses extends ShapePanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Ellipse2D.Float origin;
	
	public Ellipses(float x, float y, float w, float h) {
		super();
		
		origin = new Ellipse2D.Float(x, y, w, h);
		Rectangle r = origin.getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Get the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Get the position of this panel
	}
	
	@Override
	public int isResize(int x, int y) {
		
		if (new Rectangle(Math.round(origin.x+origin.width/2)-5, Math.round(origin.y)-5, 10, 10).contains(x, y))
			return 0;
		if (new Rectangle(Math.round(origin.x+origin.width)-5, Math.round(origin.y+origin.height/2)-5, 10, 10).contains(x, y))
			return 1;
		if (new Rectangle(Math.round(origin.x+origin.width/2)-5, Math.round(origin.y+origin.height)-5, 10, 10).contains(x, y))
			return 2;
		if (new Rectangle(Math.round(origin.x)-5, Math.round(origin.y+origin.height/2)-5, 10, 10).contains(x, y))
			return 3;
		if (origin.contains(x, y))
			return -1;
		
		return -2;
	}

	@Override
	public void moveTo(int deltaX, int deltaY) {
		
		origin.setFrame(origin.x + deltaX, origin.y + deltaY, origin.width, origin.height);
		this.setLocation(this.getX() + deltaX, this.getY() + deltaY);
	}

	@Override
	public void reSize(int j, int deltaX, int deltaY) {
		
		switch(j) {
		case 0:
			if (origin.height-deltaY > 10)
				origin.setFrame(origin.x, origin.y+deltaY, origin.width, origin.height-deltaY);
			else
				origin.setFrame(origin.x, origin.y+origin.height-10, origin.width, 10);
			break;
		case 1:
			if (origin.width+deltaX > 10)
				origin.setFrame(origin.x, origin.y, origin.width+deltaX, origin.height);
			else
				origin.setFrame(origin.x, origin.y, 10, origin.height);
			break;
		case 2:
			if (origin.height+deltaY > 10)
				origin.setFrame(origin.x, origin.y, origin.width, origin.height+deltaY);
			else
				origin.setFrame(origin.x, origin.y, origin.width, 10);
			break;
		case 3:
			if (origin.width-deltaX > 10)
				origin.setFrame(origin.x+deltaX, origin.y, origin.width-deltaX, origin.height);
			else
				origin.setFrame(origin.x+origin.width-10, origin.y, 10, origin.height);
			break;
		}
		
		Rectangle r = origin.getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Get the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Get the position of this panel
		
		if (scrollpane != null) {
			float d = (float) Math.sqrt(origin.width * origin.width + origin.height * origin.height);
	        float l = (origin.width + origin.height) / 2;
	        float dx = l * origin.width / d;
	        float dy = l * origin.height / d;
	        textpane.setSize(Math.round(dx), Math.round(dy));
	        scrollpane.setSize(Math.round(dx), Math.round(dy));
	        scrollpane.setLocation(Math.round(origin.x+(origin.width-dx)/2)-this.getX(), Math.round(origin.y+(origin.height-dy)/2)-this.getY());
		}
		
		repaint();
	}
	
	@Override
	public boolean addText() {
		super.addText();
		
		float d = (float) Math.sqrt(origin.width * origin.width + origin.height * origin.height);
        float l = (origin.width + origin.height) / 2;
        float dx = l * origin.width / d;
        float dy = l * origin.height / d;
        
        textpane.setSize(Math.round(dx), Math.round(dy));
        scrollpane.setSize(Math.round(dx), Math.round(dy));
        scrollpane.setLocation(Math.round(origin.x+(origin.width-dx)/2)-this.getX(), Math.round(origin.y+(origin.height-dy)/2)-this.getY());
        
        return true;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		
		if (drawcolor != null) {
			g2d.setColor(drawcolor);  // Draw the shape in this panel
			g2d.draw(new Ellipse2D.Float(origin.x-this.getX(), origin.y-this.getY(), origin.width, origin.height));
		}
		if (fillcolor != null) {
			g2d.setColor(fillcolor);
			Area Fill = new Area(new Ellipse2D.Float(origin.x-this.getX()+1, origin.y-this.getY()+1, origin.width-2, origin.height-2));
			if (scrollpane != null) {
				Fill.subtract(new Area(scrollpane.getBounds()));
				scrollpane.setBackground(fillcolor);
			}
			g2d.fill(Fill);
		}
		else {
			if (scrollpane != null) {
				scrollpane.setOpaque(false);
				scrollpane.repaint();
			}
		}
		
		if (seleted) {
			g2d.setStroke(new BasicStroke(1.0f));
			g2d.setColor(Color.BLACK);
			g2d.drawOval(Math.round(origin.x+origin.width/2)-this.getX()-3, Math.round(origin.y)-this.getY()-3, 6, 6);
			g2d.drawOval(Math.round(origin.x+origin.width)-this.getX()-3, Math.round(origin.y+origin.height/2)-this.getY()-3, 6, 6);
			g2d.drawOval(Math.round(origin.x+origin.width/2)-this.getX()-3, Math.round(origin.y+origin.height)-this.getY()-3, 6, 6);
			g2d.drawOval(Math.round(origin.x)-this.getX()-3, Math.round(origin.y+origin.height/2)-this.getY()-3, 6, 6);
			g2d.setColor(Color.GREEN);
			g2d.fillOval(Math.round(origin.x+origin.width/2)-this.getX()-2, Math.round(origin.y)-this.getY()-2, 5, 5);
			g2d.fillOval(Math.round(origin.x+origin.width)-this.getX()-2, Math.round(origin.y+origin.height/2)-this.getY()-2, 5, 5);
			g2d.fillOval(Math.round(origin.x+origin.width/2)-this.getX()-2, Math.round(origin.y+origin.height)-this.getY()-2, 5, 5);
			g2d.fillOval(Math.round(origin.x)-this.getX()-2, Math.round(origin.y+origin.height/2)-this.getY()-2, 5, 5);
		}
	}

	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return null;
	}
}
