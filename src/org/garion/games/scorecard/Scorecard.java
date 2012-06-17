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

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import org.garion.games.jeopardy.util.IO;
import org.garion.global.Global;
import org.garion.global.ui.UI;
import org.ho.yaml.Yaml;

/**
 * Simple scorecard keeping program. Allows a managed list of players.F
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "serial" )
public class Scorecard extends JFrame implements ActionListener {

	private static JFileChooser chooser = new JFileChooser();

	/** The list of players */
	public PlayerList players;
	private boolean changed = false;
	private boolean standalone = false;

	private JTable table;
	private PlayerModel model;

	private JPanel main;

	private JButton add, remove, edit, score;

	/**
	 * Create and show the score card program
	 * 
	 * @param standalone
	 *            whether the program is standalone (and should exit VM upon
	 *            closing)
	 */
	public Scorecard( boolean standalone ) {
		super( "Score Card" );
		Global.setSystemUI();
		players = new PlayerList();
		this.standalone = standalone;
		this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		this.addWindowListener( windowListener );
		main = new JPanel( new BorderLayout() );
		model = new PlayerModel( this );
		table = new JTable( model );
		table.setAutoCreateColumnsFromModel( true );
		add = UI.createJButton( "Add Player", this );
		remove = UI.createJButton( "Remove Player", this );
		edit = UI.createJButton( "Edit Player", this );
		score = UI.createJButton( "Change Score", this );
		JPanel buttons = new JPanel();
		BoxLayout box = new BoxLayout( buttons, BoxLayout.Y_AXIS );
		buttons.setLayout( box );
		buttons.add( Box.createVerticalGlue() );
		buttons.add( add );
		buttons.add( remove );
		buttons.add( edit );
		buttons.add( Box.createVerticalGlue() );
		buttons.add( score );
		buttons.add( Box.createVerticalGlue() );

		createMenu();
		main.add( buttons, BorderLayout.EAST );
		main.add( new JScrollPane( table ), BorderLayout.CENTER );
		this.getContentPane().add( main );
		this.setSize( 300, 300 );
		this.setLocationRelativeTo( null );
		this.setVisible( true );
		chooser.setFileFilter( IO.yaml );
	}

	private ActionListener menuListener = new ActionListener() {

		public void actionPerformed( ActionEvent evt ) {
			String command = evt.getActionCommand();
			if ( "New List".equals( command ) ) {
				if ( changed ) {
					if ( !askSave() )
						return;
				}
				players = new PlayerList();
				model.update();
				setChanged( false );
			} else if ( "Save List".equals( command ) ) {
				save();
			} else if ( "Load List".equals( command ) ) {
				if ( changed ) {
					if ( !askSave() )
						return;
				}
				load();
			} else if ( "Exit".equals( command ) ) {
				exit();
			}
		}
	};

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu( "File" );
		JMenuItem newItem = new JMenuItem( "New List" );
		JMenuItem saveItem = new JMenuItem( "Save List" );
		JMenuItem loadItem = new JMenuItem( "Load List" );
		JMenuItem exit = new JMenuItem( "Exit" );
		file.add( newItem );
		file.add( saveItem );
		file.add( loadItem );
		file.addSeparator();
		file.add( exit );
		menuBar.add( file );
		this.setJMenuBar( menuBar );
		// listeners
		newItem.addActionListener( menuListener );
		saveItem.addActionListener( menuListener );
		loadItem.addActionListener( menuListener );
		exit.addActionListener( menuListener );
		// accelerators
		newItem.setAccelerator( KeyStroke.getKeyStroke( 'N',
			InputEvent.CTRL_DOWN_MASK ) );
		saveItem.setAccelerator( KeyStroke.getKeyStroke( 'S',
			InputEvent.CTRL_DOWN_MASK ) );
		loadItem.setAccelerator( KeyStroke.getKeyStroke( 'O',
			InputEvent.CTRL_DOWN_MASK ) );
		exit.setAccelerator( KeyStroke.getKeyStroke( 'Q',
			InputEvent.CTRL_DOWN_MASK ) );
		// mnemonics
		file.setMnemonic( 'f' );
		newItem.setMnemonic( 'n' );
		saveItem.setMnemonic( 's' );
		loadItem.setMnemonic( 'o' );
		exit.setMnemonic( 'x' );
		// other funs
		file.getAccessibleContext().setAccessibleDescription(
			"Menu items to open, save, and exit." );
	}

	/**
	 * Add a player to the list
	 * 
	 * @param p
	 *            the player to add
	 */
	public void addPlayer( Player p ) {
		setChanged( true );
		players.add( p );
		players.sort();
		model.update();
	}

	/**
	 * Remove a player from the list
	 * 
	 * @param p
	 *            the player to remove
	 */
	public void removePlayer( Player p ) {
		setChanged( true );
		players.remove( p );
		model.update();
	}

	// precondition: changed = true
	private boolean askSave() {
		int s = JOptionPane.showConfirmDialog( this, "Save Changes?", "Save",
			JOptionPane.YES_NO_CANCEL_OPTION );
		if ( s == JOptionPane.YES_OPTION )
			return save();
		else if ( s == JOptionPane.NO_OPTION )
			return true;
		else
			return false;
	}

	private boolean save() {
		int s = chooser.showSaveDialog( this );
		if ( s == JFileChooser.APPROVE_OPTION ) {
			File f = chooser.getSelectedFile();
			try {
				Yaml.dump( players, f, true );
				setChanged( false );
				return true;
			} catch ( FileNotFoundException e ) {
				return false;
			}
		} else
			return false;
	}

	private void load() {
		int s = chooser.showOpenDialog( this );
		if ( s == JFileChooser.APPROVE_OPTION ) {
			File f = chooser.getSelectedFile();
			try {
				players = Yaml.loadType( f, PlayerList.class );
				players.findSize();
				model.update();
				setChanged( false );
			} catch ( FileNotFoundException e ) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Run the score card program standalone
	 * 
	 * @param args
	 *            command-line arguments, ignored
	 */
	public static void main( String[] args ) {
		new Scorecard( true );
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		JButton source = (JButton) e.getSource();
		if ( source == add ) {
			Player p = new Player( "", 0 );
			int s = PlayerEditDialog.showDialog( source, p );
			if ( s == PlayerEditDialog.OK )
				addPlayer( p );
		} else if ( source == remove ) {
			try {
				Player p = players.get( table.getSelectedRow() );
				removePlayer( p );
			} catch ( IndexOutOfBoundsException iobe ) {
			}
		} else if ( source == edit ) {
			try {
				Player p = players.get( table.getSelectedRow() );
				int s = PlayerEditDialog.showDialog( source, p );
				if ( s == PlayerEditDialog.OK ) {
					model.update();
					setChanged( true );
				}
			} catch ( IndexOutOfBoundsException iobe ) {
			}
		} else if ( source == score ) {
			try {
				Player p = players.get( table.getSelectedRow() );
				int s = ScoreEditDialog.showDialog( source, p );
				if ( s == ScoreEditDialog.OK ) {
					model.update();
					setChanged( true );
				}
			} catch ( IndexOutOfBoundsException iobe ) {
			}
		}
	}

	private void setChanged( boolean c ) {
		changed = c;
		setTitle( String.format( "Score Card %s", changed ? "*" : "" ) );
	}

	private void exit() {
		if ( standalone ) {
			if ( changed ) {
				if ( askSave() )
					System.exit( 0 );
			} else
				System.exit( 0 );
		} else
			dispose();
	}

	private WindowAdapter windowListener = new WindowAdapter() {

		public void windowClosing( WindowEvent evt ) {
			exit();
		}
	};

}

/**
 * Table model to display a list of Players
 * 
 * @author Dallin Lauritzen
 */
@SuppressWarnings( "serial" )
class PlayerModel extends AbstractTableModel {

	private final Scorecard card;

	private final static String[] colNames = new String[] { "Name", "Score" };

	public PlayerModel( Scorecard card ) {
		this.card = card;
	}

	public void update() {
		this.fireTableDataChanged();
	}

	public boolean isCellEditable( int row, int col ) {
		return false;
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	public String getColumnName( int col ) {
		try {
			return colNames[col];
		} catch ( IndexOutOfBoundsException iobe ) {
			return "IOBE " + col;
		}
	}

	@Override
	public int getRowCount() {
		return card.players.size();
	}

	public Class<?> getColumnClass( int col ) {
		return String.class;
	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex ) {
		try {
			Player p = card.players.get( rowIndex );
			switch ( columnIndex ) {
				case 0:
					return p.getName();
				case 1:
					return p.asNumber();
				default:
					return "ERR Col Value: " + columnIndex;
			}
		} catch ( IndexOutOfBoundsException iobe ) {
			return String.format( "IOBE R%dC%d", rowIndex, columnIndex );
		}
	}

}
