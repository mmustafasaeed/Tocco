package XMLReading;

import java.util.ArrayList;

import pphys2d.bodies.PPLine;

import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;

public class Line extends Shape {
	
	public Line(ArrayList<Point> p1, String name, boolean fix)
	{
		super(name, fix);
		if(p1.size()==2)
		{
		points=p1;
		}
		else 
		{
			System.out.println("Invalid number of Points given.");
		}
		print();
	}
	
	public ArrayList<Point> getPoints()
	{
		return points;
	}
	
	public PPLine draw()
	{
		PPLine line=new PPLine(points.get(0).getX(), points.get(0).getY(), (points.get(1).getX()-points.get(0).getX()), (points.get(1).getY()-points.get(0).getY()));
		if(isFixed())
		{
			line.setStaticBody(true);
		}
		line.setRestitution(2.0f);
		return line;
	}

}
