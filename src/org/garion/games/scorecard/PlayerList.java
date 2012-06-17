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

import java.util.Arrays;

/**
 * Minimally copies {@link java.util.ArrayList}. Meant for YAML serialization.
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public class PlayerList {

	private static final int INIT_SIZE = 10;

	/** The list of players */
	public Player[] list;
	private int size;

	/**
	 * Create a new playerlist
	 */
	public PlayerList() {
		list = new Player[ INIT_SIZE ];
		size = 0;
	}

	/**
	 * Discover the content size. For use after setting the list array manually.
	 */
	public void findSize() {
		for ( int i = 0; i < list.length; i++ ) {
			if ( list[i] == null ) {
				size = i;
				return;
			}
		}
		size = list.length;
	}

	/**
	 * Add a player to the list
	 * 
	 * @param p
	 *            the player
	 */
	public void add( Player p ) {
		checkSize( size + 1 );
		list[size++] = p;
	}

	/**
	 * Get the Player at given index
	 * 
	 * @param index
	 *            the index
	 * @return the Player
	 */
	public Player get( int index ) {
		checkRange( index );
		return list[index];
	}

	/**
	 * Sort the array
	 */
	public void sort() {
		Arrays.sort( list, 0, size );
	}

	/**
	 * Taken from ArrayList
	 * 
	 * @param p
	 * @return status
	 * @see java.util.ArrayList#remove(Object)
	 */
	public boolean remove( Player p ) {
		// taken from ArrayList.remove( Object )
		if ( p == null ) {
			for ( int index = 0; index < size; index++ )
				if ( list[index] == null ) {
					fastRemove( index );
					return true;
				}
		} else {
			for ( int index = 0; index < size; index++ )
				if ( p.equals( list[index] ) ) {
					fastRemove( index );
					return true;
				}
		}
		return false;
	}

	/**
	 * @see java.util.ArrayList#trimToSize()
	 */
	public void trimToSize() {
		// copied from ArrayList.trimToSize()
		int oldCapacity = list.length;
		if ( size < oldCapacity ) {
			list = Arrays.copyOf( list, size );
		}
	}

	/**
	 * Copied from ArrayList
	 * 
	 * @see java.util.ArrayList#fastRemove(int )
	 */
	private void fastRemove( int index ) {
		int numMoved = size - index - 1;
		if ( numMoved > 0 )
			System.arraycopy( list, index + 1, list, index, numMoved );
		list[--size] = null; // Let gc do its work
	}

	/**
	 * Copied from
	 * 
	 * @see java.util.ArrayList#RangeCheck(int )
	 */
	private void checkRange( int index ) {
		if ( index >= size )
			throw new IndexOutOfBoundsException( "Index: " + index + ", Size: "
				+ size );
	}

	/**
	 * Copied from ArrayList
	 * 
	 * @param minCapacity
	 * @see java.util.ArrayList#ensureCapacity(int)
	 */
	private void checkSize( int minCapacity ) {
		// taken from ArrayList.ensureCapacity( int )
		int oldCapacity = list.length;
		if ( minCapacity > oldCapacity ) {
			@SuppressWarnings( "unused" )
			Player[] oldData = list;
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if ( newCapacity < minCapacity )
				newCapacity = minCapacity;
			// minCapacity is usually close to size, so this is a win:
			list = Arrays.copyOf( list, newCapacity );
		}
	}

	/**
	 * the number of items in the list
	 * 
	 * @return the content size
	 */
	public int size() {
		return size;
	}

}
