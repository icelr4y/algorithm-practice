package com.allenmp.algs;

import java.util.List;

public interface ShortestPathAlg<T> {
    void setStart(T start);
    void setGoal(T goal);
    void setEndpoints(T start, T goal);
    List<T> path();
    double pathLength();
    int countVisited();
}

