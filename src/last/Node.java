package last;

/**
 * @author Irene Petrova
 */
public class Node {
    public Factor factor;
    public int[] child;
    public int val;

    public Node(Factor factor, int[] child, int val) {
        this.factor = factor;
        this.child = child;
        this.val = val;
    }

    public void setChild(int[] child) {
        this.child = child;
    }

}
