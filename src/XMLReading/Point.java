package XMLReading;

public class Point {
	
	private float x;
	private float y;
	
	public Point()
	{
		x=0;
		y=0;
	}
	
	public Point(float x1, float y1)
	{
		x=x1;
		y=y1;
	}
	
	public void setX(float x1)
	{
		x=x1;
	}

	public void setY(float y1)
	{
		y=y1;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void print()
	{
		System.out.println("X:"+x+" Y:"+y);
	}
	
}

