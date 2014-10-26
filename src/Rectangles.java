
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class Rectangles extends ShapePanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Rectangle origin;
	boolean state;

	public Rectangles(int x, int y, int width, int height, boolean state) {
		super();
		this.state=state;
		origin = new Rectangle(x, y, width, height);
		setSize(origin.width + offset*2, origin.height + offset*2);  // Set the size of this panel
		setLocation(origin.x - offset, origin.y - offset);  // Set the position of this panel
	}
	
	public boolean getState()
	{
		return state;
	}
	
	@Override
	public int isResize(int x, int y) {
		
		if (new Rectangle(origin.x-5, origin.y-5, 10, 10).contains(x, y))
			return 0;
		if (new Rectangle(origin.x+origin.width-5, origin.y-5, 10, 10).contains(x, y))
			return 1;
		if (new Rectangle(origin.x+origin.width-5, origin.y+origin.height-5, 10, 10).contains(x, y))
			return 2;
		if (new Rectangle(origin.x-5, origin.y+origin.height-5, 10, 10).contains(x, y))
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

		switch(j) {
		case 0:
			if (origin.width-deltaX > 10 && origin.height-deltaY > 10)
				origin.setBounds(origin.x+deltaX, origin.y+deltaY, origin.width-deltaX, origin.height-deltaY);
			else if (origin.width-deltaX <= 10 && origin.height-deltaY <= 10)
				origin.setBounds(origin.x+origin.width-10, origin.y+origin.height-10, 10, 10);
			else if (origin.width-deltaX <= 10)
				origin.setBounds(origin.x+origin.width-10, origin.y+deltaY, 10, origin.height-deltaY);
			else
				origin.setBounds(origin.x+deltaX, origin.y+origin.height-10, origin.width-deltaX, 10);
			break;
		case 1:
			if (origin.width+deltaX > 10 && origin.height-deltaY > 10)
				origin.setBounds(origin.x, origin.y+deltaY, origin.width+deltaX, origin.height-deltaY);
			else if (origin.width+deltaX <= 10 && origin.height-deltaY <= 10)
				origin.setBounds(origin.x, origin.y+origin.height-10, 10, 10);
			else if (origin.width+deltaX <= 10)
				origin.setBounds(origin.x, origin.y+deltaY, 10, origin.height-deltaY);
			else
				origin.setBounds(origin.x, origin.y+origin.height-10, origin.width+deltaX, 10);
			break;
		case 2:
			if (origin.width+deltaX > 10 && origin.height+deltaY > 10)
				origin.setBounds(origin.x, origin.y, origin.width+deltaX, origin.height+deltaY);
			else if (origin.width+deltaX <= 10 && origin.height+deltaY <= 10)
				origin.setBounds(origin.x, origin.y, 10, 10);
			else if (origin.width+deltaX <= 10)
				origin.setBounds(origin.x, origin.y, 10, origin.height+deltaY);
			else
				origin.setBounds(origin.x, origin.y, origin.width+deltaX, 10);
			break;
		case 3:
			if (origin.width-deltaX > 10 && origin.height+deltaY > 10)
				origin.setBounds(origin.x+deltaX, origin.y, origin.width-deltaX, origin.height+deltaY);
			else if (origin.width-deltaX <= 10 && origin.height+deltaY <= 10)
				origin.setBounds(origin.x+origin.width-10, origin.y, 10, 10);
			else if (origin.width-deltaX <= 10)
				origin.setBounds(origin.x+origin.width-10, origin.y, 10, origin.height+deltaY);
			else
				origin.setBounds(origin.x+deltaX, origin.y, origin.width-deltaX, 10);
			break;
		}
		
		setSize(origin.width + offset*2, origin.height + offset*2);  // Set the size of this panel
		setLocation(origin.x - offset, origin.y - offset);  // Set the position of this panel
		
		if (scrollpane != null) {
	        textpane.setSize(origin.width-1, origin.height-1);
	        scrollpane.setSize(origin.width-1, origin.height-1);
	        scrollpane.setLocation(origin.x-this.getX()+1, origin.y-this.getY()+1);
		}
	}
	
	@Override
	public boolean addText() {
		super.addText();
		
        textpane.setSize(origin.width-1, origin.height-1);
        scrollpane.setSize(origin.width-1, origin.height-1);
        scrollpane.setLocation(origin.x-this.getX()+1, origin.y-this.getY()+1);
        
        return true;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		
		if (drawcolor != null) {
			g2d.setColor(drawcolor);  // Draw the shape in this panel
			g2d.drawRect(origin.x - this.getX(), origin.y - this.getY(), origin.width, origin.height);
		}
		if (scrollpane == null) {
			if (fillcolor != null) {
				g2d.setColor(fillcolor);
				g2d.fillRect(origin.x - this.getX()+1, origin.y - this.getY()+1, origin.width-1, origin.height-1);
			}
		}
		else {
			if (fillcolor != null)
				scrollpane.setBackground(fillcolor);
			else {
				scrollpane.setOpaque(false);
				scrollpane.repaint();
			}
		}
		
		if (seleted) {
			g2d.setStroke(new BasicStroke(1.0f));
			g2d.setColor(Color.BLACK);
			g2d.drawOval(origin.x-this.getX()-3, origin.y-this.getY()-3, 6, 6);
			g2d.drawOval(origin.x+origin.width-this.getX()-3, origin.y-this.getY()-3, 6, 6);
			g2d.drawOval(origin.x+origin.width-this.getX()-3, origin.y+origin.height-this.getY()-3, 6, 6);
			g2d.drawOval(origin.x-this.getX()-3, origin.y+origin.height-this.getY()-3, 6, 6);
			g2d.setColor(Color.GREEN);
			g2d.fillOval(origin.x-this.getX()-2, origin.y-this.getY()-2, 5, 5);
			g2d.fillOval(origin.x+origin.width-this.getX()-2, origin.y-this.getY()-2, 5, 5);
			g2d.fillOval(origin.x+origin.width-this.getX()-2, origin.y+origin.height-this.getY()-2, 5, 5);
			g2d.fillOval(origin.x-this.getX()-2, origin.y+origin.height-this.getY()-2, 5, 5);
		}
	}

	@Override
	public String getXML() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("\n<shape>");
		buffer.append("\n<name value=\"rectangle\" />");
		
		buffer.append("\n<points>");
		buffer.append("\n<point x=\""+origin.x+"\" y=\""+origin.y+"\" radius=\"null\" />" );
		buffer.append("\n<point x=\""+(origin.x+origin.width)+"\" y=\""+origin.y+"\" radius=\"null\" />" );
		buffer.append("\n<point x=\""+origin.x+"\" y=\""+(origin.y+origin.height)+"\" radius=\"null\" />" );
		buffer.append("\n<point x=\""+(origin.x+origin.width)+"\" y=\""+(origin.y+origin.height)+"\" radius=\"null\" />" );
		
		buffer.append("\n</points>" +
					"\n<constraints>" +
					"\n<static value=\""+state+"\" />" +
					"\n</constraints>" +
					"\n</shape>");
		return buffer.toString();
	}
}
