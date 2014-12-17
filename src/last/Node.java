package last;

import java.util.List;

/**
 * @author Irene Petrova
 */
public class Node {
    public Factor factor;
    public List<Integer> child;
    public int val;

    public Node(Factor factor, List<Integer> child, int val) {
        this.factor = factor;
        this.child = child;
        this.val = val;
    }

    public void setChild(List<Integer> child) {
        this.child = child;
    }

}
