package BHVExtMinDistance;

import java.util.*;
import java.util.concurrent.*; 
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import distanceAlg1.*;

public class geodesicDistance{
    private double Distance;
    private RatioSequence rs;
	private Vector<PhyloTreeEdge> commonEdges;
    private PhyloTree startTree;
    private PhyloTree endTree;
    
    private void Constructor(PhyloTree T1, PhyloTree T2){
        Geodesic tempGeode = parPolyMain.getGeodesic(T1, T2);
        
        this.Distance = tempGeode.getDist();
        this.rs = tempGeode.getRS();
        this.commonEdges = tempGeode.getCommonEdges();
        this.startTree = T1;
        this.endTree = T2;
    }
    
    public geodesicDistance(){};
    
    public geodesicDistance(PhyloTree Tree1, PhyloTree Tree2){
        Constructor(Tree1, Tree2);   
    }
        
    public geodesicDistance(treePair Tpair){
        Constructor(Tpair.getTree1(), Tpair.getTree2());   
    }
    
    public RatioSequence getRS(){
        return this.rs;
    }
    
    public Vector<PhyloTreeEdge> getCEs(){
        return this.commonEdges;
    }
    
    public double getDistance(){
        return this.Distance;
    }
    
    public PhyloTree getStartTree(){
        return this.startTree;
    }
    
    public PhyloTree getEndTree(){
        return this.endTree;
    }
}