

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyleConstants;

public class Menubar extends JMenuBar {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String font;
	
	public Menubar(final withMouse wm, final JFrame f) {
		super(); 
		
		
		
		
		String[] pluggableModule={"PHYSICS","ELECTRONICS","CHEMISTRY","UML"};
		JComboBox petList = new JComboBox(pluggableModule);
        petList.setSelectedIndex(3);
        petList.addActionListener(new ActionListener()
        {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				JComboBox cb = (JComboBox)arg0.getSource();
		        String petName = (String)cb.getSelectedItem();
		        
		        if(petName=="PHYSICS")
		        {
		        	JOptionPane.showMessageDialog(f,"Physics Engine Selected");
		        }
		        else if(petName=="ELECTRONICS")
		        {
		        	JOptionPane.showMessageDialog(f,"Electronics Engine Currently Unavailable");
		        }
		        else if(petName=="CHEMISTRY")
		        {
		        	JOptionPane.showMessageDialog(f,"Chemistry Engine Currently Unavailable");
		        }
		        else if(petName=="UML")
		        {
		        	JOptionPane.showMessageDialog(f,"UML Engine Currently Unavailable");
		        }
				
			}
        	
        });
		
		JMenu file = new JMenu("File");  // First menu - File
		final JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images File", "png");
		fc.setSelectedFile(new File("pic.png"));
		fc.setFileFilter(filter);
		JMenuItem save = new JMenuItem("Save as ...");
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage image = new BufferedImage(wm.getWidth(), wm.getHeight(), BufferedImage.TYPE_INT_ARGB);
		        Graphics2D g = image.createGraphics();
		        wm.paint(g);
		        g.dispose();
				int returnVal = fc.showSaveDialog(f);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                try {
	                	if(!file.exists()){
	                		ImageIO.write(image, "png", file);
	                	}
	                	else{
	                		int returnValue=0;
                            JOptionPane notice = new JOptionPane();
                            returnValue = notice.showConfirmDialog(fc, file.getName()+" already exists. Replace?", "Save File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if(returnValue==JOptionPane.YES_OPTION){
                                ImageIO.write(image, "png", file);
                            }
                            else{
                                actionPerformed(e);
                            }
	                	}
	                }
	                catch (IOException ioe) {
	                	ioe.printStackTrace();
	                }
				}
			}});
		save.setBackground(Color.LIGHT_GRAY);
		JMenuItem exit = new JMenuItem("Exit ...");
		exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
			
		});
		exit.setBackground(Color.GRAY);
		file.setBackground(Color.GRAY);
		file.add(save);
		file.add(exit);
		//f.add(buttonPanel);
		
		this.setBackground(Color.GRAY);
		this.add(file);
		//this.add(animator);
		this.add(petList);
	}
	
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
}
