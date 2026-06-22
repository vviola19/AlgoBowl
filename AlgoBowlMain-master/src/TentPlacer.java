import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.*;

import static java.lang.Math.abs;

public class TentPlacer {
    public Tent[] tentList;
    public Tent[][] tentLocations;
    public Tree[] treeList;
    public int[] rowvals;
    public int[] colvals;
    public int tentLen;
    public int treeLen;
    public int colNum;
    public int rowNum;
    public int[] realrows;
    public int[] realcols;


    public static final int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1}, //upper row
            {0, -1}, {0, 1},  //left and right
            {1, -1}, {1, 0}, {1, 1}  //lower row
    };


    public TentPlacer(FileProcessor fileProcessor) {
        tentList = fileProcessor.getTents();
        tentLocations = fileProcessor.getTentLocations();
        treeList = fileProcessor.getTrees();
        rowvals = fileProcessor.getRowVals();
        colvals = fileProcessor.getColVals();
        tentLen = fileProcessor.getTentLen();
        treeLen = fileProcessor.getTreeLen();
        colNum = fileProcessor.getColNum();
        rowNum = fileProcessor.getRowNum();
        realrows = fileProcessor.getRealrows();
        realcols = fileProcessor.getRealcols();
    }


    //this is the main loop of the code, slightly altering the violation values for certain nodes every iteration
    public void minimizeViolations() {
        //initialize which tents each tree is connected to, and then
        //initialize the violation vaslues for every tent
        initializeBoard();

        boolean iterate = true;
        int removalIndex = findHighestViol();
        Tent tentToRemove = tentList[removalIndex];
        Tree tree = null;
        Tent next = null;
        while (iterate) {
            //if row/col val = 1, update each tent's violations
            updateRowColViolations(tentToRemove);
            //remove the tent with highest number of violations
            removeTentAtIndex(removalIndex);
            //update the violations of each node adjacent to victim
            updateAdjacencyViolations(tentToRemove);
            //if victim was connected to a tree, connect it to a new node
            //TODO this part could be made more accurate by connecting it to the
            //least likely to be removed neighbor
            updateTreeConnection(tentToRemove);

            //calc next-in-line tent
            removalIndex = findHighestViol();
            tentToRemove = tentList[removalIndex];
            //if removing the best tent would do nothing or make it worse, stop iterating
            if (tentToRemove.getvChange() <= 0) {
                iterate = false;
            }
        }
        createOutput();
        createO();
    }


    public void initializeBoard(){
        initializeTrees();
        initializeTents();
    }

    public void updateAdjacencyViolations(Tent removed) {
        //for each neighbor of this node, calc their adjacencies.
        //time could be saved here by reducing the number of neighbors checked
        Tent[] adjacentTents = findAdjacencies(removed);
        for(int i = 0;i < 8;i++){
            if (adjacentTents[i]!= null){
                calcAdjacencyViolations(adjacentTents[i]);
            }
        }

    }

    public void updateTreeConnection(Tent tentToRemove) {
        if (tentToRemove.isConnected()) {
            Tree tree = tentToRemove.getTree();
            Tent next = findNextTent(tree);
            //link next-in-line tent with tree
            if(next != null){
                tree.setCurrent(next);
                next.setTree(tree);
                calcPairingViolations(next);
            }
        }
    }

    public void updateRowColViolations(Tent tentToRemove) {
        if (abs(rowvals[tentToRemove.getRow()]) == 1) {
            //change this so it only happened for the specific row or col
            //also do this same thing for if either =0 before removal, as the na removal will add 1
            //if this if statement happens, go through every tent in the
            //same row and column, update the #violations
            updateRowViolations(tentToRemove.getRow(), tentToRemove.getCol());
        } if (abs(colvals[tentToRemove.getCol()]) == 1) {

            updateColViolations(tentToRemove.getRow(), tentToRemove.getCol());
        }
    }

    public void updateColViolations(int tentRow, int tentCol) {
        Tent currTent = null;
        //loop through each potential tent location of the column
        for (int row = 0; (row < rowNum) ; row++) {
            currTent = getTent(row, tentCol);
            if (currTent != null && (row != tentRow)) {
                //in this case, the tent will go from +1 vChange to -1 vChange from the colVal

                currTent.setColViol(-1);
            }
        }
    }

    public void updateRowViolations(int tentRow, int tentCol) {
        Tent currTent = null;

        //loop through each potential tent location of the row
        for (int col = 0; (col < colNum) ; col++) {
            currTent = getTent(tentRow, col);
            if (currTent != null && (col != tentCol)) {
                //in this case, the tent will go from +1 vChange to -1 vChange from the rowVal
                currTent.setRowViol(-1);
            }
        }
    }

    public void initializeTrees() {
        for (int i = 0; i < treeLen; i++) {
            Tree tree = treeList[i];
            int treeRow = tree.getRow();
            int treeCol = tree.getCol();

            //check all 4 adjacent squares
            Tent adjacentTent = null;

            if (tentAtIndex(treeRow, treeCol + 1)) { // right

                adjacentTent = getTent(treeRow, treeCol + 1);
                if (!adjacentTent.isConnected()) {
                    //pair the tree and tent if a tent was found
                    tree.setCurrent(adjacentTent);
                    adjacentTent.setTree(tree);

                    continue;
                }
            }
            if (tentAtIndex(treeRow-1, treeCol)) { // up
//                System.out.println("there is a tent above this tree");
                adjacentTent = getTent(treeRow-1, treeCol);

                if (!adjacentTent.isConnected()) {
                    //pair the tree and tent if a tent was found
                    tree.setCurrent(adjacentTent);
                    adjacentTent.setTree(tree);
                    continue;
                }
            }
            if (tentAtIndex(treeRow, treeCol-1)) { // left
//                System.out.println("there is a tent to the left of this tree");
                adjacentTent = getTent(treeRow, treeCol-1);
                if (!adjacentTent.isConnected()) { //pair the tree and tent if a tent was found
                    tree.setCurrent(adjacentTent);
                    adjacentTent.setTree(tree);
                    continue;
                }
            }
            if (tentAtIndex(treeRow+1, treeCol)) { // down
//                System.out.println("there is a tent below this tree");
                adjacentTent = getTent(treeRow+1, treeCol);
                if (!adjacentTent.isConnected()) { //pair the tree and tent if a tent was found
                    tree.setCurrent(adjacentTent);
                    adjacentTent.setTree(tree);
                    continue;
                }
            }

        }
    }

    /**
     * This function will find the initial change in number of violations from removing each tent
     */
    public void initializeTents() {
        //first check if removing a tent effects the rowval and colval negatiovely or positively
        // abs(colvals[i]) - abs(colvals[i] - 1)  = change in error at column i

        //next check its adjacencies with other tents and update violations accordingly
        for (int i = 0; i < tentLen; i++) {
            calcAdjacencyViolations(tentList[i]);
            calcRowColViolations(tentList[i]);

        }
        //need to do pairing calcs after the others because this calc will depend on the others
        //this is another slight source of error in our code
        for (int i = 0; i < tentLen; i++) {
            calcPairingViolations(tentList[i]);
        }
    }


    public int findHighestViol() {
        int removalIndex = 0;
        int viol = 0;
        for (int i = 1; i < tentLen; i++) {
            if (tentList[i].getvChange() > tentList[removalIndex].getvChange()) {
                removalIndex = i;

            }
        }
        return removalIndex;
    }

    public void removeTentAtIndex(int removedIndex) {
        Tent removed = tentList[removedIndex];

        //swap last available tent with the removed index
        swap(tentList,removedIndex,tentLen-1);
//        tentList[removedIndex] = tentList[tentLen];
//        tentList[tentLen] = removed;

        //remove this tent from the tentLocations
        tentLocations[removed.getRow()][removed.getCol()] = null;

        //decrement the tentLen, so this tent will not be accoutned for
        tentLen--;
        rowvals[removed.getRow()]--;
        colvals[removed.getCol()]--;
    }

    public void swap(Tent[] arr, int i, int j) {
        Tent temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    //calculates the difference in violations only from the column or row capacities.
    //the output for this should be between -2 and 2
    public void calcRowColViolations(Tent removed) {
        int tentRow = removed.getRow();
        int tentCol = removed.getCol();
        int violDiff = 0;

        //these values should only be +-1

//        System.out.println(abs(colvals[tentCol]) - abs(colvals[tentCol] - 1));
//        System.out.println(abs(rowvals[tentRow]) - abs(rowvals[tentRow] - 1));

        removed.setColViol(abs(colvals[tentCol]) - abs(colvals[tentCol] - 1));
        removed.setRowViol(abs(rowvals[tentRow]) - abs(rowvals[tentRow] - 1));

    }

    public void calcPairingViolations(Tent removed) {
        int tentRow = removed.getRow();
        int tentCol = removed.getCol();
        int violDiff = 0;
        //if tent being removed is paired to a tree, look at violations of adjacent tent
        if (removed.isConnected()) {
            //if there is no next option for the tree, increase number of violations by 1
            Tent nextTent = findNextTent(removed.getTree());
            if (nextTent == null) {
                violDiff += 1;
            }
        }
        removed.setTreeViol(violDiff);


    }

    public Tent findNextTent(Tree tree) {
        int treeRow = tree.getRow();
        int treeCol = tree.getCol();

        //check all 4 adjacent squares
        Tent connected = tree.getCurrent();
        Tent adjacentTent = null;
        if (tentAtIndex(treeRow, treeCol + 1)) { // right
//                System.out.println("there is a tent to the right of this tree");
            adjacentTent = getTent(treeRow, treeCol + 1);
            if (!adjacentTent.isConnected()&&!(adjacentTent.equals(connected))){
                //pair the tree and tent if a tent was found
                return adjacentTent;

            }
        }
        if (tentAtIndex(treeRow-1, treeCol)) { // up
//                System.out.println("there is a tent above this tree");
            adjacentTent = getTent(treeRow-1, treeCol);

            if (!adjacentTent.isConnected()&&!(adjacentTent.equals(connected))) {
                //pair the tree and tent if a tent was found

                return adjacentTent;
            }
        }
        if (tentAtIndex(treeRow, treeCol-1)) { // left
//                System.out.println("there is a tent to the left of this tree");
            adjacentTent = getTent(treeRow, treeCol-1);
            if (!adjacentTent.isConnected()&&!(adjacentTent.equals(connected))) {

                return adjacentTent;
            }
        }
        if (tentAtIndex(treeRow+1, treeCol)) { // down
//                System.out.println("there is a tent below this tree");
            adjacentTent = getTent(treeRow+1, treeCol);
            if (!adjacentTent.isConnected()&&!(adjacentTent.equals(connected))) {

                return adjacentTent;
            }
        }
        return null;
    }

    public void calcAdjacencyViolations(Tent removed) {

        int violaRed = 0; //violations reduced
        int tentRow = removed.getRow();
        int tentCol = removed.getCol();


        Tent[] adjacentTents = findAdjacencies(removed);
        //check all 8 adjacent squares
        int adjCount = 0;
        int leftAdjIndex = 0;
        int rightAdjIndex = 0;
        boolean hasLeftAdjacency = false;
        boolean hasRightAdjacency = false;
        //if an adjacent tent has no adjacent tents other than the removed tent,
        //the number of violations reduced by removing this tent will increase
        //Note: this is an approximation, to find the actual value we would need to recursively search through all connected tents to that tent
        for (int i = 0; i < 8; i++) {
            if (adjacentTents[i] != null) {
                adjCount++;
                leftAdjIndex = (i + 7) % 8; //equivalent to (i - 1) % 8 but avoids negatives
                rightAdjIndex = (i + 1) % 8;
                hasLeftAdjacency = tentAtIndex(tentRow + directions[leftAdjIndex][0],
                        tentCol + directions[leftAdjIndex][1]);
                hasRightAdjacency = tentAtIndex(tentRow + directions[rightAdjIndex][0],
                        tentCol + directions[rightAdjIndex][1]);

                if (!hasLeftAdjacency && !hasRightAdjacency) {
                    violaRed++; // Tent that isn't adjacent to another adjacent tent
                }
            }
        }
        if (adjCount >= 1) {
            violaRed++;
        }
        //updates the violation
        removed.setAdjViol(violaRed);
    }

    public Tent[] findAdjacencies(Tent removed) {
        int tentRow = removed.getRow();
        int tentCol = removed.getCol();
        Tent[] adjacentTents = new Tent[8];

        //find and count each tent adjacent to the removed tent
        for (int i = 0; i < 8; i++) {

            int newRow = tentRow + directions[i][0];
            int newCol = tentCol + directions[i][1];


            adjacentTents[i] = getTent(newRow, newCol);

            //removing tent eliminates this adjacency violation

        }
        return adjacentTents;
    }

        public boolean tentAtIndex(int row, int col){
            if( ((0 <= row)&&(row < rowNum)) && ((0 <= col)&&(col < colNum)) ) {
                return tentLocations[row][col] != null;
            }
            return false;
        }
        public Tent getTent(int row, int col){
            if( ((0 <= row)&&(row < rowNum)) && ((0 <= col)&&(col < colNum)) ) {
                return tentLocations[row][col];
            }
            return null;

        }

        public void createOutput(){
            String filename = "InitFiles/output.txt";
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                writer.write("\n");
                writer.write(tentLen + "\n");
                for (int i = 0; i < tentLen; i++) {
                    Tent tent = tentList[i];

                    String treeChar = "X";
                    if(tent.isConnected()) {
                        Tree tree = tent.getTree();
                        int treeRow = tree.getRow();
                        int treeCol = tree.getCol();

                        int tentRow = tent.getRow();
                        int tentCol = tent.getCol();
                        if(tentRow > treeRow){
                            treeChar = "D";
                        } else if (tentRow < treeRow) {
                            treeChar = "U";
                        } else if (tentCol > treeCol) {
                            treeChar = "L";
                        } else if (tentCol < treeCol) {
                            treeChar = "R";
                        }
                    }
                    writer.write(tent.getRow() + " " + tent.getCol() + " " +treeChar+"\n");

                }
                writer.close();
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }



        }
    public void createO(){
        String filename = "InitFiles/o.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            for(int realrow : realrows){
                writer.write(realrow + " ");
            }
            writer.write("\n");
            for(int realcol : realcols){
                writer.write(realcol + " ");
            }
            String[][] outputmatrix = new String[rowNum][colNum];
            for (int i = 0; i < tentLen; i++) {
                Tent tent = tentList[i];
                int row = tent.getRow();
                int col = tent.getCol();
                outputmatrix[row][col] = "A";
            }
            for (int i = 0; i < treeLen; i++) {
                Tree tree = treeList[i];
                int row = tree.getRow();
                int col = tree.getCol();
                outputmatrix[row][col] = "T";
            }
            writer.write("\n");
            for(int row = 0;row<rowNum;row++){
                for(int col = 0;col<colNum;col++){
                    if(outputmatrix[row][col] == null){
                        writer.write(".");
                    } else if (outputmatrix[row][col].equals("T")) {
                        writer.write("T");
                    } else if (outputmatrix[row][col].equals("A")) {
                        writer.write("A");
                    }

                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }



    }

    }

