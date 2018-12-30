package com.allenmp.algs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;

public class BellmanFordShortestPath<T> implements ShortestPathAlg<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DijkstraShortestPath.class);

    private ValueGraph<T, Double> graph;

    private T start;
    private T goal;
    private boolean calc = false;

    // Actual distance from source to node
    private Map<T, Double> nodeToDist = new HashMap<>();

    private Map<T, T> nodeToPrev = new HashMap<>();

    public BellmanFordShortestPath(ValueGraph<T, Double> graph) {
	super();
	this.graph = Objects.requireNonNull(graph);
	checkGraph();
    }

    private void checkGraph() {
	// Undirected graphs can't have any negative edge weights
	if (!graph.isDirected()) {
	    for (EndpointPair<T> edge : graph.edges()) {
		Double weight = graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), 0.0);
		if (weight < 0) {
		    String msg = String.format("Undirected graphs can't have any negative edge weights: U=%1$s V=%2$s weight=%3%d",
			    edge.nodeU(), edge.nodeV(), weight);
		    throw new IllegalArgumentException(msg);
		}
	    }
	}
    }

    public void setEndpoints(T start, T goal) {
	setStart(start);
	setGoal(goal);
    }

    public void setStart(T newStart) {
	if (!graph.nodes().contains(newStart)) {
	    throw new IllegalArgumentException("Graph does not contain start node: " + newStart);
	}
	if (!newStart.equals(start)) {
	    calc = false;
	}
	this.start = newStart;
    }

    public void setGoal(T newGoal) {
	if (!graph.nodes().contains(newGoal)) {
	    throw new IllegalArgumentException("Graph does not contain goal node: " + newGoal);
	}
	if (!newGoal.equals(goal)) {
	    calc = false;
	}
	this.goal = newGoal;
    }

    @Override
    public List<T> path() {
	calculate();
	if (goal == start) {
	    return Arrays.asList(start, start);
	}

	calculate();

	if (Double.isInfinite(nodeToDist.get(goal))) {
	    // goal is unreachable
	    return new ArrayList<>();
	}

	// walk path backward from goal to start
	LinkedList<T> path = new LinkedList<>();
	path.addLast(goal);
	while (!start.equals(path.getFirst())) {
	    T prev = nodeToPrev.get(path.getFirst());
	    LOG.trace("Path: node={} prev={}", path.getFirst(), prev);
	    path.addFirst(prev);
	}

	LOG.debug("Path: {}", path);
	return path;
    }

    @Override
    public double pathLength() {
	calculate();

	double minLength = nodeToDist.get(goal);
	LOG.debug("FinalDistances: nodeToDist={} min={}", nodeToDist, minLength);
	return minLength;
    }

    @Override
    public int countVisited() {
	calculate();
	return nodeToPrev.size();
    }

    private void calculate() {
	if (calc) {
	    return;
	}

	for (T n : graph.nodes()) {
	    nodeToDist.put(n, Double.POSITIVE_INFINITY);
	}

	// Distance from source to itself is zero
	nodeToDist.put(start, 0.0);

	int nodeCount = graph.nodes().size();
	for (int i = 0; i < nodeCount; i++) {
	    for (EndpointPair<T> edge : graph.edges()) {
		T u = edge.nodeU();
		T v = edge.nodeV();
		Double distUToV = graph.edgeValueOrDefault(u, v, 0.0);
		LOG.trace("Edge: U={} V={} w={}", u, v, distUToV);

		Double uDist = nodeToDist.get(u);
		Double vDist = nodeToDist.get(v);

		Double alt = uDist + distUToV;
		if (alt < vDist) {
		    LOG.trace("Updated: node={} old={} new={}", v, vDist, alt);
		    nodeToDist.put(v, alt);
		    nodeToPrev.put(v, u);
		}

		// Try them the other way too for undirected graphs
		if (!graph.isDirected()) {
		    Double uDist2 = nodeToDist.get(v);
		    Double vDist2 = nodeToDist.get(u);

		    Double alt2 = uDist2 + distUToV;
		    if (alt2 < vDist2) {
			LOG.trace("SwitchedAndUpdated: node={} old={} new={}", u, vDist2, alt2);
			nodeToDist.put(u, alt2);
			nodeToPrev.put(u, v);
		    }
		}
	    }
	}

	// Check for negative weight cycles
	for (EndpointPair<T> edge : graph.edges()) {
	    Double w = graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), 0.0);
	    Double uDist = nodeToDist.get(edge.nodeU());
	    Double vDist = nodeToDist.get(edge.nodeV());
	    if (uDist + w < vDist) {
		throw new IllegalStateException("Graph contains a negative-weight cycle");
	    }
	}

    }

}
