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

import java.util.Arrays;
import org.garion.games.jeopardy.util.Const;

/**
 * A Jeopardy Game
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public class Game {

	/** The maximum number of rounds */
	public static final int MAX_ROUNDS = 2;

	/** The game title */
	public String title;
	/** The game subject/theme */
	public String subject;
	/** The list of rounds */
	public Round[] rounds;
	/** The final jeopardy question */
	public Final f;
	private int size;

	/**
	 * Basic empty game
	 */
	public Game() {
		this( "", "" );
	}

	/**
	 * Create a game with the given title/subject
	 * 
	 * @param title
	 *            the game title
	 * @param subject
	 *            the game subject
	 */
	public Game( String title, String subject ) {
		setTitle( title );
		setSubject( subject );
		rounds = new Round[ MAX_ROUNDS ];
		f = null;
		size = 0;
	}

	/**
	 * Verify the size of the rounds list (used when rounds is edited manually)
	 */
	public void verifySize() {
		size = Math.min( size, MAX_ROUNDS );
		rounds = Arrays.copyOf( rounds, MAX_ROUNDS );
		for ( int i = 0; i < rounds.length; i++ ) {
			if ( rounds[i] != null )
				rounds[i].verifySize();
		}
		if ( size == 0 ) {
			for ( int i = 0; i < rounds.length; i++ ) {
				if ( rounds[i] != null ) {
					++size;
				}
			}
		}
	}

	/**
	 * Add a round to the list
	 * 
	 * @param r
	 *            the round
	 * @return whether the round was added (there was room)
	 */
	public boolean addRound( Round r ) {
		if ( size < MAX_ROUNDS ) {
			rounds[size] = r;
			++size;
			return true;
		} else
			return false;
	}

	/**
	 * Get a round by name
	 * 
	 * @param name
	 *            the round's name
	 * @return the round, or {@code null} if none found
	 */
	public Round getRound( String name ) {
		for ( int i = 0; i < rounds.length; i++ ) {
			if ( rounds[i].getName().equals( name ) )
				return rounds[i];
		}
		return null;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle( String title ) {
		this.title = title;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject( String subject ) {
		this.subject = subject;
	}

	/**
	 * @return the rounds
	 */
	public Round[] getRounds() {
		return rounds;
	}

	/**
	 * @return the final round
	 */
	public Final getFinalRound() {
		return f;
	}

	/**
	 * @param f
	 *            the final round to set
	 */
	public void setFinalRound( Final f ) {
		this.f = f;
	}

	/**
	 * Create a game where all fields are default
	 * 
	 * @return a new game
	 */
	public static Game createDefaultGame() {
		Game g = new Game();
		g.setTitle( "" );
		g.setSubject( "" );
		for ( int r = 0; r < MAX_ROUNDS; r++ ) {
			Round round = new Round();
			switch ( r ) {
				case Const.SINGLEROUND:
					round.setName( "Single Jeopardy" );
					break;
				case Const.DOUBLEROUND:
					round.setName( "Double Jeopardy" );
					break;
				default:
					round.setName( "Uh..." );
			}
			for ( int c = 0; c < Round.MAX_SIZE; c++ ) {
				Category cat = new Category( Const.DEFAULT_CATS[r][c] );
				for ( int ev = 0; ev < Category.MAX_SIZE; ev++ ) {
					Entry ent = new Entry( "answer", "question" );
					cat.addEntry( Const.DEFAULT_VALUES[r][ev], ent );
				}
				round.addCategory( cat );
			}
			g.addRound( round );
		}
		Final f = new Final( Const.DEFAULT_CATS[Const.FINALROUND][0],
			new Entry( "answer", "question" ) );
		g.setFinalRound( f );
		return g;
	}

	/**
	 * Print the contents of a game, YAML-style
	 * 
	 * @param g
	 *            the game to print
	 */
	public static void print( Game g ) {
		if ( g == null )
			System.out.printf( "--- #Game = null%n" );
		else {
			System.out.printf( "--- #Game%n" );
			System.out.printf( "title: %s%n", g.title );
			System.out.printf( "subject: %s%n", g.subject );
			System.out.printf( "rounds:%n" );
			for ( int i = 0; i < g.rounds.length; i++ ) {
				Round.print( g.rounds[i], "  " );
			}
			System.out.printf( "finalRound: %n" );
			Final.print( g.f, "  " );
		}
	}

}
