package com.allenmp.algs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.graph.ValueGraph;

public class AstarShortestPathImplTest {

    @Test
    public void shouldMatchWikiResults() throws Exception {

	ValueGraph<Node, Double> g = TestGraphFactory.wikipediaDijkstraExample();

	ShortestPathAlg<Node> alg = new AstarShortestPathImpl<Node>(g, new EuclideanDistance());
	
	// get source/goal by ID from the graph data to avoid declaring coords again 
	Node source = g.nodes().stream().filter(n -> n.getId()==1).findAny().get();
	Node goal = g.nodes().stream().filter(n -> n.getId()==5).findAny().get();
	
	double length = alg.pathLength(source, goal);
	assertEquals(20, length, 0.001);

	List<Node> path = alg.path(source, goal);
	assertArrayEquals(new Node[] { source, new Node(3), new Node(6), goal}, path.toArray());
    }

    @Test
    public void shouldMatchG4gResults() throws Exception {

	ValueGraph<Node, Double> g = TestGraphFactory.g4gDijkstraExample();

	ShortestPathAlg<Node> alg = new AstarShortestPathImpl<Node>(g, new EuclideanDistance());

	Node source = g.nodes().stream().filter(n -> n.getId()==0).findAny().get();
	Node goal = g.nodes().stream().filter(n -> n.getId()==4).findAny().get();
	
	double length = alg.pathLength(source, goal);
	assertEquals(21, length, 0.001);

	List<Node> path = alg.path(source, goal);
	assertArrayEquals(new Node[] { source, new Node(7), new Node(6), new Node(5), goal}, path.toArray());
    }

    @Test
    public void shouldFailForInadmissibleDistanceFunction() throws Exception {

	ValueGraph<Node, Double> g = TestGraphFactory.g4gDijkstraExample();

	ShortestPathAlg<Node> alg = new AstarShortestPathImpl<Node>(g, new DistanceFunction<Node>() {
	    @Override
	    public double between(Node o1, Node o2) {
		return Double.POSITIVE_INFINITY; 
	    }
	});

	Node source = g.nodes().stream().filter(n -> n.getId()==0).findAny().get();
	Node goal = g.nodes().stream().filter(n -> n.getId()==4).findAny().get();
	
	double length = alg.pathLength(source, goal);
	assertEquals(21, length, 0.001);

	List<Node> path = alg.path(source, goal);
	assertArrayEquals(new Node[] { source, new Node(7), new Node(6), new Node(5), goal}, path.toArray());
    }
    
}
