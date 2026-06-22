import java.awt.*;

/**
 * This class will store information for a tent REMOVAL, so the row/col to be removed, and the change in # violations if it is removed
 *
 * likely will also contain methods to update the #violations of neighboring Tents
 */
public class Tent {
    private int row;
    private int col;
    private int rowViol;
    private int colViol;
    private int adjViol;
    private int treeViol;
    private Tree tree;

    public Tent(int row, int col){
        this.row = row;
        this.col = col;
        this.rowViol = 0;
        this.colViol = 0;
        this.treeViol = 0;
        this.adjViol = 0;
        this.tree = null;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Tree getTree() {
        return tree;
    }

    public int getvChange() {
        return rowViol + colViol + adjViol + treeViol;
    }

    public int getAdjViol() {
        return adjViol;
    }

    public int getRowViol() {
        return rowViol;
    }

    public int getColViol() {
        return colViol;
    }

    public int getTreeViol() {
        return treeViol;
    }

    public void setAdjViol(int adjViol) {
        this.adjViol = adjViol;
    }

    public void setRowViol(int rowViol) {
//        System.out.println("Setting RowViol for tent "+row +", "+col+" to " + rowViol);
        this.rowViol = rowViol;
    }

    public void setColViol(int colViol) {
//        System.out.println("Setting ColViol for tent "+row +", "+col+" to " + colViol);
        this.colViol = colViol;
    }

    public void setTreeViol(int treeViol) {
        this.treeViol = treeViol;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isConnected(){
        return tree != null;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }
}
