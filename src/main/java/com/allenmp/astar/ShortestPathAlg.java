package com.allenmp.astar;

import java.util.List;

public interface ShortestPathAlg<T> {
    List<T> path(T start, T goal);
    double pathLength(T start, T goal);
}
