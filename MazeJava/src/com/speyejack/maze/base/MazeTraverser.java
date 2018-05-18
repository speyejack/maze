package com.speyejack.maze.base;

import java.util.Iterator;
import java.util.Stack;

public class MazeTraverser {
	private Graph<Integer> graph;
	private Stack<Vertex<Integer>> path;
	private Vertex<Integer> end;
	private int rowMult;
	private int colSize;
	private int rowSize;

	public static final int[] WEST = new int[] { -1, 0 };
	public static final int[] EAST = new int[] { 1, 0 };
	public static final int[] NORTH = new int[] { 0, -1 };
	public static final int[] SOUTH = new int[] { 0, 1 };

	public MazeTraverser(MazeGraph maze, Graph<Integer> graph) {
		path = new Stack<Vertex<Integer>>();
		int[] size = maze.getMazeSize();
		rowSize = size[0];
		colSize = size[1];
		rowMult = (int) Math.pow(10, Math.floor(Math.log10(rowSize)) + 1);
		this.graph = graph;
		path.push(graph.getVertex(getKey(0, 0)));
	}

	private int getKey(int row, int col) {
		return (row * rowMult + col);
	}

	private int addToKey(int key, int row, int col) {
		return key + (row * rowMult + col);
	}

	private Float[] getPos(Vertex<Integer> vertex) {
		int key = vertex.getKey();
		int col = key % (rowMult);
		return new Float[] { (float) (key - col) / rowMult / rowSize, (float) col / colSize };
	}
	
	public boolean atEnd(){
		return path.peek() == end;
	}

	public boolean north() {
		return advancePath(NORTH);
	}

	public boolean south() {
		return advancePath(SOUTH);
	}

	public boolean east() {
		return advancePath(EAST);
	}

	public boolean west() {
		return advancePath(WEST);
	}

	public synchronized boolean advancePath(int[] dir) {
		Vertex<Integer> vertex = path.peek();
		Vertex<Integer> topVertex = graph.getVertex(addToKey(vertex.getKey(), dir[0], dir[1]));
		if (topVertex == null || !vertex.isConnected(topVertex))
			return false;
		path.push(topVertex);
		return true;
	}

	public Iterator<Float[][]> pathIterator() {

		return new Iterator<Float[][]>() {
			Iterator<Vertex<Integer>> itr = path.iterator();
			Vertex<Integer> parent = itr.next();

			@Override
			public boolean hasNext() {
				return itr.hasNext();
			}

			@Override
			public Float[][] next() {
				if (!this.hasNext()) {
					return new Float[][] { getPos(parent), getPos(parent) };
				}
				return new Float[][] { getPos(parent), getPos(parent = itr.next()) };
			}
		};
	}
}
