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
package org.garion.games.jeopardy;

/**
 * A Final Jeopardy question
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public class Final {

	/** The category */
	public String name;
	/** The entry */
	public Entry entry;

	/**
	 * Empty constructor
	 */
	public Final() {
		this( "", null );
	}

	/**
	 * Real constructor
	 * 
	 * @param name
	 *            the category/name
	 * @param entry
	 *            the entry
	 */
	public Final( String name, Entry entry ) {
		this.name = name;
		this.entry = entry;
	}

	/**
	 * Set the name
	 * 
	 * @param name
	 *            the name
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * Set the entry
	 * 
	 * @param e
	 *            the entry
	 */
	public void setEntry( Entry e ) {
		entry = e;
	}

	/**
	 * Print this question, YAML-style
	 * 
	 * @param f
	 *            the final to print
	 * @param indent
	 *            the initial indent
	 */
	public static void print( Final f, String indent ) {
		if ( f == null )
			System.out.printf( "%s- #Final = null%n", indent );
		else {
			System.out.printf( "%s- #Final%n", indent );
			System.out.printf( "%s  name: %s%n", indent, f.name );
			System.out.printf( "%s  entry:%n", indent );
			System.out.printf( "%s    answer: %s%n", indent, f.entry
				.getAnswer() );
			System.out.printf( "%s    question: %s%n", indent, f.entry
				.getQuestion() );
		}
	}

}
