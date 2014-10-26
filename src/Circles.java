
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Circles extends ShapePanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Ellipse2D.Float origin;
	private boolean state;

	public Circles(float x, float y, float d, boolean state) {
		super();
		this.state=state;
		origin = new Ellipse2D.Float(x, y, d, d);
		System.out.println("value of D is:" +d);
		System.out.println("value of Height is:"+ origin.height +" "+ origin.width);
		Rectangle r = origin.getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Set the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Set the position of this panel
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
	
	public boolean getState()
	{
		return state;
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
			if (origin.width-deltaY > 10)
				origin.setFrame(origin.x+deltaY/2.0, origin.y+deltaY, origin.width-deltaY, origin.height-deltaY);
			else
				origin.setFrame(origin.x+origin.width/2-5, origin.y+origin.height-10, 10, 10);
			break;
		case 1:
			if (origin.width+deltaX > 10)
				origin.setFrame(origin.x, origin.y-deltaX/2.0, origin.width+deltaX, origin.height+deltaX);
			else
				origin.setFrame(origin.x, origin.y+origin.height/2-5, 10, 10);
			break;
		case 2:
			if (origin.width+deltaY > 10)
				origin.setFrame(origin.x-deltaY/2.0, origin.y, origin.width+deltaY, origin.height+deltaY);
			else
				origin.setFrame(origin.x+origin.width/2-5, origin.y, 10, 10);
			break;
		case 3:
			if (origin.width-deltaX > 10)
				origin.setFrame(origin.x+deltaX, origin.y+deltaX/2.0, origin.width-deltaX, origin.height-deltaX);
			else
				origin.setFrame(origin.x+origin.width-10, origin.y+origin.height/2-5, 10, 10);
			break;
		}
		
		Rectangle r = origin.getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Get the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Get the position of this panel
		
		if (scrollpane != null) {
	        float d = (float) (origin.width * Math.sin(Math.PI * 0.25));
	        float delta = (origin.width - d) / 2 + 1;
	        int dd = Math.round(d) - 1;
	        textpane.setSize(dd, dd);
	        scrollpane.setSize(dd, dd);
	        scrollpane.setLocation(Math.round(origin.x+delta)-this.getX(), Math.round(origin.y+delta)-this.getY());
		}
	}
	
	@Override
	public boolean addText() {
		super.addText();
        
        float d = (float) (origin.width * Math.sin(Math.PI * 0.25));
        float delta = (origin.width - d) / 2 + 1;
        int dd = Math.round(d) - 1;
        
        textpane.setSize(dd, dd);
        scrollpane.setSize(dd, dd);
        scrollpane.setLocation(Math.round(origin.x+delta)-this.getX(), Math.round(origin.y+delta)-this.getY());
        
        return true;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		
		if (drawcolor != null) {  // Draw the shape in this panel
			g2d.setColor(drawcolor);
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
		int radius= (int) ((origin.getMaxX()- origin.getMinX())+(origin.getMaxY()- origin.getMinY())/2);
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("\n<shape>");

		buffer.append("\n<name value=\"circle\"/>" +"\n<points>");
		buffer.append("\n<point x=\""+origin.x+"\" y=\""+origin.y+"\" radius=\""+radius+"\" />");
		buffer.append("\n</points>" +
					"\n<constraints>" +
					"\n<static value=\""+state+"\" />" +
					"\n</constraints>" +
					"\n</shape>");
		return buffer.toString();
	}
}
