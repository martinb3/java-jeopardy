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

import java.text.NumberFormat;
import java.util.Locale;

/**
 * A Player has a name and a score. This class provides methods to manipulate a
 * score.
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public class Player implements Comparable<Player> {

	private static NumberFormat currency = NumberFormat
		.getCurrencyInstance( Locale.US );
	private static NumberFormat plain = NumberFormat
		.getIntegerInstance( Locale.US );

	/** The player name */
	public String name;
	/** The player score */
	public int score;

	/**
	 * Create a new player with empty name and 0 score
	 */
	public Player() {
		this( "", 0 );
	}

	/**
	 * Create a new player
	 * 
	 * @param name
	 *            the player name
	 * @param initScore
	 *            the initial player score
	 */
	public Player( String name, int initScore ) {
		setName( name );
		setScore( initScore );
	}

	/**
	 * Set the player name
	 * 
	 * @param name
	 *            the new name
	 * @return the old name
	 */
	public String setName( String name ) {
		String ret = this.name;
		this.name = name;
		return ret;
	}

	/**
	 * Set the player score
	 * 
	 * @param score
	 *            the new score
	 * @return the old score
	 */
	public int setScore( int score ) {
		int ret = this.score;
		this.score = score;
		return ret;
	}

	/**
	 * Get the player name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the player score
	 * 
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Add a value to the player score
	 * 
	 * @param value
	 *            the value to add
	 * @return the new score
	 */
	public int add( int value ) {
		setScore( score + value );
		return score;
	}

	/**
	 * Subtract a value from the player score
	 * 
	 * @param value
	 *            the value to subtract
	 * @return the new score
	 */
	public int subtract( int value ) {
		setScore( score - value );
		return score;
	}

	/**
	 * Get a string to display this player's score as currency in the US locale
	 * (i.e. $1,242.00)
	 * 
	 * @return The string
	 */
	public String asMoney() {
		return currency.format( score );
	}

	/**
	 * Get a string to display this player's score as a comma-separated integer
	 * in the US locale (i.e. 1,242)
	 * 
	 * @return The string
	 */
	public String asNumber() {
		if ( !plain.isGroupingUsed() )
			plain.setGroupingUsed( true );
		return plain.format( score );
	}

	public int compareTo( Player p ) {
		return name.compareTo( p.name );
	}

	public boolean equals( Object o ) {
		if ( o instanceof String ) {
			return name.equals( o );
		} else if ( o instanceof Player ) {
			if ( name.equals( ((Player) o).name ) )
				return score == ((Player) o).score;
			else
				return false;
		} else
			return false;
	}

}
