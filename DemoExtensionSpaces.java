import java.util.*;
import distanceAlg1.*;
import BHVExtMinDistance.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;


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
            try {
                File myFile = new File(args[0]);
                Scanner myReader = new Scanner(myFile);
                String StringFirstTree = myReader.nextLine();
                
                FirstTree = new PhyloTree(StringFirstTree,false);
                System.out.println("\n The first tree is: \n"+ nicePrint.toString(FirstTree));
                
                String StringSecondTree = myReader.nextLine();
                SecondTree = new PhyloTree(StringSecondTree,false);
                System.out.println("\n The second tree is: \n"+ nicePrint.toString(SecondTree));
                
                String CLSstring = myReader.nextLine();
                String[] temp = CLSstring.split(",");
                completeLeafSet = new Vector<String>();
                for(String s : temp){
                    completeLeafSet.add(s);
                }
                System.out.println("\n The complete leaf set is: "+ completeLeafSet);
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
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
        
        if (Ans2.equals("Yes") || Ans2.equals("yes") || Ans2.equals("y") || Ans2.equals("Y")){
            secondES.PrintSummary(true);
            System.out.println("\n \n");
        }
        
        ExtensionSpaceDistance Distance = new ExtensionSpaceDistance(firstES, secondES, false);
        
        System.out.println("The results are: ");
        Distance.PrintSummary(true); 
        
        System.out.println("\n Would you like to print this summary on a text file?");
        String Ans3 = input.nextLine();
        
        if (Ans3.equals("Yes") || Ans3.equals("yes") || Ans3.equals("y") || Ans3.equals("Y")){
            System.out.println("What name would you like to use in result file?");
            String FileName = input.nextLine();
            Distance.PrintSummary(true, FileName);
        }
        
    }
}