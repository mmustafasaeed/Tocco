
package XMLReading;

public class Distance {

	private Point p1;
	private Point p2;
	private float dis;
	private boolean flagX=false;
	
	public Distance(Point pp, Point pq)
	{
		p1=pp;
		p2=pq;
		dis=findDistance();
	}
	
	public float findDistance()
	{
		float distance=(float) (Math.sqrt((Math.pow((p1.getX()-p2.getX()),2)+Math.pow((p1.getY()-p2.getY()),2))));
		System.out.println("The distance between :" +p1+ " and " +p2+" is :" +distance);
		
		if((Math.pow((p1.getX()-p2.getX()),2)>Math.pow((p1.getY()-p2.getY()),2)))
		{
			flagX=true;
		}
		
		return distance;
	}
	
	public void setDistance(float distance)
	{
		dis=distance;
	}
	
	public boolean getFlagX()
	{
		return flagX;
	}
	
	public float getDistance()
	{
		return dis;
	}
	
	public void print()
	{
		System.out.println("Distance:");
		System.out.println("Point1: ("+ p1.getX()+", "+p1.getY()+")");
		System.out.println("Point2: ("+ p2.getX()+", "+p2.getY()+")");
		System.out.println("Distance: "+ dis+ "FlagX:"+ flagX );
	}
	
}
