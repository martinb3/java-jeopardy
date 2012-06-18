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
import org.garion.games.jeopardy.*;
import org.garion.global.ui.GridBoxLayoutHandler;

/**
 * Dialog for editing an entry
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "serial" )
public class EntryEditDialog extends JDialog implements ActionListener {

	/** Return index for answer field */
	public static final int ANSWER = 0;
	/** Return index for question field */
	public static final int QUESTION = 1;
	/** Return index for value field */
	public static final int VALUE = 2;

	private static EntryEditDialog dialog;
	private static String[] values = null;

	private GridBoxLayoutHandler handler;
	private JPanel main;
	private JTabbedPane tab;
	private JTextField valueField;
	private JTextArea answerField;
	private JTextArea questionField;

	/**
	 * Show the dialog
	 * 
	 * @param frameComp
	 *            the component calling this dialog
	 * @param f
	 *            the Final entry being edited
	 * @return new values
	 */
	public static String[] showDialog( Component frameComp, Final f ) {
		values = new String[ 3 ];
		Frame frame = JOptionPane.getFrameForComponent( frameComp );
		dialog = new EntryEditDialog( frame, f );
		dialog.setVisible( true );
		return values;
	}

	/**
	 * Show the dialog
	 * 
	 * @param frameComp
	 *            the component calling this dialog
	 * @param c
	 *            the category
	 * @param e
	 *            the entry to edit
	 * @return the new values
	 */
	public static String[] showDialog( Component frameComp, Category c, Entry e ) {
		values = new String[ 3 ];
		Frame frame = JOptionPane.getFrameForComponent( frameComp );
		dialog = new EntryEditDialog( frame, c, e );
		dialog.setVisible( true );
		return values;
	}

	private EntryEditDialog( Frame owner, String category, String value, Entry e ) {
		super( owner, "Edit Entry", true );
		main = new JPanel();
		handler = new GridBoxLayoutHandler( main );
		// Edits
		JLabel categoryLabel = new JLabel( "Category: " + category );
		JLabel valueLabel = new JLabel( "Value: " );
		valueField = new JTextField( value );
		valueField.setColumns( 6 );
		answerField = new JTextArea( e.getAnswer() );
		questionField = new JTextArea( e.getQuestion() );
		answerField.setLineWrap( true );
		answerField.setWrapStyleWord( true );
		questionField.setLineWrap( true );
		questionField.setWrapStyleWord( true );
		valueLabel.setLabelFor( valueField );

		handler.addItem( categoryLabel );
		handler.newRow();
		handler.addItem( valueLabel );
		handler.addItem( valueField );

		tab = new JTabbedPane();
		tab.addTab( "Answer", new JScrollPane( answerField ) );
		tab.addTab( "Question", new JScrollPane( questionField ) );

		// Buttons
		JButton cancelButton = new JButton( "Cancel" );
		cancelButton.addActionListener( this );
		final JButton okButton = new JButton( "OK" );
		okButton.setActionCommand( "OK" );
		okButton.addActionListener( this );
		getRootPane().setDefaultButton( okButton );

		// Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout( new BoxLayout( buttonPane, BoxLayout.LINE_AXIS ) );
		buttonPane.setBorder( BorderFactory.createEmptyBorder( 0, 10, 10, 10 ) );
		buttonPane.add( Box.createHorizontalGlue() );
		buttonPane.add( okButton );
		buttonPane.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
		buttonPane.add( cancelButton );

		getContentPane().add( main, BorderLayout.NORTH );
		getContentPane().add( tab, BorderLayout.CENTER );
		getContentPane().add( buttonPane, BorderLayout.SOUTH );
		setSize( 300, 200 );
		setLocationRelativeTo( owner );
	}

	private EntryEditDialog( Frame owner, Final f ) {
		this( owner, f.name, "Wager", f.entry );
		valueField.setEnabled( false );
	}

	private EntryEditDialog( Frame owner, Category c, Entry e ) {
		this( owner, c.name, c.getValue( e ), e );
	}

	private static void setValues( String value, String answer, String question ) {
		values[VALUE] = value;
		values[ANSWER] = answer;
		values[QUESTION] = question;
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		if ( e.getActionCommand().equals( "OK" ) ) {
			setValues( valueField.getText().trim(), answerField.getText()
				.trim(), questionField.getText().trim() );
		} else {
			setValues( null, null, null );
		}
		dialog.setVisible( false );
	}

}
