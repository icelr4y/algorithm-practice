package com.allenmp.astar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.graph.ValueGraph;

public class DijkstraShortestPathImplTest {

    @Test
    public void shouldMatchWikiResults() throws Exception {

	ValueGraph<Node, Double> g = TestGraphFactory.wikipediaDijkstraExample();

	ShortestPathAlg<Node> alg = new DijkstraShortestPathImpl<Node>(g);

	Node source = new Node(1);
	Node goal = new Node(5);
	
	double length = alg.pathLength(source, goal);
	assertEquals(20, length, 0.001);

	List<Node> path = alg.path(source, goal);
	assertArrayEquals(new Node[] { source, new Node(3), new Node(6), goal}, path.toArray());
    }

    @Test
    public void shouldMatchG4gResults() throws Exception {

	ValueGraph<Node, Double> g = TestGraphFactory.g4gDijkstraExample();

	ShortestPathAlg<Node> alg = new DijkstraShortestPathImpl<Node>(g);

	Node source = new Node(0);
	Node goal = new Node(4);
	
	double length = alg.pathLength(source, goal);
	assertEquals(21, length, 0.001);

	List<Node> path = alg.path(source, goal);
	assertArrayEquals(new Node[] { source, new Node(7), new Node(6), new Node(5), goal}, path.toArray());
    }
    
}
