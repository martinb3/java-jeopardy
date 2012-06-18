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
 * Constant global values
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public class Const {

	/** default (basic) category names */
	public static final String[][] DEFAULT_CATS = new String[][] {
		{ "Single A", "Single B", "Single C", "Single D", "Single E" },
		{ "Double A", "Double B", "Double C", "Double D", "Double E" },
		{ "Final" } };
	/** Default entry values */
	public static final String[][] DEFAULT_VALUES = new String[][] {
		{ "100", "200", "300", "400", "500" },
		{ "200", "400", "600", "800", "1000" }, { "Wager" } };

	/** indicator for single jeopardy */
	public static final int SINGLEROUND = 0;
	/** indicator for double jeopardy */
	public static final int DOUBLEROUND = 1;
	/** indicator for final jeopardy */
	public static final int FINALROUND = 2;

}
