package XMLReading;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.test.AbstractDemo;

public class Draw  extends AbstractDemo implements MouseMotionListener, MouseListener {
	
	private Point currentPoint;
	private Point prevPoint;
	private JFrame frame;

	public Draw(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init(World arg0) {
		
		initializeWorld(world);
		//frame.addMouseMotionListener(this);
		//frame.addMouseListener(this);
		//System.out.println(frame.getComponentCount());
		
	}
	
	protected void initializeWorld(World world)
	{
		net.phys2d.raw.shapes.Line line1 = new net.phys2d.raw.shapes.Line(0, 400, 50, 450);
	    Body ground1 = new StaticBody(line1);
	    
	    net.phys2d.raw.shapes.Line line2 = new net.phys2d.raw.shapes.Line(450, 450, 500, 400);
	    Body ground2 = new StaticBody(line2);
	    
	    net.phys2d.raw.shapes.Line line3 = new net.phys2d.raw.shapes.Line(50, 450, 450, 450);
	    Body ground3 = new StaticBody(line3);
	    
	    world.add(ground1);
	    world.add(ground2);
	    world.add(ground3);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		prevPoint.setX(e.getX());
		prevPoint.setY(e.getY());
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		currentPoint.setX(e.getX());
		currentPoint.setY(e.getY());
		
		Graphics g=frame.getGraphics();
		g.setColor(frame.getForeground());
		g.drawLine((int)currentPoint.getX(), (int)currentPoint.getY(), (int)prevPoint.getX(), (int)prevPoint.getX());
		
		prevPoint.setX(e.getX());
		prevPoint.setY(e.getY());
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
}
