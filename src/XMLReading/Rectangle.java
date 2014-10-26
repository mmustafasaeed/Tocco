package XMLReading;

import java.util.ArrayList;

import pphys2d.bodies.PPBox;

public class Rectangle extends Shape {
	
	public float width;
	public float length;
	
	java.awt.Rectangle delegate = new java.awt.Rectangle();
	
	
	public Rectangle(ArrayList<Point> p1, String name, boolean fix) {
		super(name, fix);
		if(p1.size()==4)
		{
			points=p1;
			
		}
		else 
		{
			System.out.println("Invalid number of Points given.");
		}
		print();
		delegate.setLocation((int)p1.get(0).getX(), (int)p1.get(0).getY());
		for(Point p:p1) {
			delegate.add(p.getX(), p.getY());
		}
	}
	
	/*public void arrange()
	{
		Distance[] dis=getDistances();
		
		//to get the lowest distance variable at the zeroth index  
		for(int i=1; i<dis.length; i++)
		{
			Distance temp;
			if(dis[i].getDistance()<dis[0].getDistance())
			{
				temp=dis[i];
				dis[i]=dis[0];
				dis[0]=temp;
			}
		}
		
		//to get the second lowest variable at the 1th index
		for(int i=2; i<dis.length; i++)
		{
			Distance temp;
			if(dis[i].getDistance()<dis[1].getDistance())
			{
				temp=dis[i];
				dis[i]=dis[1];
				dis[1]=temp;
			}
		}
		
		//save the approximate lengths in first and third slot of the array
		
		dis[0].setDistance((dis[0].getDistance()+dis[1].getDistance())/2);
		dis[2].setDistance((dis[2].getDistance()+dis[3].getDistance())/2);
		
		
		//setting width and lengths
		if(dis[0].getFlagX() && !dis[2].getFlagX())
		{
			width=dis[0].getDistance();
			length=dis[2].getDistance();
		}
		else
		{
			width=dis[2].getDistance();
			length=dis[0].getDistance();
		}
		
		
		for(int i=0; i<dis.length; i++)
		{
			dis[i].print();
		}
		
	}
	
	public Distance[] getDistances()
	{
		Distance[] dis=new Distance[4];
		for(int i=0; i<points.size(); i++)
		{
			dis[i]=new Distance(points.get(i),points.get((i+1)%points.size()));
		}
		
		return dis;
	}
	
	public float getDistance(Point p1, Point p2)
	{
		Distance d=new Distance(p1, p2);
		return d.getDistance();
	}*/
	
	public float getWidth() {
		return delegate.width;
	}
	
	public float getLength() {
		return delegate.height;
	}
	
	public float getX() {
		return delegate.x;
	}
	
	public float getY() {
		return delegate.y;
	}
	
	public ArrayList<Point> getPoints()
	{
		return points;
	}
	
	public PPBox draw() {
//		arrange();
		PPBox box= new PPBox(getWidth(), getLength());
		box.setPosition(getX(), getY());
		if(isFixed()) {
			box.setStaticBody(true);
		}
		box.setRestitution(1.0f);	
		return box;
	}
	
	/*private int findSmallest(float[] dis) {
		int index=0;
		for(int i=0; i<dis.length; i++) {
			for(int j=i; j<dis.length; j++)
			{
				if(dis[i]>dis[(i+1)% dis.length] && dis[i]!=dis[index])
				{
					index=(i+1) % dis.length;
				}
			}
			
		}
		return index;
	}*/
}
