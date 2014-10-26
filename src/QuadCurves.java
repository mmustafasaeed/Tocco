
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.QuadCurve2D;

public class QuadCurves extends ShapePanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	QuadCurve2D.Float origin;
	
	// Constructor
	public QuadCurves(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
		super();
		
		origin = new QuadCurve2D.Float(x1, y1, ctrlx, ctrly, x2, y2);
		Rectangle r = origin.getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Set the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Set the position of this panel
	}
	
	@Override
	public int isResize(int x, int y) {
		
		if (new Rectangle(Math.round(origin.x1)-5, Math.round(origin.y1)-5, 10, 10).contains(x, y))
			return 0;
		if (new Rectangle(Math.round(origin.ctrlx)-5, Math.round(origin.ctrly)-5, 10, 10).contains(x, y))
			return 1;
		if (new Rectangle(Math.round(origin.x2)-5, Math.round(origin.y2)-5, 10, 10).contains(x, y))
			return 2;
		
		return -1;
	}
	
	@Override
	public void moveTo(int deltaX, int deltaY) {
		
		origin.setCurve(origin.x1+deltaX, origin.y1+deltaY, origin.ctrlx+deltaX,
				origin.ctrly+deltaY,origin.x2+deltaX, origin.y2+deltaY);
		this.setLocation(this.getX() + deltaX, this.getY() + deltaY);
	}

	@Override
	public void reSize(int j, int deltaX, int deltaY) {
		
		switch(j) {
		case 0:
			origin.setCurve(origin.x1+deltaX, origin.y1+deltaY, origin.ctrlx, origin.ctrly, origin.x2, origin.y2);
			break;
		case 1:
			origin.setCurve(origin.x1, origin.y1, origin.ctrlx+deltaX, origin.ctrly+deltaY, origin.x2, origin.y2);
			break;
		case 2:
			origin.setCurve(origin.x1, origin.y1, origin.ctrlx, origin.ctrly, origin.x2+deltaX, origin.y2+deltaY);
		}
		
		Rectangle r = origin.getBounds();
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
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		
		g2d.setColor(drawcolor);  // Draw the shape in this panel
		g2d.draw(new QuadCurve2D.Float(origin.x1-this.getX(), origin.y1-this.getY(), origin.ctrlx-this.getX(),
				origin.ctrly-this.getY(), origin.x2-this.getX(), origin.y2-this.getY()));
		
		if (seleted) {
			g2d.setStroke(new BasicStroke(1.0f));
			g2d.setColor(Color.BLACK);
			g2d.drawOval(Math.round(origin.x1)-this.getX()-3, Math.round(origin.y1)-this.getY()-3, 6, 6);
			g2d.drawOval(Math.round(origin.ctrlx)-this.getX()-3, Math.round(origin.ctrly)-this.getY()-3, 6, 6);
			g2d.drawOval(Math.round(origin.x2)-this.getX()-3, Math.round(origin.y2)-this.getY()-3, 6, 6);
			g2d.setColor(Color.GREEN);
			g2d.fillOval(Math.round(origin.x1)-this.getX()-2, Math.round(origin.y1)-this.getY()-2, 4, 4);
			g2d.fillOval(Math.round(origin.ctrlx)-this.getX()-2, Math.round(origin.ctrly)-this.getY()-2, 4, 4);
			g2d.fillOval(Math.round(origin.x2)-this.getX()-2, Math.round(origin.y2)-this.getY()-2, 4, 4);
		}
	}

	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return "";
	}
}
