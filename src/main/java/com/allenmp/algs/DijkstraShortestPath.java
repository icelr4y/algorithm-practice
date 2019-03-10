package com.allenmp.algs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.graph.ValueGraph;

public class DijkstraShortestPath<T> implements ShortestPathAlg<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DijkstraShortestPath.class);

    private ValueGraph<T, Double> graph;

    // Each node to its final calculated distance
    private Map<T, Double> nodeToDist = new HashMap<>();

    // Priority Queue to always return closest unvisited node
    private Queue<T> unvisited = new PriorityQueue<>(new Comparator<T>() {
	@Override
	public int compare(T o1, T o2) {
	    return nodeToDist.get(o1).compareTo(nodeToDist.get(o2));
	}
    });

    // Each node to the previous node along the calculated shortest route
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

	nodeToDist.clear();
	nodeToPrev.clear();

	// Start node is initialized to zero distance, everything else is
	// unknown
	unvisited.clear();
	for (T node : graph.nodes()) {
	    if (start.equals(node)) {
		nodeToDist.put(node, 0.0);
		unvisited.add(node);
	    } else {
		nodeToDist.put(node, Double.POSITIVE_INFINITY);
		unvisited.add(node);
	    }
	}

	while (!unvisited.isEmpty()) {
	    // "visit" next unvisited node
	    T closestNode = unvisited.poll();
	    Double distance = nodeToDist.get(closestNode);

	    LOG.trace("ClosestNode: node={} dist={}", closestNode, distance);

	    // terminate early if we reached the goal
	    if (goal.equals(closestNode)) {
		LOG.trace("Terminating");
		calc = true;
		break;
	    }

	    // shortest (cumulative) path to each of U's neighbors N
	    evaluateNeighbors(closestNode);
	}

	calc = true;
    }

    private void evaluateNeighbors(T start) {

	for (T neighbor : graph.adjacentNodes(start)) {
	    double alternate = nodeToDist.get(start) + graph.edgeValueOrDefault(start, neighbor, Double.POSITIVE_INFINITY);

	    LOG.trace("Neighbor: n={} alt={}", neighbor, alternate);

	    // update if there's a shorter path to N
	    double oldDist = getDistance(neighbor);
	    if (alternate < oldDist) {
		LOG.trace("UpdatedDist: neighbor={} old={} new={}", neighbor, oldDist, alternate);

		updateDistance(neighbor, alternate);
		nodeToPrev.put(neighbor, start);
	    }
	}
    }

    public Double getDistance(T node) {
	return nodeToDist.get(node);
    }

    private void updateDistance(T node, Double dist) {
	nodeToDist.put(node, dist);
	// remove and re-add to update queue
	unvisited.remove(node);
	unvisited.add(node);
    }

    /**
     * Keeps track of a graph Node and its shortest distance from the source
     * 
     * @author mallen
     *
     * @param <X>
     */
    private class NodeDist {
	public T node;
	public Double distance = Double.POSITIVE_INFINITY;

	public NodeDist(T node, Double distance) {
	    super();
	    this.node = node;
	    this.distance = distance;
	}

    }

}
