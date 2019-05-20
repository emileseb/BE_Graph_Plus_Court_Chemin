package org.insa.algo.shortestpath;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraAlgorithmTest {
	
	@SuppressWarnings("unused")
	private static String hotGaronne, hotNormandie, k_rey ;
	private static ArcInspector arcinsp1, arcinsp2;
	// List of nodes
    private static Node[] nodes;
    //nom  : R = routière NR = non Routière; Type de parcours (Noeud origine et dest) ; arc inspector 1 ou 3 : A1 A3; T = temps D = distance
    private static ShortestPathData R1singlenodA1T, NRclassiqueA1T, R1classiqueA2D, NRclassiqueA2D ;
    
    // Create a graph reader.
    private static GraphReader R1, R2, NR;

    // TODO: Read the graph. Fait
    private static Graph grR1, grR2, grNR;
    
    private static DijkstraAlgorithm dijAlg1, dijAlg2, dijAlg3, dijAlg4;
    
    private static Path p1, p2, p3, p4;
    
    @BeforeClass
	public static void initAll() throws IOException {
		hotGaronne = "/home/emile/Documents/INSA/sem6/europe/france/haute-garonne.mapgr"; //des cartes chaudes de ta région
		hotNormandie = "/home/emile/Documents/INSA/sem6/europe/france/haute-normandie.mapgr";  //il fait quand même moins chaud qu'à Toulouse
		k_rey = "/home/emile/Documents/INSA/sem6/extras/carre-dense.mapgr";
		R1 = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(hotGaronne))));
		R2 = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(hotNormandie))));
		NR = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(k_rey))));
		
		grR1 = R1.read();
		grR2 = R2.read();
		grNR = NR.read();
		
		arcinsp1 = ArcInspectorFactory.getAllFilters().get(0);
		arcinsp2 = ArcInspectorFactory.getAllFilters().get(2);
		
        R1singlenodA1T = new ShortestPathData(grR1, grR1.getNodes().get(0), grR1.getNodes().get(0), arcinsp1);
        NRclassiqueA1T = new ShortestPathData(grNR,grNR.getNodes().get(0), grNR.getNodes().get(1),arcinsp1);
        R1classiqueA2D = new ShortestPathData(grR1,grR1.getNodes().get(0), grR1.getNodes().get(6),arcinsp2);
        NRclassiqueA2D = new ShortestPathData(grNR,grNR.getNodes().get(0), grNR.getNodes().get(1),arcinsp2);
        
        dijAlg1 = new DijkstraAlgorithm(R1singlenodA1T) ; //il faut les .doRun() pour obtenir le shortest path solution 
        dijAlg2 = new DijkstraAlgorithm(NRclassiqueA1T);
        dijAlg3 = new DijkstraAlgorithm(R1classiqueA2D);
        dijAlg4 = new DijkstraAlgorithm(NRclassiqueA2D);
        
        p1 = dijAlg1.doRun().getPath();
        p2 = dijAlg2.doRun().getPath();
        p3 = dijAlg3.doRun().getPath();
        p4 = dijAlg4.doRun().getPath();
	}
	
	@Test
	public void testIsValid() {
		assertTrue(p1.isValid());
		assertTrue(p2.isValid());
		assertTrue(p3.isValid());
		assertTrue(p4.isValid());
	}
	
	@Test
	public void testOrigDest() {
		assertTrue(p1.getOrigin().equals(dijAlg1.getInputData().getOrigin()));
		assertTrue(p1.getDestination().equals(dijAlg1.getInputData().getDestination()));
		assertTrue(p2.getOrigin().equals(dijAlg2.getInputData().getOrigin()));
		assertTrue(p2.getDestination().equals(dijAlg2.getInputData().getDestination()));
		assertTrue(p3.getOrigin().equals(dijAlg3.getInputData().getOrigin()));
		assertTrue(p3.getDestination().equals(dijAlg3.getInputData().getDestination()));
		assertTrue(p4.getOrigin().equals(dijAlg4.getInputData().getOrigin()));
		assertTrue(p4.getDestination().equals(dijAlg4.getInputData().getDestination()));

	}

}
