
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Draw {

	static final short width = 800;
	static final short height = 600;
	static ImageIcon img;
	static Image imageForSplashScreen;  
	static ImageIcon ii; 
	private static void createAndShow() {
		
		// Create frame
		JFrame f = new JFrame("Tocco: The Multi Touch Simulation Framework");
		//f.setIconImages((List<? extends Image>) new ImageIcon("images/animator.png"));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocation(565, 190);
		
		// Main drawing class
		withMouse wm = new withMouse(width, height, f);
		wm.setFocusable(true);
		
		f.setContentPane(wm);
		f.pack();
		f.setVisible(true);
	}
	
	public static void main(String[] args)  {
		 
		JWindow window = new JWindow();
		 JPanel pane=new JPanel(new BorderLayout());
		  //img=new ImageIcon("images/Tocco Splash Screen.png");
		  JLabel label=new JLabel("Loading Tocco");
		  label.setHorizontalAlignment(JLabel.CENTER);
		  pane.add(label);
		//JPanel panel1=new JPanel(new);
		 //Set JWindow size from image size  
		  //window.
		// window.setSize(ii.getIconWidth(),ii.getIconHeight());  
		 
        window.getContentPane().add(pane);
                        
        //window.getContentPane().add(new ImageIcon("images/Tocco Splash Screen.png"));
        //window.getContentPane().
        window.setBounds(400, 300, 200, 100);
        
        window.setVisible(true);
        try {
                Thread.sleep(5000);
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
        window.setVisible(false);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				createAndShow();
			}
		});
	}
	
	
	  }
	
	
