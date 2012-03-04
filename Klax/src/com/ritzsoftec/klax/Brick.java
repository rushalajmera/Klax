/*
 * Name		:	Klax game in Java
 * Desc		:	This is a Java implementation of Klax, a famous game published by Atari.
 * Author	:	Rushal Ajmera
 * Email	:	rushalajmera@gmail.com
 * 
 * NOTE: The Klax game is a trademark of Atari Games. 
 * 
 * =====================================================================================
 * 
 * Copyright (C) 2012 Rushal Ajmera
 * 
 * This file is part of Klax.
 * 
 * Klax is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Klax is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ritzsoftec.klax;

/**
 * A brick class to hold information about the falling brick while it moves
 * through the paths.
 * 
 * @author Rushal
 */
public class Brick {
	int row, column, color;

	/**
	 * Constructor to initialize the brick.
	 * 
	 * @param column
	 *            The column in which the brick falls.
	 * @param color
	 *            The color of the brick.
	 */
	public Brick(int column, int color) {
		super();
		this.row = 0;
		this.column = column;
		this.color = color;
	}
}
