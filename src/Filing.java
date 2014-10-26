
/*
 * *
 * Written by 
 * Maheen Khalid
 * 
 * This class caters the making of xml files from the figures being drawn.Each point and a line
 * or a cpologon and ellispe data goes here. This is the core for the CUS section of the
 */
import java.awt.Polygon;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * 
 * Written By Maheen Khalid
 * */
 
public class Filing {
	
	protected File shapes;
	protected BufferedWriter out;
	protected int lineCount=0;
	protected int ellipseCount=0;
	
	
	
	
	boolean gravity;
	boolean totable;
	
	public Filing()
	{
		try{
			shapes=new File("shapes.xml");
			out = new BufferedWriter(new FileWriter(shapes));
			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.write("<shapes>");
			//Point2D pP=new Point2D();
			
			
			

		}
		catch(IOException ex)
		{
			System.out.println("Please there is some problem");
		}
		System.out.println("Application done");
		
	}
	
	public void writeScenarioConstraints()
	{
		try{
			
			out.write("\n<scenario-constraints>" +
					"<gravity value=\""+gravity+"\" />" +
					"<totable value=\""+totable+"\" />" +
					"\n</scenario-constraints>");
			
		}catch(IOException ex)
		{
			System.out.println(ex);
		}
	}
	
	public String lineOrientation(int initialX,int initialY,int finalX,int finalY)
	{
		if(finalX-initialX<=5 && finalX-initialX>=-5 )
		{
			System.out.println("vertical line");
			return "vertical";
		}
		else if(finalY-initialY<=5 && finalY-initialY>=-5 )
		{
			System.out.println("horizontal line");
			return "horizontal";
		}else 
			{
			System.out.println("Slanting Line");
			return "slanting";
			}
	}

	public void drawLine(Lines line)
	{
		try{
			out.write("\n<shape>" +
					"\n<name value=\"line\"/>" +
					"\n<points>" +
					"\n<point x=\""+line.getLine().x1+"\" y=\""+line.getLine().y1+"\" radius=\"null\" />" +
					"\n<point x=\""+line.getLine().x2+"\" y=\""+line.getLine().y2+"\" radius=\"null\" />" +
					"\n</points>" +
					"<constraints>" +
					"<static value=\""+line.getState()+ "\"/>" +
					"</constraints>" +
					"</shape>");
		}catch(IOException ex)
		{
			System.out.println("Please there is some problem");
		}
	}
	
	
	
	public void drawCircle(int xAxis, int yAxis, int radius, boolean state)
	{
		try{
			out.write("\n<shape>" +
					"\n<name value=\"circle\"/>" +
					"\n<points>" +
					"\n<point x=\""+xAxis+"\" y=\""+yAxis+"\" radius=\""+radius+"\" />" +
					"\n</points>" +
					"<constraints>" +
					"<static value=\""+state+ "\"/>" +
					"</constraints>" +
					"</shape>");
		}catch(IOException ex)
		{
			System.out.println("Please there is some problem");
		}
	}
	
	public void drawRectangle(int x1,int y1,
			int x2,int y2,
			int x3,int y3,
			int x4,int y4, boolean state)
	{
		try{
			out.write("\n<shape>" +
					"\n<name value=\"rectangle\"/>" +
					"\n<points>" +
					"\n<point x=\""+x1+"\" y=\""+y1+"\" radius=\"null\" />" +
					"\n<point x=\""+x2+"\" y=\""+y2+"\" radius=\"null\" />" +
					"\n<point x=\""+x3+"\" y=\""+y3+"\" radius=\"null\" />" +
					"\n<point x=\""+x4+"\" y=\""+y4+"\" radius=\"null\" />" +
					"\n</points>" +
					"<constraints>" +
					"<static value=\""+state+ "\"/>" +
					"</constraints>" +
					"</shape>");
			
		}catch(IOException ex)
		{
			System.out.println("Please there is some problem");
		}
	}

	public void drawPolygon(Polygons polygon)
	{
		Polygon poly=polygon.getPolygon();
		
		System.out.println("The nPoingts are: "+poly.npoints);
		
		if(poly.npoints<=4 )
		{
			try
			{
				out.write("\n<shape>" +
						"\n<name value=\"triangle\" />" +
						"\n<points>");
				
				for(int i=0; i<3; i++)
				{
					out.write("\n<point x=\""+poly.xpoints[i]+"\" y=\""+poly.ypoints[i]+"\" />");
				}
				
				out.write("\n</points>" +
						"\n<constraints>" +
						"\n<static value=\""+polygon.getState()+"\" />" +
						"\n</constraints>" +
						"\n</shape>");
				
			}catch(IOException ex)
			{
				System.out.println(ex);
			}
		}
		
		else
		{
			try
			{
				out.write("\n<shape>" +
						"\n<name value=\"polygon\" />" +
						"\n<points>");
				
				for(int i=0; i<poly.npoints; i++)
				{
					out.write("\n<point x=\""+poly.xpoints[i]+"\" y=\""+poly.ypoints[i]+"\"  radius=\"null\"/>");
				}
				
				out.write("\n</points>" +
						"\n<constraints>" +
						"\n<static value=\""+polygon.getState()+"\" />" +
						"\n</constraints>" +
						"\n</shape>");
				
			}catch(IOException ex)
			{
				System.out.println(ex);
			}
		}
			
	}
	
	public void closeFile()
	{
		try{
			writeScenarioConstraints();
			out.write("\n</shapes>");
			out.close();
		}catch(IOException ex)
		{
			System.out.println("Please there is some problem");
		}
	}

	public void setGravity(boolean b) {
		if(b)
		{
			gravity=true;
			System.out.println("gravity is true");
		}
		else
			gravity=false;
		
	}

	public void setTotable(boolean b) {
		if(b)
		{
			totable=true;
			System.out.println("Totable is true");
		}
		else
			totable=false;
		
	}


	
	
	
	
}
