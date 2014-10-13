package second.random_forest;

/**
 * @author Irene Petrova
 */
public class Tree {
    private final Node root;

    public Tree(Dataset dataset, int maxDepth) {
        Node.maxDepth = maxDepth;
        this.root = new Node(dataset, 0);
        root.splitNode();
    }

    public double testClassifier (Dataset testData) {
        int cou = 0;
        for (Data test : testData.dataset) {
            if (classify(test) == test.label) {
                cou++;
            }
        }
        return 100.0 * cou / testData.size();
    }

    public int classify(Data test) {
        Node cur = root;
        while (cur.canBeSplited) {
            if (test.features.get(cur.splitFeature) > cur.splitVal) {
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        int c = 0;
        int maxCou = Integer.MIN_VALUE;
        for (int l : cur.dataset.labelSet) {
            int lCou = cur.dataset.countLabel(l);
            if (lCou > maxCou) {
                maxCou = lCou;
                c = l;
            }
        }
        return  c;
    }
}
