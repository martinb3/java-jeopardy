/*
 * Copyright (C) 2010 Dallin Lauritzen
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program.  If not, see <http://www.gnu/org.licenses/>.
 */
package org.garion.global.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Global access to UI-related actions
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public final class UI {

	/**
	 * Create a JButton and attach an ActionListener
	 * 
	 * @param text
	 *            The text to display on the button
	 * @param listener
	 *            The action listener to register
	 * @return the created JButton
	 */
	public static final JButton createJButton( String text,
		ActionListener listener ) {
		JButton ret = new JButton( text );
		ret.addActionListener( listener );
		return ret;
	}

	/**
	 * Create a new JMenuItem with the given text. Assigns the item an action
	 * listener, and adds it to a menu.
	 * 
	 * @param text
	 *            The menu item text
	 * @param add
	 *            optional menu to add this item to (pass {@code null} to
	 *            ignore)
	 * @param listener
	 *            The action listener for this menu item
	 * @return the menu item
	 */
	public static final JMenuItem createMenuItem( String text, JMenu add,
		ActionListener listener ) {
		JMenuItem ret = new JMenuItem( text );
		ret.addActionListener( listener );
		if ( add != null )
			add.add( ret );
		return ret;
	}

	/**
	 * Checks to see if any windows are still displayable (not disposed). If all
	 * are closed, then call {@link System#exit(int)}.
	 */
	public static final void exitIfClosed() {
		Window[] wins = JFrame.getWindows();
		boolean closed = true;
		for ( Window w : wins ) {
			if ( w.isDisplayable() ) {
				closed = false;
				break;
			}
		}
		if ( closed )
			System.exit( 0 );
	}

	/**
	 * Adds a WindowListener to the given frame to call {@link #exitIfClosed()}
	 * after frame is closed.
	 * 
	 * @param frame
	 */
	public static final void addExitListener( final JFrame frame ) {
		frame.addWindowListener( new WindowAdapter() {

			public void windowClosing( WindowEvent evt ) {
				frame.dispose();
				exitIfClosed();
			}
		} );
	}

	/**
	 * Ensure a specified row index is visible.
	 * 
	 * @param rowIndex
	 *            The row to scroll to
	 * @param table
	 *            The table
	 */
	public static final void scrollToTableRow( int rowIndex, JTable table ) {
		if ( !(table.getParent() instanceof JViewport) ) {
			return;
		}
		JViewport viewport = (JViewport) table.getParent();

		// This rectangle is relative to the table where the
		// northwest corner of cell (0,0) is always (0,0).
		Rectangle rect = table.getCellRect( rowIndex, 0, true );

		// The location of the viewport relative to the table
		Point pt = viewport.getViewPosition();

		// Translate the cell location so that it is relative
		// to the view, assuming the northwest corner of the
		// view is (0,0)
		rect.setLocation( rect.x - pt.x, rect.y - pt.y );

		// Scroll the area into view
		viewport.scrollRectToVisible( rect );
	}

}
