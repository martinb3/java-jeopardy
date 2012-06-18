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
package org.garion.games.jeopardy.util;

import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.garion.games.jeopardy.Game;
import org.ho.yaml.Yaml;

/**
 * Input/Output functions for the Jeopardy package
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public final class IO {

	/** Filter for supported file types */
	public static final FileNameExtensionFilter ext = new FileNameExtensionFilter(
		"Supported Files (*.yaml)", "yaml" );
	/** Filter for YAML files */
	public static final FileNameExtensionFilter yaml = new FileNameExtensionFilter(
		"YAML Markup Files (*.yaml)", "yaml" );

	private IO() {}

	/**
	 * Get the extension part of a file name
	 * 
	 * @param f
	 *            the file
	 * @return the extension (part of file name following last .)
	 */
	public static String extension( File f ) {
		int i = f.getName().lastIndexOf( "." );
		if ( i != -1 )
			return f.getName().substring( i + 1 );
		else
			return "";
	}

	/**
	 * Test whether a given file is one of the given extensions
	 * 
	 * @param f
	 *            the file
	 * @param exts
	 *            the varargs list of extensions
	 * @return whether the file matched one of the extensions
	 */
	public static boolean extension( File f, String... exts ) {
		String e = extension( f );
		for ( int i = 0; i < exts.length; i++ ) {
			if ( e.equalsIgnoreCase( exts[i] ) )
				return true;
		}
		return false;
	}

	/**
	 * Whether the file is accepted by the supported file filter
	 * 
	 * @param f
	 *            the file
	 * @return if the file is supported or not
	 */
	public static boolean supported( File f ) {
		return ext.accept( f );
	}

	/**
	 * Detect the file type and load accordingly
	 * 
	 * @param in
	 *            the file to load
	 * @return the created Game
	 * @throws IOException
	 *             If an error occurs while parsing the file
	 * @throws UnsupportedFileTypeException
	 *             whether the file type was not supported
	 */
	public static final Game detectAndLoad( File in ) throws IOException,
		UnsupportedFileTypeException {
		BufferedReader reader = new BufferedReader( new FileReader( in ) );
		String firstLine = reader.readLine();
		if ( firstLine.startsWith( "---" ) )
			return loadYaml( in );
		else {
			throw new UnsupportedFileTypeException(
				"Only YAML file types are supported." );
		}
	}

	/**
	 * Load a YAML-formatted file
	 * 
	 * @param in
	 *            the file
	 * @return the created game
	 * @throws IOException
	 *             if an error occurs while loading file
	 */
	public static final Game loadYaml( File in ) throws IOException {
		Game g = Yaml.loadType( in, Game.class );
		g.verifySize();
		return g;
	}

	/**
	 * Save a game in YAML-formatted file
	 * 
	 * @param g
	 *            the game
	 * @param out
	 *            the output file
	 * @return success
	 * @throws IOException
	 *             if an error occurs while writing
	 */
	public static final boolean dumpYaml( Game g, File out ) throws IOException {
		Yaml.dump( g, out, true );
		return true;
	}

	/**
	 * Get YAML-formatted text for a game (rather than writing to a file)
	 * 
	 * @param g
	 *            the game
	 * @return the string contents
	 * @throws IOException
	 *             if thrown by Yaml.dump( Object )
	 */
	public static final String yamlString( Game g ) throws IOException {
		String ret = Yaml.dump( g );
		return ret;
	}

}
