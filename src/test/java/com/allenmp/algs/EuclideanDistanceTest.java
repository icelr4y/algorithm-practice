package com.allenmp.algs;

import static org.junit.Assert.*;

import org.junit.Test;

import com.allenmp.algs.DistanceFunction;
import com.allenmp.algs.EuclideanDistance;

public class EuclideanDistanceTest {

    @Test
    public void shouldCalculateDistance() {
	Node n1 = new Node(1, new double[] {0, 0});
	Node n2 = new Node(1, new double[] {1, 1});
	
	DistanceFunction<Node> dist = new EuclideanDistance();
	assertEquals(Math.sqrt(2), dist.between(n1, n2), 0.000001);
    }

    @Test
    public void shouldBeZeroDistanceToSelf() {
	Node n1 = new Node(1, new double[] {1, 1});
	
	DistanceFunction<Node> dist = new EuclideanDistance();
	assertEquals(0, dist.between(n1, n1), 0.000001);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldFailForDifferentDimensionality() {
	Node n1 = new Node(1, new double[] {0, 0});
	Node n2 = new Node(1, new double[] {0, 0, 0});
	n1.distanceTo(n2);
    }
    
    
}
