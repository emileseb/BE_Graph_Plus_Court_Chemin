package org.insa.algo.shortestpath;

public class AStarAlgorithmTest extends ShortestPathAlgorithmTest {

    @Override
    protected ShortestPathAlgorithm createAlgorithm(ShortestPathData data) {
        return new AStarAlgorithm(data);
    }
}