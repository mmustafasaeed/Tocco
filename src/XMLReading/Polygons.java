package XMLReading;

import java.util.ArrayList;

import pphys2d.bodies.PPLine;
import pphys2d.bodies.PPPoly;

public class Polygons extends Shape {
	
	public Polygons(ArrayList<Point> p1, String name, boolean fix)
	{
		super(name, fix);
		
		points=p1;
		
		print();
	}
	
	public ArrayList<Point> getPoints()
	{
		return points;
	}
	
	public PPPoly draw()
	{
		PPPoly poly=new PPPoly();
		for(int i=0; i<points.size(); i++)
		{
			poly.vertex((int)points.get(i).getX(), (int)points.get(i).getY());
		}
			
		poly.setPosition(180, 10);
		return poly;
	}

}

