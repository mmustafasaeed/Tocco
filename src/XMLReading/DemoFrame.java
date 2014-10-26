package XMLReading;

import javax.swing.*;
import java.awt.*;

public class DemoFrame {

	public static void main(String[] args) {
		JFrame frame = new JFrame("FrameDemo");

		// Create components and put them in the frame
		Demo demo = new Demo();
		demo.init();
		demo.start();
		frame.getContentPane().add(demo, BorderLayout.CENTER);

		// Frame configuration
		frame.setSize(500,540);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Show it
		frame.setVisible(true);
	}
}
