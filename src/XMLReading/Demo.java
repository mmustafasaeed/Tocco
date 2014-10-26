package XMLReading;

import java.util.*;

import net.phys2d.raw.shapes.Shape;

import pphys2d.PPWorld;
import pphys2d.bodies.PPCircle;
import pphys2d.bodies.PPLine;
import pphys2d.phys2d.math.MathUtil;
import pphys2d.phys2d.math.ROVector2f;
import pphys2d.phys2d.math.Vector2f;
import pphys2d.phys2d.raw.Arbiter;
import pphys2d.phys2d.raw.ArbiterList;
import pphys2d.phys2d.raw.Body;
import pphys2d.phys2d.raw.BodyList;
import pphys2d.phys2d.raw.Contact;
import pphys2d.phys2d.raw.shapes.Box;
import pphys2d.phys2d.raw.shapes.Circle;
import pphys2d.phys2d.raw.shapes.DynamicShape;
import pphys2d.phys2d.raw.shapes.Polygon;
import processing.core.PApplet;
import processing.core.PFont;




public class Demo extends PApplet{
	

public PPWorld world;

ArrayList shape;

final float DISTANCE_SQUARED_THRESHOLD = 25.0f;
final float ANGLE_THRESHOLD = radians(3);

// True if we should render normals
private boolean normals = false;
// True if we should render contact points
private boolean contacts = false;
// True if the simulation should be reset
private boolean needsReset = false;


private final int ACTION_NONE = 0;
private final int ACTION_DRAWING = 1;
private final int ACTION_DELETING = 2;

private int action = ACTION_NONE;

private final int POLY_CLOCKWISE = -1;
private final int POLY_COUNTERCLOCKWISE = 1;
private final int POLY_INCOMPUTABLE = 0;

private final float PI=(float) 3.142;

//PFont font;

public void setup()
{
    // Setup window
    size(400, 400);
    frameRate(200);
    
  //  font = loadFont("d.vlw");
    //textFont(font, 12);
    
    initialiseWorld();
}

void initialiseWorld()
{
    // Initialise physics
    world = new PPWorld();
    
    // Add ground
    PPLine line1 = new PPLine(0, height - 100, 50, height - 50);
    line1.setStaticBody(true);
    world.add(line1);
    
    PPLine line2 = new PPLine(width - 50, height - 50, width, height - 100);
    line2.setStaticBody(true);
    world.add(line2);
    
    PPLine line3 = new PPLine(50, height - 50, width - 50, height - 50);
    line3.setStaticBody(true);
    world.add(line3);
    
}

public void draw()
{
    background(255);

    noFill();
    stroke(200);
    rect(0, 0, width - 1, height - 1);

    fill(0);
    noStroke();
    text("FPS: " + frameRate, 5, 20);

    if (needsReset)
    {
        pphys2d.phys2d.raw.BodyList bodies = world.getBodies();
        
        for (int i = bodies.size() - 1; i >= 0; i--)
        {
            pphys2d.phys2d.raw.Body body = bodies.get(i);
            
            if (!body.isStatic())
            {
                world.remove(body);
            }
        }
        
        shape = null;
        
        needsReset = false;
    }

    world.step();
    
    if (shape != null)
    {    
        drawShape(shape);
    }
    
    pphys2d.phys2d.raw.BodyList bodies = world.getBodies();

    for (int i = 0; i < bodies.size(); i++)
    {
        Body body = bodies.get(i);

        drawBody(body);
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
                drawContact(contacts[j]);
            }
        }
    }
}

void drawShape(ArrayList shape)
{
    ellipseMode(CENTER);
    
    if (shape.size() == 0)
    {
        return;
    }
    else
    {
        noStroke();
        fill(100);
        Vector2f p = (Vector2f)shape.get(0);
        PPCircle circle=new PPCircle(2);
        circle.setPosition(p.x, p.y);
        world.add(circle);
        
        if (shape.size() > 1)
        {
            for (int i = 1; i < shape.size(); i++)
            {
                Vector2f p1 = (Vector2f)shape.get(i - 1);
                Vector2f p2 = (Vector2f)shape.get(i);
                
                noStroke();
                fill(100);
                PPCircle circle1=new PPCircle(2);
                circle1.setPosition(p2.x, p2.y);
                world.add(circle1);
                
                stroke(150);
                PPLine l1= new PPLine(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);
                world.add(l1);
            }
        }
    }
}

void drawBody(Body body)
{
    if (body.getShape() instanceof Box)
    {
        drawBoxBody(body, (Box) body.getShape());
    }
    else if (body.getShape() instanceof Circle)
    {
        drawCircleBody(body, (Circle) body.getShape());
    }
    else if (body.getShape() instanceof PPLine)
    {
        drawLineBody(body, (pphys2d.phys2d.raw.shapes.Line) body.getShape());
    }
    else if (body.getShape() instanceof net.phys2d.raw.shapes.Polygon)
    {
        drawPolygonBody(body, (Polygon) body.getShape());
    }
}

void drawPolygonBody(Body body, Polygon poly)
{

    ROVector2f[] verts = poly.getVertices();
    
    stroke(50);
    fill(200);

    beginShape();

    for (int i = 0; i < verts.length; i++)
    {
        vertex(verts[i].getX(), verts[i].getY());
    }
    
    endShape(CLOSE);
}

