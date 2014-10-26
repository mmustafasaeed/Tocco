
import java.io.*;
import java.util.*;

public class Scene {

	private boolean gravity;
	
	private ArrayList<ShapePanel> shapes = new ArrayList<ShapePanel>();
	
	public void setGravity(boolean gravity) {
		this.gravity = gravity;
	}
	
	public boolean isGravity() {
		return gravity;
	}
	
	public void addShape(ShapePanel shape) {
		shapes.add(shape);
	}
	
	public ArrayList<ShapePanel> getShapes() {
		return shapes;
	}
	
	public void write(OutputStream out) throws Exception {
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
		out.write("<shapes>".getBytes());
		for(ShapePanel shape: shapes) {
			out.write(shape.getXML().getBytes());
		}
		out.write(("\n<scenario-constraints>\n<gravity value=\""+gravity+"\" />\n</scenario-constraints>").getBytes());
		out.write("\n</shapes>".getBytes());
	}
	
	public void write(File file) throws IOException {
		FileOutputStream fileout = new FileOutputStream(file);
		try {
			write(fileout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileout.close();
	}
}