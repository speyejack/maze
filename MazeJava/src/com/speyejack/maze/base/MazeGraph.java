package com.speyejack.maze.base;

import java.awt.Dimension;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.speyejack.maze.gui.MazeGUI;
import com.speyejack.maze.traverser.AILeftTraverser;

public class MazeGraph {

	MinimumSpanningGraph<Integer> graph;
	private int rowSize;
	private int colSize;
	private int rowMult;

	public MazeGraph(int numRow, int numCol) {
		rowSize = numRow;
		colSize = numCol;
		rowMult = (int) Math.pow(10, Math.floor(Math.log10(numCol)) + 1);
		Graph<Integer> graph = new Graph<Integer>();
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				addVertex(graph, row, col);
				if (!(row == 0 && col == 0)) {
					if (row != 0)
						addEdge(graph, getKey(row, col), getKey(row - 1, col));
					if (col != 0)
						addEdge(graph, getKey(row, col), getKey(row, col - 1));
				}
			}
		}
		this.graph = new MinimumSpanningGraph<Integer>(graph, getStart(graph));
		this.graph.stepExpandGraph();
		// this.graph.completeGraph();
	}

	public int[] getMazeSize() {
		return new int[] { rowSize, colSize };
	}

	private int getKey(int row, int col) {
		return (row * rowMult + col);
	}

	private Float[] getPos(Vertex<Integer> vertex) {
		int key = vertex.getKey();
		int col = key % (rowMult);
		return new Float[] { (float) (key - col) / rowMult / rowSize, (float) col / colSize };
	}

	public Vertex<Integer> getStart(Graph<Integer> graph) {
		return graph.getVertex(getKey(0, 0));
	}

	public Vertex<Integer> getEnd(Graph<Integer> graph) {
		return graph.getVertex(getKey(rowSize, colSize));
	}

	private void addVertex(Graph<Integer> graph, int row, int col) {
		graph.addVertex(getKey(row, col));
	}

	private void addEdge(Graph<Integer> graph, int key1, int key2) {
		graph.addEdge(key1, key2, new Random().nextInt(1000));
	}

	/**
	 * Finds Minimum Spanning Tree Using Primms algorithum
	 */

	public Dimension getSize() {
		return new Dimension(colSize, rowSize);
	}

	public int[] getRowColData() {
		return new int[] { rowSize, colSize, rowMult };
	}

	@SuppressWarnings("unchecked")
	public Iterator<Float[][]> posIterator() {
		Queue<Vertex<Integer>> processing = new ArrayDeque<Vertex<Integer>>();
		processing.add(this.getStart(this.graph));
		List<Vertex<Integer>> visited = new ArrayList<Vertex<Integer>>(processing);
		Queue<Vertex<Integer>[]> vertices = new ArrayDeque<Vertex<Integer>[]>();
		while (!processing.isEmpty()) {
			Vertex<Integer> parent = processing.remove();
			Iterator<Vertex<Integer>> childIter = parent.remainingChildrenIterator(visited);
			Vertex<Integer> child = null;
			while (childIter.hasNext()) {
				child = childIter.next();
				processing.add(child);
				visited.add(child);
				vertices.add(new Vertex[] { parent, child });
			}
		}

		return new Iterator<Float[][]>() {
			Queue<Vertex<Integer>[]> queue = vertices;

			@Override
			public boolean hasNext() {
				return !queue.isEmpty();
			}

			@Override
			public Float[][] next() {
				Vertex<Integer>[] pair = queue.remove();
				return new Float[][] { getPos(pair[0]), getPos(pair[1]) };
			}
		};
	}

	MinimumSpanningGraph<Integer> getGraph() {
		return graph;
	}

	public static void main(String[] args) {
		MazeGraph maze = new MazeGraph(50, 50);
		// maze.graph.completeGraph();
		Vertex<Integer> temp = maze.getStart(maze.graph);
		System.out.println("Generation finished");
		AILeftTraverser trav = new AILeftTraverser(maze, maze.getGraph());
		MazeGUI gui = new MazeGUI(maze);

		while (!maze.graph.full) {
			maze.graph.stepExpandGraph();
			gui.updateMazeVisual();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		gui.addTraverser(trav);
		while (!trav.atEnd()) {
			trav.advanceForward();
			gui.updateMazeVisual();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
