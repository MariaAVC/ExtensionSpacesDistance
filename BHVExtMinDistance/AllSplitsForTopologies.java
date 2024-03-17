/** This is intended as the representation of the of the extension space of a tree.

Part of the package BHVExtMinDistance and it is constructed using tools from the packages: 
 * distanceAlg1; PolyAlg; constructed by Megan Owen

Part of the package that computes distances between Extension Spaces.
*/

package BHVExtMinDistance;

import java.util.*;
import distanceAlg1.*;

public class AllSplitsForTopologies{
    //Number of splits created for the extension space;
    private int splitsNum;
    //List of the vertices in the graph, so we always have the i-th vertex with the ID equal to i.
    private Vector<Bipartition> orderedSplits; 
    //All leaves in the 'maximal' BHV space.
    private Vector<String> completeLeafSet; 
    
    //Constructor
    public AllSplitsForTopologies(PhyloTree T, Vector<String> cLeafSet){
        
        this.completeLeafSet = cLeafSet;
        
        //Vector indicating which leaves in the completeLeafSet are part of the Original Tree
        BitSet originalLeaves = new BitSet(cLeafSet.size());
        
        //Vector of the original tree's leaf set.
        Vector<String> oLeafSet = polyAlg.Tools.myVectorCloneString(T.getLeaf2NumMap()); 
        
        //Vector that maps the leaves in the original tree to the leaves in the complete leaf set. 
        int[] orgLeaves2compLeaves = new int[oLeafSet.size()]; 
        
        //Number of internal edges in the original tree. 
        int m = T.getEdges().size(); 
        
        //Number of leaves to be added. 
        int l = cLeafSet.size() - oLeafSet.size(); 
        
        //The number of potentially new edges is the number of internal edges in the original tree times the different ways the l extra leaves can be added to this edges.
        this.splitsNum = (int) (m*Math.pow(2,l));   
        
        //Array of the leaves that are being added
        int[] listAddedLeaves = new int[l];
        
        //For each leaf in the original tree's leaf set, we find its position in the complete set, and modify the arrays and BitSet accordingly. 
        for (int i = 0; i < oLeafSet.size(); i++){
            int temp = cLeafSet.indexOf(oLeafSet.get(i));
            if (temp == -1){
                System.err.println("Error: The original tree has a leaf that is not part of the complete leaf set");
			    System.exit(1);
            }
            orgLeaves2compLeaves[i] = temp;
            originalLeaves.set(temp);
            
        }
        
        
        //System.out.println("Org 2 Comp is: " + Arrays.toString(orgLeaves2compLeaves));
        
        //System.out.println("original Leaves" + originalLeaves);
        
        //We iterate over the set of complete leaves to find which leafs must be added to the original tree.
        int count = 0;
        for (int i = 0; i < cLeafSet.size(); i++){
            if (!originalLeaves.get(i)){
                listAddedLeaves[count] = i;
                count++;
            }
        }
        
        //System.out.println("listAddedLeaves: " + Arrays.toString(listAddedLeaves));
        //System.out.println("Number of leaves added = " + l);
        
        //For each edge in the original tree, we loop through all the ways the extra leaves can be added to both parts of the Bipartition in that edge, and add the resulting edges after doing that to the vertices of the graph. 
        this.orderedSplits = new Vector<Bipartition>();
        Iterator<PhyloTreeEdge> edgesIter = T.getEdges().iterator();
        
        //counter to assign IDs from 0 to vertexNum - 1. 
        //int VertexCount = 0;
        
		while (edgesIter.hasNext()){
			PhyloTreeEdge e = (PhyloTreeEdge) edgesIter.next();
            //Bitset that indicates how the original leaves appear in the partition of e, but with the positions in the complete leaf set
            BitSet moldPartition = new BitSet(); 
            for (int i = 0; i < oLeafSet.size(); i++){
                if (e.getOriginalEdge().getPartition().get(i)){
                    moldPartition.set(orgLeaves2compLeaves[i]);
                }
            }
            //System.out.println("Mold Partition for " + VertexCount + " is : " + moldPartition);
            //Loop on all the ways to add the new leaves to moldPartition
			for (int bitc = 0; bitc < Math.pow(2,l); bitc++){
                BitSet tempPartition = (BitSet) moldPartition.clone();
                int rest = bitc;
                //Adding the leaves in the bipartition in concordance to bitc (bitc to binary to decide which side)
                for (int j = 0; j < l; j++){
                    if(rest % 2 != 0){
                        tempPartition.set(listAddedLeaves[j]);
                    }
                    rest = rest/2; 
                }
                //Adding the new edge created by adding those leaves to the Vertex list, with proper ID.
                if (tempPartition.get(cLeafSet.size()-1)){//We always use the split not including the last leaf (considered the root as per Megan's Owen code) as the representative
                    tempPartition.flip(0,cLeafSet.size());
                }
                
                //System.out.println("One temp Partition for " + VertexCount + " is : " + tempPartition);
                Bipartition newB =new Bipartition(tempPartition);
                orderedSplits.add(newB);
            }
		}
        
        
    } //End of first constructor
    
