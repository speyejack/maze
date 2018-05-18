package com.speyejack.maze.base;

import java.util.Hashtable;

public class Graph<E> {
	private int numVerts = 0;
	private int numEdges = 0;
	private Hashtable<E, Vertex<E>> vertexTable;

	/**
	 * Creates a list of vertices and the adjacency matrix.
	 */
	public Graph() {
		vertexTable = new Hashtable<E, Vertex<E>>();
	}

	public Vertex<E> getVertex(E key) {
		return vertexTable.get(key);
	}

	public void addVertex(E key) {
		// Adds the vertex to the matrix
		numVerts++;
		vertexTable.put(key, new Vertex<E>(key));
	}

	// Adds an edge in the adjacency matrix
	public boolean addEdge(E key1, E key2, double weight) {
		numEdges++;
		// Checks if the vertices exists
		Vertex<E> node1 = getVertex(key1);
		Vertex<E> node2 = getVertex(key2);
		if (node1 == null || node2 == null)
			return false;

		// Adds a edge to the matrix
		node1.addEdge(node2, weight);
		node2.addEdge(node1, weight);
		return true;
	}

	public int getSize() {
		return numVerts;
	}

	public int getEdgeNumber() {
		return numEdges;
	}

	protected Hashtable<E, Vertex<E>> getVertexTable() {
		return vertexTable;
	}
	
}
