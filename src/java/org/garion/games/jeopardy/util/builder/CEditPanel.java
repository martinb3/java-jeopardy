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

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import org.garion.games.jeopardy.Category;

/**
 * Panel for editing a Jeopardy category
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "serial" )
public class CEditPanel extends JPanel {

	private EditPanel ep;
	private Category c;
	private JLabel label;

	/**
	 * create a panel
	 * 
	 * @param c
	 *            the category
	 * @param panel
	 *            the parent edit panel
	 */
	public CEditPanel( Category c, EditPanel panel ) {
		super( new BorderLayout() );
		this.c = c;
		this.ep = panel;
		if ( c == null )
			label = new JLabel( "", JLabel.CENTER );
		else
			label = new JLabel( "<html>" + c.getName() + "</html>",
				JLabel.CENTER );
		label.setFont( Font.decode( "Arial-BOLD-18" ) );
		add( label, BorderLayout.CENTER );
		setBackground( Color.LIGHT_GRAY );
		setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
		addMouseListener( listener );
	}

	/**
	 * Set the currently-editable category
	 * 
	 * @param c
	 *            the new category
	 */
	public void setCategory( Category c ) {
		this.c = c;
		label.setText( "<html>" + c.getName() + "</html>" );
	}

	private MouseAdapter listener = new MouseAdapter() {

		public void mouseClicked( MouseEvent evt ) {
			String ret = CategoryEditDialog.showDialog( label, c );
			if ( ret != null && !ret.isEmpty() ) {
				c.setName( ret );
				label.setText( "<html>" + c.getName() + "</html>" );
				ep.getBuilder().setChanged( true );
			}
		}
	};

}
