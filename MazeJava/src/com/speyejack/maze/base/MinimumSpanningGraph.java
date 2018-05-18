package com.speyejack.maze.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MinimumSpanningGraph<E> extends Graph<E> {
	List<Vertex<E>> visiting;
	List<Vertex<E>> visited;
	boolean full = false;
	
	
	public MinimumSpanningGraph(Graph<E> old_graph, Vertex<E> start) {
		for (Iterator<Vertex<E>> v = old_graph.getVertexTable().values().iterator(); v.hasNext();) {
			this.addVertex(v.next().getKey());
		}
		visiting = new ArrayList<Vertex<E>>();
		visited = new ArrayList<Vertex<E>>();
		visiting.add(start);
		visited.add(start);
			
	}
	
	public boolean stepExpandGraph(){
		if (full)
			return false;
		double minWeight = Double.POSITIVE_INFINITY;
		Vertex<E> minParent = null;
		Vertex<E> minChild = null;
		List<Vertex<E>> deadends = new ArrayList<Vertex<E>>();
		for (Iterator<Vertex<E>> parentIter = visiting.iterator(); parentIter.hasNext();) {
			boolean children = false;
			Vertex<E> parent = parentIter.next();
			Iterator<Vertex<E>> childIter = parent.remainingChildrenIterator(visited);
			while(childIter.hasNext()){
				Vertex<E> child = childIter.next();
				children = true;
				if (parent.getChildWeight(child) < minWeight) {
					minWeight = parent.getChildWeight(child);
					minParent = parent;
					minChild = child;
				}
			}
			if (!children) {
				deadends.add(parent);
			}
		}
		// if (minParent != null){
		visiting.removeAll(deadends);
		if (minParent != null) {
			this.addEdge(minParent.getKey(), minChild.getKey(), minWeight);
			visiting.add(minChild);
			visited.add(minChild);
		}
		if (visiting.isEmpty()){
			full = true;
			visited = null;
			visiting = null;
		}
		return true;
			
	}
	
	public void completeGraph(){
		while (stepExpandGraph());
		System.out.println("Completed Graph");
	}
	

}
