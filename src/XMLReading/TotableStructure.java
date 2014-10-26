package XMLReading;

import pphys2d.bodies.PPBox;
import processing.core.PApplet;
import processing.core.PImage;
import net.phys2d.raw.Body;

import net.phys2d.raw.shapes.Box;

public class TotableStructure extends PApplet{
	
	//is class mein woh sara structure banay ga.

	PPBox box1;
	PPBox box2;
	PPBox box3;
	PPBox box4;
	PPBox box5;
	PPBox box6;
	PImage image;
	
	
	
	public TotableStructure()
	{
		
		box1=new PPBox(30.0f, 60.0f);
		box1.setPosition(250, 400);
		
		box2=new PPBox(30.0f, 60.0f);
		box2.setPosition(290, 400);
		
		box3=new PPBox(150.0f, 15.0f);
		box3.setPosition(260, 340);
		
		box4=new PPBox(30.0f, 60.0f);
		box4.setPosition(250, 325);
		
		box5=new PPBox(30.0f, 60.0f);
		//box5.attachImage(loadImage("woodknots.jpg"));
		box5.setPosition(290, 325);
		
		box6=new PPBox(150.0f, 15.0f);
		box6.setPosition(260, 265);
	}
	
	

	public PPBox getBox1()
	{
		image=loadImage("images/woodknots.jpg");
		image.width=30;
		image.height=60;
		box1.attachImage(image);
		return box1;
	}
	
	public PPBox getBox2()
	{
		return box2;
	}
	
	public PPBox getBox3()
	{
		return box3;
	}
	
	public PPBox getBox4()
	{
		return box4;
	}
	
	public PPBox getBox5()
	{
		return box5;
	}
	
	public PPBox getBox6()
	{
		return box6;
	}
	
	
}
