package com.allenmp.algs;

import com.allenmp.algs.EuclideanDistance;

public class Node {

    private int id;
    private double[] coords;

    public Node(int id) {
	super();
	this.id = id;
    }

    public Node(int id, double[] xy) {
	super();
	this.id = id;
	this.coords = xy;
    }

    public int getId() {
	return id;
    }

    public double[] getCoords() { 
	return coords;
    }
    
    public double distanceTo(Node other) {
	return new EuclideanDistance().between(this, other);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + id;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Node other = (Node) obj;
	if (id != other.id)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "Node-" + id;
    }

}
