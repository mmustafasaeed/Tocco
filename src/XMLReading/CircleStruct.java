package XMLReading;

import java.util.ArrayList;

import pphys2d.bodies.PPCircle;
import processing.core.PApplet;
import processing.core.PImage;

import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;

public class CircleStruct extends Shape  {
	
	private float radius;
	PImage material;
	PPCircle circle;
	
	public CircleStruct(ArrayList<Point> p1, String name, boolean fix, float rad)
	{
		super(name, fix);
		if(p1.size()==1)
		{
			points=p1;
			radius=rad;
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

	public float getRadius()
	{
		return radius;
	}
	
	public void setRadius(int rad)
	{
		radius=rad;
	}
	
	
	public void print()
	{
		//super.print();		
		System.out.println("Radius:"+radius);
	}
	
	public PPCircle draw()
	{
		//material=loadImage("");
		circle=new PPCircle(radius/2);
		if(isFixed())
		{
			circle.setStaticBody(true);
		}
		circle.setPosition(getPoints().get(0).getX(), getPoints().get(0).getY());
		//circle.setFriction(0);
		circle.setRestitution(1.0f);
		//circle.attachImage(PApplet.loadImage("images/glass-pane.jpg"));
		return circle;
	}
}
