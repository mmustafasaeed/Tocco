package XMLReading;

import java.awt.BorderLayout;

import javax.swing.JFrame;


public class Main {
	
	public Main()
	{
		JFrame frame = new JFrame("FrameDemo");

		// Create components and put them in the frame
		Simulation demo = new Simulation();
		demo.init();
		demo.start();
		frame.getContentPane().add(demo, BorderLayout.CENTER);

		// Frame configuration
		frame.setSize(800,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Show it
		frame.setVisible(true);
	}
	public static void main(String[] args)
	{
		Main m=new Main(); 
	}
	

}
