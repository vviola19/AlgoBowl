import java.io.IOException;
import java.nio.file.Path;

import static java.lang.Math.abs;

public class Tests {
    public static void testInitialization(TentPlacer placer) {
        placer.initializeBoard();
        int[] cols = placer.colvals;
        int[] rows = placer.rowvals;
        int index = 0;

        for (int col = 0; col < placer.colNum; col++) {
            System.out.print(cols[col] + " ");
        }
        System.out.println();
        for (int row = 0; row < placer.rowNum; row++) {
            for (int col = 0; col < placer.colNum; col++) {
                if (placer.tentLocations[row][col] != null) {

                    System.out.print("A ");

                } else {

                    Tree tree = placer.treeList[index];
                    int treeRow = tree.getRow();
                    int treeCol = tree.getCol();

                    Tent next = tree.getCurrent();
                    if(next != null) {
                        int conRow = next.getRow();
                        int conCol = next.getCol();
                        if((conRow > treeRow)&&(conCol==treeCol)){
                            System.out.print("v ");
                        } else if ((conRow < treeRow)&&(conCol==treeCol)) {
                            System.out.print("^ ");
                        } else if (conCol > treeCol) {
                            System.out.print("> ");
                        } else if (conCol < treeCol) {
                            System.out.print("< ");
                        }
                    }
                    else {
                        System.out.print("X ");
                    }

                    index++;
                }
            }
            System.out.print(rows[row]);
            System.out.println();
        }
//        for(Tree tree : placer.treeList){
//            if(tree != null) {
//                int conRow = tree.getCurrent().getRow();
//                int conCol = tree.getCurrent().getCol();
//                System.out.println("Tent " + conRow + "," + conCol + " Connected to Tree " + tree.getRow() + "," + tree.getCol());
//            }
//        }
    }
    public static void testRowColViolations(TentPlacer placer) {
        placer.initializeBoard();

        Tent test1 = placer.getTent(1,3);
//        System.out.println(test1.getColViol());
//        System.out.println(test1.getRowViol());

        Tent test2 = placer.getTent(0,1);
        int test2Index = 0;
        Tent test3 = placer.getTent(2,1);
        System.out.println("colVal before removing 0,1: "+placer.colvals[1]);
        System.out.println("rowVal before removing 0,1: "+placer.rowvals[0]);
        System.out.println("before removing 0,1: "+test3.getColViol());
        System.out.println("Removing 0,1...");
        System.out.println();
        placer.updateRowColViolations(test2);
        placer.removeTentAtIndex(test2Index);
        System.out.println("colVal after removing 0,1: "+placer.colvals[1]);
        System.out.println("rowVal after removing 0,1: "+placer.rowvals[0]);
        System.out.println("val of 2,1 after removing 0,1: "+test3.getColViol());











    }
    public static void testPairinglViolations(TentPlacer placer) {
        placer.initializeBoard();
        Tent test0 = placer.getTent(0,1);
        placer.calcPairingViolations(test0);
        //expect this vaslue to be 1 since if this tent is removed, tree 0,0 will have no other tent option
        System.out.println("connection violations from removal of tent 0,1: "+test0.getTreeViol());
        //expected from this test is that the tent at (1,2) will have a new attatchment at (0,2)
        Tent test1 = placer.getTent(1,3);
        int test1Index = 4;
        Tree tree3 = placer.treeList[2];
        System.out.println(placer.tentList[test1Index] == test1);
        System.out.println("Before Adjustment, tree at " + tree3.getRow() +","+ tree3.getCol() + " connected to tent at " + tree3.getCurrent().getRow() + ","+ tree3.getCurrent().getCol());
        System.out.println("before adjustment, #tree violations of tent "+ 0 + ","+ 2 + ": " + placer.getTent(0,2).getTreeViol());
        placer.removeTentAtIndex(test1Index);
        placer.updateTreeConnection(test1);
        System.out.println("After Adjustment, tree at " + tree3.getRow() +","+ tree3.getCol() + " connected to tent at " + tree3.getCurrent().getRow() + ","+ tree3.getCurrent().getCol());
        System.out.println("after adjustment, #tree violations of tent "+ 0 + ","+ 2 + ": " + placer.getTent(0,2).getTreeViol());

    }

    public static void testUpdateAdjacencyViolations(TentPlacer placer){

    }
}

