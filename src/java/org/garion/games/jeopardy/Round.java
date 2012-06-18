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
import java.util.Comparator;

/**
 * A round of Jeopardy (single/double)
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public class Round {

	/** The maximum allowed number of categories */
	public static final int MAX_SIZE = 5;

	/** the name of the round */
	public String name;
	/** the list of categories */
	public Category[] categories;
	private int size;

	/**
	 * Basic empty-name round
	 */
	public Round() {
		this( "" );
	}

	/**
	 * Create a named round
	 * 
	 * @param name
	 */
	public Round( String name ) {
		setName( name );
		categories = new Category[ MAX_SIZE ];
		size = 0;
	}

	/**
	 * Create a named round with the given list of categories
	 * 
	 * @param name
	 *            the name
	 * @param cats
	 *            the categories
	 */
	public Round( String name, Category... cats ) {
		setName( name );
		categories = cats;
		size = cats.length;
		verifySize();
	}

	/**
	 * Verify that the category list fits within limits (should be called when
	 * categories is changed manually)
	 */
	public void verifySize() {
		categories = Arrays.copyOf( categories, MAX_SIZE );
		size = Math.min( MAX_SIZE, size );
		for ( int i = 0; i < categories.length; i++ ) {
			if ( categories[i] != null )
				categories[i].verifySize();
		}
		if ( size == 0 ) {
			for ( int i = 0; i < categories.length; i++ ) {
				if ( categories[i] != null ) {
					++size;
				}
			}
		}
	}

	/**
	 * Set the round name
	 * 
	 * @param name
	 *            the name
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * Get the round name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the number of categories in the round
	 * 
	 * @return the number of categories
	 */
	public int size() {
		return size;
	}

	/**
	 * Add a category to the round
	 * 
	 * @param c
	 *            the category
	 * @return whether the category was added (there was room)
	 */
	public boolean addCategory( Category c ) {
		if ( size < MAX_SIZE ) {
			categories[size] = c;
			++size;
			Arrays.sort( categories, 0, size, COMPARATOR );
			return true;
		} else
			return false;
	}

	/**
	 * Get a category by name
	 * 
	 * @param name
	 *            the name of the category
	 * @return the category, or {@code null} if none found
	 */
	public Category getCategory( String name ) {
		for ( int i = 0; i < categories.length; i++ ) {
			if ( categories[i].getName().equals( name ) )
				return categories[i];
		}
		return null;
	}

	/**
	 * Get the list of categories
	 * 
	 * @return the categories
	 */
	public Category[] getCategoryList() {
		return categories;
	}

	/**
	 * category list comparator
	 */
	public static final Comparator<Category> COMPARATOR = new Comparator<Category>() {

		public int compare( Category o1, Category o2 ) {
			return o1.compareTo( o2 );
		}

	};

	/**
	 * Print the contents of this round, YAML-style
	 * 
	 * @param r
	 *            the round to print
	 * @param indent
	 *            the initial indent for this round
	 */
	public static void print( Round r, String indent ) {
		if ( r == null )
			System.out.printf( "%s- ~ #Round = null%n", indent );
		else {
			System.out.printf( "%s- #Round%n", indent );
			System.out.printf( "%s  name: %s%n", indent, r.name );
			System.out.printf( "%s  categories: #size= %d%n", indent, r.size );
			for ( int i = 0; i < r.categories.length; i++ ) {
				Category.print( r.categories[i], indent + "    " );
			}
		}
	}

}
