package XMLReading;

import java.util.ArrayList;

import pphys2d.bodies.PPConvexPoly;
import pphys2d.bodies.PPPoly;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.ConvexPolygon;

public class Triangle extends Shape{
	//shape has the point arraylist. Just add how to manage it according to picture.
	
	public Triangle(ArrayList<Point> p1, String name, boolean fix)
	{
		super(name, fix);
		if(p1.size()==3)
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
	
	public PPPoly draw()
	{
		PPPoly tripoly=new PPPoly();
		tripoly.vertex((int)points.get(0).getX(), (int)points.get(0).getY());
		tripoly.vertex((int)points.get(1).getX(), (int)points.get(1).getY());
		tripoly.vertex((int)points.get(2).getX(), (int)points.get(2).getY());
		tripoly.setPosition(180, 10);
		return tripoly;
		
	}
	
	
	
}
