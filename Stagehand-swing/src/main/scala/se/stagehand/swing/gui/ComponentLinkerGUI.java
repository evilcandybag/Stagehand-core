package se.stagehand.swing.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ComponentLinkerGUI extends JComponent
{
	private Map<JComponent, java.util.List<JComponent>> linked;

	public ComponentLinkerGUI ()
	{
		super ();
		linked = new HashMap<JComponent, java.util.List<JComponent>> ();
	}

	public void linkOrUnlink( JComponent c1, JComponent c2 ) {
		if (areLinked(c1,c2)) {
			unlink(c1,c2);
		} else {
			link(c1,c2);
		}
	}

	public void link ( JComponent c1, JComponent c2 )
	{
		java.util.List<JComponent> l = linked.get(c1);
		if (l == null) {
			l = new LinkedList<JComponent>();
			l.add(c2);
			linked.put(c1,l);
		} else {
			l.add(c2);
		}
		repaint ();
	}

	public void unlink( JComponent c1, JComponent c2 ) {
		java.util.List<JComponent> l;
		if (linked.containsKey(c1)) {
			l = linked.get(c1);
			if (l.contains(c2)) {
				l.remove(c2);
			}
		} 
		if (linked.containsKey(c2)) {
			l = linked.get(c2);
			if (l.contains(c1)) {
				l.remove(c1);
			}
		}
		repaint();
	}

	public boolean areLinked( JComponent c1, JComponent c2 ) {
		java.util.List<JComponent> l1 = linked.get(c1);
		java.util.List<JComponent> l2 = linked.get(c2);

		return l1 != null && l1.contains(c2) || l2 != null && l2.contains(c1);
	}

	protected void paintComponent ( Graphics g )
	{
		Graphics2D g2d = ( Graphics2D ) g;
		g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

		
		g2d.setPaint ( Color.BLACK );
		//If something is selected, draw a line between it and the mouse pointer.
		if (last != null) {
			Point mPos = MouseInfo.getPointerInfo().getLocation();
			Point lPos = getRectCenter(last.getBounds());
			
			g2d.drawLine(lPos.x, lPos.y, mPos.x, mPos.y);
		}
		
		for ( JComponent c1 : linked.keySet () )
		{
			java.util.List<JComponent> l = linked.get(c1);
			for (JComponent c2 : linked.get(c1)) {
				Point p1 = getRectCenter ( getBoundsInWindow ( c1 ) );
				Point p2 = getRectCenter ( getBoundsInWindow ( c2 ) );
				g2d.drawLine ( p1.x, p1.y, p2.x, p2.y );
			}
		}
	}

	private Point getRectCenter ( Rectangle rect )
	{
		return new Point ( rect.x + rect.width / 2, rect.y + rect.height / 2 );
	}

	private Rectangle getBoundsInWindow ( Component component )
	{
		return getRelativeBounds ( component, getRootPaneAncestor ( component ) );
	}

	private Rectangle getRelativeBounds ( Component component, Component relativeTo )
	{
		return new Rectangle ( getRelativeLocation ( component, relativeTo ),
				component.getSize () );
	}

	private Point getRelativeLocation ( Component component, Component relativeTo )
	{
		Point los = component.getLocationOnScreen ();
		Point rt = relativeTo.getLocationOnScreen ();
		return new Point ( los.x - rt.x, los.y - rt.y );
	}

	private JRootPane getRootPaneAncestor ( Component c )
	{
		for ( Container p = c.getParent (); p != null; p = p.getParent () )
		{
			if ( p instanceof JRootPane )
			{
				return ( JRootPane ) p;
			}
		}
		return null;
	}

	public boolean contains ( int x, int y )
	{
		return false;
	}

	private static ComponentLinkerGUI linker;

	public static void main ( String[] args )
	{
		setupLookAndFeel ();

		JFrame frame = new JFrame ();

		linker = new ComponentLinkerGUI ();
		frame.setGlassPane ( linker );
		linker.setVisible ( true );

		JPanel content = new JPanel ();
		content.setLayout ( new GridLayout ( 10, 5, 5, 5 ) );
		content.setBorder ( BorderFactory.createEmptyBorder ( 5, 5, 5, 5 ) );
		frame.add ( content );

		for ( int i = 0; i < 50; i++ )
		{
			final JButton button = new JButton ( "Button" + i );
			button.addActionListener ( new ActionListener ()
			{
				public void actionPerformed ( ActionEvent e )
				{
					link ( button );
				}
			} );
			content.add ( button );
		}

		frame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
		frame.pack ();
		frame.setLocationRelativeTo ( null );
		frame.setVisible ( true );
	}

	private static JButton last = null;

	private static void link ( JButton button )
	{
		if ( last == null )
		{
			last = button;
		}
		else
		{
			linker.linkOrUnlink ( last, button );
			last = null;
		}
	}

	private static void setupLookAndFeel ()
	{
		try
		{
			UIManager.setLookAndFeel ( UIManager.getSystemLookAndFeelClassName () );
		}
		catch ( ClassNotFoundException e )
		{
			e.printStackTrace ();
		}
		catch ( InstantiationException e )
		{
			e.printStackTrace ();
		}
		catch ( IllegalAccessException e )
		{
			e.printStackTrace ();
		}
		catch ( UnsupportedLookAndFeelException e )
		{
			e.printStackTrace ();
		}
	}
}