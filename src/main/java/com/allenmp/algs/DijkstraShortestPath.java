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

public class DijkstraShortestPath<T> implements ShortestPathAlg<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DijkstraShortestPath.class);

    private ValueGraph<T, Double> graph;
    private Map<T, Double> nodeToDist = new HashMap<>();
    private Map<T, T> nodeToPrev = new HashMap<>();

    private T start;
    private T goal;
    private boolean calc = false;

    public DijkstraShortestPath(ValueGraph<T, Double> graph) {
	super();
	this.graph = Objects.requireNonNull(graph);
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

    public int countVisited() {
	calculate();
	return nodeToPrev.size();
    }
    
    public List<T> path() {
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

    public double pathLength() {

	calculate();

	double minLength = nodeToDist.get(goal);
	LOG.debug("FinalDistances: nodeToDist={} min={}", nodeToDist, minLength);
	return minLength;
    }

    private void calculate() {
	if (calc == true) {
	    LOG.trace("AlreadyCalculated");
	    return;
	}
	
	Objects.requireNonNull(start);
	Objects.requireNonNull(goal);
	

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
