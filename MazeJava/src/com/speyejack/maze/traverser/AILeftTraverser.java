package com.speyejack.maze.traverser;

import com.speyejack.maze.base.Graph;
import com.speyejack.maze.base.MazeGraph;
import com.speyejack.maze.base.MazeTraverser;

public class AILeftTraverser extends MazeTraverser{
	
	private int dir = 0;

	public AILeftTraverser(MazeGraph maze, Graph<Integer> graph) {
		super(maze, graph);
		maze.getEnd(graph);
	}
	
	
	
	public boolean advanceForward(){
		if (travel(dir - 1)){
			dir = Math.floorMod(dir-1, 4);
			return true;
		} else if (travel(dir)){
			return true;
		} else if (travel(dir + 1)){
			dir = Math.floorMod(dir + 1, 4);
			return true;
		} else {
			dir = Math.floorMod(dir + 2, 4);
			travel(dir);
			return true;
		}
	}
	
	private boolean travel(int dir){
		dir = Math.floorMod(dir, 4);
		if (dir == 0)
			return advancePath(NORTH);
		else if (dir == 1)
			return advancePath(EAST);
		else if (dir == 2)
			return advancePath(SOUTH);
		else if (dir == 3)
			return advancePath(WEST);
		else
			return false;
	}
	

}
