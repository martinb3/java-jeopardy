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
import java.util.ArrayList;
import javax.swing.*;

/**
 * Manages a panel's layout manager to lay components out in rows of BoxLayout
 * panels.
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public class GridBoxLayoutHandler {

	private LP lp;
	private ArrayList<LP> rows;
	private int currentRow = -1;

	/**
	 * Create a new handler for the given jpanel
	 * 
	 * @param panel
	 *            the panel to handle
	 */
	public GridBoxLayoutHandler( JPanel panel ) {
		BoxLayout l = new BoxLayout( panel, BoxLayout.Y_AXIS );
		lp = new LP( panel, l );
		rows = new ArrayList<LP>();
		newRow();
	}

	/**
	 * Create and begin adding to a new row
	 * 
	 * @return the index of the new row
	 */
	public int newRow() {
		rows.add( createLP() );
		currentRow = rows.size() - 1;
		lp.panel.add( rows.get( rows.size() - 1 ).panel );
		return currentRow;
	}

	/**
	 * @return The number of rows
	 */
	public int size() {
		return rows.size();
	}

	/**
	 * Set the active row to add to
	 * 
	 * @param r
	 *            the row index. If it is out of range, the closest row (0 or
	 *            size()-1) will be selected and the new row will be returned
	 * @return the row that was set
	 */
	public int setRow( int r ) {
		currentRow = r;
		if ( currentRow < 0 )
			currentRow = 0;
		else if ( currentRow >= rows.size() )
			currentRow = rows.size() - 1;
		return currentRow;
	}

	/**
	 * Add an item to the current row
	 * 
	 * @param comp
	 *            the item to add
	 */
	public void addItem( Component comp ) {
		rows.get( currentRow ).panel.add( comp );
	}

	/**
	 * Create and add a vertical glue (expanding vertical whitespace)
	 * 
	 * @return the new row index
	 */
	public int addVertGlue() {
		lp.panel.add( Box.createVerticalGlue() );
		return newRow();
	}

	/**
	 * Add a horizontal glue (expanding whitespace) to the current row
	 */
	public void addHorizontalGlue() {
		addItem( Box.createHorizontalGlue() );
	}

	private LP createLP() {
		JPanel p = new JPanel();
		BoxLayout l = new BoxLayout( p, BoxLayout.X_AXIS );
		return new LP( p, l );
	}

	private class LP {

		public BoxLayout layout;
		public JPanel panel;

		public LP( JPanel panel, BoxLayout layout ) {
			this.panel = panel;
			this.layout = layout;
		}

		@SuppressWarnings( "unused" )
		public BoxLayout layout() {
			return layout;
		}

		@SuppressWarnings( "unused" )
		public JPanel panel() {
			return panel;
		}
	}
}
