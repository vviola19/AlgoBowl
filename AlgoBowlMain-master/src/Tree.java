import java.awt.*;

/**
 * This class will store row and col of each tree.
 *
 * Additionally, this class will store the current tent that it points to as well as the "next in line tent" which will be determined the same way that attaching a tent to a tree is determined.
 * This way, this information can be used when calculating the change in total violations from removing a tent attached to a tree.
 */
public class Tree {
    private Point location;
    private int row;
    private int col;
    private Tent current;
    private Tent next;

    public Tree(int row, int col){
        this.row = row;
        this.col = col;
        this.current = null;
        this.next = null;
    }

    public Point getLocation() {
        return location;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Tent getCurrent() {
        return current;
    }

    public Tent getNext() {
        return next;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setCurrent(Tent current) {
        this.current = current;
    }

    public void setNext(Tent next) {
        this.next = next;
    }

    public boolean isConnected(){
        return current != null;
    }
}
