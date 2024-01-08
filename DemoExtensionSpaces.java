import java.util.*;
import distanceAlg1.*;
import BHVExtMinDistance.*;

public class DemoExtensionSpaces{
    private static PhyloTree FirstTree;
    private static PhyloTree SecondTree;
    private static Vector<String> completeLeafSet;
    
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        PhyloNicePrinter nicePrint = new PhyloNicePrinter();
        if(args.length == 0){
            System.out.println("Please input the first tree in Newick format:");
            String StringFirstTree = input.nextLine();
            FirstTree = new PhyloTree(StringFirstTree,false);
            
            
            System.out.println("\n The first tree is: \n"+ nicePrint.toString(FirstTree));
            
            
            System.out.println("\n \n Please input the second tree in Newick format:");
            String StringSecondTree = input.nextLine();
            SecondTree = new PhyloTree(StringSecondTree,false);
            
            
            System.out.println("\n The second tree is: \n"+ nicePrint.toString(SecondTree));
            
            System.out.println("\n \n Please input the complete leaf set, separated by commas without blank spaces:");
            String CLSstring = input.nextLine();
            String[] temp = CLSstring.split(",");
            completeLeafSet = new Vector<String>();
            for(String s : temp){
                completeLeafSet.add(s);
            }
            System.out.println("\n The complete leaf set is: "+ completeLeafSet);
        } else if (args.length == 1){
            System.out.println("We are in the case where information is passed through a file");
        } else if(args.length == 2){
            System.out.println("We are in the case where things were already inputed manually, only trees");
        } else if (args.length == 3){
            System.out.println("We are in the case where things were already inputed manually");
        } else {
            System.out.println("Error: Format for data entry not correct");
            System.exit(1);
        }
        
        ExtensionSpace firstES = new ExtensionSpace(FirstTree, completeLeafSet, false);
        
        System.out.println("Would you like to see the Extension Space of the first tree?");
        String Ans1 = input.nextLine();
        
        if (Ans1.equals("Yes") || Ans1.equals("yes")){
            firstES.PrintSummary(true);
            System.out.println("\n \n");
        }
        
        ExtensionSpace secondES = new ExtensionSpace(SecondTree, completeLeafSet, false);
        
        System.out.println("Would you like to see the Extension Space of the second tree?");
        String Ans2 = input.nextLine();
        
        if (Ans2.equals("Yes") || Ans2.equals("yes")){
            secondES.PrintSummary(true);
            System.out.println("\n \n");
        }
        
        ExtensionSpaceDistance Distance = new ExtensionSpaceDistance(firstES, secondES, false);
        
        System.out.println("And the distances are: ");
        Distance.PrintSummary(true);    
    }
}