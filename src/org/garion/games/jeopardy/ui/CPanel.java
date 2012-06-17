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

import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import org.garion.games.jeopardy.Category;

/**
 * panel for displaying a category name
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "serial" )
public class CPanel extends JPanel {

	private Category c;
	@SuppressWarnings( "unused" )
	private BoardPanel bp;

	private JLabel label;

	/**
	 * Create an empty panel
	 * 
	 * @param bp
	 *            the parent panel
	 */
	public CPanel( BoardPanel bp ) {
		this( null, bp );
	}

	/**
	 * Create a panel
	 * 
	 * @param c
	 *            the category
	 * @param bp
	 *            the parent panel
	 */
	public CPanel( Category c, BoardPanel bp ) {
		super( new BorderLayout() );
		this.c = c;
		this.bp = bp;
		if ( c == null )
			label = new JLabel( "", JLabel.CENTER );
		else
			label = new JLabel( "<html>" + c.getName() + "</html>",
				JLabel.CENTER );
		label.setFont( Font.decode( "Arial-BOLD-18" ) );
		add( label, BorderLayout.CENTER );
		setBackground( Color.LIGHT_GRAY );
		setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
	}

	/**
	 * Set the category
	 * 
	 * @param c
	 *            the new category
	 */
	public void setCategory( Category c ) {
		this.c = c;
		if ( c == null )
			label.setText( "" );
		else
			label.setText( "<html>" + c.getName() + "</html>" );
	}

	/**
	 * Get the category
	 * 
	 * @return the category
	 */
	public Category getCategory() {
		return c;
	}

}
