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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.garion.games.jeopardy.Category;
import org.garion.global.ui.Layout;

/**
 * Dialog for editing a category name
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "serial" )
public class CategoryEditDialog extends JDialog implements ActionListener {

	private static JDialog dialog;
	private static String value;

	private JTextField field;
	private JButton ok, cancel;

	/**
	 * Show the dialog
	 * 
	 * @param caller
	 *            the component calling this dialog
	 * @param c
	 *            the category
	 * @return the new name
	 */
	public static final String showDialog( Component caller, Category c ) {
		value = null;
		dialog = new CategoryEditDialog( JOptionPane
			.getFrameForComponent( caller ), c );
		dialog.setVisible( true );
		return value;
	}

	private CategoryEditDialog( Frame owner, Category c ) {
		super( owner, "Edit Category Name", true );
		field = new JTextField( c.getName() );
		field.addActionListener( this );
		field.setActionCommand( "OK" );
		ok = new JButton( "OK" );
		cancel = new JButton( "Cancel" );
		ok.addActionListener( this );
		cancel.addActionListener( this );

		JPanel input = new JPanel( new GridLayout( 0, 1 ) );
		input.add( new JLabel( "Enter new Category name: " ) );
		input.add( field );

		JPanel buttons = Layout.createBoxPanel( Layout.X_AXIS );
		buttons.add( Box.createHorizontalGlue() );
		buttons.add( ok );
		buttons.add( cancel );
		getRootPane().setDefaultButton( ok );

		JPanel main = new JPanel( new BorderLayout() );
		main.add( input, BorderLayout.CENTER );
		main.add( buttons, BorderLayout.SOUTH );
		main.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( main );
		// pack();
		setSize( 200, 110 );
		setLocationRelativeTo( owner );
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		if ( "OK".equals( e.getActionCommand() ) )
			value = field.getText();
		dialog.setVisible( false );
	}

}
