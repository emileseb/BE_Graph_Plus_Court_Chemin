package org.insa.algo.utils;
import org.insa.graph.*;


public class Label implements Comparable<Label>{
	private int sommetCourant; 	// : sommet associé à ce label (sommet ou numéro de sommet).
    private boolean marque;		// : booléen, vrai lorsque le coût min de ce sommet est définitivement connu par l'algorithme.
    protected float cout ; 			// : valeur courante du plus court chemin depuis l'origine vers le sommet.
    private Arc pere  ;			// : correspond au sommet précédent sur le chemin correspondant au plus court chemin courant. Afin de reconstruire le chemin à la fin de l'algorithme, mieux vaut stocker l'arc plutôt que seulement le père.
    
    public Label(int sommetCourant, boolean marque, float cout, Arc pere) {
    	this.sommetCourant = sommetCourant;
    	this.marque = marque;
    	this.cout = cout;
    	this.pere = pere;
    }
    
    public float getCost() {
    	return this.cout;
    }
    
    public float getTotalCost() {
    	return this.cout;
    }
    
    public int getSommetCourant() {
    	return this.sommetCourant;
    }
    
    public Arc getPere() {
    	return this.pere;
    }
    
    public void setCost(float cost) {
    	this.cout = cost;
    }
    
    public void setPere(Arc pere) {
    	this.pere = pere;
    }
    
    public void setMarque(boolean bool) {
    	this.marque = bool;
    }
    
    
    public double getHeuristic() {return 0;}
    
    public boolean getMarque() {return this.marque;}

    public int compareTo(Label l) {
    	if (this.getTotalCost() < l.getTotalCost())
    		return -1;
    	else if(this.getTotalCost() > l.getTotalCost())
    		return 1;
    	else 
    		return 0;
    }
    
    

}
