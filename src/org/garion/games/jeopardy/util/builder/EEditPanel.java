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
package org.garion.games.jeopardy.util.builder;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import org.garion.games.jeopardy.Category;
import org.garion.games.jeopardy.Entry;

/**
 * JPanel for entry panels
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "serial" )
public class EEditPanel extends JPanel {

	private EditPanel ep;
	private Entry e;
	private Category c;
	private JLabel label;

	/**
	 * Create a new panel
	 * 
	 * @param c
	 *            the category
	 * @param e
	 *            the entry
	 * @param ep
	 *            the parent edit panel
	 */
	public EEditPanel( Category c, Entry e, EditPanel ep ) {
		super( new BorderLayout() );
		this.ep = ep;
		label = new JLabel( "", JLabel.CENTER );
		add( label, BorderLayout.CENTER );
		addMouseListener( adapter );
		setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
		setEntry( c, e );
	}

	/**
	 * Set the entry
	 * 
	 * @param c
	 *            the new category
	 * @param e
	 *            the new entry
	 */
	public void setEntry( Category c, Entry e ) {
		this.c = c;
		this.e = e;
		label.setText( "$" + c.getValue( e ) );
		setToolTipText( "<html><b>A:</b> " + e.getAnswer() + "<br /><b>Q:</b>"
			+ e.getQuestion() + "</html>" );
	}

	private MouseAdapter adapter = new MouseAdapter() {

		public void mouseClicked( MouseEvent evt ) {
			String[] values = EntryEditDialog.showDialog( ep, c, e );
			for ( int i = 0; i < values.length; i++ ) {
				if ( values[i] == null )
					continue;
				switch ( i ) {
					case EntryEditDialog.VALUE:
						c.setValue( e, values[i] );
						break;
					case EntryEditDialog.ANSWER:
						e.setAnswer( values[i] );
						break;
					case EntryEditDialog.QUESTION:
						e.setQuestion( values[i] );
						break;
				}
				ep.getBuilder().setChanged( true );
			}
			setEntry( c, e );
		}
	};

}
