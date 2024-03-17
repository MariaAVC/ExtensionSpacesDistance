package BHVExtMinDistance;

import java.util.*;
import java.util.concurrent.*; 
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import distanceAlg1.*;
//import BHVExtMinDistance.*;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.FileNotFoundException;


public class extendedGeoDistance{
    
    private List<geodesicDistance> listGeoDistance;
    
    private double bestDistance;
    
    public extendedGeoDistance(Vector<PhyloTree> ListTrees1, Vector<PhyloTree> ListTrees2){
        listGeoDistance = new ArrayList<geodesicDistance>();
        
        int oNum1 = ListTrees1.size();
        int oNum2 = ListTrees2.size();
        
        //For each pair of orthant extensions in the extension spaces we compute the Orthant Extension Distances in between them, find how it compares to the other distances already added to the list, and we add it to the correct position, also adding the orthants that produced this distance to each the list of orthants. 
        for (int k1 = 0; k1 < oNum1; k1++){
            PhyloTree T1 = ListTrees1.get(k1);
            for (int k2 = 0; k2 < oNum2; k2++){
                //System.out.println("************");
                //System.out.println("STARTING pair ("+k1+", "+k2+")");
                PhyloTree T2 = ListTrees2.get(k2);
                //long Start = System.currentTimeMillis();
                geodesicDistance tempGD = new geodesicDistance( T1, T2);
                //long End = System.currentTimeMillis();
                //double TimeSeconds = ((double)(End - Start))/1000;
                //System.out.println("THE DISTANCE WAS "+ tempOED.getDistance());
                //System.out.println("Time needed: " + TimeSeconds);
                //System.out.println("************");
                //System.out.println("");
                
                /*boolean Added = false;
                for(int i = 0; i < orderedOrthExtDistances.size(); i++){
                    if (tempOED.getDistance() < orderedOrthExtDistances.get(i).getDistance()){
                        Added = true;
                        orderedOrthExtDistances.add(i, tempOED);
                        break;
                    }
                }
                if (!Added){
                    orderedOrthExtDistances.add(orderedOrthExtDistances.size(), tempOED);
                }*/
                
                /*if ((orderedOrthExtDistances.size()>0) && (tempOED.getDistance() <= orderedOrthExtDistances.get(0).getDistance())){
                    orderedOrthExtDistances.add(0, tempOED);
                } else {
                    orderedOrthExtDistances.add(orderedOrthExtDistances.size(),tempOED);
                }*/
                listGeoDistance.add(tempGD);
            }
        }
        
        //The best trees, distance and geodesic will be those at the beginning of our list. 
        
        bestDistance = listGeoDistance.get(0).getDistance();
    } //end of constructor 2
    
    private void processParallelyWithExecutorService(List<treePair> Input){// throws InterruptedException {
        //ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        //List<CompletableFuture<Void>> futures = new ArrayList<>();

        final ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<geodesicDistance>> futures = new ArrayList<>();

        for (int i = 0; i < Input.size(); i++) {
            final treePair inpT = Input.get(i);
            try{
                Future<geodesicDistance> future = executor.submit(new callableGeoD(inpT));
                futures.add(future);
            } catch (Exception e){
                System.out.println("This happened " + i);
                System.out.println(e.getCause());
            }


                /*CompletableFuture.runAsync(() -> {
                //try {
                    DistancesTemp[i] = new OrthExtDistance(inpT, false);
                /*} catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, executorService);*/

        }
        System.out.println("The length of futures is: " + futures.size());
        int tempCount = 0;
        for (Future<geodesicDistance> future : futures) {
            tempCount++;
            try{
                listGeoDistance.add(future.get());   
            } catch (Exception e){
                System.out.println("Catched " + tempCount);
                System.out.println(e);
            }
        }
        /*try {
            for (Future<OrthExtDistance> future : futures) {
                orderedOrthExtDistances.add(future.get()); // do anything you need, e.g. isDone(), ...
            }
        } catch (Exception e) {
            System.out.println("And this");
            System.out.println(e);
        }*/
        executor.shutdown();
    }
    
    public extendedGeoDistance(Vector<PhyloTree> ListTrees1, Vector<PhyloTree> ListTrees2, int numThreads){
        listGeoDistance = new ArrayList<geodesicDistance>();
        
        List<treePair> TreePairs = new ArrayList<treePair>();
        
        for (PhyloTree TE1 : ListTrees1){
            for (PhyloTree TE2 : ListTrees2){
                TreePairs.add(new treePair(new PhyloTree(TE1), new PhyloTree(TE2)));
            }
        }
        
        //DistancesTemp = new OrthExtDistance[OEpairs.size()];
        
        processParallelyWithExecutorService(TreePairs);
        //orderedOrthExtDistances = OEs1.parallelStream().flatMap(OE1 -> OEs2.parallelStream().map(OE2 -> new OrthExtDistance(OE1, OE2, restricted))).collect(Collectors.toList());
        
        //orderedOrthExtDistances = OEpairs.parallelStream().map(OEpair -> new OrthExtDistance(OEpair, false)).collect(Collectors.toList());
        
        //for (orthantExtPair OEpar : OEpairs){
        //    orderedOrthExtDistances.add(new OrthExtDistance(OEpar, restricted));
        //}

        
        //The best trees, distance and geodesic will be those at the beginning of our list. 
        bestDistance = listGeoDistance.get(0).getDistance();
    } //end of constructor 3*/
    
    public void PrintSummary(){
        double[] tempDistances = new double[this.listGeoDistance.size()];
        for (int i = 0; i < this.listGeoDistance.size(); i++){
            tempDistances[i] = listGeoDistance.get(i).getDistance();
        }
        System.out.println("The distances are: " + Arrays.toString(tempDistances));
    }
    
    public void PrintTotal(){
        PhyloNicePrinter treePrinter = new PhyloNicePrinter();
        for (geodesicDistance GDp : listGeoDistance){
            System.out.println("---------------");
            System.out.println("Tree 1: " + treePrinter.toString(GDp.getStartTree()));
            System.out.println("Tree 2: " + treePrinter.toString(GDp.getEndTree()));
            System.out.println("The distance is " + GDp.getDistance());   
            System.out.println(treePrinter.toString(GDp.getCEs(), GDp.getStartTree().getLeaf2NumMap()));
            System.out.println(treePrinter.toString(GDp.getRS(), GDp.getStartTree().getLeaf2NumMap()));
            System.out.println("--------------- \n");
        }
        
        
    }
    
}