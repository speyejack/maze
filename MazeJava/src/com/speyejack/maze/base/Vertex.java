package com.speyejack.maze.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Vertex<E> {

	private E key;
	private HashMap<Vertex<E>, Double> map;

	public Vertex(E key) {
		// Creates the vertex with the label
		this.key = key;
		map = new HashMap<Vertex<E>, Double>();
	}

	public void addEdge(Vertex<E> vertex, double weight) {
		map.put(vertex, weight);
	}

	// returns the label
	public E getKey() {
		return key;
	}

	public Iterator<Vertex<E>> childrenIterator() {
		return map.keySet().iterator();
	}

	public Iterator<Vertex<E>> remainingChildrenIterator(Collection<Vertex<E>> overlapping) {
		@SuppressWarnings("unchecked")
		Set<Vertex<E>> children = ((HashMap<Vertex<E>, Double>) map.clone()).keySet();
		children.removeAll(overlapping);
		return children.iterator();

	}

	public Collection<Double> getChildrenWeights() {
		return map.values();
	}

	public double getChildWeight(Vertex<E> vertex) {
		return map.get(vertex);
	}

	public boolean isConnected(Vertex<E> vertex) {
		return map.containsKey(vertex);
	}

	@Override
	public String toString() {
		String str = key.toString() + ": ";
		for (Iterator<Vertex<E>> itr = childrenIterator(); itr.hasNext();)
			str += itr.next().getKey().toString() + " ";
		return str.trim();
	}

}
