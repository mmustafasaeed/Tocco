package XMLReading;



import net.phys2d.math.Vector2f;
import net.phys2d.raw.BasicJoint;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.shapes.DynamicShape;

public class Pendulum {

	Line l1;
	CircleStruct c1;
	
	public Pendulum()
	{
		
	}
	
	public void setLine(Line l)
	{
		l1=l;
	}
	
	public void setCircle(CircleStruct c)
	{
		c1=c;
	}
	
	public Pendulum(Line l, CircleStruct c)
	{
		l1=l;
		c1=c;
	}
	
	public Line getLine()
	{
		return l1;
	}
	
	public CircleStruct getCircle()
	{
		return c1;
	}
	
	public void draw(World world)
	{
		//drawing the anchor
		Body anchor=new StaticBody("anchor", new Box(5.0f, 3.0f));
		anchor.setFriction(0.2f);
		anchor.setPosition(250.0f, 100);
		anchor.setRotation(0);
		world.add(anchor);
		
		DynamicShape shape=new Circle(c1.getRadius());
		Body body = new Body(shape, 10.0f);
		body.setFriction(0.4f);
		body.setPosition(c1.getPoints().get(0).getX(), c1.getPoints().get(0).getY());
		body.setRotation(0.0f);
		world.add(body);
		
		BasicJoint j = new BasicJoint(anchor, body, new Vector2f(170.0f, 111.0f));
		world.add(j);
		
		
	}
}
