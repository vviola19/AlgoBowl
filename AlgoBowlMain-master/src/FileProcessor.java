import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.String;
import java.util.HashMap;


/**
 * This class will extract any information from the input file
 *
 */
public class FileProcessor {
    private int rowNum;
    private int colNum;
    private int[] rowVals;
    private int[] colVals;
    private Tent[] tents;
    private Tree[] trees;
    private int tentLen;
    private int treeLen;
    private Tent[][] tentLocations;
    private int[] realrows;
    private int[] realcols;

    //System.out.println();
    public FileProcessor(Path filePath) throws IOException{
        //get the file info into one string and split it by row
        String str = Files.readString(filePath);
        String[] lines = str.split("\n");

        //row 0 contains row and col info
        String[] rowcol = lines[0].trim().split(" ");
        this.rowNum = Integer.parseInt(rowcol[0]);
        this.colNum = Integer.parseInt(rowcol[1]);

        //row 1 contains tent "capacity" before violation of each row
        this.realrows = new int[rowNum];
        this.rowVals = new int[rowNum];
        String[] rowStr = lines[1].trim().split(" ");
        for(int i = 0; i<this. rowNum; i++){
            this.rowVals[i] = -1*Integer.parseInt(rowStr[i]);
            this.realrows[i] = Integer.parseInt(rowStr[i]);
        }

        //row 2 contains tent "capacity" before violation of each row
        this.realcols = new int[colNum];
        this.colVals = new int[colNum];
        String[] colStr = lines[2].trim().split(" ");
        for(int i = 0; i<this. colNum; i++){
            this.colVals[i] = -1*Integer.parseInt(colStr[i]);
            this.realcols[i] = Integer.parseInt(colStr[i]);
        }

        //rows past the second will handle the actual graph structure. Likely Tree and Tent locations will need to be stored
        //This part of the code runs in O(n^2), so hopefully we can keep other parts within this upper bound

        //these arrays will be much bigger than the amount of useful elements in them to avoid using a dynamic list. resizing an array is an O(n) operation
        this.tents = new Tent[colNum*rowNum];
        this.trees = new Tree[colNum*rowNum];
        //TODO make sure this actually works
        this.tentLocations = new Tent[rowNum][colNum];
        String[] tentRows = Arrays.copyOfRange(lines, 3, this.rowNum+3);

        int tentNum = 0;
        int treeNum = 0;
        for(int row=0; row < this.rowNum; row++){
            String line = tentRows[row];
            String[] lineInfo = line.trim().split("");
            for(int col=0; col < this.colNum; col++){
                String currGrid = lineInfo[col];
                if(currGrid.equals("T")){
                    //handle Tree
                    this.trees[treeNum] = new Tree(row,col);
                    treeNum++;
                    tentLocations[row][col] = null;
                }
                else if (currGrid.equals(".")) {
                    //handle Tent
                    Tent newTent = new Tent(row,col);
                    this.tents[tentNum] = newTent;
                    tentNum++;
                    rowVals[row] ++ ;
                    colVals[col] ++ ;
                    tentLocations[row][col] = newTent;
                }
            }
        }
        this.tentLen = tentNum;
        this.treeLen = treeNum;
    }

    public int getRowNum(){
        return rowNum;
    }
    public int getColNum(){
        return colNum;
    }
    public int[] getColVals() {
        return colVals;
    }
    public int[] getRowVals() {
        return rowVals;
    }
    public Tent[] getTents() {
        return tents;
    }

    public Tree[] getTrees() {
        return trees;
    }
    public int[] getRealrows(){
        return realrows;
    }

    public int[] getRealcols() {
        return realcols;
    }

    public int getTentLen() {
        return tentLen;
    }

    public int getTreeLen() {
        return treeLen;
    }

    public Tent[][] getTentLocations() {
        return tentLocations;
    }
}