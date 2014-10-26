

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;

public class CubicCurves extends ShapePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CubicCurve2D.Float origin;
	
	public CubicCurves (float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
		super();
		
		origin = new CubicCurve2D.Float(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
		Rectangle r = origin.getBounds();
		setSize(r.width + offset*2, r.height + offset*2);  // Set the size of this panel
		setLocation(r.x - offset, r.y - offset);  // Set the position of this panel
	}
	
	@Override
	public int isResize(int x, int y) {
		
		if (new Rectangle(Math.round(origin.x1)-5, Math.round(origin.y1)-5, 10, 10).contains(x, y))
			return 0;
		if (new Rectangle(Math.round(origin.ctrlx1)-5, Math.round(origin.ctrly1)-5, 10, 10).contains(x, y))
			return 1;
		if (new Rectangle(Math.round(origin.ctrlx2)-5, Math.round(origin.ctrly2)-5, 10, 10).contains(x, y))
			return 2;
		if (new Rectangle(Math.round(origin.x2)-5, Math.round(origin.y2)-5, 10, 10).contains(x, y))
			return 3;
		
		return -1;
	}

	@Override
	public void moveTo(int deltaX, int deltaY) {
		
		origin.setCurve(origin.x1+deltaX, origin.y1+deltaY, origin.ctrlx1+deltaX, origin.ctrly1+deltaY,
				origin.ctrlx2+deltaX, origin.ctrly2+deltaY, origin.x2+deltaX, origin.y2+deltaY);
		this.setLocation(this.getX() + deltaX, this.getY() + deltaY);
	}

	@Override
	public void reSize(int j, int deltaX, int deltaY) {
		
		switch(j) {
		case 0:
			origin.setCurve(origin.x1+deltaX, origin.y1+deltaY, origin.ctrlx1, origin.ctrly1, origin.ctrlx2, origin.ctrly2, origin.x2, origin.y2);
			break;
		case 1:
			origin.setCurve(origin.x1, origin.y1, origin.ctrlx1+deltaX, origin.ctrly1+deltaY, origin.ctrlx2, origin.ctrly2, origin.x2, origin.y2);
			break;
		case 2:
			origin.setCurve(origin.x1, origin.y1, origin.ctrlx1, origin.ctrly1, origin.ctrlx2+deltaX, origin.ctrly2+deltaY, origin.x2, origin.y2);
			break;
		case 3:
			origin.setCurve(origin.x1, origin.y1, origin.ctrlx1, origin.ctrly1, origin.ctrlx2, origin.ctrly2, origin.x2+deltaX, origin.y2+deltaY);
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
		g2d.draw(new CubicCurve2D.Float(origin.x1-this.getX(), origin.y1-this.getY(), origin.ctrlx1-this.getX(), origin.ctrly1-this.getY(),
				origin.ctrlx2-this.getX(), origin.ctrly2-this.getY(), origin.x2-this.getX(), origin.y2-this.getY()));
		
		if (seleted) {
			g2d.setStroke(new BasicStroke(1.0f));
			g2d.setColor(Color.BLACK);
			g2d.drawOval(Math.round(origin.x1)-this.getX()-3, Math.round(origin.y1)-this.getY()-3, 6, 6);
			g2d.drawOval(Math.round(origin.ctrlx1)-this.getX()-3, Math.round(origin.ctrly1)-this.getY()-3, 6, 6);
			g2d.drawOval(Math.round(origin.ctrlx2)-this.getX()-3, Math.round(origin.ctrly2)-this.getY()-3, 6, 6);
			g2d.drawOval(Math.round(origin.x2)-this.getX()-3, Math.round(origin.y2)-this.getY()-3, 6, 6);
			g2d.setColor(Color.GREEN);
			g2d.fillOval(Math.round(origin.x1)-this.getX()-2, Math.round(origin.y1)-this.getY()-2, 5, 5);
			g2d.fillOval(Math.round(origin.ctrlx1)-this.getX()-2, Math.round(origin.ctrly1)-this.getY()-2, 5, 5);
			g2d.fillOval(Math.round(origin.ctrlx2)-this.getX()-2, Math.round(origin.ctrly2)-this.getY()-2, 5, 5);
			g2d.fillOval(Math.round(origin.x2)-this.getX()-2, Math.round(origin.y2)-this.getY()-2, 5, 5);
		}
	}

	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return "";
	}
}
