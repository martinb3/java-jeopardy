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
 * Dialog for adding to (or subtracting from) a player's score.
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "serial" )
public class ScoreEditDialog extends JDialog implements ActionListener {

	/** Dialog termination status. Player score was updated. */
	public static final int OK = 0;
	/** Dialog termination status. Update was cancelled. */
	public static final int CANCEL = 1;

	private static ScoreEditDialog dialog;
	private static int status;

	private Player p;

	private JLabel playerLabel;
	private JTextField scoreField;
	private JButton ok, cancel;

	/**
	 * Create and show a new dialog
	 * 
	 * @param caller
	 *            the component calling this dialog
	 * @param p
	 *            the player to edit
	 * @return termination status (OK or CANCEL)
	 */
	public static int showDialog( Component caller, Player p ) {
		status = CANCEL;
		dialog = new ScoreEditDialog(
			JOptionPane.getFrameForComponent( caller ), p );
		dialog.setVisible( true );
		return status;
	}

	private ScoreEditDialog( Frame owner, Player p ) {
		super( owner, "Edit Player Score", true );
		this.p = p;
		// create
		playerLabel = new JLabel( "Player: " + p.getName() );
		JLabel scoreDisplay = new JLabel( "Score: " + p.asNumber() );
		JLabel scoreLabel = new JLabel( "Add: ", JLabel.RIGHT );
		scoreField = new JTextField( "" );
		scoreField.setActionCommand( "OK" );
		scoreField.addActionListener( this );
		scoreLabel.setLabelFor( scoreField );
		// score input
		JPanel score = new JPanel( new BorderLayout() );
		score.add( scoreLabel, BorderLayout.WEST );
		score.add( scoreField, BorderLayout.CENTER );
		// label display
		JPanel labels = new JPanel();
		BoxLayout horiz = new BoxLayout( labels, BoxLayout.X_AXIS );
		labels.setLayout( horiz );
		labels.add( playerLabel );
		labels.add( Box.createHorizontalGlue() );
		labels.add( scoreDisplay );
		// buttons
		JPanel buttons = new JPanel();
		ok = UI.createJButton( "OK", this );
		cancel = UI.createJButton( "Cancel", this );
		buttons.add( ok );
		buttons.add( cancel );
		// add and finish
		JPanel main = new JPanel( new BorderLayout() );
		main.add( labels, BorderLayout.NORTH );
		main.add( score, BorderLayout.CENTER );
		main.add( buttons, BorderLayout.SOUTH );
		main.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		getRootPane().setDefaultButton( ok );
		scoreField.requestFocusInWindow();
		getContentPane().add( main );
		setSize( 200, 110 );
		setLocationRelativeTo( owner );
	}

	public void actionPerformed( ActionEvent evt ) {
		String command = evt.getActionCommand();
		if ( "OK".equals( command ) ) {
			int score = 0;
			try {
				score = Integer.parseInt( scoreField.getText() );
			} catch ( NumberFormatException nfe ) {
				JOptionPane.showMessageDialog( dialog,
					"Score must be an integer. No commas, spaces, or letters.",
					"Invalid Score", JOptionPane.ERROR_MESSAGE );
				scoreField.requestFocusInWindow();
				return;
			}
			p.add( score );
			status = OK;
		}
		dialog.setVisible( false );
	}

}
