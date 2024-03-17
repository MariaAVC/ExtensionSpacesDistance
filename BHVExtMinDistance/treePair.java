/** This is intended as the class defining the vertex of a graph, which represents a complete maximal orthant that is part of the extension spaces of a tree. It will be connected to other vertices representing other orthants that are neighbouring by rotation. 

Part of the package BHVExtMinDistance and it is constructed using tools from the packages: 
 * distanceAlg1; PolyAlg; constructed by Megan Owen

Part of the package that computes distances between Extension Spaces.
*/

package BHVExtMinDistance;

import java.util.*;
import distanceAlg1.*;

public class treePair{
    private PhyloTree Tree1;
    private PhyloTree Tree2;
    
    //Constructor
    public treePair(PhyloTree T1, PhyloTree T2){
        this.Tree1 = T1;
        this.Tree2 = T2;
    }
    
    //Getters
    
    public PhyloTree getTree1(){
        return this.Tree1;
    }
    
    public PhyloTree getTree2(){
        return this.Tree2;
    }
    
    public PhyloTree getGoe(){
        return this.Tree2;
    }
    
}