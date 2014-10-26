package XMLReading;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.xpath.*;

public class ReadXML{
	
	private Document dom;
	
	private ArrayList<CircleStruct> circle=new ArrayList<CircleStruct>();
	private ArrayList<Line> line=new ArrayList<Line>();
	private ArrayList<Triangle> triangle=new ArrayList<Triangle>();
	private ArrayList<Rectangle> rectangle=new ArrayList<Rectangle>();
	private ArrayList<Polygons> poly=new ArrayList<Polygons>();

	private boolean gravity;

	private ArrayList<Point> points;

	private boolean totable;
	
	public ArrayList<CircleStruct> getCircles()
	{
		return circle;
	}
	
	public ArrayList<Line> getLines()
	{
		return line;
	}
	
	public ArrayList<Triangle> getTriangles()
	{
		return triangle;
	}
	
	public ArrayList<Rectangle> getRects()
	{
		return rectangle;
	}
	
	public boolean getGravity()
	{
		return gravity;
	}
	
	public boolean getTotable()
	{
		return totable;
	}
	
	public ReadXML()
	{
		
		//get the factory
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		
		try
		{
			//Using factory get an instance for document builder
			DocumentBuilder db=dbf.newDocumentBuilder();
			
			//parse the file using builder to get the DOM representation of the XML File
			dom=db.parse("shapes.xml");
			
		}catch(ParserConfigurationException pce)
		{
			pce.printStackTrace();
		}
		catch(SAXException se)
		{
			se.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		points=new ArrayList<Point>();
	}
	
	public  void readScenario()
	{
		Element docEle = dom.getDocumentElement();
		readShape(docEle);
		if(readConstraints(docEle).contains("true"))
		{
			gravity=true;
		}
		else gravity=false;
		
	}
	
	
	public void readShape(Element docEle)
	{
		XMLReading.CircleStruct cir;
		Triangle tri;
		Polygons polygon;
		Line ln;
		Rectangle rect;
		//save all the shapes in the ArrayList and return them
		ArrayList<Shape> shapes=new ArrayList<Shape>();
		// get the Shape in nodeLists
		String shape;
		NodeList nl=docEle.getElementsByTagName("shape");
		if(nl!=null)
		{
			Shape sh;
			for(int i=0; i<nl.getLength(); i++)
			{
				
				Element e=(Element) nl.item(i);
				shape=readName(e);
				System.out.println(shape);
				if(shape.contains("line"))
				{
					String cons=readShapeConstraints(e);
					if(cons.contains("true"))
					{
						ArrayList<Point> point=readPoints(e);
						//float radius=getRadius(e);
						ln=new Line(point,shape, true);
						//sh.print();
						line.add(ln);
					}
					else if(cons.contains("false"))
					{
						ArrayList<Point> point=readPoints(e);
						//float radius=getRadius(e);
						ln=new Line(point,shape, false);
						//sh.print();
						line.add(ln);
					}
					
				}
				
				else 
				if (shape.contains("circle"))
				{
					
					String cons=readShapeConstraints(e);
					if(cons.contains("true"))
					{
						ArrayList<Point> point=readPoints(e);
						float radius=getRadius(e);
						cir=new XMLReading.CircleStruct(point,shape, true, radius);
						//sh.print();
						circle.add(cir);
					}
					else if(cons.contains("false"))
					{
						ArrayList<Point> point=readPoints(e);
						float radius=getRadius(e);
						cir=new CircleStruct(point,shape, false, radius);
						//sh.print();
						circle.add(cir);
					}
					
				}
				
				else if(shape.contains("triangle"))
				{
					
					String cons=readShapeConstraints(e);
					if(cons.contains("true"))
					{
						ArrayList<Point> point=readPoints(e);
						tri=new Triangle(point,shape, true);
						//sh.print();
						triangle.add(tri);
					}
					else if(cons.contains("false"))
					{
						ArrayList<Point> point=readPoints(e);
						tri=new Triangle(point,shape, false);
						//sh.print();
						triangle.add(tri);
					}
				
					
				}
				
				else if(shape.contains("polygon"))
				{
					
					String cons=readShapeConstraints(e);
						ArrayList<Point> point=readPoints(e);
						polygon=new Polygons(point,shape, cons.contains("true"));
						//sh.print();
						poly.add(polygon);
				}
					
				else if(shape.contains("rectangle"))
				{
					
					String cons=readShapeConstraints(e);
						ArrayList<Point> point=readPoints(e);
						rect=new Rectangle(point,shape, cons.contains("true"));
						//sh.print();
						rectangle.add(rect);
					
				}
				
				
			}
			
		}
		
		
		
	}
	
	public String readName(Element e)
	{
		NodeList nl=e.getElementsByTagName("name");
		Element e1=(Element) nl.item(0);
			System.out.println(e1.getAttribute("value"));
			return e1.getAttribute("value");
		
		
	}
	
	public ArrayList<Point> readPoints(Element e)
	{
		
		NodeList nl=e.getElementsByTagName("points");
			Element e1=(Element) nl.item(0);
			System.out.println(e1.getTagName());
			ArrayList<Point> p1=readPoint(e1);
			
			return p1;
		
	}
	
	public float getRadius(Element e)
	{
		NodeList nl=e.getElementsByTagName("points");
			Element e1=(Element) nl.item(0);
			System.out.println(e1.getTagName());
			NodeList n2=e.getElementsByTagName("point");
			Element e2=(Element) n2.item(0);
			//float rad=60.0f;
			float rad=Float.parseFloat(e2.getAttribute("radius"));
			System.out.println("Radiuus Being printed : "+ rad);	
			
			return rad;
		
	}
	
	public ArrayList<Point> readPoint(Element e)
	{
		ArrayList<Point> p1=new ArrayList<Point>(); 
		NodeList nl=e.getElementsByTagName("point");
		for(int i=0; i<nl.getLength(); i++)
		{
			Element e1=(Element) nl.item(i);
			Point p=new Point(Float.parseFloat(e1.getAttribute("x")), Float.parseFloat(e1.getAttribute("y")));
			p1.add(p);
		}
		return p1;
	}
	
	public String readShapeConstraints(Element e)
	{
		NodeList nl=e.getElementsByTagName("constraints");
			Element e1=(Element) nl.item(0);
			String value=readStatic(e1);
			return value;
		
		
	}
	
	public String readConstraints(Element e)
	{
		NodeList nl=e.getElementsByTagName("scenario-constraints");
		Element e1=(Element) nl.item(0);
		//totable=readTotable(e1);
		return readGravity(e1);
		
	}
	
	public String readStatic(Element e)
	{
		NodeList nl=e.getElementsByTagName("static");
		Element e1=(Element) nl.item(0);
			return e1.getAttribute("value");
	}
	
	public String readGravity(Element e)
	{
		NodeList nl=e.getElementsByTagName("gravity");
			Element e1=(Element) nl.item(0);
		return e1.getAttribute("value");
	}
	
	public boolean readTotable(Element e)
	{
		NodeList nl=e.getElementsByTagName("totable");
			Element e1=(Element) nl.item(0);
			if(e1.getAttribute("value").contains("true"))
			{
				return true ;
			}
			else return false;
	}

	public ArrayList<Polygons> getPolygons() {
		// TODO Auto-generated method stub
		return poly;
	}

	
}
