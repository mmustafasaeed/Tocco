package XMLReading;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

import edu.mit.sketch.geom.Point;

//import XMLReading.Drawable.VectorAngleComparator;


import net.phys2d.math.MathUtil;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.test.AbstractDemo;

public class Drawable2 extends AbstractDemo implements MouseListener, MouseMotionListener {
	
	
	final float DISTANCE_SQUARED_THRESHOLD = 25.0f;
	final float ANGLE_THRESHOLD = (float) Math.toRadians(3);
	
	private final int ACTION_NONE = 0;
	private final int ACTION_DRAWING = 1;
	private final int ACTION_DELETING = 2;
	
	// True if we should render normals
	private boolean normals = false;
	// True if we should render contact points
	private boolean contacts = false;
	// True if the simulation should be reset
	private boolean needsReset = false;
	
	private int action = ACTION_NONE;
	
	private final int POLY_CLOCKWISE = -1;
	private final int POLY_COUNTERCLOCKWISE = 1;
	private final int POLY_INCOMPUTABLE = 0;
	
	private Point prevPoint;
	private Point curPoint;
	
	private final float PI = (float) 3.142;
	
	private ArrayList shape;
	
	public Drawable2(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init(World world) {

		initializeWorld(world);
		frame.addMouseMotionListener(this);
		frame.addMouseListener(this);
		
	}
	
	protected void initializeWorld(World world)
	{
		net.phys2d.raw.shapes.Line line1 = new net.phys2d.raw.shapes.Line(0, frame.getHeight() - 100, 50, frame.getHeight() - 50);
	    Body ground1 = new StaticBody(line1);
	    
	    net.phys2d.raw.shapes.Line line2 = new net.phys2d.raw.shapes.Line(frame.getWidth() - 50, frame.getHeight() - 50, frame.getWidth(), frame.getHeight() - 100);
	    Body ground2 = new StaticBody(line2);
	    
	    net.phys2d.raw.shapes.Line line3 = new net.phys2d.raw.shapes.Line(50, frame.getHeight() - 50, frame.getWidth() - 50, frame.getHeight() - 50);
	    Body ground3 = new StaticBody(line3);
	    
	    world.add(ground1);
	    world.add(ground2);
	    world.add(ground3);
	}
	
	
	/*public void draw(Graphics2D g)
	{
		world.step();
	    
	    if (shape != null)
	    {    
	        drawShape(g, shape);
	    }
	    
	    BodyList bodies = world.getBodies();

	    for (int i = 0; i < bodies.size(); i++)
	    {
	        Body body = bodies.get(i);

	        drawBody(g, body);
	    }
	    
	    if (contacts || normals)
	    {
	        ArbiterList arbs = world.getArbiters();
			
	        for (int i = 0; i < arbs.size(); i++)
	        {
	            Arbiter arb = arbs.get(i);

	            Contact[] contacts = arb.getContacts();
	            int numContacts = arb.getNumContacts();

	            for (int j = 0; j < numContacts; j++)
	            {
	                drawContact(g, contacts[j]);
	            }
	        }
	    }
	}
	
	void drawShape(Graphics2D g, ArrayList shape)
	{
	   // ellipseMode();
	    
	    if (shape.size() == 0)
	    {
	        return;
	    }
	    else
	    {
	        Vector2f p = (Vector2f)shape.get(0);
	        Ellipse2D ele=new Ellipse2D.Double(p.x, p.y, 2, 2);
	        g.draw(ele);
	        
	        if (shape.size() > 1)
	        {
	            for (int i = 1; i < shape.size(); i++)
	            {
	                Vector2f p1 = (Vector2f)shape.get(i - 1);
	                Vector2f p2 = (Vector2f)shape.get(i);
	                
	                Ellipse2D ele1=new Ellipse2D.Double(p2.x, p2.y, 2, 2);
	    	        g.draw(ele1);
	    	        
	    	        net.phys2d.raw.shapes.Line line=new Line(p1.x, p1.y, p2.x, p2.y);
	                g.draw((Shape) line);
	            }
	        }
	    }
	}
	
	protected void drawBody(Graphics2D g, Body body)
	{
	    if (body.getShape() instanceof Box)
	    {
	        drawBoxBody(g, body, (Box) body.getShape());
	    }
	    else if (body.getShape() instanceof Circle)
	    {
	        drawCircleBody(g, body, (Circle) body.getShape());
	    }
	    else if (body.getShape() instanceof net.phys2d.raw.shapes.Line)
	    {
	        drawLineBody(g, body, (Line) body.getShape());
	    }
	    else if (body.getShape() instanceof net.phys2d.raw.shapes.Polygon)
	    {
	        drawPolygonBody(g, body, (net.phys2d.raw.shapes.Polygon) body.getShape());
	    }
	}
	
	protected void drawPolygonBody(Graphics2D g, Body body, net.phys2d.raw.shapes.Polygon poly)
	{

	    ROVector2f[] verts = poly.getVertices(body.getPosition(), body.getRotation());

	    for (int i = 0; i < verts.length; i++)
	    {
	        Vertex2D vert=new Vertex2D.(verts[i].getX(), verts[i].getY());
	    }
	    
	}

	protected void drawLineBody(Graphics2D g, Body body, Line l)
	{
	    

	    Vector2f[] verts = l.getVertices(body.getPosition(), body.getRotation());
	    net.phys2d.raw.shapes.Line line=new net.phys2d.raw.shapes.Line(
	    (int) verts[0].getX(),
	    (int) verts[0].getY(), 
	    (int) verts[1].getX(),
	    (int) verts[1].getY());
	    g.draw((Shape) line);
	}

	protected void drawCircleBody(Graphics2D g, Body body, Circle circle)
	{
	   
	    
	    float x = body.getPosition().getX();
	    float y = body.getPosition().getY();
	    float r = circle.getRadius();
	    float rot = body.getRotation();
	    float xo = (float) (Math.cos(rot) * r);
	    float yo = (float) (Math.sin(rot) * r);

	    Ellipse2D elli=new Ellipse2D.Double(x, y, r * 2, r * 2);
	    g.draw(elli);
	    net.phys2d.raw.shapes.Line line=new net.phys2d.raw.shapes.Line((int) x, (int) y, (int) (x+xo), (int) (y+yo));
	    g.draw((Shape) line);
	}

	protected void drawBoxBody(Graphics2D g, Body body, Box b)
	{
	    Vector2f[] pts = b.getPoints(body.getPosition(), body.getRotation());

	    Vector2f v1 = pts[0];
	    Vector2f v2 = pts[1];
	    Vector2f v3 = pts[2];
	    Vector2f v4 = pts[3];

	    
	    net.phys2d.raw.shapes.Line line1=new net.phys2d.raw.shapes.Line((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
	    net.phys2d.raw.shapes.Line line2=new net.phys2d.raw.shapes.Line((int) v2.x, (int) v2.y, (int) v3.x, (int) v3.y);
	    net.phys2d.raw.shapes.Line line3=new net.phys2d.raw.shapes.Line((int) v3.x, (int) v3.y, (int) v4.x, (int) v4.y);
	    net.phys2d.raw.shapes.Line line4=new net.phys2d.raw.shapes.Line((int) v4.x, (int) v4.y, (int) v1.x, (int) v1.y);
	    
	    g.draw((Shape) line1);
	    g.draw((Shape) line2);
	    g.draw((Shape) line3);
	    g.draw((Shape) line4);
	}
	
	*/
	
	float vectorAngle(Vector2f p1, Vector2f p2, Vector2f p3)
	{
	    
	    Vector2f a = MathUtil.sub(p2, p1);
	    Vector2f b = MathUtil.sub(p2, p3);
	    
	    float dotProduct = a.dot(b);
	    float crossProduct = MathUtil.cross(a, b);
	    
	    float angle = (float) Math.atan2(crossProduct, dotProduct);
	    return Math.abs(angle);
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
	public void mousePressed(MouseEvent event) {
		
		System.out.println("Mouse Pressed");
		
		prevPoint.x=event.getX();
		prevPoint.y=event.getY();
		
		if (event.getButton() == MouseEvent.BUTTON1)
	    {
	        action = ACTION_DRAWING;
	        shape = new ArrayList();
	        Vector2f p = new Vector2f(event.getX(), event.getY());
	        shape.add(p);
	        System.out.println("Point added: "+p);
	    }
		
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		
		 
		     /* Point point = new Point( event.getPoint() );
		        Graphics g = frame.getGraphics();
		        g.setColor( frame.getForeground() );
		        g.drawLine( point.x,
		            point.y,
		            prevPoint.x,
		            prevPoint.y ); */
		      
		
		if (action == ACTION_DRAWING)
	    {
	        if (shape != null && shape.size() > 2)
	        {
	            
	            Vector2f[] vertexArray;
	            
	            Vector2f[] arrayType = new Vector2f[1];

	            vertexArray = (Vector2f[])shape.toArray(arrayType);

	            int polyOrientation = orientation(shape);

	            if (polyOrientation == POLY_CLOCKWISE)
	            {
	                int j = vertexArray.length - 1;
	                for (int i = 0; i < j; i++, j--)
	                {
	                    Vector2f temp = vertexArray[i];
	                    vertexArray[i] = vertexArray[j];
	                    vertexArray[j] = temp;
	                }
	            }
	        
	            net.phys2d.raw.shapes.Polygon poly = new net.phys2d.raw.shapes.Polygon(vertexArray);
	            Vector2f centroid = poly.getCentroid();
	        
	            for (int i = 0; i < vertexArray.length; i++)
	            {
	                vertexArray[i].sub(centroid);
	            }
	        
	            poly = new net.phys2d.raw.shapes.Polygon(vertexArray);
	        
	            Body polyBody = new Body(poly, poly.getArea());
	            polyBody.setPosition(centroid.x, centroid.y);
	            polyBody.setRotation(0.0f);
	            polyBody.setForce(0, 10.0f);
	            world.add(polyBody);
	            System.out.println("Mouse Released and poly added to world");
	        }
	        shape = null;
	    }
	    
	    action = ACTION_NONE;
		
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		
		curPoint.x=event.getX();
		curPoint.y=event.getY();
		
		Graphics g = frame.getGraphics();
	    g.setColor( frame.getForeground() );
	    g.drawLine( curPoint.x,
	            curPoint.y,
	            prevPoint.x,
	            prevPoint.y );
	    
	    prevPoint.x=curPoint.x;
	    prevPoint.y=curPoint.y;
		
		if (event.getButton() == MouseEvent.BUTTON1)
	    {
	        int pointCount = shape.size();
	        boolean addThisPoint = true;
	        // p1 is the most recent point - the one caused by the drag event
	        // p2 is the previously stored point
	        // p3, if it exists, is the point before the previously stored point.
	        Vector2f p1 = new Vector2f(event.getX(), event.getY());
	        Vector2f p2 = (Vector2f)shape.get(pointCount - 1);
	    
	        float distanceSquared = p1.distanceSquared(p2);
	    
	        if (distanceSquared < DISTANCE_SQUARED_THRESHOLD)
	        {
	            addThisPoint = false;
	        }
	    
	        if (pointCount > 1)
	        {
	            Vector2f p3 = (Vector2f)shape.get(pointCount - 2);
	        
	            float angle = vectorAngle(p1, p2, p3);
	            
	            if (angle < ANGLE_THRESHOLD ||
	                (angle < PI + ANGLE_THRESHOLD && angle > PI - ANGLE_THRESHOLD))
	            {
	                addThisPoint = false;
	            }
	        }
	    
	        if (addThisPoint)
	        {
	            shape.add(p1);
	        }
	    }
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public int orientation(ArrayList poly)
	{
	    float area;
	    int i, pointCount = poly.size();

	    Vector2f p1, p2;
	    
	    p1 = (Vector2f)poly.get(pointCount - 1);
	    p2 = (Vector2f)poly.get(0);
	    
	    area = p1.x * p2.y - p2.x * p1.y;

	    for (i = 0; i < pointCount - 1; i++)
	    {
	        p1 = (Vector2f)poly.get(i);
	        p2 = (Vector2f)poly.get(i + 1);
	        area += p1.x * p2.y - p2.x * p1.y;
	    }
	    
	    if (area >= 0.0)
	    {
	        return POLY_COUNTERCLOCKWISE;
	    }
	    
	    return POLY_CLOCKWISE;
	}
	
	public Vector2f[] convexHull(ArrayList points)
	{
	    // Find the lowest point
	    Vector2f base = (Vector2f)points.get(0);
	    
	    for (int i = 1; i < points.size(); i++)
	    {
	        Vector2f temp = (Vector2f)points.get(i);
	        
	        if (temp.y < base.y || (temp.y == base.y && temp.x < base.x))
	        {
	            base = (Vector2f)points.get(i);
	        }
	    }
	    
	    // Sort the point array
	    Vector2f[] sortedPoints;
	    Vector2f[] arrayType = new Vector2f[1];
	    
	    sortedPoints = (Vector2f[])points.toArray(arrayType);
	    VectorAngleComparator vac = new VectorAngleComparator(base);
	    Arrays.sort(sortedPoints, vac);
	    
	    // Calculate the convex hull
	    Stack stack = new Stack();
	    
	    stack.push(sortedPoints[0]);
	    stack.push(sortedPoints[1]);
	    
	    for (int i = 3; i < sortedPoints.length; i++)
	    {
	        while (stack.size() >= 2 &&
	                crossProduct((Vector2f)stack.get(stack.size() - 2), (Vector2f)stack.peek(), sortedPoints[i]) <= 0)
	        {
	            stack.pop();
	        }
	        
	        stack.push(sortedPoints[i]);
	    }
	    
	    //stack.push(base);
	    
	    /*
	    Vector2f[] hull = new Vector2f[stack.size()];
	    
	    for (int i = 0; i < hull.length; i++)
	    {
	        hull[i] = (Vector2f)stack.get(i);//(Vector2f)stack.pop();
	    }
	    
	    return hull;
	    */
	    return (Vector2f[])stack.toArray(arrayType);
	}
	
	public float crossProduct(Vector2f p1, Vector2f p2, Vector2f p3)
	{
	    return (p2.x - p1.x) * (p3.y - p1.y) - (p3.x - p1.x) * (p2.y - p1.y);
	}
	
	
	class VectorAngleComparator implements Comparator
	{
	    
	    private Vector2f base;
	    
	    VectorAngleComparator(Vector2f base)
	    {
	        this.base = base;
	    }
	    
	    public int compare(Object p1, Object p2)
	    {
	        if (p1 instanceof Vector2f && p2 instanceof Vector2f)
	        {
	            Vector2f a = MathUtil.sub((Vector2f)p1, base);
	            Vector2f b = MathUtil.sub((Vector2f)p2, base);
	            
	            float aAngle = (float) Math.atan2(a.y, a.x);
	            float bAngle = (float) Math.atan2(b.y, b.x);
	            
	            float angleDiff = aAngle - bAngle;
	            
	            if (angleDiff < 0)
	            {
	                return -1;
	            }
	            
	            if (angleDiff > 0)
	            {
	                return 1;
	            }
	        }
	        
	        return 0;
	    }
	    
	    public boolean equals(Object obj)
	    {
	        if (obj instanceof VectorAngleComparator)
	        {
	            VectorAngleComparator vac = (VectorAngleComparator)obj;
	            
	            return (vac.base.x == this.base.x) && (vac.base.y == this.base.y);
	        }
	        
	        return false;
	    }
	}


}
