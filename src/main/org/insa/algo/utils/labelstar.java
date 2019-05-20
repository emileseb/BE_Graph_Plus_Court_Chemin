package org.insa.algo.utils;
import org.insa.graph.Arc;

import org.insa.graph.Point; // pour la distance
import org.insa.algo.AbstractInputData; 
import org.insa.algo.shortestpath.ShortestPathData;//pour le mode (choix de l'heuristique);

public class labelstar extends Label{

private double heuristic;
	
	public labelstar(int sommetC, boolean marque, float cout, Arc pere, ShortestPathData data) {
    	super(sommetC,marque,cout,pere);
    	if (data.getMode() == Mode.LENGTH) {}
    	
    }

public double getHeuristic() {return this.heuristic;}

public void setheuristic(double h) {this.heuristic = h;}

public float getTotalCost() {return ((float)this.heuristic + this.cout);}
	
	
	
}
//ATTENTION DANS DIJKSTRA UTILISER TOTAL COST ET COMPARE TO A LA PLACE DES GET COST