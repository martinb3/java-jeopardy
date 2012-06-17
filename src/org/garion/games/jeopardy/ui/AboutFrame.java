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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.*;
import org.garion.global.Global;
import org.garion.global.ui.UI;

/**
 * A frame displaying information about the Jeopardy program and its sub-tools.
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
@SuppressWarnings( "serial" )
public class AboutFrame extends JFrame {

	/** Name and accessor for "About" tab */
	public static final String ABOUT = "About";
	/** Name and accessor for "ReadMe" tab */
	public static final String README = "ReadMe";
	/** Name and accessor for "License" tab */
	public static final String LICENSE = "License";

	private static AboutFrame frame = null;

	private JPanel main;
	private JButton close;
	private JEditorPane aboutDisplay, licenseDisplay, readmeDisplay;
	private JTabbedPane tab;
	private JScrollPane about, readme, license;

	private AboutFrame() {
		super( "About Java Jeopardy" );
		Global.setSystemUI();
		main = new JPanel( new BorderLayout() );
		// close button
		close = UI.createJButton( "Close", listener );
		JPanel buttons = new JPanel();
		buttons.add( close );
		// main display windows
		createAboutDisplay();
		createReadmeDisplay();
		createLicenseDisplay();
		tab = new JTabbedPane();
		about = new JScrollPane( aboutDisplay );
		readme = new JScrollPane( readmeDisplay );
		license = new JScrollPane( licenseDisplay );
		tab.add( ABOUT, about );
		tab.add( README, readme );
		tab.add( LICENSE, license );
		// top copyright and title
		JLabel title = new JLabel( "Java Jeopardy" );
		JLabel copy = new JLabel( "<html>&copy; 2010 Dallin Lauritzen</html>" );
		JPanel top = new JPanel( new BorderLayout() );
		top.add( title, BorderLayout.WEST );
		top.add( copy, BorderLayout.EAST );
		// add and finish
		main.add( top, BorderLayout.NORTH );
		main.add( tab, BorderLayout.CENTER );
		main.add( buttons, BorderLayout.SOUTH );
		main.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( main );
		setSize( 400, 400 );
		setLocationRelativeTo( null );
	}

	private void showTab( String name ) {
		if ( name.equals( ABOUT ) )
			tab.setSelectedComponent( about );
		else if ( name.equals( README ) )
			tab.setSelectedComponent( readme );
		else if ( name.equals( LICENSE ) )
			tab.setSelectedComponent( license );
	}

	private void createAboutDisplay() {
		aboutDisplay = new JEditorPane();
		aboutDisplay.setEditable( false );
		File aboutFile = new File( "ABOUT.html" );
		try {
			aboutDisplay.setPage( aboutFile.toURI().toURL() );
			aboutDisplay.setCaretPosition( 0 );
		} catch ( IOException ioe ) {
			aboutDisplay.setText( "IOException parsing file "
				+ aboutFile.getAbsolutePath() + ". " + ioe.getMessage() );
		}
	}

	private void createReadmeDisplay() {
		readmeDisplay = new JEditorPane();
		readmeDisplay.setEditable( false );
		File readmeFile = new File( "README.html" );
		try {
			readmeDisplay.setPage( readmeFile.toURI().toURL() );
			readmeDisplay.setCaretPosition( 0 );
		} catch ( IOException ioe ) {
			readmeDisplay.setText( "Could not find README.html. ("
				+ readmeFile.getAbsolutePath() + ") "
				+ "Please visit http://dallin.lauritzenfamily.net/"
				+ "jeopardy/README.html to read it." );
		}
	}

	private void createLicenseDisplay() {
		licenseDisplay = new JEditorPane();
		licenseDisplay.setEditable( false );
		File licenseFile = new File( "GNU_GPL.html" );
		try {
			licenseDisplay.setPage( licenseFile.toURI().toURL() );
			licenseDisplay.setCaretPosition( 0 ); // force scroll to top
		} catch ( IOException ioe ) {
			licenseDisplay.setText( "Could not find GNU_GPL.html license. ("
				+ licenseFile.getAbsolutePath() + ") "
				+ "Please visit http://www.gnu.org/licenses/gpl-3.0-"
				+ "standalone.html to read the license." );
		}
	}

	private ActionListener listener = new ActionListener() {

		// only called by Close button
		public void actionPerformed( ActionEvent e ) {
			frame.setVisible( false );
		}
	};

	/**
	 * Display the About Frame
	 */
	public static void showFrame() {
		if ( frame == null )
			frame = new AboutFrame();
		frame.setVisible( true );
	}

	/**
	 * Display the About Frame, opening a specific tab.
	 * 
	 * @see #ABOUT
	 * @see #README
	 * @see #LICENSE
	 * @param tab
	 *            The name of the tab to display.
	 */
	public static void showFrame( String tab ) {
		showFrame();
		frame.showTab( tab );
	}

}
