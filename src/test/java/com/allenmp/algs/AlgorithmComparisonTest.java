package com.allenmp.algs;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.common.graph.ValueGraph;

public class AlgorithmComparisonTest {
    
    @Test
    public void astarShouldVisitFewerNodesThanDijkstra() throws Exception {

	ValueGraph<Node, Double> g = TestGraphFactory.wikipediaGridAstarExample();
	
	Node start = g.nodes().stream().filter(n -> n.getId() == 22).findAny().get();
	Node goal = g.nodes().stream().filter(n -> n.getId() == 400).findAny().get();
	
	AstarShortestPath<Node> astar = new AstarShortestPath<Node>(g, new EuclideanDistance());
	astar.setEndpoints(start, goal);
	int vis1 = astar.countVisited();
	
	DijkstraShortestPath<Node> dijkstra = new DijkstraShortestPath<Node>(g);
	dijkstra.setEndpoints(start, goal);
	int vis2 = dijkstra.countVisited();

	assertTrue(vis2 > vis1);
    }
    
    @Test
    public void astarWithZeroHeuristicShouldMatchDijkstra() throws Exception {
	ValueGraph<Node, Double> g = TestGraphFactory.wikipediaGridAstarExample();
	
	Node start = g.nodes().stream().filter(n -> n.getId() == 22).findAny().get();
	Node goal = g.nodes().stream().filter(n -> n.getId() == 400).findAny().get();
	
	AstarShortestPath<Node> astar = new AstarShortestPath<Node>(g, new ZeroDistance());
	astar.setEndpoints(start, goal);
	
	DijkstraShortestPath<Node> dijkstra = new DijkstraShortestPath<Node>(g);
	dijkstra.setEndpoints(start, goal);
	
	int vis1 = astar.countVisited();
	int vis2 = dijkstra.countVisited();
	assertEquals(vis1, vis2);
	
	List<Node> astarPath = astar.path();
	List<Node> dijkstraPath = dijkstra.path();
	assertEquals(astarPath, dijkstraPath);
    }
    
    
    @Test
    public void astarShouldVisitMoreNodesWithInadmissibleHeuristic() throws Exception {

	ValueGraph<Node, Double> g = TestGraphFactory.wikipediaGridAstarExample();
	
	Node start = g.nodes().stream().filter(n -> n.getId() == 22).findAny().get();
	Node goal = g.nodes().stream().filter(n -> n.getId() == 400).findAny().get();
	
	AstarShortestPath<Node> admissible = new AstarShortestPath<Node>(g, new EuclideanDistance());
	admissible.setEndpoints(start, goal);
	int vis1 = admissible.countVisited();
	
	AstarShortestPath<Node> inadmissible = new AstarShortestPath<Node>(g, new DistanceFunction<Node>() {
	    @Override
	    public double between(Node o1, Node o2) {
		return 1000;
	    }
	});
	inadmissible.setEndpoints(start, goal);
	int vis2 = inadmissible.countVisited();

	assertTrue(vis2 > vis1);
    }

}
