import java.util.*;
import distanceAlg1.*;
import BHVExtMinDistance.*;

public class TestWholeCode{
    private static PhyloTree StartingTree;
    private static PhyloTree EndingTree;
    private static Vector<String> completeLeafSet;
    
    public static void main(String[] args){
        PhyloNicePrinter nicePrint = new PhyloNicePrinter();
        //StartingTree = new PhyloTree("(A:2,E:9,((D:14,(H:8,G:10):31):5,(F:11,C:14):15):21)",false);
        StartingTree = new PhyloTree("(A:1,B:1,(E:1,(C:1,D:1):3):5)",false);
        
        System.out.println("Starting Tree: "+ nicePrint.toString(StartingTree));
        
        //EndingTree = new PhyloTree("(A:5,B:3,(G:15,((D:10,F:20):24,(E:10,(H:14,I:12):26):30):16):12)",false);
        EndingTree = new PhyloTree("(A:2,D:2,((E:2,F:1):3,(B:2,C:2):4):4)",false);
        
        System.out.println("Ending Tree: "+ nicePrint.toString(EndingTree));
        
        completeLeafSet = new Vector<String>();
        completeLeafSet.add("A");
        completeLeafSet.add("B");
        completeLeafSet.add("C");
        completeLeafSet.add("D");
        completeLeafSet.add("E");
        completeLeafSet.add("F");
        //completeLeafSet.add("G");
        //completeLeafSet.add("H");
        //completeLeafSet.add("I");
        
        ExtensionSpace startES = new ExtensionSpace(StartingTree, completeLeafSet);
        
        ExtensionSpace endES = new ExtensionSpace(EndingTree, completeLeafSet);
        
        endES.PrintSummary(true);
        System.out.println("\n \n");
        
        ExtensionSpaceDistance testDistance = new ExtensionSpaceDistance(startES, endES);
        
        
        System.out.println("");
        System.out.println("");
        System.out.println("Orthants in the Starting Extension Space: ");
        startES.PrintOrthants();
        System.out.println("");
        System.out.println("");
        System.out.println("Orthants in the Ending Extension Space: ");
        endES.PrintOrthants();
        System.out.println("");
        System.out.println("");
        
        if (args.length>0){
            testDistance.PrintSummary(false, args[0]);
        } else{
            System.out.println("And the distances are: ");
            testDistance.PrintSummary(true);    
        }
        
        Map<Integer, List<Integer>> TestJointNNImap = testDistance.JointNNI(startES, endES);
        
        System.out.println("The joint NNI map: ");
        
        for (int i = 0; i < TestJointNNImap.size(); i++){
            System.out.println("   "+i+": "+TestJointNNImap.get(i));   
        }
    }
}