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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import org.garion.games.jeopardy.*;
import org.garion.games.jeopardy.util.Const;
import org.garion.games.jeopardy.util.ExternalPlayerController;

/**
 * Panel to display a jeopardy game
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "serial" )
public class BoardPanel extends JPanel implements ActionListener, ExternalPlayerController {

	/** Panel name for the grid panel */
	public static final String GRID = "GRID";
	/** Panel name for the entry display panel */
	public static final String ENTRY = "ENTRY";
	/** Panel name for the final jeopardy panel */
	public static final String FINALPANEL = "FINAL";
	/** Panel name for endgame panel */
	public static final String ENDGAME = "ENDGAME";

	private Game g;
	private Category currentCategory;
	private Entry currentEntry;
	private int round;

	// grid
	private JPanel grid;
	private CPanel[] cats;
	private EPanel[][] entries;

	// entry
	private JPanel entry;
	private JLabel entryCat;
	private JLabel entryVal;
	private JLabel playerVal;
	private JLabel entryAns;
	private String entryQu;
	private JButton respond;

	// final
	private JPanel finalpanel;
	private JLabel finalCat;
	private JLabel finalValue;
	private JLabel finalText;
	@SuppressWarnings( "unused" )
	private String finalQu;
	private JButton finalButton;

	// end game
	private JPanel endGame;
	private JLabel congrats;

	/**
	 * Create a panel
	 */
	public BoardPanel() {
		super( new CardLayout() );
		g = null;
		createGridPanel();
		createDisplayPanel();
		createFinalPanel();
		createEndGamePanel();
	}

	private void createGridPanel() {
		// grid panel
		grid = new JPanel( new GridLayout( Category.MAX_SIZE + 1,
			Round.MAX_SIZE ) );
		cats = new CPanel[ Round.MAX_SIZE ];
		entries = new EPanel[ cats.length ][ Category.MAX_SIZE ];
		// create
		for ( int y = 0; y < cats.length; y++ ) {
			cats[y] = new CPanel( this );
			for ( int x = 0; x < entries[y].length; x++ ) {
				entries[y][x] = new EPanel( this );
			}
		}
		// layout
		for ( int x = 0; x < cats.length; x++ ) {
			grid.add( cats[x] );
		}
		for ( int x = 0; x < cats.length; x++ ) {
			for ( int y = 0; y < entries[x].length; y++ ) {
				grid.add( entries[y][x] );
			}
		}
		add( grid, GRID );
	}

	private void createDisplayPanel() {
		entry = new JPanel( new BorderLayout() );
		JPanel info = new JPanel();
		BoxLayout horiz = new BoxLayout( info, BoxLayout.X_AXIS );
		info.setLayout( horiz );
		
		entryCat = new JLabel( "", JLabel.LEFT );
		entryCat.setFont( Font.decode( "Arial-BOLD-18" ) );
		
		entryVal = new JLabel( "", JLabel.RIGHT );
		entryVal.setFont( Font.decode( "Arial-BOLD-18" ) );

		playerVal = new JLabel( "", JLabel.CENTER );
		playerVal.setFont( Font.decode( "Arial-BOLD-38" ) );
		playerVal.setForeground(Color.RED);
		
		
		info.add( entryCat );
		info.add( Box.createHorizontalGlue() );
		info.add( entryVal );
		
		entryAns = new JLabel( "", JLabel.CENTER );
		entryAns.setFont( Font.decode( "Arial-BOLD-24" ) );
		entry.add( info, BorderLayout.NORTH );
		entry.add( entryAns, BorderLayout.CENTER );
		
		respond = new JButton( "Question" );
		respond.addActionListener( this );
		
		//entry.add( respond, BorderLayout.SOUTH );
		
		JPanel jp = new JPanel(new BorderLayout());
		jp.add(respond, BorderLayout.SOUTH); jp.add(playerVal, BorderLayout.NORTH);
		
		entry.add( jp, BorderLayout.SOUTH );

		add( entry, ENTRY );
	}

	private void createFinalPanel() {
		finalpanel = new JPanel( new BorderLayout() );
		JPanel info = new JPanel();
		BoxLayout horiz = new BoxLayout( info, BoxLayout.X_AXIS );
		info.setLayout( horiz );
		finalCat = new JLabel( "", JLabel.LEFT );
		finalCat.setFont( Font.decode( "Arial-BOLD-18" ) );
		finalValue = new JLabel( "", JLabel.RIGHT );
		finalValue.setFont( Font.decode( "Arial-BOLD-18" ) );
		info.add( finalCat );
		info.add( Box.createHorizontalGlue() );
		info.add( finalValue );
		finalText = new JLabel( "", JLabel.CENTER );
		finalText.setFont( Font.decode( "Arial-BOLD-24" ) );
		finalpanel.add( info, BorderLayout.NORTH );
		finalpanel.add( finalText, BorderLayout.CENTER );
		finalButton = new JButton( "Show Answer" );
		finalButton.addActionListener( finalListener );
		finalpanel.add( finalButton, BorderLayout.SOUTH );
		add( finalpanel, FINALPANEL );
	}

	private void createEndGamePanel() {
		endGame = new JPanel( new BorderLayout() );
		congrats = new JLabel(
			"<html><h1>Congratulations!</h1><p align='center'>Thanks for "
				+ "playing this game!</p></html>", JLabel.CENTER );
		endGame.add( congrats, BorderLayout.CENTER );
		add( endGame, ENDGAME );
	}

	private ActionListener finalListener = new ActionListener() {

		public void actionPerformed( ActionEvent evt ) {
			if ( finalButton.getText().equals( "Show Answer" ) ) {
				finalButton.setText( "Show Question" );
				finalCat.setVisible( true );
				finalValue.setVisible( true );
				finalText.setText( g.f.entry.getAnswer() );
			} else if ( finalButton.getText().equals( "Show Question" ) ) {
				finalButton.setText( "End Game" );
				finalText.setText( g.f.entry.getQuestion() );
			} else if ( finalButton.getText().equals( "End Game" ) ) {
				finalButton.setText(  "Show Answer" );
				showPanel( ENDGAME );
			}
		}
	};

	/**
	 * Load a new game
	 * 
	 * @param g
	 *            the game
	 */
	public void loadGame( Game g ) {
		this.g = g;
		finalCat.setText( g.f.name );
		finalValue.setText( "Wager!" );
		finalCat.setVisible( false );
		finalValue.setVisible( false );
		finalText.setText( g.f.name );
		loadRound( 0 );
	}

	/**
	 * Display a specific round
	 * 
	 * @param r
	 */
	public void loadRound( int r ) {
		this.round = r;
		if ( round == Const.FINALROUND ) {
			showPanel( FINALPANEL );
		} else {
			for ( int y = 0; y < cats.length; y++ ) {
				cats[y].setCategory( g.rounds[r].categories[y] );
				for ( int x = 0; x < entries[y].length; x++ ) {
					Category c = cats[y].getCategory();
					entries[y][x].setEntry( c,
						(c == null ? null : c.entries[x]) );
				}
			}
			showPanel( GRID );
		}
	}

	/**
	 * Get the current category of the entry being displayed
	 * 
	 * @return the category
	 */
	public Category getCurrentCategory() {
		return currentCategory;
	}

	/**
	 * Get the current display entry
	 * 
	 * @return the entry
	 */
	public Entry getCurrentEntry() {
		return currentEntry;
	}

	/**
	 * Display an entry
	 * 
	 * @param c
	 *            the category
	 * @param e
	 *            the entry
	 */
	public void displayEntry( Category c, Entry e ) {
		this.currentCategory = c;
		this.currentEntry = e;
		entryCat.setText( c.getName() );
		entryVal.setText( "$" + c.getValue( e ) );
		entryAns.setText( "<html>" + e.getAnswer() + "</html>" );
		playerVal.setText("");
		entryQu = e.getQuestion();
		showPanel( ENTRY );
	}

	private void showPanel( String name ) {
		((CardLayout) getLayout()).show( this, name );
	}

	public void actionPerformed( ActionEvent evt ) {
		if ( respond.getText().equals( "Question" ) ) {
			entryAns.setText( "<html>" + entryQu + "</html>" );
			respond.setText( "Return to Board" );
		} else if ( respond.getText().equals( "Return to Board" ) ) {
			respond.setText( "Question" );
			showPanel( GRID );
		}
	}

	@Override
	public void resetPlayer() {
		playerVal.setText("");
	}

	@Override
	public void signalPlayer(String name) {
		java.awt.Toolkit.getDefaultToolkit().beep();
		playerVal.setText(name);
	}

}
