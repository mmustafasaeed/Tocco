package XMLReading;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Random;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Circle;
import pphys2d.PPWorld;
import pphys2d.bodies.PPBox;
import pphys2d.bodies.PPCircle;
import pphys2d.bodies.PPLine;
import pphys2d.bodies.PPPoly;
import processing.core.PApplet;
import processing.core.PImage;

public class Simulation extends PApplet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean gravity;
	private boolean totable;
	private PPWorld world= new PPWorld();

	private ArrayList<CircleStruct> circle=new ArrayList<CircleStruct>();
	private ArrayList<Line> line=new ArrayList<Line>();
	private ArrayList<Triangle> triangle=new ArrayList<Triangle>();
	private ArrayList<Rectangle> rectangle=new ArrayList<Rectangle>();
	private ArrayList<Polygons> poly=new ArrayList<Polygons>();
	//PImage background;
	TotableStructure ts=new TotableStructure();
	PImage background;
	PImage material;
	
	public Simulation()
	{
		ReadXML read=new ReadXML();
		read.readScenario();
		
		gravity=read.getGravity();
		totable=read.getTotable();
		
		circle=read.getCircles();
		line=read.getLines();
		triangle=read.getTriangles();
		rectangle=read.getRects();
		poly=read.getPolygons();
		
	}
	
	public void setup()
	{
		//Set size and framerate
		background=loadImage("images/back.jpg");
		
		//background.height=600;
		//.6background.width=800;
		  frameRate(5000);
		  size(800,600);
		 // background=loadImage("woodknots.jpg");
		if(gravity)
		{
			world.setGravity(0, 10);
		}
		
		if(totable)
		{
			
			world.add(ts.getBox1());
			world.add(ts.getBox2());
			world.add(ts.getBox3());
			world.add(ts.getBox4());
			world.add(ts.getBox5());
			world.add(ts.getBox6());
			
		}
		for(int i=0; i<circle.size(); i++)
		{
			//material=loadImage("images/glass-pane.jpg");
			
			CircleStruct cir=(CircleStruct)circle.get(i);
				PPCircle cir1=cir.draw();
				cir1.setRestitution(1.0f);
				//cir1.attachImage(material);
			//	cir1.set
				world.add(cir1);
			
		}
		for(int i=0; i<line.size(); i++)
		{
			Line ln=(Line)line.get(i);
			
				world.add(ln.draw());
		}
		for(int i=0; i<triangle.size(); i++)
		{
				//material=loadImage("images/glass-pane.jpg");
				Triangle tri=(Triangle)triangle.get(i);
				PPPoly tri1=tri.draw();
				//tri1.attachImage(material);
				world.add(tri1);
		}
		for(int i=0; i<rectangle.size(); i++)
		{
				material=loadImage("images/glass-pane.jpg");
				Rectangle rect=(Rectangle)rectangle.get(i);
				PPBox rect1=rect.draw();
				rect1.attachImage(material);
				world.add(rect.draw());
		}
		for(int i=0; i<poly.size(); i++)
		{
				Polygons ply=(Polygons)poly.get(i);
				world.add(ply.draw());
		}
		PPBox line1=new PPBox(1600, 10);
		line1.setPosition(0, 580);
		line1.setStaticBody(true);
		line1.setRestitution(2.0f);
	//PPLine base1=new PPLine(0.0f,30.0f,300.0f,100.0f);
		//base1.setPosition(0, 500);
		world.add(line1);
		//world.add(base1);
		//PPBox line2=new PPLine(1600, 10);
		
		
	}
	
	private Random random=new Random();
	
	protected void keyHit(char c)
	{
		
		PPCircle newBody;
		if(c!='r')
		if(c=='c')
		{
			newBody = new PPCircle(30);
			newBody.setPosition(10, (float) (150+(Math.random()*(400-150))));
			newBody.setRestitution(1.0f);
			newBody.setRotation(0.5f);
			newBody.adjustVelocity(250,0);
			newBody.setForce(25, 25);
			newBody.setRotation((float) (random.nextFloat() * 2 * Math.PI));
			world.add(newBody);
		}
	}
	
	public void draw()
	{
		  //Clear screen
		//image(background, 0, 0);
		
		//background(255); 
		  //Draw world
		image(background,0,0);
		world.draw(this);
	}

}
