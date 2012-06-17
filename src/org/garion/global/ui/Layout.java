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
package org.garion.global.ui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.*;

/**
 * Provide global access to layout methods
 * 
 * @author Dallin Lauritzen
 * @version 1.0 (7 May 2010)
 */
public final class Layout {

	/** @see javax.swing.BoxLayout#X_AXIS */
	public static final int X_AXIS = BoxLayout.X_AXIS;
	/** @see javax.swing.BoxLayout#Y_AXIS */
	public static final int Y_AXIS = BoxLayout.Y_AXIS;
	/** @see javax.swing.BoxLayout#PAGE_AXIS */
	public static final int PAGE_AXIS = BoxLayout.PAGE_AXIS;
	/** @see javax.swing.BoxLayout#LINE_AXIS */
	public static final int LINE_AXIS = BoxLayout.LINE_AXIS;

	/**
	 * Create a JPanel with a BoxLayout manager assigned
	 * 
	 * @see #X_AXIS
	 * @see #Y_AXIS
	 * @see #PAGE_AXIS
	 * @see #LINE_AXIS
	 * @param axis
	 *            the layout axis for the box layout. Defined in
	 *            {@link javax.swing.BoxLayout} but cloned in Layout for ease of
	 *            access.
	 * @return the created JPanel
	 */
	public static final JPanel createBoxPanel( int axis ) {
		JPanel ret = new JPanel();
		BoxLayout layout = new BoxLayout( ret, axis );
		ret.setLayout( layout );
		return ret;
	}

	/**
	 * <p>
	 * Add an item to a JPanel with a GridBagLayout.
	 * </p>
	 * <p>
	 * Fill values are defined as such:
	 * </p>
	 * <ul>
	 * <li>0: Both horizontally and vertically</li>
	 * <li>1: No fill</li>
	 * <li>2: Horizontally, but not vertically</li>
	 * <li>3: Vertically, but not horizontally</li>
	 * <li>Anything else: Both</li>
	 * </ul>
	 * 
	 * @param y
	 *            Position y
	 * @param x
	 *            Position x
	 * @param wy
	 *            Weight y
	 * @param wx
	 *            Weight x
	 * @param w
	 *            Width
	 * @param h
	 *            Height
	 * @param fill
	 *            Fill
	 * @param p
	 *            Panel to add to
	 * @param gc
	 *            Constraints to edit
	 * @param item
	 *            Item to add
	 */
	public static void addItem( int y, int x, double wy, double wx, int w,
		int h, int fill, JPanel p, GridBagConstraints gc, JComponent item ) {
		gc.gridy = y;
		gc.gridx = x;
		gc.weighty = wy;
		gc.weightx = wx;
		gc.gridwidth = w;
		gc.gridheight = h;
		switch ( fill ) {
			case 0:
				gc.fill = GridBagConstraints.BOTH;
				break;
			case 1:
				gc.fill = GridBagConstraints.NONE;
				break;
			case 2:
				gc.fill = GridBagConstraints.HORIZONTAL;
				break;
			case 3:
				gc.fill = GridBagConstraints.VERTICAL;
				break;
			default:
				gc.fill = GridBagConstraints.BOTH;
		}
		p.add( item, gc );
	}

	/**
	 * Create and return a "default" set up of a GridBagConstraints object
	 * 
	 * @return The object
	 */
	public static GridBagConstraints makeDefaultGC() {
		GridBagConstraints gc = new GridBagConstraints();
		gc.weightx = 1.0;
		gc.weighty = 1.0;
		gc.insets = new Insets( 2, 2, 2, 2 );
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		return gc;
	}
}
