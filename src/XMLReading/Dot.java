package XMLReading;

import processing.core.PApplet;

public class Dot extends PApplet{


		  private float x;    // x position
		  private float y;    // y position
		  private float dx;   // x displacement
		  private float dy;   // y displacement
		  int   c;    // color (gray value)
		  float dc;   // color change
		  float s;    // size
		  float ds;   // size change
		  int   health; // when health == 0 => fade out
		  int   a;      // alpha value
		   
		  public Dot() {
		    reset();
		  }
		   
		  private void reset() {
			   
		    x  = (float) (Math.random()* (width-0));
		    y  = (float) ((0.45*height)+(float) (Math.random()*(0.55*height-0.45*height)));
		    dx = (float) ((float) (-0.5)+((Math.random()*(0.5-(- 0.5)))));
		    dy = (float) (-0.25+(Math.random()*(0.25-(-0.25))));
		    c  = (int)(127+(Math.random()*(255-127)));
		    dc = (float) (-10.0 + Math.random()*(10.0-(-10.0)));
		    s  = (float) 5.0;//random(1.0, 7.5);
		    ds = (float) (-0.25+Math.random()*(0.25-(-0.25)));
		     
		    health = (int)(100+Math.random()*(500-100));
		    a = 240;
		  }
		   
		  private boolean isOffscreen() {
		    return x < -s || x > width+s || y < -s || y > height+s;
		  }
		   
		  public void move() {
		    x = x + dx;
		    y = y + dy;
		     
		    if( health > 0 ) {
		      health--;
		    } else {
		      a = a - 5;
		    }
		 
		    if( isOffscreen() || a<=0 ) {
		      reset();
		    }
		     
		    if( c+dc < 127 || c+dc >= 256 ) {
		      dc = -dc;
		    }
		    c = (int)(c + dc);
		     
		    if( s+ds < 3.5 || s+ds > 15.0 ) {
		      ds = -ds;
		    }
		    s = s + ds;
		  }
		   
		  public void draw() {
		    fill(c-32, c-32, 255, a);
		    ellipse(x, y, s, s);
		  }
		

}
