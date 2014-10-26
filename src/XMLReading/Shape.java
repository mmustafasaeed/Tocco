package XMLReading;
import java.util.ArrayList;

import net.phys2d.raw.Body;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Shape {
	
	protected boolean fixed; 
	protected String name;
	protected ArrayList<Point> points=new ArrayList<Point>();
	
	public Shape(String name1, boolean fix)
	{
		name=name1;
		fixed=fix;
	}
	
	public Shape()
	{
		name="";
	}
	public void setName(String name2)
	{
		name=name2;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setFixed(boolean b)
	{
		fixed=b;
	}
	
	public boolean isFixed()
	{
		return fixed;
	}
	
	public void print()
	{
		System.out.println("The name of the Object is :"+ name);
		if(fixed)
		{
			System.out.println("The body is static");
		}
		else
			System.out.println("The body is not static");
		
		System.out.println("The list of Points is:");
		for (int i=0; i<points.size(); i++)
		{
			points.get(i).print();
		}
	}
	
	
	
	
	
	

}
