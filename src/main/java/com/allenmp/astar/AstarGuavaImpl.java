package com.allenmp.astar;

import java.util.List;

import com.google.common.graph.ValueGraph;

public class AstarGuavaImpl<T> implements ShortestPathAlg<T> {

    private ValueGraph<T, Double> graph;

    public AstarGuavaImpl(ValueGraph<T, Double> graph) {
	super();
	this.graph = graph;
    }

    public List<T> path(T start, T goal) {

	return null;
    }

    public double pathLength(T start, T goal) {

	return 0;
    }

}