void drawLineBody(Body body, pphys2d.phys2d.raw.shapes.Line l)
{
    stroke(50);
    noFill();

    Vector2f[] verts = l.getVertices(body.getPosition(), body.getRotation());
    line(
    (int) verts[0].getX(),
    (int) verts[0].getY(), 
    (int) verts[1].getX(),
    (int) verts[1].getY());
}

void drawCircleBody(Body body, Circle circle)
{
    stroke(50);
    noFill();
    
    float x = body.getPosition().getX();
    float y = body.getPosition().getY();
    float r = circle.getRadius();
    float rot = body.getRotation();
    float xo = (float) (cos(rot) * r);
    float yo = (float) (sin(rot) * r);

    ellipse(x, y, r * 2, r * 2);
    line((int) x, (int) y, (int) (x+xo), (int) (y+yo));
}

void drawBoxBody(Body body, Box b)
{
    Vector2f[] pts = b.getPoints(body.getPosition(), body.getRotation());

    Vector2f v1 = pts[0];
    Vector2f v2 = pts[1];
    Vector2f v3 = pts[2];
    Vector2f v4 = pts[3];

    stroke(50);
    noFill();
    line((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
    line((int) v2.x, (int) v2.y, (int) v3.x, (int) v3.y);
    line((int) v3.x, (int) v3.y, (int) v4.x, (int) v4.y);
    line((int) v4.x, (int) v4.y, (int) v1.x, (int) v1.y);
}

void drawContact(Contact contact)
{
    int x = (int) contact.getPosition().getX();
    int y = (int) contact.getPosition().getY();
    if (contacts)
    {
        noStroke();
        fill(0, 0, 255);
        ellipse(x, y, 6, 6);
    }

    if (normals)
    {
        int dx = (int) (contact.getNormal().getX() * 10);
        int dy = (int) (contact.getNormal().getY() * 10);
        stroke(150);
        noFill();
        line(x, y, x + dx, y + dy);
    }
}


public void mousePressed()
{
    if (mouseButton == LEFT)
    {
        action = ACTION_DRAWING;
        shape = new ArrayList();
        Vector2f p = new Vector2f(mouseX, mouseY);
        shape.add(p);
    }
    
    if (mouseButton == RIGHT)
    {
        action = ACTION_DELETING;
        
        boolean found = false;
        Vector2f mouseVector = new Vector2f(mouseX, mouseY);
        
        BodyList bodies = world.getBodies();
        
        for (int i = 0; i < bodies.size() && !found; i++)
        {
            Body body = bodies.get(i);
            net.phys2d.raw.shapes.Shape bodyShape = (Shape) body.getShape();
            if (bodyShape instanceof net.phys2d.raw.shapes.Polygon)
            {
                net.phys2d.raw.shapes.Polygon poly = (net.phys2d.raw.shapes.Polygon)bodyShape;
                if (poly.contains((net.phys2d.math.ROVector2f) mouseVector))
                {
                    world.remove(body);
                    found = true;
                }
            }
        }
    }
}

public void mouseDragged()
{
    if (mouseButton == LEFT)
    {
        int pointCount = shape.size();
        boolean addThisPoint = true;
        // p1 is the most recent point - the one caused by the drag event
        // p2 is the previously stored point
        // p3, if it exists, is the point before the previously stored point.
        Vector2f p1 = new Vector2f(mouseX, mouseY);
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

public void mouseReleased()
{
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
        
            Polygon poly = new Polygon(vertexArray);
            Vector2f centroid = poly.getCentroid();
        
            for (int i = 0; i < vertexArray.length; i++)
            {
                vertexArray[i].sub(centroid);
            }
        
            poly = new Polygon(vertexArray);
        
            Body polyBody = new Body(poly, poly.getArea());
            polyBody.setPosition(centroid.x, centroid.y);
            polyBody.setRotation(0.0f);
            polyBody.setForce(0, 10.0f);
            world.add(polyBody);
        }
        shape = null;
    }
    
    action = ACTION_NONE;
}

public void keyPressed()
{
    switch (key)
    {
        
        case ' ':
            Vector2f[] circleVerts = new Vector2f[30];
            float[] radius = {20,10};
            for(int i = 0; i < 30; i++)
            {
                float angle = (float) (3 * 4 * i * PI / 180);
                circleVerts[i] = new Vector2f(
                    (float) (Math.cos(angle) * radius[i % 2]), 
                    (float) (Math.sin(angle) * radius[i % 2]));
            }
            Polygon circlePolygon = new Polygon(circleVerts);
            Body newBody = new Body((DynamicShape) circlePolygon, 4);
		
            newBody.setPosition(250, 150);
            newBody.setRotation(random((float)(1.0 * 2 * PI)));
            newBody.setForce(0, 10.0f);
            world.add(newBody);
            break;

        case 'r':
            needsReset = true;
            break;
            
        case 'c':
            contacts = !contacts;
            break;
            
        case 'n':
            normals = !normals;
            break;
    }
}


float vectorAngle(Vector2f p1, Vector2f p2, Vector2f p3)
{
    
    Vector2f a = MathUtil.sub(p2, p1);
    Vector2f b = MathUtil.sub(p2, p3);
    
    float dotProduct = a.dot(b);
    float crossProduct = MathUtil.cross(a, b);
    
    float angle = (float) Math.atan2(crossProduct, dotProduct);
    return Math.abs(angle);
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

}