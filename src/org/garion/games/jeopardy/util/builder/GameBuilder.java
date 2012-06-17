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
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import org.garion.games.jeopardy.Game;
import org.garion.games.jeopardy.util.IO;
import org.garion.games.jeopardy.util.UnsupportedFileTypeException;
import org.garion.global.Global;

/**
 * Program for building Jeopardy games
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "unused" )
public class GameBuilder {

	private static final int YES = JOptionPane.YES_OPTION;
	private static final int NO = JOptionPane.NO_OPTION;
	private static final int CANCEL = JOptionPane.CANCEL_OPTION;

	private Game game;
	private boolean changed = false;
	private boolean standalone;

	private JFrame frame;
	private JPanel main;
	private JFileChooser chooser;

	private EditPanel ep;

	// menu
	JMenuBar menuBar;
	JMenu file;
	JMenuItem newGame, save, open, exit;
	JMenu round;
	JMenuItem singleRound, doubleRound;

	/**
	 * Create game builder
	 * 
	 * @param standalone
	 *            whether program is running standalone (should exit VM upon
	 *            closing)
	 */
	public GameBuilder( boolean standalone ) {
		Global.setSystemUI();
		this.standalone = standalone;
		game = Game.createDefaultGame();
		chooser = new JFileChooser();
		chooser.setFileFilter( IO.ext );
		frame = new JFrame( "Jeopardy Game Builder" );
		frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		frame.addWindowListener( new WindowAdapter() {

			public void windowClosing( WindowEvent evt ) {
				exit();
			}
		} );
		main = new JPanel( new BorderLayout() );
		ep = new EditPanel( this );
		// add and show
		main.add( ep, BorderLayout.CENTER );
		main.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		frame.getContentPane().add( main );
		frame.setSize( 800, 600 );
		frame.setLocationRelativeTo( null );
		frame.setVisible( true );
		createMenuBar();
	}

	private void createMenuBar() {
		// create
		menuBar = new JMenuBar();
		file = new JMenu( "File" );
		newGame = new JMenuItem( "New" );
		open = new JMenuItem( "Open" );
		save = new JMenuItem( "Save" );
		exit = new JMenuItem( "Exit" );
		round = new JMenu( "Select Round" );
		singleRound = new JMenuItem( "Single Jeopardy" );
		doubleRound = new JMenuItem( "Double Jeopardy" );
		// listeners
		newGame.addActionListener( menuListener );
		open.addActionListener( menuListener );
		save.addActionListener( menuListener );
		exit.addActionListener( menuListener );
		singleRound.addActionListener( menuListener );
		doubleRound.addActionListener( menuListener );
		// add
		file.add( newGame );
		file.add( open );
		file.add( save );
		file.addSeparator();
		file.add( exit );
		round.add( singleRound );
		round.add( doubleRound );
		menuBar.add( file );
		menuBar.add( round );
		frame.setJMenuBar( menuBar );
		// accelerators
		newGame.setAccelerator( KeyStroke.getKeyStroke( 'N',
			InputEvent.CTRL_DOWN_MASK ) );
		open.setAccelerator( KeyStroke.getKeyStroke( 'O',
			InputEvent.CTRL_DOWN_MASK ) );
		save.setAccelerator( KeyStroke.getKeyStroke( 'S',
			InputEvent.CTRL_DOWN_MASK ) );
		exit.setAccelerator( KeyStroke.getKeyStroke( 'Q',
			InputEvent.CTRL_DOWN_MASK ) );
		singleRound.setAccelerator( KeyStroke.getKeyStroke( 'S',
			InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK ) );
		doubleRound.setAccelerator( KeyStroke.getKeyStroke( 'D',
			InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK ) );
		// mnemonics
		file.setMnemonic( 'f' );
		newGame.setMnemonic( 'n' );
		open.setMnemonic( 'o' );
		save.setMnemonic( 's' );
		exit.setMnemonic( 'x' );
		round.setMnemonic( 'r' );
		singleRound.setMnemonic( 's' );
		doubleRound.setMnemonic( 'd' );
	}

	/**
	 * Get the game being built
	 * 
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Save the current game to file
	 * 
	 * @param f
	 *            the file
	 * @return success
	 */
	public boolean save( File f ) {
		String ext = IO.extension( f );
		try {
			if ( ext.equalsIgnoreCase( "yaml" ) ) {
				return IO.dumpYaml( game, f );
			} else {
				// unsupported format
				return false;
			}
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
			return false;
		}
	}

	/**
	 * Exit the program
	 */
	public void exit() {
		if ( changed ) {
			int save = askSave();
			if ( save == YES ) {
				if ( showSave() )
					System.exit( 0 );
			} else if ( save == NO ) {
				if ( standalone )
					System.exit( 0 );
				else
					frame.dispose();
			}
		} else {
			if ( standalone )
				System.exit( 0 );
			else
				frame.dispose();
		}
	}

	private boolean showSave() {
		game.setTitle( ep.gameTitleField.getText() );
		game.setSubject( ep.gameSubjectField.getText() );
		game.f.name = ep.finalCatField.getText();
		int s = chooser.showSaveDialog( frame );
		if ( s == JFileChooser.APPROVE_OPTION ) {
			File f = chooser.getSelectedFile();
			if ( !IO.supported( f ) ) {
				JOptionPane.showMessageDialog( frame,
					"Only YAML files are supported.",
					"Unsupported File Format", JOptionPane.ERROR_MESSAGE );
				return showSave();
			}
			try {
				if ( IO.extension( f, "yaml" ) )
					IO.dumpYaml( game, f );
			} catch ( IOException ioe ) {
				JOptionPane.showMessageDialog( frame, "Exception saving file "
					+ f.getAbsolutePath() + ". " + ioe.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE );
				return false;
			}
			setChanged( false );
			return true;
		} else {
			return false;
		}
	}

	private void showOpen() {
		int s = chooser.showOpenDialog( frame );
		if ( s == JFileChooser.APPROVE_OPTION ) {
			File f = chooser.getSelectedFile();
			if ( !IO.supported( f ) ) {
				JOptionPane.showMessageDialog( frame, "Unsupported File Type.",
					"Could Not Open", JOptionPane.ERROR_MESSAGE );
			} else {
				try {
					game = IO.detectAndLoad( f );
					ep.loadSingleRound();
					ep.gameTitleField.setText( game.getTitle() );
					ep.gameSubjectField.setText( game.getSubject() );
					ep.finalCatField.setText( game.f.name );
					setChanged( false );
				} catch ( IOException ioe ) {
					JOptionPane.showMessageDialog( frame,
						"Exception loading file " + f.getAbsolutePath() + ". "
							+ ioe.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE );
				} catch ( UnsupportedFileTypeException ufte ) {
					JOptionPane.showMessageDialog( frame, ufte.getMessage(),
						"Unsupported File", JOptionPane.ERROR_MESSAGE );
				}
			}
		}
	}

	private void loadNew() {
		game = Game.createDefaultGame();
		ep.loadSingleRound();
		ep.finalCatField.setText( game.f.name );
		ep.gameSubjectField.setText( game.subject );
		ep.gameTitleField.setText( game.title );
		setChanged( false );
	}

	private int askSave() {
		return JOptionPane.showConfirmDialog( frame, "Save Changes?", "Save",
			JOptionPane.YES_NO_CANCEL_OPTION );
	}

	/**
	 * Set the changed parameter
	 * 
	 * @param c
	 *            whether file data has been changed since last save/load
	 */
	public void setChanged( boolean c ) {
		changed = c;
		frame.setTitle( String.format( "%s %s", "Jeopardy Game Builder",
			changed ? "*" : "" ) );
	}

	private ActionListener menuListener = new ActionListener() {

		public void actionPerformed( ActionEvent evt ) {
			String command = evt.getActionCommand();
			if ( command.equals( "New" ) ) {
				if ( changed ) {
					int save = askSave();
					if ( save == YES ) {
						if ( showSave() )
							loadNew();
					} else if ( save == NO )
						loadNew();
				} else
					loadNew();
			} else if ( command.equals( "Open" ) ) {
				if ( changed ) {
					int save = askSave();
					if ( save == YES ) {
						if ( showSave() )
							showOpen();
					} else if ( save == NO )
						showOpen();
				} else
					showOpen();
			} else if ( command.equals( "Save" ) ) {
				showSave();
			} else if ( command.equals( "Exit" ) ) {
				exit();
			} else if ( command.equals( "Single Jeopardy" ) ) {
				ep.loadSingleRound();
			} else if ( command.equals( "Double Jeopardy" ) ) {
				ep.loadDoubleRound();
			}
		}
	};

	/**
	 * Run the program standalone
	 * 
	 * @param args
	 *            command-line arguments (ignored)
	 */
	public static void main( String[] args ) {
		new GameBuilder( true );
	}

}
