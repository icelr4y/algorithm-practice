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

public class AstarShortestPath<T> implements ShortestPathAlg<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DijkstraShortestPath.class);

    private ValueGraph<T, Double> graph;
    private DistanceFunction<T> heuristic;

    private T start;
    private T goal;
    private boolean calc = false;
    
    // Actual distance from source to node
    private Map<T, Double> nodeToGScore = new HashMap<>();

    // fScore(node) = gScore(start, node) + heuristic(node, goal)
    // "total cost is actual cost to current node, plus estimated cost from current node to goal"
    private Map<T, Double> nodeToFScore = new HashMap<>();
    
    private Map<T, T> nodeToPrev = new HashMap<>();

    public AstarShortestPath(ValueGraph<T, Double> graph, DistanceFunction<T> heuristic) {
	super();
	this.graph = Objects.requireNonNull(graph);
	this.heuristic = Objects.requireNonNull(heuristic);
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

    public List<T> path() {
	if (goal == start) {
	    return Arrays.asList(start, start);
	}

	calculate();

	if (Double.isInfinite(nodeToGScore.get(goal))) {
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
    
    public int countVisited() {
	calculate();
	return nodeToPrev.size();
    }

    public double pathLength() {
	calculate();
	
	double minLength = nodeToGScore.get(goal);
	LOG.debug("FinalDistances: nodeToDist={} min={}", nodeToGScore, minLength);
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
	Set<T> openSet = new HashSet<>();
	openSet.add(start);
	Set<T> closedSet = new HashSet<>();
	nodeToGScore.clear();
	nodeToFScore.clear();
	nodeToPrev.clear();
	for (T n : graph.nodes()) {
	    nodeToGScore.put(n, Double.POSITIVE_INFINITY);
	    nodeToFScore.put(n, Double.POSITIVE_INFINITY);
	}

	// source-source distance = 0
	nodeToGScore.put(start, 0.0);
	// source-goal distance is entirely heuristic
	nodeToFScore.put(start, heuristic.between(start, goal));

	while (!openSet.isEmpty()) {
	    
	    // unvisited node with smallest heuristic distance
	    T u = Collections.min(openSet, new Comparator<T>() {
		@Override
		public int compare(T o1, T o2) {
		    return nodeToFScore.get(o1).compareTo(nodeToFScore.get(o2));
		}
	    });
	    double gScore = nodeToGScore.get(u);
	    double fScore = nodeToFScore.get(u);

	    // mark U as "visited"
	    openSet.remove(u);
	    closedSet.add(u);

	    LOG.trace("Visit: node={} gScore={} fScore={}", u, gScore, fScore);

	    // terminate early if we reached the goal
	    if (goal.equals(u)) {
		LOG.trace("Terminating");
		calc = true;
		return;
	    }
	    
	    // shortest (cumulative) path to each of U's neighbors N
	    for (T n : graph.adjacentNodes(u)) {
		if (closedSet.contains(n)) {
		    LOG.trace("AlreadyVisited: n={}", n);
		    continue;
		}
		
		double newGScore = gScore + graph.edgeValueOrDefault(u, n, Double.POSITIVE_INFINITY);

		LOG.trace("Neighbor: n={} alt={}", n, newGScore);
		openSet.add(n);

		// if there's a shorter path to N, update G and F scores
		double oldGScore = nodeToGScore.get(n);
		if (newGScore < oldGScore) {
		    LOG.trace("FoundShorterPath: node={} old={} new={}", n, oldGScore, newGScore);
		    nodeToGScore.put(n, newGScore);
		    nodeToFScore.put(n, newGScore + heuristic.between(n, goal));
		    nodeToPrev.put(n, u);
		}
	    }
	}
	
	calc = true;
    }
}
