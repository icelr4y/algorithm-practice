package com.allenmp.algs;

import com.allenmp.algs.Node;

public class ManhattanDistance implements DistanceFunction<Node> {

    @Override
    public double between(Node o1, Node o2) {
	
	double[] c1 = o1.getCoords();
	int dim1 = c1.length;
	double[] c2 = o2.getCoords();
	int dim2 = c2.length;
	
	if (dim1 != dim2) {
	    throw new IllegalArgumentException("Node coords have different dimensionality (" + dim1 + " vs " + dim2 + ")");
	}
	
	// d = sqrt( (x1-x2)^2 + (y1-y2)^2 + ... )
	double d = 0;
	for (int i=0; i<dim1; i++) {
	    d += Math.abs(c1[i] - c2[i]);
	}
	return d;
    }

    
    
    
}
