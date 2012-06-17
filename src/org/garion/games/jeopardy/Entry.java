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
 * A Jeopardy entry (question/answer pair)
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public class Entry {

	private String a, q;

	/** Empty constructor */
	public Entry() {
		this( "", "" );
	}

	/**
	 * Full constructor
	 * 
	 * @param answer
	 *            the answer
	 * @param question
	 *            the question
	 */
	public Entry( String answer, String question ) {
		setAnswer( answer );
		setQuestion( question );
	}

	/**
	 * Set the answer
	 * 
	 * @param answer
	 *            the new answer
	 */
	public void setAnswer( String answer ) {
		a = answer;
	}

	/**
	 * Set the question
	 * 
	 * @param question
	 *            the new question
	 */
	public void setQuestion( String question ) {
		q = question;
	}

	/**
	 * Get the answer
	 * 
	 * @return the answer
	 */
	public String getAnswer() {
		return a;
	}

	/**
	 * Get the question
	 * 
	 * @return the question
	 */
	public String getQuestion() {
		return q;
	}

	/**
	 * Print this entry, YAML-style
	 * 
	 * @param e
	 *            the entry
	 * @param indent
	 *            the initial indent
	 */
	public static void print( Entry e, String indent ) {
		if ( e == null )
			System.out.printf( "%s- ~ #Entry = null%n", indent );
		else {
			System.out.printf( "%s- #Entry%n", indent );
			System.out.printf( "%s  answer: %s%n", indent, e.a );
			System.out.printf( "%s  question: %s%n", indent, e.q );
		}
	}

}
