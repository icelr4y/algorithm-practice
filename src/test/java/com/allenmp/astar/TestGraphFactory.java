package com.allenmp.astar;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import junit.framework.TestCase;

public class TestGraphFactory extends TestCase {

    public static ValueGraph<Node, Double> wikipediaDijkstraExample() {
	// https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
	
	MutableValueGraph<Node, Double> g1 = ValueGraphBuilder.undirected().build();
	
	Node n1 = new Node(1);
	Node n2 = new Node(2);
	Node n3 = new Node(3);
	Node n4 = new Node(4);
	Node n5 = new Node(5);
	Node n6 = new Node(6);
	
	g1.addNode(n1);
	g1.addNode(n2);
	g1.addNode(n3);
	g1.addNode(n4);
	g1.addNode(n5);
	g1.addNode(n6);
	
	g1.putEdgeValue(n1, n2, 7.0);
	g1.putEdgeValue(n1, n3, 9.0);
	g1.putEdgeValue(n1, n6, 14.0);
	g1.putEdgeValue(n2, n3, 10.0);
	g1.putEdgeValue(n2, n4, 15.0);
	g1.putEdgeValue(n3, n4, 11.0);
	g1.putEdgeValue(n3, n6, 2.0);
	g1.putEdgeValue(n4, n5, 6.0);
	g1.putEdgeValue(n5, n6, 9.0);
	
	return ImmutableValueGraph.copyOf(g1);
    }
    
    public static ValueGraph<Node, Double> g4gDijkstraExample() {
	// https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/
	
	MutableValueGraph<Node, Double> g1 = ValueGraphBuilder.undirected().build();
	
	Node n0 = new Node(0);
	Node n1 = new Node(1);
	Node n2 = new Node(2);
	Node n3 = new Node(3);
	Node n4 = new Node(4);
	Node n5 = new Node(5);
	Node n6 = new Node(6);
	Node n7 = new Node(7);
	Node n8 = new Node(8);
	
	g1.addNode(n0);
	g1.addNode(n1);
	g1.addNode(n2);
	g1.addNode(n3);
	g1.addNode(n4);
	g1.addNode(n5);
	g1.addNode(n6);
	g1.addNode(n7);
	g1.addNode(n8);
	
	g1.putEdgeValue(n0, n1, 4.0);
	g1.putEdgeValue(n0, n7, 8.0);
	g1.putEdgeValue(n1, n2, 8.0);
	g1.putEdgeValue(n1, n7, 11.0);
	g1.putEdgeValue(n2, n3, 7.0);
	g1.putEdgeValue(n2, n5, 4.0);
	g1.putEdgeValue(n2, n8, 2.0);
	g1.putEdgeValue(n3, n4, 9.0);
	g1.putEdgeValue(n3, n5, 14.0);
	g1.putEdgeValue(n4, n5, 10.0);
	g1.putEdgeValue(n5, n6, 2.0);
	g1.putEdgeValue(n6, n7, 1.0);
	g1.putEdgeValue(n6, n8, 6.0);
	g1.putEdgeValue(n7, n8, 7.0);
	
	
	return ImmutableValueGraph.copyOf(g1);
    }
    
    public ValueGraph<Node, Double> g4gAstarExample() {
	// https://www.geeksforgeeks.org/a-search-algorithm/
	return null;
    }
    
    
    
}
