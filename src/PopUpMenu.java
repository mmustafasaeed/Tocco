
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;


public class PopUpMenu extends JPopupMenu
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPopupMenu menu;
    private Toolkit toolkit;

    public PopUpMenu(final JFrame frame)
    {
        super();
        
       // this.initUI();
        toolkit = frame.getToolkit();

        menu = new JPopupMenu();
        JMenuItem menuItemBeep = new JMenuItem("Beep");

        menuItemBeep.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                toolkit.beep();
            }
        });

        menu.add(menuItemBeep);

        JMenuItem menuItemClose = new JMenuItem("Close");
        menuItemClose.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        menu.add(menuItemClose);

        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setSize(250, 200);
        frame.setLocationRelativeTo(null);
    }

    /*private void initUI()
    {
        toolkit = this.getToolkit();

        menu = new JPopupMenu();
        JMenuItem menuItemBeep = new JMenuItem("Beep");

        menuItemBeep.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                toolkit.beep();
            }
        });

        menu.add(menuItemBeep);

        JMenuItem menuItemClose = new JMenuItem("Close");
        menuItemClose.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        menu.add(menuItemClose);

        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(250, 200);
        this.setLocationRelativeTo(null);
    }*/

  /*  public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                PopUpMenu pm = new PopUpMenu("JPopupMenu");
                pm.setVisible(true);
            }
        });

    }*/
}


 