import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;


public class StateDialog extends JDialog implements ActionListener, PropertyChangeListener
{

	private boolean state;
	private JButton yes;
	private JButton no;
	
	public StateDialog()
	{
		yes=new JButton("Yes");
		no=new JButton("Yes");
	}
	
	public boolean getState()
	{
		if(state)
			return true;
		else
			return false;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==yes)
		{
			state=true;
		}
		else if (arg0.getSource()==no)
		{
			state=false;
		}
		
	}
	
	

}
