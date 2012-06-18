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

/**
 * Simple enough, this exception complains that the file type is not supported.
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public class UnsupportedFileTypeException extends Exception {

	private static final long serialVersionUID = -1725703359242969375L;

	/**
	 * Create an exception with the given message
	 * 
	 * @see java.lang.Exception#Exception(String)
	 * @param message
	 *            the message
	 */
	public UnsupportedFileTypeException( String message ) {
		super( message );
	}

	/**
	 * Create an exception with the given cause
	 * 
	 * @see java.lang.Exception#Exception(Throwable)
	 * @param cause
	 *            the cause
	 */
	public UnsupportedFileTypeException( Throwable cause ) {
		super( cause );
	}

	/**
	 * Create an exception with the given message and cause
	 * 
	 * @see java.lang.Exception#Exception(String, Throwable)
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public UnsupportedFileTypeException( String message, Throwable cause ) {
		super( message, cause );
	}

}
