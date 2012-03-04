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

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * KLAX game.
 * 
 * @author Rushal
 */
public class Klax extends Applet implements Runnable, KeyListener {

	private static final long serialVersionUID = 8544540778065429477L;

	private static final int APPLET_HEIGHT = 600;
	private static final int APPLET_WIDTH = 800;
	private static final int BRICK_WIDTH = 50;
	private static final int BRICK_HEIGHT = 20;
	private static final int STACK_X = 250;
	private static final int STACK_Y = 480;
	private static final int PATH_X = 250;
	private static final int PATH_Y = 150;
	private static final int PADDLE_X = 250;
	private static final int PADDLE_Y = 410;
	private static final int PATH_HEIGHT = 200;
	private static final int PATH_BRICKS = 10;
	private static final int PADDLE_HEIGHT = 10;
	private static final int PADDLE_WIDTH = BRICK_WIDTH;
	private static final int MAX = 10;
	private static final int MAX_MISSES = 3;
	private static final int NUM = 5;
	private static final int DELAY = 100;
	private static final int SCORE_X = 10;
	private static final int SCORE_Y = 20;
	private static final int MISSES_X = APPLET_WIDTH - 100;
	private static final int MISSES_Y = 20;
	private static final int TIME_X = APPLET_WIDTH - 100;
	private static final int TIME_Y = APPLET_HEIGHT - 10;

	private final Random rand = new Random();
	private final int stack[][] = new int[NUM][NUM];

	private Graphics g;
	private Thread fallThread;
	private Brick brick;
	private Brick paddleBrick;

	private int paddlePosition;
	private final int score = 0;
	private int misses = 0;
	private int time = 0;
	private boolean isGameOver = false;

	@Override
	public void init() {
		fallThread = new Thread(this);
		fallThread.start();
		addKeyListener(this);
	}

	@Override
	public void start() {
		// Sets the dimensions of the Applet.
		this.setSize(APPLET_WIDTH, APPLET_HEIGHT);

		// Temporary initial bricks in the stack.
		stack[0][4] = 1;
		stack[0][3] = 2;
		stack[1][4] = 3;
		stack[2][4] = 4;
		stack[4][4] = 1;

		// The position of the paddle.
		paddlePosition = 2;
	}

	@Override
	public void paint(Graphics g) {
		this.g = g;
		g.setColor(Color.BLACK);
		checkSeries();
		drawBackground();
		drawStackBricks();
		drawPaddle();
		drawFallingBrick();
		drawScore();
		drawMiss();
		drawTime();
	}

	/**
	 * Checks the stack to find if any klax series has been formed.
	 */
	private void checkSeries() {
		// TODO Implement the logic for checking whether a hit has been made.

	}

	/**
	 * Draws the background of the game. i.e. the stacks and the paths.
	 */
	private void drawBackground() {
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.drawString("KLAX", 300, 50);

		// stacks
		for (int i = 0; i <= NUM; i++) {
			g.drawLine(STACK_X, STACK_Y + i * BRICK_HEIGHT, STACK_X + 250,
					STACK_Y + i * BRICK_HEIGHT);
		}

		for (int i = 0; i <= NUM; i++) {
			g.drawLine(STACK_X + i * BRICK_WIDTH, STACK_Y, STACK_X + i
					* BRICK_WIDTH, STACK_Y + 100);
		}

		// paths
		for (int i = 0; i <= NUM; i++) {
			g.drawLine(PATH_X + i * BRICK_WIDTH, PATH_Y, PATH_X + i
					* BRICK_WIDTH, PATH_Y + PATH_HEIGHT);
		}

		g.setColor(Color.LIGHT_GRAY);
		for (int i = 0; i <= PATH_BRICKS; i++) {
			g.drawLine(PATH_X, PATH_Y + i * BRICK_HEIGHT, PATH_X + 250, PATH_Y
					+ i * BRICK_HEIGHT);
		}
		g.setColor(Color.BLACK);
	}