    //Constructor 2: allows for unrestricted case
    public AllSplitsForTopologies(PhyloTree T, Vector<String> cLeafSet, boolean restricted){
        
        PhyloNicePrinter nicePrint = new PhyloNicePrinter();
        
        this.completeLeafSet = cLeafSet;
        
        //Vector indicating which leaves in the completeLeafSet are part of the Original Tree
        BitSet originalLeaves = new BitSet(cLeafSet.size());
        
        //Vector of the original tree's leaf set.
        Vector<String> oLeafSet = polyAlg.Tools.myVectorCloneString(T.getLeaf2NumMap()); 
        
        //Vector that maps the leaves in the original tree to the leaves in the complete leaf set. 
        int[] orgLeaves2compLeaves = new int[oLeafSet.size()]; 
        
        //Number of internal edges in the original tree. 
        int m = T.getEdges().size(); 
        
        //Number of leaves to be added. 
        int l = cLeafSet.size() - oLeafSet.size(); 
        
        //In the restricted case, the number of potentially new internal edges is the number of internal edges in the original tree times the different ways the l extra leaves can be added to this edges.
        //in the unrestricted case, we also obtain those edges obtained by adding to the external edges in the original tree, minus those that end up being the external edge again. 
        this.splitsNum = (int) (m*Math.pow(2,l));  
        if (!restricted){
            this.splitsNum += (int) (oLeafSet.size()*(Math.pow(2,l) - 1)); 
            this.splitsNum += (int) Math.pow(2,l) - l - 1;
        }
        
        //Array of the leaves that are being added
        int[] listAddedLeaves = new int[l];
        
        //For each leaf in the original tree's leaf set, we find its position in the complete set, and modify the arrays and BitSet accordingly. 
        for (int i = 0; i < oLeafSet.size(); i++){
            int temp = cLeafSet.indexOf(oLeafSet.get(i));
            if (temp == -1){
                System.err.println("Error: The original tree has a leaf that is not part of the complete leaf set");
			    System.exit(1);
            }
            orgLeaves2compLeaves[i] = temp;
            originalLeaves.set(temp);
            
        }
        
        //We iterate over the set of complete leaves to make find which leafs must be added to the original tree.
        int count = 0;
        for (int i = 0; i < cLeafSet.size(); i++){
            if (!originalLeaves.get(i)){
                listAddedLeaves[count] = i;
                count++;
            }
        }
        
        //For each edge in the original tree, we loop through all the ways the extra leaves can be added to both parts of the Bipartition in that edge, and add the resulting edges after doing that to the vertices of the graph. 
        this.orderedSplits = new Vector<Bipartition>();
        Iterator<PhyloTreeEdge> edgesIter = T.getEdges().iterator();
        
        //counter to assign IDs from 0 to vertexNum - 1. 
        //int VertexCount = 0;
        
		while (edgesIter.hasNext()){
			PhyloTreeEdge e = (PhyloTreeEdge) edgesIter.next();
            //Bitset that indicates how the original leaves appear in the partition of e, but with the positions in the complete leaf set
            BitSet moldPartition = new BitSet(); 
            for (int i = 0; i < oLeafSet.size(); i++){
                if (e.getOriginalEdge().getPartition().get(i)){
                    moldPartition.set(orgLeaves2compLeaves[i]);
                }
            }
            //Loop on all the ways to add the new leaves to moldPartition
			for (int bitc = 0; bitc < Math.pow(2,l); bitc++){
                BitSet tempPartition = (BitSet) moldPartition.clone();
                int rest = bitc;
                //Adding the leaves in the bipartition in concordance to bitc (bitc to binary to decide which side)
                for (int j = 0; j < l; j++){
                    if(rest % 2 != 0){
                        tempPartition.set(listAddedLeaves[j]);
                    }
                    rest = rest/2; 
                }
                //Adding the new edge created by adding those leaves to the Vertex list, with proper ID.
                if (tempPartition.get(cLeafSet.size()-1)){//We always use the split not including the last leaf (considered the root as per Megan's Owen code) as the representative
                    tempPartition.flip(0,cLeafSet.size());
                }
                Bipartition newB = new Bipartition(tempPartition);
                orderedSplits.add(newB);
            }
		}
        
        //We are adding extra edges in the unrestricted case: 
        
        if (!restricted){
            for(int i = 0; i < oLeafSet.size(); i++){
                BitSet moldPartition = new BitSet(); 
                moldPartition.set(orgLeaves2compLeaves[i]);
                for(int bitc = 1; bitc < Math.pow(2,l); bitc++){//We start in 1 to ensure at least one leaf is added to the side of the leaf in the external branch
                    BitSet tempPartition = (BitSet) moldPartition.clone();
                    int rest = bitc;
                    //Adding the leaves in the bipartition in concordance to bitc (bitc to binary to decide which side)
                    for (int j = 0; j < l; j++){
                        if(rest % 2 != 0){
                            tempPartition.set(listAddedLeaves[j]);
                        }
                        rest = rest/2; 
                    }
                    //Adding the new edge created by adding those leaves to the Vertex list, with proper ID.
                    if (tempPartition.get(cLeafSet.size()-1)){//We always use the split not including the last leaf (considered the root as per Megan's Owen code) as the representative
                        tempPartition.flip(0,cLeafSet.size());
                    }
                    Bipartition newB = new Bipartition(tempPartition);
                    orderedSplits.add(newB);
                }
            }
            //System.out.println("About over here and + " + (l-1));
            BitSet moldEmptyPartition = new BitSet();
            for (int pow2 = 1; pow2 < l; pow2++){//We are using the formula 2^pow2 + bitext to generate all bitc numbers that have at least 2 ones in the binary representation
                //System.out.println("pow 2 = "+ pow2);
                int vPow2 = (int) Math.pow(2,pow2);
                //System.out.println("V pow 2 = "+ vPow2);
                for (int bitext = 1; bitext < vPow2; bitext++){
                    int rest = vPow2 + bitext;
                    //System.out.println("Rest = "+ rest);
                    BitSet tempPartition = (BitSet) moldEmptyPartition.clone();
                    for (int j = 0; j < l; j++){
                        if(rest % 2 != 0){
                            tempPartition.set(listAddedLeaves[j]);
                        }
                        rest = rest/2; 
                    }
                    //Adding the new edge created by adding those leaves to the Vertex list, with proper ID.
                    if (tempPartition.get(cLeafSet.size()-1)){//We always use the split not including the last leaf (considered the root as per Megan's Owen code) as the representative
                        tempPartition.flip(0,cLeafSet.size());
                    }
                    Bipartition newB = new Bipartition(tempPartition);
                    //System.out.println("for -->" + nicePrint.toString(newB, this.completeLeafSet));
                    orderedSplits.add(newB);
                }
            }
        }
    }
    
 
    //Printers and Getters
    
    public void PrintAll(){
        PhyloNicePrinter nicePrint = new PhyloNicePrinter();
        System.out.println("Theres " + this.splitsNum + " splits: ");
        Iterator<Bipartition> keyIter = this.orderedSplits.iterator();
        while (keyIter.hasNext()){
            Bipartition eKey = (Bipartition) keyIter.next();
            System.out.println("   " + nicePrint.toString(eKey, this.completeLeafSet));
        }
    }
}