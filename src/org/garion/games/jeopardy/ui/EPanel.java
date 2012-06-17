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
package org.garion.games.jeopardy.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import org.garion.games.jeopardy.Category;
import org.garion.games.jeopardy.Entry;

/**
 * Panel for displaying and accessing an entry
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "serial" )
public class EPanel extends JPanel {

	private Category c;
	private Entry e;
	private BoardPanel bp;
	private JLabel label;

	/**
	 * Create a new empty panel
	 * 
	 * @param bp
	 *            the parent panel
	 */
	public EPanel( BoardPanel bp ) {
		this( "", null, null, bp );
	}

	/**
	 * Create a new panel
	 * 
	 * @param text
	 *            the text to display
	 * @param c
	 *            the category of the entry
	 * @param e
	 *            the entry to access
	 * @param bp
	 *            the parent panel
	 */
	public EPanel( String text, Category c, Entry e, BoardPanel bp ) {
		super( new BorderLayout() );
		label = new JLabel( text, JLabel.CENTER );
		this.c = c;
		this.e = e;
		this.bp = bp;
		add( label, BorderLayout.CENTER );
		addMouseListener( adapter );
		setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
	}

	/**
	 * Set the entry
	 * 
	 * @param c
	 *            the category
	 * @param e
	 *            the entry
	 */
	public void setEntry( Category c, Entry e ) {
		this.c = c;
		this.e = e;
		if ( e == null ) {
			setText( "" );
			label.setVisible( false );
		} else {
			String v = c.getValue( e );
			int i = v.indexOf( " " );
			if ( i != -1 )
				setText( v.substring( 0, i ) );
			else
				setText( v );
		}
	}

	/**
	 * Set the displayed text
	 * 
	 * @param t
	 *            the new text
	 */
	public void setText( String t ) {
		label.setText( t );
		label.setVisible( true );
	}

	private MouseAdapter adapter = new MouseAdapter() {

		public void mouseClicked( MouseEvent evt ) {
			if ( label.isVisible() ) {
				label.setVisible( false );
				bp.displayEntry( c, e );
			}
		}
	};

}
