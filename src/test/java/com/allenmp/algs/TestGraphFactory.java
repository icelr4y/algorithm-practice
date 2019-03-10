package com.allenmp.algs;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

public class TestGraphFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TestGraphFactory.class);

    /**
     * Creates a directed graph approximately like the example at
     * https://www.geeksforgeeks.org/bellman-ford-algorithm-dp-23/
     * 
     * @return
     */
    public static ValueGraph<Node, Double> g4gBellmanFordExample() {
	MutableValueGraph<Node, Double> g = ValueGraphBuilder.directed().build();

	Node n1 = new Node(1, new double[] { 0, 1 });
	Node n2 = new Node(2, new double[] { 2, 2 });
	Node n3 = new Node(3, new double[] { 1, 0 });
	Node n4 = new Node(4, new double[] { 3, 0 });
	Node n5 = new Node(5, new double[] { 4, 1 });

	g.addNode(n1);
	g.addNode(n2);
	g.addNode(n3);
	g.addNode(n4);
	g.addNode(n5);

	g.putEdgeValue(n1, n2, -1.0);
	g.putEdgeValue(n1, n3, 4.0);
	g.putEdgeValue(n2, n3, 3.0);
	g.putEdgeValue(n2, n4, 2.0);
	g.putEdgeValue(n2, n5, 2.0);
	g.putEdgeValue(n4, n2, 1.0);
	g.putEdgeValue(n4, n3, 5.0);
	g.putEdgeValue(n5, n4, -3.0);

	return ImmutableValueGraph.copyOf(g);
    }

    /**
     * Creates an undirected graph approximately like the animated example at
     * https://en.wikipedia.org/wiki/A*_search_algorithm
     * 
     * @return
     */
    public static ValueGraph<Node, Double> wikipediaGridAstarExample() {

	// init a grid of nodes
	int m = 20;
	int n = 20;
	int id = 0;
	Set<Node> nodes = new HashSet<>();
	for (int x = 0; x < m; x++) {
	    for (int y = 0; y < n; y++) {
		id++;
		nodes.add(new Node(id, new double[] { x, y }));
	    }
	}

	// remove some
	Function<Node, Boolean> isBlocked = new Function<Node, Boolean>() {
	    @Override
	    public Boolean apply(Node nn) {
		double[] coords = nn.getCoords();
		double x = coords[0];
		double y = coords[1];

		// a rough "knight-move" shape covering 1/3 of x and y
		if (x > m / 3 && x < 2 * m / 3 && y > n / 2 && y < 4 * n / 6) {
		    return true;
		} else if (x > 4 * n / 6 && x < 5 * n / 6 && y > n / 3 && y < 2 * n / 3) {
		    return true;
		} else {
		    return false;
		}
	    }
	};

	Iterator<Node> iter = nodes.iterator();
	while (iter.hasNext()) {
	    Node node = iter.next();
	    if (isBlocked.apply(node)) {
		LOG.trace("Remove: node={}", node);
		iter.remove();
	    }
	}

	// Create the graph
	MutableValueGraph<Node, Double> g = ValueGraphBuilder.undirected().build();
	for (Node node : nodes) {
	    g.addNode(node);
	}

	// Link to adjacent nodes
	for (Node node : g.nodes()) {
	    DistanceFunction<Node> distFunc = new EuclideanDistance();
	    Set<Node> linked = nodes.stream().filter(i -> distFunc.between(i, node) < 2).collect(Collectors.toSet());
	    linked.remove(node); // no self-loops

	    LOG.trace("Linking: node={} others={}", node, linked);
	    for (Node toLink : linked) {
		g.putEdgeValue(node, toLink, distFunc.between(node, toLink));
	    }
	}

	return ImmutableValueGraph.copyOf(g);

    }

    /**
     * A directed graph with a cycle of negative weights, really a modified
     * version of {@link #g4gBellmanFordExample()}.
     * 
     * @return
     */
    public static ValueGraph<Node, Double> negativeCycleGraph() {
	MutableValueGraph<Node, Double> g = ValueGraphBuilder.directed().build();

	Node n1 = new Node(1, new double[] { 0, 1 });
	Node n2 = new Node(2, new double[] { 2, 2 });
	Node n3 = new Node(3, new double[] { 1, 0 });
	Node n4 = new Node(4, new double[] { 3, 0 });
	Node n5 = new Node(5, new double[] { 4, 1 });

	g.addNode(n1);
	g.addNode(n2);
	g.addNode(n3);
	g.addNode(n4);
	g.addNode(n5);

	g.putEdgeValue(n1, n2, -1.0);
	g.putEdgeValue(n1, n3, 4.0);
	g.putEdgeValue(n2, n3, 3.0);
	g.putEdgeValue(n2, n4, 2.0); 
	g.putEdgeValue(n2, n5, -2.0); // flipped
	g.putEdgeValue(n4, n2, -1.0); // flipped
	g.putEdgeValue(n4, n3, 5.0);
	g.putEdgeValue(n5, n4, -3.0);

	return ImmutableValueGraph.copyOf(g);
    }

    /**
     * Creates an undirected graph roughly like the one at
     * https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm.
     * 
     * @return
     */
    public static ValueGraph<Node, Double> wikipediaDijkstraExample() {
	MutableValueGraph<Node, Double> g = ValueGraphBuilder.undirected().build();

	Node n1 = new Node(1, new double[] { 0, 0 });
	Node n2 = new Node(2, new double[] { 1, 0 });
	Node n3 = new Node(3, new double[] { 1, 1 });
	Node n4 = new Node(4, new double[] { 2, 1 });
	Node n5 = new Node(5, new double[] { 1, 2 });
	Node n6 = new Node(6, new double[] { 0, 1 });

	g.addNode(n1);
	g.addNode(n2);
	g.addNode(n3);
	g.addNode(n4);
	g.addNode(n5);
	g.addNode(n6);

	g.putEdgeValue(n1, n2, 7.0);
	g.putEdgeValue(n1, n3, 9.0);
	g.putEdgeValue(n1, n6, 14.0);
	g.putEdgeValue(n2, n3, 10.0);
	g.putEdgeValue(n2, n4, 15.0);
	g.putEdgeValue(n3, n4, 11.0);
	g.putEdgeValue(n3, n6, 2.0);
	g.putEdgeValue(n4, n5, 6.0);
	g.putEdgeValue(n5, n6, 9.0);

	return ImmutableValueGraph.copyOf(g);
    }

    /**
     * Creates an undirected graph roughly like the one at
     * https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/
     * 
     * @return
     */
    public static ValueGraph<Node, Double> g4gDijkstraExample() {
	MutableValueGraph<Node, Double> g = ValueGraphBuilder.undirected().build();

	Node n0 = new Node(0, new double[] { 0, 1 });
	Node n1 = new Node(1, new double[] { 1, 2 });
	Node n2 = new Node(2, new double[] { 2, 2 });
	Node n3 = new Node(3, new double[] { 3, 2 });
	Node n4 = new Node(4, new double[] { 4, 1 });
	Node n5 = new Node(5, new double[] { 3, 0 });
	Node n6 = new Node(6, new double[] { 2, 0 });
	Node n7 = new Node(7, new double[] { 1, 0 });
	Node n8 = new Node(8, new double[] { 2, 1 });

	g.addNode(n0);
	g.addNode(n1);
	g.addNode(n2);
	g.addNode(n3);
	g.addNode(n4);
	g.addNode(n5);
	g.addNode(n6);
	g.addNode(n7);
	g.addNode(n8);

	g.putEdgeValue(n0, n1, 4.0);
	g.putEdgeValue(n0, n7, 8.0);
	g.putEdgeValue(n1, n2, 8.0);
	g.putEdgeValue(n1, n7, 11.0);
	g.putEdgeValue(n2, n3, 7.0);
	g.putEdgeValue(n2, n5, 4.0);
	g.putEdgeValue(n2, n8, 2.0);
	g.putEdgeValue(n3, n4, 9.0);
	g.putEdgeValue(n3, n5, 14.0);
	g.putEdgeValue(n4, n5, 10.0);
	g.putEdgeValue(n5, n6, 2.0);
	g.putEdgeValue(n6, n7, 1.0);
	g.putEdgeValue(n6, n8, 6.0);
	g.putEdgeValue(n7, n8, 7.0);

	return ImmutableValueGraph.copyOf(g);
    }

}
