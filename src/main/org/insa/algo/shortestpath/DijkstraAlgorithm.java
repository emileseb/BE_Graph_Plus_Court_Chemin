package org.insa.algo.shortestpath;
import java.util.ArrayList;
import java.util.*;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.*;
import org.insa.graph.Arc;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        }

    @Override
    protected ShortestPathSolution doRun() {
    	ShortestPathData data = getInputData();
    	ShortestPathSolution solution = null;
    	// TODO:
    	ArrayList<Label> labelTab = new ArrayList<Label>();
    	BinaryHeap<Label> labelTas = new BinaryHeap<Label>();
    	
    	ArrayList<Arc> arcs= new ArrayList<Arc>();  
    	//Initialisation : L'idée est de remplir un tableau de label DANS L'ORDRE DE 0 a N-1, avec tous les noeuds du graph. 
    	for (Node n: data.getGraph().getNodes()) {	//On suppose que les nodes sont rangés dans l'ordre de leur ID !!!
    		labelTab.add(new Label(n.getId(),false,Float.POSITIVE_INFINITY,null)); 
    	}
    	labelTab.get(data.getOrigin().getId()).setCost(0);
    	labelTas.insert(labelTab.get(data.getOrigin().getId()));
    	
    	//Iterations
    	while (!labelTab.get(data.getDestination().getId()).getMarque()){	//label de data.dest n'est pas marqué on tourne
    		Label x = labelTas.deleteMin();
    		x.setMarque(true);
    		for (Arc a : data.getGraph().get(x.getSommetCourant()).getSuccessors()){
    			if (data.isAllowed(a)){
	    			Label y = labelTab.get(a.getDestination().getId()); 
	    			if(!(y.getMarque())) {
	    				if(y.getCost()>x.getCost()+data.getCost(a)) {
	    					y.setCost((float) (x.getCost()+data.getCost(a)));
	    					y.setPere(a);
	    					try {
	    						labelTas.remove(y);
	    					}
	    					catch (ElementNotFoundException e) {}
	    					labelTas.insert(y);
	    					labelTab.set(y.getSommetCourant(), y);
	    				}
	    			}
    			}
    		}
    	}
    	
    	//creation du chemin
    	Stack<Arc> pile = new Stack<Arc>();
    	Arc pere = labelTab.get(data.getDestination().getId()).getPere();
    	while(!(pere == null)) {
    		pile.push(pere);
    		pere = labelTab.get(pere.getOrigin().getId()).getPere();
    	}
    	while(!pile.isEmpty())
    		arcs.add(pile.pop());
    	
       /*
        * On commence Dijkstra a init on marque sommet init, on rajoute ses successeurs dans le tas on tej init du tas.
        *  ATTENTION ON OUBLIE D4INITIALISER LE COUT ET LE PERE A CHAQUE NOUVELLE ITERATION
        *  On prends le minimum en cout et pere du tas, on rajoute ses successeurs non marqués dans le tas et on l'enleve du tas. Ain si de suite jusqu arriver a marquer dest.  
        */
        solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(), arcs));
        return solution;
    }
    
    //unr fois qu'on a ordonné les noeuds on utilise shortest path creé avant 

}

