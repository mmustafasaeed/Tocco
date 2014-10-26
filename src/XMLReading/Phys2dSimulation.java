package XMLReading;

import java.util.ArrayList;

import pphys2d.PPWorld;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.SpringJoint;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.test.AbstractDemo;

public class Phys2dSimulation extends AbstractDemo {
	
	boolean gravity;
	private boolean totable;
	//private World world= new World();

	private ArrayList<CircleStruct> circle=new ArrayList<CircleStruct>();
	private ArrayList<Line> line=new ArrayList<Line>();
	private ArrayList<Triangle> triangle=new ArrayList<Triangle>();
	private ArrayList<Rectangle> rectangle=new ArrayList<Rectangle>();
	
	public Phys2dSimulation()
	{
		super("Maheen Rocks :P");
		
		ReadXML read=new ReadXML();
		read.readScenario();
		
		gravity=read.getGravity();
		totable=read.getTotable();
		
		circle=read.getCircles();
		line=read.getLines();
		triangle=read.getTriangles();
		rectangle=read.getRects();
	}

	@Override
	protected void init(World world) {

		
		
		if(gravity)
		{
			world.setGravity(0, 10);
		}
		
		if(totable)
		{
			TotableStructure ts=new TotableStructure();
			/*world.add(ts.getBox1());
			world.add(ts.getBox2());
			world.add(ts.getBox3());
			world.add(ts.getBox4());
			world.add(ts.getBox5());
			world.add(ts.getBox6());*/
			
		}
		for(int i=0; i<circle.size(); i++)
		{
			CircleStruct cir=(CircleStruct)circle.get(i);
				//world.add(cir.draw());
			
		}
		for(int i=0; i<line.size(); i++)
		{
			Line ln=(Line)line.get(i);
				world.add(drawLine(ln));
		}
		for(int i=0; i<triangle.size(); i++)
		{
				Triangle tri=(Triangle)triangle.get(i);
				//world.add(tri.draw());
		}
		for(int i=0; i<rectangle.size(); i++)
		{
				Rectangle rect=(Rectangle)rectangle.get(i);
				//world.add(rect.draw());
		}
		
		
		Body knot=new StaticBody(new net.phys2d.raw.shapes.Circle(5.0f));
		knot.setPosition(100, 120);
		world.add(knot);
		Body s1=new Body(new net.phys2d.raw.shapes.Line(100, 120, 150, 140), 100.0f);
		world.add(s1);
		Body s2=new Body(new net.phys2d.raw.shapes.Line(150, 140, 200, 120), 100.0f);
		world.add(s2);
		
		SpringJoint j2=new SpringJoint(knot, s2, new Vector2f(),new Vector2f() );
		j2.setCompressedSpringConst(100);
		world.add(j2);
		SpringJoint j1=new SpringJoint(s1, s2, new Vector2f(),new Vector2f() );
		j1.setCompressedSpringConst(100);
		world.add(j1);

		
	}
	
	public Body drawLine(Line l1)
	{
		Body l;
		ArrayList<Point> points=l1.getPoints();
		if(l1.isFixed())
			{
				l=new StaticBody(new net.phys2d.raw.shapes.Line(points.get(0).getX(), points.get(0).getY(), points.get(1).getX(),points.get(1).getY() ));
			}
			else
				l=new Body(new net.phys2d.raw.shapes.Line(points.get(0).getX(), points.get(0).getY(), points.get(1).getX(),points.get(1).getY() ), 100.0f);
			
		return l;
	
	}
	
	public static void main(String[] args)
	{
		Phys2dSimulation sim=new Phys2dSimulation();
		sim.start();
	}

}
