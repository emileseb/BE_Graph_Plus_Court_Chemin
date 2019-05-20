package org.insa.algo.utils;
import org.insa.graph.Arc;

import org.insa.graph.Point; // pour la distance
import org.insa.algo.AbstractInputData;
import org.insa.algo.AbstractInputData.Mode;
import org.insa.algo.shortestpath.ShortestPathData;//pour le mode (choix de l'heuristique);

public class labelstar extends Label{

private double heuristic;

public labelstar(int sommetC, boolean marque, float cout, Arc pere, ShortestPathData data) {
	super(sommetC,marque,cout,pere);
	float h = (float)Point.distance(data.getGraph().getNodes().get(sommetC).getPoint(), data.getDestination().getPoint());
	if (data.getMode() == Mode.TIME) {
		int speed = (Math.max(data.getMaximumSpeed(), data.getGraph().getGraphInformation().getMaximumSpeed()))*1000/3600; //vitesse en km/h qu'on transforme en m/s 
		h = h/(float)(speed);
	}
	this.heuristic = h;
}


public double getHeuristic() {return this.heuristic;}

public void setheuristic(double h) {this.heuristic = h;}

public float getTotalCost() {return ((float)this.heuristic + this.cout);}


	
	
	
}
