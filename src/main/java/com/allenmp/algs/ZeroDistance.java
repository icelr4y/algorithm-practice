package com.allenmp.algs;

public class ZeroDistance implements DistanceFunction<Node> {

    @Override
    public double between(Node o1, Node o2) {
	return 0;
    }

}
