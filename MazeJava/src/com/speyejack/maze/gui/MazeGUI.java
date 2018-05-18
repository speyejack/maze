package com.speyejack.maze.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.JFrame;

import com.speyejack.maze.base.MazeGraph;
import com.speyejack.maze.base.MazeTraverser;

public class MazeGUI implements Runnable {
	private Canvas canvas;
	private JFrame frame;
	private int WIDTH_SCALER = 900;
	private int HEIGHT_SCALER = 900;
	private int HOZI_SHIFT = 10;
	private int VERT_SHIFT = 10;
	private double SIZE_SCALER = 0.9;
	private float PENSIZE = 40;
	private float YLENGTH = 10;
	private float XLENGTH = 10;
	// f(50,50) = 10
	// f(9,9) = 70
	// f(20) = 30
	// f(15) = 40
	private MazeGraph maze;
	private MazeTraverser traverser;

	public MazeGUI(MazeGraph maze) {
		this.maze = maze;
		PENSIZE = Math.max(WIDTH_SCALER, HEIGHT_SCALER) / Math.max(maze.getMazeSize()[0], maze.getMazeSize()[1]) * 5 / 9;
		XLENGTH = 1 / (float) maze.getMazeSize()[0];
		YLENGTH = 1 / (float) maze.getMazeSize()[1];
		frame = new JFrame();
		canvas = new Canvas();
		traverser = null;
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stop();
			}