	/**
	 * Method to redraw the stacks according to the bricks contained.
	 */
	private void drawStackBricks() {
		int len = 5;
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				if (stack[i][j] > 0) {
					g.setColor(findColor(stack[i][j]));
					g.fillRect(STACK_X + i * BRICK_WIDTH + 1, STACK_Y + j
							* BRICK_HEIGHT + 1, BRICK_WIDTH - 1,
							BRICK_HEIGHT - 1);
				}
			}
		}
		g.setColor(Color.BLACK);
	}

	/**
	 * Draws the paddle at its current position.
	 */
	private void drawPaddle() {
		g.fillRect(PADDLE_X + paddlePosition * BRICK_WIDTH, PADDLE_Y,
				PADDLE_WIDTH, PADDLE_HEIGHT);
		if (paddleBrick != null) {
			g.setColor(findColor(paddleBrick.color));
			g.fillRect(PADDLE_X + paddlePosition * BRICK_WIDTH, PADDLE_Y - 30,
					BRICK_WIDTH, BRICK_HEIGHT);
			g.setColor(Color.BLACK);
		}
	}

	/**
	 * Checks the current position of a falling brick and draws it at the
	 * correct position.
	 */
	private void drawFallingBrick() {
		if (brick == null) {
			brick = new Brick(getRandomColumn(), getRandomColor());
		}
		if (brick.row == MAX) {
			if (paddlePosition != brick.column) {
				addMiss();
			} else {
				if (spaceOnPaddle()) {
					paddleBrick = brick;
				} else {
					addMiss();
				}
			}
			brick = new Brick(getRandomColumn(), getRandomColor());
		}
		g.setColor(findColor(brick.color));
		g.fillRect(PATH_X + brick.column * BRICK_WIDTH + 1, PATH_Y + brick.row
				* BRICK_HEIGHT + 1, BRICK_WIDTH - 1, BRICK_HEIGHT - 1);
		g.setColor(Color.BLACK);
	}

	/**
	 * Draws the score at the top left corner.
	 */
	private void drawScore() {
		g.drawString("Score: " + score, SCORE_X, SCORE_Y);
	}

	/**
	 * Draws the misses at the top right corner.
	 */
	private void drawMiss() {
		g.drawString("Misses: " + misses, MISSES_X, MISSES_Y);
	}

	/**
	 * Draws the time elapsed at the bottom right corner.
	 */
	private void drawTime() {
		g.drawString("Time: " + time, TIME_X, TIME_Y);
	}

	/**
	 * Checks if there is space on the paddle.
	 * 
	 * @return
	 */
	private boolean spaceOnPaddle() {
		// TODO Make changes to allow paddle to hold 3 bricks.
		if (paddleBrick == null)
			return true;
		else
			return false;
	}

	/**
	 * Converts int representation of color to Color object.
	 * 
	 * @param color
	 *            int value of color.
	 * @return Color object.
	 */
	private Color findColor(int color) {
		switch (color) {
		case 1:
			return Color.RED;
		case 2:
			return Color.GREEN;
		case 3:
			return Color.BLUE;
		case 4:
			return Color.YELLOW;
		default:
			return Color.BLACK;
		}
	}

	/**
	 * Adds a miss to the misses count.
	 */
	private void addMiss() {
		misses++;
		if (misses == MAX_MISSES)
			gameOver("You missed " + MAX_MISSES + " bricks. Game Over!");
	}

	/**
	 * Gets random column number from 0 to 4.
	 * 
	 * @return Column number.
	 */
	private int getRandomColumn() {
		return rand.nextInt(5);
	}

	/**
	 * Gets random color from 1 to 4.
	 * 
	 * @return Random color number.
	 */
	private int getRandomColor() {
		// Gets colors from 1 to 4.
		return rand.nextInt(3) + 1;
	}

	/**
	 * Displays a game over message and stops execution.
	 * 
	 * @param msg
	 *            The message to be displayed.
	 */
	private void gameOver(String msg) {
		isGameOver = true;
		JOptionPane.showMessageDialog(this, msg);
		System.exit(0);
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char key = e.getKeyChar();
		if (key == 'j' || key == 'J') {
			moveLeft();
		} else if (key == 'k' || key == 'K') {
			moveRight();
		} else if (key == ' ') {
			unloadPaddle();
		}
		repaint();
		e.consume();
	}

	/**
	 * Unload the top most brick from the paddle.
	 */
	private void unloadPaddle() {
		int i = 4;
		for (i = 4; i >= 0; i--) {
			if (stack[paddlePosition][i] == 0) {
				stack[paddlePosition][i] = paddleBrick.color;
				paddleBrick = null;
				break;
			}
		}
		if (i < 0) {
			gameOver("Stack Full. Game Over!");
		}
	}

	/**
	 * Moves the paddle to the left.
	 */
	private void moveLeft() {
		if (paddlePosition == 0)
			paddlePosition = 0;
		else
			paddlePosition--;
	}

	/**
	 * Moves the paddle to the right.
	 */
	private void moveRight() {
		if (paddlePosition == 4)
			paddlePosition = 4;
		else
			paddlePosition++;
	}

	@Override
	public void run() {
		int i = 0;
		while (!isGameOver) {
			i++;
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (i % 5 == 0) {
				if (brick != null) {
					if (brick.row <= MAX)
						brick.row++;
				}
			}
			if (i % 10 == 0) {
				time++;
			}
			repaint();
		}
	}
}
