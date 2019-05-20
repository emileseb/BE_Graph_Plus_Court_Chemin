package org.insa.algo.shortestpath;

import org.insa.algo.utils.Label;
import org.insa.algo.utils.LabelStar;
import org.insa.graph.Arc;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected Label newLabel(int sommetCourant, boolean marque, float cout, Arc pere, ShortestPathData data) {
    	Label l = new LabelStar(sommetCourant, marque, cout, pere, data);
    	return l;
    }
    
    
    
}