			@Override
			public void windowActivated(WindowEvent e) {
				super.windowActivated(e);
				updateMazeVisual();
			}

		});
		canvas.addKeyListener(new KeyListener() {


			@Override
			public void keyPressed(KeyEvent e) {
				if (traverser == null)
					return;
				switch (e.getKeyCode()) {
				case KeyEvent.VK_RIGHT:
					traverser.advancePath(MazeTraverser.EAST);
					break;
				case KeyEvent.VK_LEFT:
					traverser.advancePath(MazeTraverser.WEST);
					break;
				case KeyEvent.VK_UP:
					traverser.advancePath(MazeTraverser.NORTH);
					break;
				case KeyEvent.VK_DOWN:
					traverser.advancePath(MazeTraverser.SOUTH);
					break;
				}
				updateMazeVisual();
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}
		});
		// canvas.addComponentListener(new ComponentAdapter() {
		// @Override
		// public void componentResized(ComponentEvent e) {
		// super.componentResized(e);
		// generateTreeVisual();
		// }
		// });
		canvas.setMaximumSize(new Dimension(WIDTH_SCALER, HEIGHT_SCALER));
		canvas.setSize(new Dimension(WIDTH_SCALER, HEIGHT_SCALER));
		canvas.setVisible(true);
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	

	public synchronized void updateMazeVisual() {

		WIDTH_SCALER = (int) (canvas.getWidth() * SIZE_SCALER);
		HEIGHT_SCALER = (int) (canvas.getHeight() * SIZE_SCALER);
		HOZI_SHIFT = (int) (canvas.getWidth() * (1.0 - SIZE_SCALER) / 2);
		VERT_SHIFT = (int) (canvas.getHeight() * (1.0 - SIZE_SCALER) / 2);

		BufferStrategy bs;
		if ((bs = canvas.getBufferStrategy()) == null) {
			canvas.createBufferStrategy(3);
		}
		bs = canvas.getBufferStrategy();
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		g.setColor(Color.BLACK);
		g.drawImage(new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB), 0, 0,
				canvas.getWidth(), canvas.getHeight(), null);

		g.setStroke(new BasicStroke(PENSIZE));
		addMazeEdgesGraphic(g);
		g.setColor(Color.WHITE);
		addVerticesGraphic(g);
		addStartEndGraphic(g);
		g.setColor(Color.red);
		addTraverserGraphic(g);

		// Debugging
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.blue);
		g.drawLine((0) * WIDTH_SCALER + HOZI_SHIFT, (0) * HEIGHT_SCALER + VERT_SHIFT, (0) * WIDTH_SCALER + HOZI_SHIFT,
				(0) * HEIGHT_SCALER + VERT_SHIFT);

		g.dispose();
		bs.show();
		frame.setVisible(true);
		canvas.setVisible(true);
		frame.pack();
	}

	private void addMazeEdgesGraphic(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		float[][][] pos = new float[][][] {
				{ { -XLENGTH * 5 / 8, -YLENGTH * 5 / 8 }, { -XLENGTH * 5 / 8, 1 - YLENGTH * 3 / 8 } }, // Left
																										// Edge
				{ { -XLENGTH * 5 / 8, -YLENGTH * 5 / 8 }, { 1 - XLENGTH * 3 / 8, -YLENGTH * 5 / 8 } }, // Top
																										// Edge
				{ { 1 - XLENGTH * 3 / 8, -YLENGTH * 3 / 8 }, { 1 - XLENGTH * 3 / 8, 1 - YLENGTH * 3 / 8 } },
				{ { -XLENGTH * 3 / 8, 1 - YLENGTH * 3 / 8 }, { 1 - XLENGTH * 3 / 8, 1 - YLENGTH * 3 / 8 } } };
		for (int i = 0; i < pos.length; i++) {
			g.drawLine((int) ((pos[i][0][0]) * WIDTH_SCALER + HOZI_SHIFT),
					(int) ((pos[i][0][1]) * HEIGHT_SCALER + VERT_SHIFT),
					(int) ((pos[i][1][0]) * WIDTH_SCALER + HOZI_SHIFT),
					(int) ((pos[i][1][1]) * HEIGHT_SCALER + VERT_SHIFT));
		}
	}

	private void addVerticesGraphic(Graphics g) {
		Iterator<Float[][]> poss = maze.posIterator();
		g.setColor(Color.WHITE);
		while (poss.hasNext()) {
			Float[][] pos = poss.next();
			if (pos != null) {
				g.drawLine((int) ((pos[0][0]) * WIDTH_SCALER + HOZI_SHIFT),
						(int) ((pos[0][1]) * HEIGHT_SCALER + VERT_SHIFT),
						(int) ((pos[1][0]) * WIDTH_SCALER + HOZI_SHIFT),
						(int) ((pos[1][1]) * HEIGHT_SCALER + VERT_SHIFT));
			}
		}
	}

	private void addStartEndGraphic(Graphics g) {
		float[][][] pos = new float[][][] { { { 0, 0 }, { 0, 0 } },
				{ { 1 - XLENGTH, 1 - YLENGTH }, { 1 - XLENGTH, 1 - YLENGTH } } };
		g.setColor(Color.RED);
		g.drawLine((int) ((pos[0][0][0]) * WIDTH_SCALER + HOZI_SHIFT),
				(int) ((pos[0][0][1]) * HEIGHT_SCALER + VERT_SHIFT), (int) ((pos[0][1][0]) * WIDTH_SCALER + HOZI_SHIFT),
				(int) ((pos[0][1][1]) * HEIGHT_SCALER + VERT_SHIFT));
		g.setColor(Color.GREEN);
		g.drawLine((int) ((pos[1][0][0]) * WIDTH_SCALER + HOZI_SHIFT),
				(int) ((pos[1][0][1]) * HEIGHT_SCALER + VERT_SHIFT), (int) ((pos[1][1][0]) * WIDTH_SCALER + HOZI_SHIFT),
				(int) ((pos[1][1][1]) * HEIGHT_SCALER + VERT_SHIFT));
	}

	private void addTraverserGraphic(Graphics2D g) {
		if (traverser != null) {
			g.setStroke(new BasicStroke(PENSIZE/4));
			Iterator<Float[][]> itr = traverser.pathIterator();
			Float[][] pos;
			while (itr.hasNext()) {
				pos = itr.next();
				g.drawLine((int) ((pos[0][0]) * WIDTH_SCALER + HOZI_SHIFT),
						(int) ((pos[0][1]) * HEIGHT_SCALER + VERT_SHIFT),
						(int) ((pos[1][0]) * WIDTH_SCALER + HOZI_SHIFT),
						(int) ((pos[1][1]) * HEIGHT_SCALER + VERT_SHIFT));
			}
			g.setStroke(new BasicStroke(PENSIZE * 3 / 4));
			pos = itr.next();
			g.drawLine((int) ((pos[0][0]) * WIDTH_SCALER + HOZI_SHIFT),
					(int) ((pos[0][1]) * HEIGHT_SCALER + VERT_SHIFT), (int) ((pos[1][0]) * WIDTH_SCALER + HOZI_SHIFT),
					(int) ((pos[1][1]) * HEIGHT_SCALER + VERT_SHIFT));
		}
	}

	public void addTraverser(MazeTraverser traverser) {
		this.traverser = traverser;
	}

	public synchronized void start() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		updateMazeVisual();
	}

	public synchronized void stop() {
		System.exit(0);
	}

	public void run() {
		start();
	}
}
