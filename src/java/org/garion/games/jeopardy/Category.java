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

/**
 * Category of entries in Jeopardy.
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public class Category implements Comparable<Category> {

	/** The maximum number of entries in each category. */
	public static final int MAX_SIZE = 5;

	/** The category name */
	public String name;
	/** The category values */
	public String[] values;
	/** The category entries */
	public Entry[] entries;
	private int size;

	/**
	 * Empty constructor
	 */
	public Category() {
		this( "" );
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the category name
	 */
	public Category( String name ) {
		setName( name );
		entries = new Entry[ MAX_SIZE ];
		values = new String[ MAX_SIZE ];
		size = 0;
	}

	/**
	 * Full constructor
	 * 
	 * @param name
	 *            the category name
	 * @param values
	 *            the value list
	 * @param entries
	 *            the entry list
	 */
	public Category( String name, String[] values, Entry[] entries ) {
		setName( name );
		int vs = values.length;
		int es = entries.length;
		if ( vs == es ) {
			size = vs;
		} else {
			size = Math.min( vs, es );
		}
		this.values = values;
		this.entries = entries;
		verifySize();
	}

	/**
	 * Verify category size. Should be called when values or entries are edited
	 * manually.
	 */
	public void verifySize() {
		// truncate or expand to MAX_SIZE
		if ( size >= MAX_SIZE )
			size = MAX_SIZE;
		values = Arrays.copyOf( values, MAX_SIZE );
		entries = Arrays.copyOf( entries, MAX_SIZE );
		if ( size == 0 ) {
			for ( int i = 0; i < entries.length; i++ ) {
				if ( entries[i] != null ) {
					++size;
				}
			}
		}
	}

	/**
	 * Get the number of entries in this category
	 * 
	 * @return the category size
	 */
	public int size() {
		return size;
	}

	/**
	 * Add an entry to this category
	 * 
	 * @param value
	 *            the entry value
	 * @param entry
	 *            the entry
	 * @return whether the entry was added (there was room)
	 */
	public boolean addEntry( String value, Entry entry ) {
		if ( size < MAX_SIZE ) {
			values[size] = value;
			entries[size] = entry;
			++size;
			return true;
		} else
			return false;
	}

	/**
	 * Set the category name
	 * 
	 * @param name
	 *            the name
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * Get the category name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the list of entries
	 * 
	 * @return the entries
	 */
	public Entry[] getEntries() {
		return entries;
	}

	/**
	 * Get the list of values
	 * 
	 * @return the values
	 */
	public String[] getValues() {
		return values;
	}

	/**
	 * Set the entry of a given value
	 * 
	 * @param value
	 *            the value
	 * @param entry
	 *            the new entry
	 * @return whether the entry was changed (will be {@code false} if no
	 *         matching value was found)
	 */
	public boolean setEntry( String value, Entry entry ) {
		int e = -1;
		for ( int i = 0; i < values.length; i++ ) {
			if ( values[i].startsWith( value ) ) {
				e = i;
				break;
			}
		}
		if ( e != -1 ) {
			entries[e] = entry;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get the entry of a certain value
	 * 
	 * @param value
	 *            the value
	 * @return the matching entry ({@code null} if none found)
	 */
	public Entry getEntry( String value ) {
		int e = -1;
		for ( int i = 0; i < values.length; i++ ) {
			if ( values[i].startsWith( value ) ) {
				e = i;
				break;
			}
		}
		if ( e != -1 )
			return entries[e];
		else
			return null;
	}

	/**
	 * Set the value of a given entry
	 * 
	 * @param e
	 *            the entry
	 * @param value
	 *            the new value
	 * @return whether the value was set (a matching entry was found)
	 */
	public boolean setValue( Entry e, String value ) {
		int v = -1;
		for ( int i = 0; i < entries.length; i++ ) {
			if ( entries[i].equals( e ) ) {
				v = i;
				break;
			}
		}
		if ( v != -1 ) {
			values[v] = value;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get the value of a certain entry
	 * 
	 * @param e
	 *            the entry
	 * @return the value
	 */
	public String getValue( Entry e ) {
		int v = -1;
		for ( int i = 0; i < entries.length; i++ ) {
			if ( entries[i].equals( e ) ) {
				v = i;
				break;
			}
		}
		if ( v != -1 )
			return values[v];
		else
			return null;
	}

	public int compareTo( Category c ) {
		return name.compareTo( c.name );
	}

	/**
	 * Print the contents of this category
	 * 
	 * @param c
	 *            the category
	 * @param indent
	 *            the initial indent
	 */
	public static void print( Category c, String indent ) {
		if ( c == null )
			System.out.printf( "%s- ~ #Category = null%n", indent );
		else {
			System.out.printf( "%s- #Category%n", indent );
			System.out.printf( "%s  name: %s%n", indent, c.name );
			System.out.printf( "%s  values:%n", indent );
			for ( int i = 0; i < c.values.length; i++ ) {
				if ( c.values[i] == null )
					System.out.printf( "%s    - ~ #value = null%n", indent );
				else
					System.out.printf( "%s    - \"%s\"%n", indent, c.values[i] );
			}
			System.out.printf( "%s  entries: #size = %d%n", indent, c.size );
			for ( int i = 0; i < c.entries.length; i++ ) {
				Entry.print( c.entries[i], indent + "    " );
			}
		}
	}

}
