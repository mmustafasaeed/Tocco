

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
//import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;

import XMLReading.Main;

public class withMouse extends JPanel implements MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPopupMenu menu;
	JComponent temp;
	static ShapePanel at;
	ShapePanel paste;
	JFrame parent;
	Component glasspane;
	Polygon shape;  // Original shape
	RecoProc Rec;
	int MenuBarH, ctrl;  // Height of MenuBar & Control point index
	boolean newShape, dragged, atthis, atMenu, copied;  // Operation indicator
	int oldposx, oldposy, newposx, newposy;  // Mouse position
	ArrayList<ShapePanel> deleted = new ArrayList<ShapePanel>();  // All deleted shapes
	JButton animator;
	//Filing file=new Filing();
	Scene scene=new Scene();
	
	public withMouse(short width, short height, JFrame f) {
		setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        setLayout(null);  // Disable layout manager
        addMouseListener(this);  // Prevent losing mouse events after initialising or visiting MenuBar
        addMouseMotionListener(this);
        
        parent = f;
        glasspane = parent.getGlassPane();
        glasspane.addMouseListener(this);
        glasspane.addMouseMotionListener(this);
        
        Menubar bar = new Menubar(this, f);
		System.out.println("Height of menubar : "+bar.getHeight());
		f.setJMenuBar(bar);
		
		animator=new JButton("Animate",new ImageIcon("images/animator.png"));
		
		animator.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(parent,"Welcome to the Animator");
				
				try {
					scene.write(new File("shapes.xml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				Main main=new Main();
			}
			
		}
		);
		
		bar.add(animator);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		if (newShape && (shape.npoints != 1))
			g.drawPolyline(shape.xpoints, shape.ypoints, shape.npoints);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		// Initialise fields
		newShape = false;
		dragged = false;
		atthis = false;
		atMenu = false;
		MenuBarH = parent.getJMenuBar().getHeight();
		
		newposx = e.getX();  // Get current mouse position
		newposy = e.getY();
		//System.out.println("("+newposx+" ,"+newposy+")");
		if (!e.getComponent().equals(this)) {
			if (newposy > MenuBarH)
				newposy -= MenuBarH;
			else {
				atMenu = true;
				glasspane.setVisible(false);
				Component bar = SwingUtilities.getDeepestComponentAt(
						parent.getJMenuBar(), newposx, newposy);
				//bar.dispatchEvent(e);
				return;
			}
		}
		else
			atthis = true;
		
		if (at != null) {
			at.seleted = false;
			this.requestFocusInWindow();
			at = null;
		}
		
		temp = (JComponent) this.getComponentAt(newposx, newposy);
		
		if (temp.equals(this)) {
			newShape = true;
			shape = new Polygon();
			shape.addPoint(newposx, newposy);
		}
		else {
			at = (ShapePanel) temp;
			ctrl = at.isResize(newposx, newposy);
			if (ctrl == -2) {
				newShape = true;
				shape = new Polygon();
				shape.addPoint(newposx, newposy);
				at = null;
			}
			else
				at.seleted = true;
		}
		
		repaint();
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		dragged = true;
		
		oldposx = newposx;  // Store old position
		oldposy = newposy;
		
		newposx = e.getX();  // Get current mouse position
		newposy = e.getY();
		//System.out.println("Old Position ("+oldposx+" , "+oldposy+")");
		//System.out.println("Old Position ("+newposx+" , "+newposy+")");
		if (!atthis) {
			newposy -= MenuBarH;
		}
		
		if (newShape) {
			shape.addPoint(newposx, newposy);
			repaint();
		}
		else {
			if (at != null && !atMenu) {
				if (ctrl == -1)  // Move
					at.moveTo(newposx - oldposx, newposy - oldposy);
				else  // Resize
					at.reSize(ctrl, newposx - oldposx, newposy - oldposy);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		if (atthis && dragged) {
			glasspane.setVisible(true);
		}
		
		if (newShape && (shape.npoints != 1)){
			Rec = new RecoProc(shape, scene);
			if (Rec.a != null)
				add(Rec.a, 0);
			else if (Rec.c != null)
				add(Rec.c, 0);
			else if (Rec.cc != null)
				add(Rec.cc, 0);
			else if (Rec.ds != null)
				add(Rec.ds, 0);
			else if (Rec.e != null)
				add(Rec.e, 0);
			else if (Rec.l != null)
				add(Rec.l, 0);
			else if (Rec.pg != null)
				add(Rec.pg, 0);
			else if (Rec.pl != null)
				add(Rec.pl, 0);
			else if (Rec.qc != null)
				add(Rec.qc, 0);
			else if (Rec.r != null)
				add(Rec.r, 0);
			else if (Rec.po != null)
				add(Rec.po, 0);
			else if (Rec.s != null)
				add(Rec.s, 0);
			Rec = null;
		}
		
		newShape = false;  // Prevent repaint the original drawing shape
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (atthis) {
			glasspane.setVisible(true);
		}
	
		if (e.getClickCount() == 1 && !newShape && at != null) {
			
			menu = new JPopupMenu();
			JMenu edit = new JMenu("Edit");  
			
			JMenuItem delete = new JMenuItem("Delete");
			delete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					delete();
				}});
			
			JMenuItem undo = new JMenuItem("Undo");
			undo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					undo();
				}});
			
			JMenuItem copy = new JMenuItem("Copy");
			copy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					copy();
				}});
			
			JMenuItem paste = new JMenuItem("Paste");
			paste.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					paste();
				}});
			
			edit.add(undo);
			edit.add(delete);
			edit.add(copy);
			edit.add(paste);
			
			JMenu addMaterial = new JMenu("Add Material"); 
			JMenuItem wood = new JMenuItem("Wood");
			delete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					
				}});
			wood.setIcon(new ImageIcon("//animator.gif"));
			JMenuItem paper = new JMenuItem("Paper");
			undo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
				}});
			
			JMenuItem glass = new JMenuItem("Glass");
			copy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
				}});
			
			JMenuItem rock = new JMenuItem("Rock");
			paste.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
				}});
			
			addMaterial.add(wood);
			addMaterial.add(paper);
			addMaterial.add(glass);
			addMaterial.add(rock);
			
			JMenu customise = new JMenu("Customise");  // Third menu - Customise

			JMenu color = new JMenu("Select Color for ");  // First submenu of Customise
			JMenuItem draw = new JMenuItem("Drawing ...");
			draw.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
			        Color newColor = JColorChooser.showDialog(parent, "Choose Drawing Color",
			        		(withMouse.at != null)?withMouse.at.drawcolor: Color.BLACK);
			        if (newColor != null && withMouse.at != null) {
			        	withMouse.at.setDrawColor(newColor);
			        }
				}});
			
			JMenuItem nodraw = new JMenuItem("Set Non-outline");
			nodraw.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
			        if (withMouse.at != null) {
			        	withMouse.at.setDrawColor(null);
			        }
				}});
			
			JMenuItem fill = new JMenuItem("Filling ...");
			fill.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
			        Color newColor = JColorChooser.showDialog(parent, "Choose Drawing Color",
			        		(withMouse.at != null)?withMouse.at.fillcolor: Color.BLACK);
			        if (newColor != null && withMouse.at != null) {
			        	withMouse.at.setFillColor(newColor);
			        }
				}});
			
			JMenuItem nofill = new JMenuItem("Set Transparent Background");
			nofill.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
			        if (withMouse.at != null) {
			        	withMouse.at.setFillColor(null);
			        }
				}});
			
			color.add(draw);
			color.add(nodraw);
			color.add(fill);
			color.add(nofill);
			customise.add(color);
			menu.add(edit);
			menu.add(customise);
			menu.add(addMaterial);
	      
	        this.addMouseListener(new MouseAdapter()
	        {
	            @Override
	            public void mouseReleased(MouseEvent e)
	            {
	                
	                    menu.show(e.getComponent(), e.getX(), e.getY());
	                
	            }
	        });
	        parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        //this.setSize(250, 200);
	        parent.setLocationRelativeTo(null);
			
			glasspane.setVisible(false);
		}}
	
