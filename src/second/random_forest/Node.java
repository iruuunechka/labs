package second.random_forest;

/**
 * @author Irene Petrova
 */
public class Node {
    public static int maxDepth;
    public final Dataset dataset;
    public Node left;
    public Node right;
    public int splitFeature;
    public int splitVal;
    public boolean canBeSplited;
    private final int depth;

    public Node(Dataset dataset, int depth) {
        this.dataset = dataset;
        this.depth = depth;
        this.left = null;
        this.right = null;
        canBeSplited = (!dataset.isSameLabel() && maxDepth > depth);

    }

    private double gini(Dataset left, Dataset right) {
        int l = left.size();
        int r = right.size();
        double index = 0;
        for (int lLabel : left.labelSet) {
            index += Math.pow(left.countLabel(lLabel), 2) / l;
        }
        for (int rLabel : right.labelSet) {
            index += Math.pow(right.countLabel(rLabel), 2) / r;
        }
        return index;
    }

    public void splitNode() {
        split();
        if (left != null) {
            left.splitNode();
        }
        if (right != null) {
            right.splitNode();
        }
    }

    private void split() {
        if (!canBeSplited) {
            return;
        }
        double bestGini = Double.NEGATIVE_INFINITY;
        for (int feature = 0; feature < dataset.getFeatureCount(); ++feature) {
            if (!dataset.canSplitByFeature(feature)) {
                continue;
            }
            int splitIndex = dataset.getSplitIndex(feature);
            Dataset left = dataset.getLeft(splitIndex);
            Dataset right = dataset.getRight(splitIndex);
            double newGini = gini(left, right);
            if (newGini > bestGini) {
                bestGini = newGini;
                splitFeature = feature;
                splitVal = dataset.getSplitVal(splitIndex, feature);
            }
        }
        int splitIndex = dataset.getSplitIndex(splitFeature);
        Dataset splitLeft = dataset.getLeft(splitIndex);
        Dataset splitRight = dataset.getRight(splitIndex);
        left = new Node(splitLeft, depth + 1);
        right = new Node(splitRight, depth + 1);
    }

}

