package org.insa.algo.shortestpath;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.*;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(Parameterized.class) //pour créer une liste de teste, cf test priority Queue
public abstract class ShortestPathAlgorithmTest {

    @Parameterized.Parameters
    public static Collection<Object> data() throws IOException {
        Collection<Object> data = new ArrayList<>();

        //On fait des Shortest path data avec la Map France 
        GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream("/home/emile/Documents/INSA/sem6/europe/france/toulouse.mapgr"))));

        Graph graph1 = reader.read();

        ArcInspector arcinsp1 = ArcInspectorFactory.getAllFilters().get(0);
        ArcInspector arcinsp2 = ArcInspectorFactory.getAllFilters().get(2);
        
        //TEST [0]
        Node origin = graph1.get(14);
        Node dest = graph1.get(11156);
        data.add(new ShortestPathData(graph1, origin, dest, arcinsp1));
        
        //TEST [1]
        origin = graph1.get(3264);
        dest = graph1.get(835);
        data.add(new ShortestPathData(graph1, origin, dest, arcinsp2));
        
        //TEST [2]
        //Chemin imposible car le long du canal en voiture
        origin = graph1.get(17957);
        dest = graph1.get(17955);
        data.add(new ShortestPathData(graph1, origin, dest, arcinsp2));

        
        
        //On fait des Shortest path data avec la Map CarréDense
        reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream("/home/emile/Documents/INSA/sem6/extras/carre.mapgr"))));

        //TEST [3]
        Graph graph2 = reader.read();
        origin = graph2.get(5);
        dest = graph2.get(12);
        data.add(new ShortestPathData(graph2, origin, dest, arcinsp1));
        
        //TEST [4]
        origin = graph2.get(12);
        dest = graph2.get(16);
        data.add(new ShortestPathData(graph2, origin, dest, arcinsp1));
        
        //TEST [5]
        origin = graph2.get(4);
        dest = graph2.get(3);
        data.add(new ShortestPathData(graph2, origin, dest, arcinsp2));

        return data;
    }

    //C'est la classe qui sera modifiée lors de l'instanciation de la classe en Dijkstra ou A*
    protected abstract ShortestPathAlgorithm createAlgorithm(ShortestPathData data);

    @Parameterized.Parameter
    public ShortestPathData data;

    @Test
    public void testRunAvecOracle() {

        //On créé dl'oracle et le testé 
        ShortestPathAlgorithm oracle = new BellmanFordAlgorithm(data);
        ShortestPathSolution oracleSol = oracle.run();
        ShortestPathAlgorithm algoUsed = createAlgorithm(data);
        ShortestPathSolution algoSol = algoUsed.run();

        //On compare les deux
        assertEquals(oracleSol.isFeasible(), algoSol.isFeasible());
        if (oracleSol.isFeasible() && algoSol.isFeasible()) {
            Path oraclePath = oracleSol.getPath();
            Path algoUsedPath = algoSol.getPath();

            switch (data.getMode()) {
                case TIME:
                    assertEquals(0, Double.compare(oraclePath.getMinimumTravelTime(), algoUsedPath.getMinimumTravelTime()));
                    break;
                case LENGTH:
                    assertEquals(0, Double.compare(oraclePath.getLength(), algoUsedPath.getLength()));
                    break;
            }
        }
    }

    
    /*
     * La philosophie de ce test est dé vérifier que le chemin obtenu est optimal. 
     * Pour cela, on concatène deux chemins supposé optimaux partant de l'origine et arrivant à la destination passant par 
     * un point intermédiaire aléatoire. 
     * Ce nouveau chemin doit être supérieur ou egale au chemin direct obtenu initialement
     */
    @Test
    public void testRunSansOracle() {
        ShortestPathAlgorithm algoUsed = createAlgorithm(data);
        ShortestPathSolution algoSol = algoUsed.run();

        //Test : cost(a->c) <= cost(a->b) + cost(b->c) for every b

        //On prends les params de data
        Node origin = data.getOrigin();
        Node destination = data.getDestination();
        ArcInspector inspector = data.getArcInspector();

        Graph graph = data.getGraph();
        int graphSize = graph.size();

        Random rand = new Random();
        for (int i = 0; i < 5; i++) {

            //on prends un noeud différent du noeud origine et dest qui est aléatoire
            Node noeudInter = data.getGraph().get(rand.nextInt(graphSize));
            while (noeudInter.equals(origin) || noeudInter.equals(destination))
                noeudInter = data.getGraph().get(rand.nextInt(graphSize));

            //Prix de origine à intermédiaire
            ShortestPathData firstPathData = new ShortestPathData(graph, origin, noeudInter, inspector);
            ShortestPathAlgorithm firstPathAlgo = createAlgorithm(firstPathData);
            ShortestPathSolution firstPathSoluce = firstPathAlgo.run();

            //prix de Intermédiaire à destination
            ShortestPathData secondPathData = new ShortestPathData(graph, noeudInter, destination, inspector);
            ShortestPathAlgorithm secondPathAlgo = createAlgorithm(secondPathData);
            ShortestPathSolution secondPathSoluce = secondPathAlgo.run();
            
            //prix du chemin direct
            ShortestPathData directPathData = new ShortestPathData(graph, origin, destination, inspector);
            ShortestPathAlgorithm directPathAlgo = createAlgorithm(directPathData);
            ShortestPathSolution directPathSoluce = directPathAlgo.run();

            if (algoSol.isFeasible()) {
                if(firstPathSoluce.isFeasible() && secondPathSoluce.isFeasible()) {

                    Path firstPath = firstPathSoluce.getPath();
                    Path secondPath = secondPathSoluce.getPath();

                    /*Le chemin alternatif est la concaténation des deux intermédiaires :
                     * Origine -> Intermédiaire + Intermédiaire -> Destination
                     */
                    Path altPath = Path.concatenate(firstPath, secondPath);

                    //Soluce path est le chemin direct Origine -> Destination
                    Path solucePath = directPathSoluce.getPath();

                    //On vérifie que O->I->D est plus long ou égal à O->D
                    switch (data.getMode()) {
                        case TIME:
                            assertTrue(Double.compare(altPath.getMinimumTravelTime(),
                                    solucePath.getMinimumTravelTime()) >= 0);
                            break;
                        case LENGTH:
                            assertTrue(Double.compare(altPath.getLength(),
                                    solucePath.getLength()) >= 0);
                            break;
                    }
                }

            } else {
                assertTrue(!firstPathSoluce.isFeasible()
                        || !secondPathSoluce.isFeasible());
            }
        }
    }
}