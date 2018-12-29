package com.allenmp.algs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.graph.ValueGraph;

public class DijkstraShortestPathImpl<T> implements ShortestPathAlg<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DijkstraShortestPathImpl.class);

    private ValueGraph<T, Double> graph;
    private Map<T, Double> nodeToDist = new HashMap<>();
    private Map<T, T> nodeToPrev = new HashMap<>();
    private boolean calc = false;

    public DijkstraShortestPathImpl(ValueGraph<T, Double> graph) {
	super();
	this.graph = Objects.requireNonNull(graph);
    }

    public List<T> path(T start, T goal) {
	if (!graph.nodes().contains(start)) {
	    throw new IllegalArgumentException("Graph does not contain start node: " + start);
	}
	if (!graph.nodes().contains(goal)) {
	    throw new IllegalArgumentException("Graph does not contain goal node: " + goal);
	}

	if (goal == start) {
	    return Arrays.asList(start, start);
	}

	calculate(start, goal);;

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

    public double pathLength(T start, T goal) {
	if (!graph.nodes().contains(start)) {
	    throw new IllegalArgumentException("Graph does not contain start node: " + start);
	}
	if (!graph.nodes().contains(goal)) {
	    throw new IllegalArgumentException("Graph does not contain goal node: " + goal);
	}
	
	calculate(start, goal);
	
	double minLength = nodeToDist.get(goal);
	LOG.debug("FinalDistances: nodeToDist={} min={}", nodeToDist, minLength);
	return minLength;
    }

    private void calculate(T start, T goal) {
	if (calc == true) { 
	    LOG.trace("AlreadyCalculated");
	    return;
	}
	
	// init
	
	// TODO use PriorityQueue
	Set<T> q = new HashSet<>(graph.nodes());
	nodeToDist.clear();
	nodeToPrev.clear();
	for (T n : graph.nodes()) {
	    nodeToDist.put(n, Double.POSITIVE_INFINITY);
	}

	// source-source distance = 0
	nodeToDist.put(start, 0.0);

	while (!q.isEmpty()) {
	    
	    // find closest unvisited node U
	    T u = Collections.min(q, new Comparator<T>() {
		@Override
		public int compare(T o1, T o2) {
		    return nodeToDist.get(o1).compareTo(nodeToDist.get(o2));
		}
	    });
	    double dist = nodeToDist.get(u);

	    // mark U as "visited"
	    q.remove(u);

	    LOG.trace("MinDist: node={} dist={}", u, dist);

	    // terminate early if we reached the goal
	    if (goal.equals(u)) {
		LOG.trace("Terminating");
		calc = true;
		return;
	    }
	    
	    
	    // shortest (cumulative) path to each of U's neighbors N
	    for (T n : graph.adjacentNodes(u)) {

		double alt = dist + graph.edgeValueOrDefault(u, n, Double.POSITIVE_INFINITY);

		LOG.trace("Neighbor: n={} alt={}", n, alt);

		// update if there's a shorter path to N
		double oldDist = nodeToDist.get(n);
		if (alt < oldDist) {
		    LOG.trace("UpdatedDist: neighbor={} old={} new={}", n, oldDist, alt);
		    nodeToDist.put(n, alt);
		    nodeToPrev.put(n, u);
		}
	    }
	}
	
	calc = true;
    }

}
