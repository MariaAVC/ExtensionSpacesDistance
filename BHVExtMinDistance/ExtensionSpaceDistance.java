/** This is intended as the class defining the distance between two extension spaces. It will include methods to compute this distance, as well as a list of pairs of trees that obtain smaller distances per each maximal orthant covered by these extension spaces.

Part of the package BHVExtMinDistance and it is constructed using tools from the packages: 
 * distanceAlg1; PolyAlg; constructed by Megan Owen

Part of the package that computes distances between Extension Spaces.
*/

package BHVExtMinDistance;

import java.util.*;
import distanceAlg1.*;
import static polyAlg.PolyMain.getGeodesic;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExtensionSpaceDistance{
    //List of all the distances between the subset of the extension spaces restricted to particular orthants.
    //This list will be ordered from the shorter distance to the higher.
    private List<OrthExtDistance> orderedOrthExtDistances;
    //List of the ID's of the orthants in the first and second extension space that are involved in the respective orthant extension distance
    private List<Integer> OrthantID1;
    private List<Integer> OrthantID2;
    //The two trees belonging to each of the extension spaces that produces the smaller distance possible.
    private PhyloTree bestTree1;
    private PhyloTree bestTree2;
    //The distance (smaller of the orthant extension distances) between the extension spaces.
    private double Distance;
    //The geodesic between the extension spaces that produces the smaller distance.
    private Geodesic bestGeode;
    
    //Constructor
    public ExtensionSpaceDistance(ExtensionSpace ES1, ExtensionSpace ES2){
        orderedOrthExtDistances = new ArrayList<OrthExtDistance>();
        OrthantID1 = new ArrayList<Integer>();
        OrthantID2 = new ArrayList<Integer>();
        
        Vector<OrthExt> OEs1 = ES1.getOrthExts();
        Vector<OrthExt> OEs2 = ES2.getOrthExts();
        
        int oNum1 = OEs1.size();
        int oNum2 = OEs2.size();
        
        //For each pair of orthant extensions in the extension spaces we compute the Orthant Extension Distances in between them, find how it compares to the other distances already added to the list, and we add it to the correct position, also adding the orthants that produced this distance to each the list of orthants. 
        for (int k1 = 0; k1 < oNum1; k1++){
            OrthExt OE1 = OEs1.get(k1);
            for (int k2 = 0; k2 < oNum2; k2++){
                System.out.println("************");
                System.out.println("STARTING O pair ("+k1+", "+k2+")");
                OrthExt OE2 = OEs2.get(k2);
                OrthExtDistance tempOED = new OrthExtDistance(OE1, OE2);
                System.out.println("THE DISTANCE WAS "+ tempOED.getDistance());
                System.out.println("************");
                System.out.println("");
                
                boolean Added = false;
                for(int i = 0; i < orderedOrthExtDistances.size(); i++){
                    if (tempOED.getDistance() < orderedOrthExtDistances.get(i).getDistance()){
                        Added = true;
                        orderedOrthExtDistances.add(i, tempOED);
                        OrthantID1.add(i, k1);
                        OrthantID2.add(i, k2);
                        break;
                    }
                }
                if (!Added){
                    OrthantID1.add(orderedOrthExtDistances.size(), k1);
                    OrthantID2.add(orderedOrthExtDistances.size(), k2);
                    orderedOrthExtDistances.add(orderedOrthExtDistances.size(), tempOED);
                }
            }
        }
        
        //The best trees, distance and geodesic will be those at the beginning of our list. 
        bestTree1 = orderedOrthExtDistances.get(0).getFirstTree();
        bestTree2 = orderedOrthExtDistances.get(0).getSecondTree();
        Distance = orderedOrthExtDistances.get(0).getDistance();
        bestGeode = orderedOrthExtDistances.get(0).getFinalGeode();
    }//end of constructor 
    
    //Constructor 2: allowing for unrestricted version
    public ExtensionSpaceDistance(ExtensionSpace ES1, ExtensionSpace ES2, boolean restricted){
        orderedOrthExtDistances = new ArrayList<OrthExtDistance>();
        OrthantID1 = new ArrayList<Integer>();
        OrthantID2 = new ArrayList<Integer>();
        
        Vector<OrthExt> OEs1 = ES1.getOrthExts();
        Vector<OrthExt> OEs2 = ES2.getOrthExts();
        
        int oNum1 = OEs1.size();
        int oNum2 = OEs2.size();
        
        //For each pair of orthant extensions in the extension spaces we compute the Orthant Extension Distances in between them, find how it compares to the other distances already added to the list, and we add it to the correct position, also adding the orthants that produced this distance to each the list of orthants. 
        for (int k1 = 0; k1 < oNum1; k1++){
            OrthExt OE1 = OEs1.get(k1);
            for (int k2 = 0; k2 < oNum2; k2++){
                //System.out.println("************");
                //System.out.println("STARTING O pair ("+k1+", "+k2+")");
                OrthExt OE2 = OEs2.get(k2);
                OrthExtDistance tempOED = new OrthExtDistance(OE1, OE2, restricted);
                //System.out.println("THE DISTANCE WAS "+ tempOED.getDistance());
                //System.out.println("************");
                //System.out.println("");
                
                boolean Added = false;
                for(int i = 0; i < orderedOrthExtDistances.size(); i++){
                    if (tempOED.getDistance() < orderedOrthExtDistances.get(i).getDistance()){
                        Added = true;
                        orderedOrthExtDistances.add(i, tempOED);
                        OrthantID1.add(i, k1);
                        OrthantID2.add(i, k2);
                        break;
                    }
                }
                if (!Added){
                    OrthantID1.add(orderedOrthExtDistances.size(), k1);
                    OrthantID2.add(orderedOrthExtDistances.size(), k2);
                    orderedOrthExtDistances.add(orderedOrthExtDistances.size(), tempOED);
                }
            }
        }
        
        //The best trees, distance and geodesic will be those at the beginning of our list. 
        bestTree1 = orderedOrthExtDistances.get(0).getFirstTree();
        bestTree2 = orderedOrthExtDistances.get(0).getSecondTree();
        Distance = orderedOrthExtDistances.get(0).getDistance();
        bestGeode = orderedOrthExtDistances.get(0).getFinalGeode();
    } //end of constructor 2
    
    //Printers and Getters
    
    public List<OrthExtDistance> getOOED(){
        return orderedOrthExtDistances;
    }
    
    public List<Integer> getOrthantID1(){
        return OrthantID1;
    }
    
    public List<Integer> getOrthantID2(){
        return OrthantID2;
    }
    
    public PhyloTree getBestTree1(){
        return bestTree1;
    }
    
    public PhyloTree getBestTree2(){
        return bestTree2;
    }
    
    public double getDistance(){
        return Distance;
    }
    
    public Geodesic getBestGeodesic(){
        return bestGeode;
    }
    
    public void PrintSummary(boolean withTrees){
        System.out.println("There are a total of "+ orderedOrthExtDistances.size()+" orthant pairs");
        for(int i = 0; i < orderedOrthExtDistances.size(); i++){
            System.out.println("Pair "+ i +": For orthant pair (" + OrthantID1.get(i) + ", " + OrthantID2.get(i) + ") the distance is " + orderedOrthExtDistances.get(i).getDistance());
            if (withTrees){
                PhyloNicePrinter treePrinter = new PhyloNicePrinter();
                System.out.println("  Best Tree 1: ");
                System.out.println(treePrinter.toString(orderedOrthExtDistances.get(i).getFirstTree()));
                System.out.println("");
                System.out.println("  Best Tree 2: ");
                System.out.println(treePrinter.toString(orderedOrthExtDistances.get(i).getSecondTree()));
            }
            System.out.println("---------------------------------------------------------------");
        }
    }
    
    public void PrintSummary(boolean withTrees, String fileName){
        try {
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                System.out.println("Report created in: " + myObj.getName());
            } else {
                System.out.println("Report already exists in: " + myObj.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("There are a total of "+ orderedOrthExtDistances.size()+" orthant pairs \n");
            for(int i = 0; i < orderedOrthExtDistances.size(); i++){
                myWriter.write("Pair "+ i +": For orthant pair (" + OrthantID1.get(i) + ", " + OrthantID2.get(i) + ") the distance is " + orderedOrthExtDistances.get(i).getDistance() + "\n");
                if(withTrees){
                    PhyloNicePrinter treePrinter = new PhyloNicePrinter();
                    myWriter.write("  Best Tree 1: \n" + treePrinter.toString(orderedOrthExtDistances.get(i).getFirstTree()) + "\n \n");
                    myWriter.write("  Best Tree 2: \n" + treePrinter.toString(orderedOrthExtDistances.get(i).getSecondTree()) + "\n");   
                }
                myWriter.write("---------------------------------------------------------------\n");
                myWriter.write(" \n");
            }
            myWriter.close();
            System.out.println("Successfully wrote the report.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
    }
    
    // Graphical representations to explore the relationship between NNI and shorter distances. 
    
    //We are creating a graph that shows, for each pair of orthants, which are "adjacent", which means: one of the orthants is identical to the other for one of the extension spaces, and the other ones in the pair are neighbours by rotation.
    public Map<Integer, List<Integer>> JointNNI(ExtensionSpace ES1, ExtensionSpace ES2){
        orthantGraph connectCluster1 = ES1.getConnectCluster();
        orthantGraph connectCluster2 = ES2.getConnectCluster();
        
        Map<Integer, List<Integer>> ReturnValue = new HashMap<>();
        
        for(int i = 0; i < orderedOrthExtDistances.size(); i++){
            List<Integer> adjTemp = new ArrayList<Integer>();
            for(int j = 0; j < orderedOrthExtDistances.size(); j++){
                if(((OrthantID1.get(i) == OrthantID1.get(j)) && (connectCluster2.getAdjIDsList(OrthantID2.get(i)).contains(OrthantID2.get(j)))) || ((OrthantID2.get(i) == OrthantID2.get(j)) && (connectCluster1.getAdjIDsList(OrthantID1.get(i)).contains(OrthantID1.get(j))))){
                    adjTemp.add(j);
                }
            }
            ReturnValue.put(i, adjTemp);
            
        }
        
        return(ReturnValue);
    }
    
    public Map<Integer, List<Integer>> StartTreeNNI(ExtensionSpace ES1){
        orthantGraph connectCluster1 = ES1.getConnectCluster();
        
        Map<Integer, List<Integer>> ReturnValue = new HashMap<>();
        
        for(int i = 0; i < orderedOrthExtDistances.size(); i++){
            List<Integer> adjTemp = new ArrayList<Integer>();
            for(int j = 0; j < orderedOrthExtDistances.size(); j++){
                if((OrthantID1.get(i) == OrthantID1.get(j)) || (connectCluster1.getAdjIDsList(OrthantID1.get(i)).contains(OrthantID1.get(j)))){
                    adjTemp.add(j);
                }
            }
            ReturnValue.put(i, adjTemp);
            
        }
        
        return(ReturnValue);
    }
    
    public Map<Integer, List<Integer>> EndTreeNNI(ExtensionSpace ES2){
        orthantGraph connectCluster2 = ES2.getConnectCluster();
        
        Map<Integer, List<Integer>> ReturnValue = new HashMap<>();
        
        for(int i = 0; i < orderedOrthExtDistances.size(); i++){
            List<Integer> adjTemp = new ArrayList<Integer>();
            for(int j = 0; j < orderedOrthExtDistances.size(); j++){
                if((connectCluster2.getAdjIDsList(OrthantID2.get(i)).contains(OrthantID2.get(j))) || (OrthantID2.get(i) == OrthantID2.get(j))){
                    adjTemp.add(j);
                }
            }
            ReturnValue.put(i, adjTemp);
            
        }
        
        return(ReturnValue);
    }
    
}