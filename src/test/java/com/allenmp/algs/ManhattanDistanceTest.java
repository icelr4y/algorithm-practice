package com.allenmp.algs;

import static org.junit.Assert.*;

import org.junit.Test;

import com.allenmp.algs.DistanceFunction;
import com.allenmp.algs.ManhattanDistance;

public class ManhattanDistanceTest {

    @Test
    public void shouldCalculateDistance() {
	Node n1 = new Node(1, new double[] {2, 2});
	Node n2 = new Node(1, new double[] {1, 5});
	
	DistanceFunction<Node> dist = new ManhattanDistance();
	assertEquals(4, dist.between(n1, n2), 0.000001);
    }

    @Test
    public void shouldBeOrderIndependent() throws Exception {
	
	Node n1 = new Node(1, new double[] {1, 2});
	Node n2 = new Node(1, new double[] {5, 1});
	
	DistanceFunction<Node> dist = new ManhattanDistance();
	assertEquals(5, dist.between(n1, n2), 0.000001);
	assertEquals(5, dist.between(n2, n1), 0.000001);
    }
    
    @Test
    public void shouldBeZeroDistanceToSelf() {
	Node n1 = new Node(1, new double[] {1, 1});
	
	DistanceFunction<Node> dist = new ManhattanDistance();
	assertEquals(0, dist.between(n1, n1), 0.000001);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldFailForDifferentDimensionality() {
	Node n1 = new Node(1, new double[] {0, 0});
	Node n2 = new Node(1, new double[] {0, 0, 0});
	n1.distanceTo(n2);
    }
}
