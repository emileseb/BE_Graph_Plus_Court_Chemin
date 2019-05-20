package org.insa.algo.utils;
import org.insa.graph.Arc;

import org.insa.graph.Point; // pour la distance
import org.insa.algo.AbstractInputData.*;
import org.insa.algo.shortestpath.ShortestPathData;//pour le mode (choix de l'heuristique);

public class LabelStar extends Label implements Comparable<Label>{

	private double heuristic;

	public LabelStar(int sommetC, boolean marque, float cout, Arc pere, ShortestPathData data) {
		super(sommetC,marque,cout,pere);
		float h = (float)Point.distance(data.getGraph().getNodes().get(sommetC).getPoint(), data.getDestination().getPoint());
		if (data.getMode() == Mode.TIME) {
			int speed;
			if (data.getMaximumSpeed()==-1) {
				speed = data.getGraph().getGraphInformation().getMaximumSpeed()*1000/3600; //vitesse en km/h qu'on transforme en m/s
			}else {
				speed = (Math.min(data.getMaximumSpeed(), data.getGraph().getGraphInformation().getMaximumSpeed()))*1000/3600; //vitesse en km/h qu'on transforme en m/s
			}
			h = h/(float)(speed);
		}
		this.heuristic = h;
	}


	public double getHeuristic() {return this.heuristic;}

	public void setheuristic(double h) {this.heuristic = h;}
	
	public float getCost() {
		return this.cout;
	} 

	public float getTotalCost() {
		return ((float)this.heuristic + this.cout);
	}   
	
	public int compareTo(Label l) {
		if (super.compareTo(l)==0) {
			if (this.cout > l.cout)
	    		return -1;
	    	else if(this.cout < l.cout)
	    		return 1;
	    	else 
	    		return 0;
		}			
		else 
			return super.compareTo(l);
	}
}
