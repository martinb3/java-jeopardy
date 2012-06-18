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
import org.garion.games.jeopardy.Game;
import org.garion.games.jeopardy.Round;
import org.garion.games.jeopardy.util.Const;
import org.garion.global.ui.Layout;

/**
 * Panel for editing a jeopardy game
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "serial" )
public class EditPanel extends JPanel {

	/** Displayed instructions for the editor */
	public static final String instructions = "<html>"
		+ "<table border='1' align='center' width='100%'>" + "	<tr>"
		+ "		<td align='center'>" + "<h3>Jeopardy Game Builder</h3>"
		+ "<p>Use this tool to create a new Jeopardy Game.</p>" + "		</td>"
		+ "	</tr>" + "</table>" + "</html>";

	private final GameBuilder builder;

	private int round = Const.SINGLEROUND;

	private JPanel grid;
	private CEditPanel[] cats;
	private EEditPanel[][] entries;

	private JPanel fieldPanel;
	/** Editor for the game's title */
	public JTextField gameTitleField;
	/** Editor for the game's subject */
	public JTextField gameSubjectField;
	/** Editor for the final jeopardy category */
	public JTextField finalCatField;

	/**
	 * Create a new panel for the given game builder
	 * 
	 * @param b
	 *            the builder
	 */
	public EditPanel( GameBuilder b ) {
		super( new BorderLayout() );
		builder = b;
		createGridPanel();
		createFieldPanel();
		add( grid, BorderLayout.CENTER );
		JPanel north = new JPanel( new GridLayout( 1, 0 ) );
		north.add( fieldPanel );
		JLabel i = new JLabel( instructions, JLabel.CENTER );
		north.add( i );
		add( north, BorderLayout.NORTH );
	}

	/**
	 * get the currently-displayed round
	 * 
	 * @return the round
	 */
	public int getRound() {
		return round;
	}

	private Game game() {
		return builder.getGame();
	}

	private void loadRound( int round ) {
		this.round = round;
		Round r = game().rounds[round];
		for ( int y = 0; y < cats.length; y++ ) {
			cats[y].setCategory( r.categories[y] );
			for ( int x = 0; x < entries[y].length; x++ ) {
				entries[y][x].setEntry( r.categories[x],
					r.categories[x].entries[y] );
			}
		}
	}

	/**
	 * Load single jeopardy
	 */
	public void loadSingleRound() {
		loadRound( Const.SINGLEROUND );
	}

	/**
	 * Load double jeopardy
	 */
	public void loadDoubleRound() {
		loadRound( Const.DOUBLEROUND );
	}

	private void createGridPanel() {
		grid = new JPanel( new GridLayout( 6, 5 ) );
		cats = new CEditPanel[ 5 ];
		entries = new EEditPanel[ 5 ][ 5 ];
		for ( int i = 0; i < cats.length; i++ ) {
			cats[i] = new CEditPanel(
				game().rounds[Const.SINGLEROUND].categories[i], this );
			grid.add( cats[i] );
		}
		Round r = game().rounds[round];
		for ( int y = 0; y < cats.length; y++ ) {
			for ( int x = 0; x < entries[y].length; x++ ) {
				entries[y][x] = new EEditPanel( r.categories[x],
					r.categories[x].entries[y], this );
				grid.add( entries[y][x] );
			}
		}
	}

	/**
	 * Get the game builder
	 * 
	 * @return the builder
	 */
	public GameBuilder getBuilder() {
		return builder;
	}

	private void createFieldPanel() {
		fieldPanel = new JPanel( new GridBagLayout() );
		GridBagConstraints gc = Layout.makeDefaultGC();
		gameTitleField = new JTextField( game().getTitle() );
		gameSubjectField = new JTextField( game().getSubject() );
		finalCatField = new JTextField( game().f.name );
		final JButton finalButton = new JButton( "Edit Final Jeopardy Entry" );
		finalButton.addActionListener( new ActionListener() {

			public void actionPerformed( ActionEvent evt ) {
				String[] values = EntryEditDialog.showDialog( fieldPanel,
					game().f );
				for ( int i = 0; i < values.length; i++ ) {
					if ( values[i] == null )
						continue;
					switch ( i ) {
						case EntryEditDialog.ANSWER:
							game().f.entry.setAnswer( values[i] );
							break;
						case EntryEditDialog.QUESTION:
							game().f.entry.setQuestion( values[i] );
							break;
					}
					builder.setChanged( true );
				}
			}
		} );
		Layout.addItem( 0, 0, 0.50, 0.25, 1, 1, 0, fieldPanel, gc, new JLabel(
			"Game Title: " ) );
		Layout.addItem( 0, 1, 0.50, 0.75, 1, 1, 0, fieldPanel, gc,
			gameTitleField );
		Layout.addItem( 1, 0, 0.50, 0.25, 1, 1, 0, fieldPanel, gc, new JLabel(
			"Game Subject: " ) );
		Layout.addItem( 1, 1, 0.50, 0.75, 1, 1, 0, fieldPanel, gc,
			gameSubjectField );
		Layout.addItem( 2, 0, 0.50, 0.25, 1, 1, 0, fieldPanel, gc, new JLabel(
			"Final Category: " ) );
		Layout.addItem( 2, 1, 0.50, 0.75, 1, 1, 0, fieldPanel, gc,
			finalCatField );
		Layout.addItem( 3, 0, 0.50, 1.00, 2, 1, 1, fieldPanel, gc, finalButton );
	}

}
