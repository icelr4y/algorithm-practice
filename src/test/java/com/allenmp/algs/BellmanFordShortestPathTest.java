package com.allenmp.algs;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.common.graph.ValueGraph;

public class BellmanFordShortestPathTest {

    @Test
    public void shouldFindShortestPathWithNegativeWeights() throws Exception {

	ValueGraph<Node, Double> g = TestGraphFactory.g4gBellmanFordExample();

	ShortestPathAlg<Node> alg = new BellmanFordShortestPath<Node>(g);

	Node start = g.nodes().stream().filter(n -> n.getId() == 1).findAny().get();
	Node goal = g.nodes().stream().filter(n -> n.getId() == 5).findAny().get();
	
	alg.setEndpoints(start, goal);
	
	double length = alg.pathLength();
	assertEquals(1.0, length, 0.001);

	List<Node> path = alg.path();
	assertArrayEquals(new Node[] { start, new Node(2), goal}, path.toArray());
    }

    @Test
    public void shouldWorkOnUndirectedGraph() throws Exception {
	ValueGraph<Node, Double> g = TestGraphFactory.wikipediaDijkstraExample();

	ShortestPathAlg<Node> alg = new BellmanFordShortestPath<Node>(g);

	Node start = g.nodes().stream().filter(n -> n.getId() == 1).findAny().get();
	Node goal = g.nodes().stream().filter(n -> n.getId() == 5).findAny().get();
	
	alg.setEndpoints(start, goal);
	
	double length = alg.pathLength();
	assertEquals(20, length, 0.001);

	List<Node> path = alg.path();
	assertArrayEquals(new Node[] { start, new Node(3), new Node(6), goal}, path.toArray());
    }

    @Test(expected=IllegalStateException.class)
    public void shouldFailForNegativeCycleGraphs() throws Exception {
	ValueGraph<Node, Double> g = TestGraphFactory.negativeCycleGraph();
	ShortestPathAlg<Node> alg = new BellmanFordShortestPath<Node>(g);
	
	Node start = g.nodes().stream().filter(n -> n.getId() == 1).findAny().get();
	Node goal = g.nodes().stream().filter(n -> n.getId() == 5).findAny().get();
	alg.setEndpoints(start, goal);

	alg.pathLength();
    }
    
    
}
