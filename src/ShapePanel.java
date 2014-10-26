

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public abstract class ShapePanel extends JComponent implements FocusListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int offset = 5;
	boolean seleted = false;
	JTextPane textpane;
	JScrollPane scrollpane;
	Color drawcolor = Color.BLACK;
	Color fillcolor = Color.WHITE;
	
	public ShapePanel () {
		super();
		this.
		setLayout(null);  // Disable layout manager
		setOpaque(false);  // Set transparent background
	}

	public void setDrawColor(Color c) {
		drawcolor = c;
		repaint();
	}
	
	public void setFillColor(Color c) {
		fillcolor = c;
		repaint();
	}
	
	public int isResize(int x, int y) {
		return -1;
	}
	
	public void moveTo(int deltaX, int deltaY) {
	}
	
	public void reSize(int j, int deltaX, int deltaY) {
	}
	
	public boolean addText() {
		
		textpane = new JTextPane();
		textpane.setOpaque(false);  // Set transparent background
        textpane.addFocusListener(this);
		StyledDocument doc = textpane.getStyledDocument();
		
		SimpleAttributeSet attr = new SimpleAttributeSet();  // Paragraph attributes
		StyleConstants.setFontSize(attr, 12);
		StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), attr, true);
        
        scrollpane = new JScrollPane(textpane);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollpane.setBorder(null);
        scrollpane.getViewport().setOpaque(false);  // Set viewport to be transparent
        
        if (fillcolor != null)
        	scrollpane.setBackground(fillcolor);
        else
        	scrollpane.setOpaque(false);
        
        this.add(scrollpane);
        
        return true;
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		seleted = true;
		withMouse.at = this;
		repaint();
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		seleted = false;
		repaint();
	}
	
	public abstract String getXML();
}