public JDialog createFontDialog(JFrame f) {
		
		final JDialog font = new JDialog(f, "Choose Text Font", true);  // Create a dialog for choosing text font
		
		JPanel up = new JPanel();
		
		JPanel middle = new JPanel();    // Middle
		middle.setBorder(BorderFactory.createTitledBorder("Preview"));
		final JLabel sample = new JLabel("Sameple Text", JLabel.CENTER);
		sample.setFont(new Font("SansSerif", Font.PLAIN, 12));
		sample.setPreferredSize(new Dimension(380, 150));
		middle.add(sample);
		
		JPanel left = new JPanel();  // Left-up
		left.setBorder(BorderFactory.createTitledBorder("Font List"));
		final JList fontlist = new JList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		fontlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fontlist.setVisibleRowCount(5);
		fontlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				String previewfont = (String) fontlist.getSelectedValue();
				Font temp = sample.getFont();
				sample.setFont(new Font(previewfont, Font.PLAIN, temp.getSize()));
			}});
		
		JScrollPane listScroller = new JScrollPane(fontlist);
		listScroller.setPreferredSize(new Dimension(200, 130));
		left.add(listScroller);
		
		JPanel right = new JPanel(new BorderLayout());  // Right-up
		right.setBorder(BorderFactory.createTitledBorder("Text Size"));
		final JTextField size = new JTextField("12", 10);
		size.setHorizontalAlignment(JTextField.CENTER);
		size.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				String text = size.getText();
				if (text.compareTo("") == 0)
					return;
				try {
					int s = Integer.valueOf(text);
					Font temp = sample.getFont();
					sample.setFont(new Font(temp.getFamily(), Font.PLAIN, s));
				}
				catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(font,
						    "Error Input: Please only input NUMBERS",
						    "Error Input",
						    JOptionPane.ERROR_MESSAGE);
				}
			}});
		right.add(size);
		
		up.add(left);
		up.add(right);
		
		JPanel bottom = new JPanel();
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (withMouse.at != null) {
					if (withMouse.at.textpane != null) {
						StyleConstants.setFontFamily(withMouse.at.textpane.getInputAttributes(), sample.getFont().getFamily());
						StyleConstants.setFontSize(withMouse.at.textpane.getInputAttributes(), sample.getFont().getSize());
						withMouse.at.textpane.setCharacterAttributes(withMouse.at.textpane.getInputAttributes(), true);
					}
				}
				font.setVisible(false);
			}});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				font.setVisible(false);
			}});
		
		bottom.add(ok);
		bottom.add(cancel);
		
		font.add(up, BorderLayout.NORTH);
		font.add(middle, BorderLayout.CENTER);
		font.add(bottom, BorderLayout.SOUTH);
		
        font.pack();
        font.setLocationRelativeTo(f);
        font.setVisible(false);
		System.out.println(cancel.getSize());
        
		return font;
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void delete() {
		if (!newShape && at != null) {
			deleted.add(at);
			this.remove(at);
			repaint();
		}
	}
	
	public void undo() {
		if (deleted.size() >  0) {
			if (at != null)
				at.seleted = false;
			at = deleted.get(deleted.size()-1);
			this.add(at, 0);
			at.seleted = true;
			deleted.remove(deleted.size()-1);
			repaint();
		}
	}
	
	public void copy() {
		if (!newShape && at != null) {
			String s = at.getClass().getName();
			
			if (s.compareTo("Drawing.Arrows") == 0) {
				Arrows copy = (Arrows)at;
				paste = new Arrows(copy.origin.xpoints, copy.origin.ypoints);
			}
			else if (s.compareTo("Drawing.Circles") == 0) {
				Circles copy = (Circles) at;
				paste = new Circles(copy.origin.x, copy.origin.y, copy.origin.width, copy.getState());
			}
			else if (s.compareTo("Drawing.CubicCurves") == 0) {
				CubicCurves copy = (CubicCurves) at;
				paste = new CubicCurves(copy.origin.x1, copy.origin.y1, copy.origin.ctrlx1, copy.origin.ctrly1,
						copy.origin.ctrlx2, copy.origin.ctrly2, copy.origin.x2, copy.origin.y2);
			}
			else if (s.compareTo("Drawing.Datastores") == 0) {
				Datastores copy = (Datastores) at;
				paste = new Datastores(copy.origin.xpoints, copy.origin.ypoints);
			}
			else if (s.compareTo("Drawing.Ellipses") == 0) {
				Ellipses copy = (Ellipses) at;
				paste = new Ellipses(copy.origin.x, copy.origin.y, copy.origin.width, copy.origin.height);
			}
			else if (s.compareTo("Drawing.Lines") == 0) {
				Lines copy = (Lines) at;
				paste = new Lines(copy.origin.x1, copy.origin.y1, copy.origin.x2, copy.origin.y2, copy.getState());
			}
			else if (s.compareTo("Drawing.Polygons") == 0) {
				Polygons copy = (Polygons) at;
				paste = new Polygons(copy.origin.xpoints, copy.origin.ypoints, copy.origin.npoints, copy.getState());
			}
			else if (s.compareTo("Drawing.Polylines") == 0) {
				Polylines copy = (Polylines) at;
				paste = new Polylines(copy.origin.xpoints, copy.origin.ypoints, copy.origin.npoints);
			}
			else if (s.compareTo("Drawing.QuadCurves") == 0) {
				QuadCurves copy = (QuadCurves) at;
				paste = new QuadCurves(copy.origin.x1, copy.origin.y1, copy.origin.ctrlx,
						copy.origin.ctrly, copy.origin.x2, copy.origin.y2);
			}
			else if (s.compareTo("Drawing.Rectangles") == 0) {
				Rectangles copy = (Rectangles) at;
				paste = new Rectangles(copy.origin.x, copy.origin.y, copy.origin.width, copy.origin.height, copy.getState());
			}
			else if (s.compareTo("Drawing.Rhombuses") == 0) {
				Parallelograms copy = (Parallelograms) at;
				paste = new Parallelograms(copy.origin.xpoints, copy.origin.ypoints);
			}
			else if (s.compareTo("Drawing.Squares") == 0) {
				Squares copy = (Squares) at;
				paste = new Squares(copy.origin.x, copy.origin.y, copy.origin.width, copy.getState());
			}
			
			paste.drawcolor = at.drawcolor;
			paste.fillcolor = at.fillcolor;
			if (at.scrollpane != null) {
				paste.addText();
		        try {
		        	paste.textpane.getStyledDocument().insertString(0,
		        			at.textpane.getText(), at.textpane.getInputAttributes());
		        } catch (BadLocationException ble) {
		            System.err.println("Couldn't insert initial text into text pane.");
		        }
			}
			
			copied = true;
		}
	}
	
	public void paste() {
		if (!copied)
			copy();
		if (copied) {
			copied = false;
			at.seleted = false;
			paste.moveTo(20, 20);
			at = paste;
			at.seleted = true;
			add(at, 0);
			repaint();
		}
	}
}
