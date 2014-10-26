
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

public class Lines extends ShapePanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Line2D.Float origin;
	
	boolean state;
	
	
	public boolean getState()
	{
		return state;
	}
	
	public Lines(float x1, float y1, float x2, float y2, boolean state1) {
		super();
		state=state1;
		origin = new Line2D.Float(x1, y1, x2, y2);
		Rectangle r = origin.getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Set the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Set the position of this panel
	}
	
	@Override
	public int isResize(int x, int y) {
		
		if (new Rectangle(Math.round(origin.x1)-5, Math.round(origin.y1)-5, 10, 10).contains(x, y))
			return 0;
		if (new Rectangle(Math.round(origin.x2)-5, Math.round(origin.y2)-5, 10, 10).contains(x, y))
			return 1;
		if (Line2D.ptSegDist(origin.x1, origin.y1, origin.x2, origin.y2, x, y) < 5)
			return -1;
		
		return -2;
	}
	
	@Override
	public void moveTo(int deltaX, int deltaY) {
		
		origin.setLine(origin.x1+deltaX, origin.y1+deltaY, origin.x2+deltaX, origin.y2+deltaY);
		this.setLocation(this.getX() + deltaX, this.getY() + deltaY);
	}
	
	@Override
	public void reSize(int j, int deltaX, int deltaY) {
		
		switch(j) {
		case 0:
			origin.x1 += deltaX;
			origin.y1 += deltaY;
			break;
		case 1:
			origin.x2 += deltaX;
			origin.y2 += deltaY;
			break;
		}
		
		Rectangle r = origin.getBounds();
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
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		
		g2d.setColor(drawcolor);  // Draw the shape in this panel
		g2d.draw(new Line2D.Float(origin.x1-this.getX(), origin.y1-this.getY(), origin.x2-this.getX(), origin.y2-this.getY()));
		
		if (seleted) {
			g2d.setStroke(new BasicStroke(1.0f));
			g2d.setColor(Color.BLACK);
			g2d.drawOval(Math.round(origin.x1)-this.getX()-3, Math.round(origin.y1)-this.getY()-3, 6, 6);
			g2d.drawOval(Math.round(origin.x2)-this.getX()-3, Math.round(origin.y2)-this.getY()-3, 6, 6);
			g2d.setColor(Color.GREEN);
			g2d.fillOval(Math.round(origin.x1)-this.getX()-2, Math.round(origin.y1)-this.getY()-2, 5, 5);
			g2d.fillOval(Math.round(origin.x2)-this.getX()-2, Math.round(origin.y2)-this.getY()-2, 5, 5);
		}
	}
	
	public void print()
	{
		System.out.println("Point 1: "+ origin.getX1() + " , "+ origin.getY1());
		System.out.println("Point 2: "+ origin.getX2() + " , "+ origin.getY2());
		System.out.println("State: "+ getState());
	}
	
	public Line2D.Float getLine()
	{
		return origin;
	}

	@Override
	public String getXML() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("\n<shape>");

		int count = 2;
		
		buffer.append("\n<name value=\"line\" />\n<points>");
		
		buffer.append("\n<point x=\""+origin.getX1()+"\" y=\""+origin.getY1()+"\" />");
		buffer.append("\n<point x=\""+origin.getX2()+"\" y=\""+origin.getY2()+"\" />");
		buffer.append("\n</points>" +
					"\n<constraints>" +
					"\n<static value=\""+state+"\" />" +
					"\n</constraints>" +
					"\n</shape>");
		return buffer.toString();
	}
}
