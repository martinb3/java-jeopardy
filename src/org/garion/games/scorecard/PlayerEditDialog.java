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
package org.garion.games.scorecard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.garion.global.ui.UI;

/**
 * A popup dialog to edit the attributes of a Player.
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "serial" )
public class PlayerEditDialog extends JDialog implements ActionListener {

	/** Dialog termination status. Player information was updated. */
	public static final int OK = 0;
	/** Dialog termination status. Update was cancelled. */
	public static final int CANCEL = 1;

	private static PlayerEditDialog dialog;
	private static int status;

	private Player p;

	private JTextField nameField, scoreField;
	private JButton ok, cancel;
	private JPanel main, input, buttons;

	/**
	 * Create and show a modal dialog to edit the given Player's name and score
	 * 
	 * @param caller
	 *            the component calling this dialog
	 * @param p
	 *            the player to edit
	 * @return the contents of the edit fields upon dialog submit. If values are
	 *         null, then dialog was closed without clicking "OK" button
	 */
	public static int showDialog( Component caller, Player p ) {
		status = CANCEL;
		dialog = new PlayerEditDialog( JOptionPane
			.getFrameForComponent( caller ), p );
		dialog.setVisible( true );
		return status;
	}

	private PlayerEditDialog( Frame owner, Player p ) {
		super( owner, "Edit Player", true );
		this.p = p;
		JLabel nameLabel = new JLabel( "Player Name: ", JLabel.RIGHT );
		JLabel scoreLabel = new JLabel( "Score: ", JLabel.RIGHT );
		nameField = new JTextField( p.getName(), 10 );
		scoreField = new JTextField( String.valueOf( p.getScore() ), 10 );
		nameField.setActionCommand( "OK" );
		scoreField.setActionCommand( "OK" );
		nameLabel.setLabelFor( nameField );
		scoreLabel.setLabelFor( scoreField );
		nameField.addActionListener( this );
		scoreField.addActionListener( this );
		ok = UI.createJButton( "OK", this );
		cancel = UI.createJButton( "Cancel", this );
		getRootPane().setDefaultButton( ok );
		input = new JPanel();
		buttons = new JPanel();
		BoxLayout inputLayout = new BoxLayout( input, BoxLayout.PAGE_AXIS );
		BoxLayout buttonLayout = new BoxLayout( buttons, BoxLayout.X_AXIS );
		input.setLayout( inputLayout );
		buttons.setLayout( buttonLayout );
		input.add( nameLabel );
		input.add( nameField );
		input.add( scoreLabel );
		input.add( scoreField );
		buttons.add( Box.createHorizontalGlue() );
		buttons.add( ok );
		buttons.add( cancel );
		main = new JPanel( new BorderLayout() );
		main.add( buttons, BorderLayout.SOUTH );
		main.add( input, BorderLayout.CENTER );
		main.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( main );
		setSize( 200, 150 );
		setLocationRelativeTo( owner );
	}

	public void actionPerformed( ActionEvent evt ) {
		String command = evt.getActionCommand();
		if ( "OK".equals( command ) ) {
			try {
				p.setName( nameField.getText() );
				p.setScore( Integer.parseInt( scoreField.getText() ) );
				status = OK;
				dialog.setVisible( false );
			} catch ( NumberFormatException nfe ) {
				JOptionPane.showMessageDialog( dialog,
					"Score must be an integer.", "Invalid Score",
					JOptionPane.ERROR_MESSAGE );
				scoreField.requestFocusInWindow();
			}
		} else if ( "Cancel".equals( command ) )
			dialog.setVisible( false );
	}

}
