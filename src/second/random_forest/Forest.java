package second.random_forest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Irene Petrova
 */
public class Forest {
    private final int treeCount;
    private final int maxDepth;
    private final List<Tree> forest;
    private final int percent;

    public Forest(Dataset train, int treeCount, int maxDepth, int percent) {
        this.treeCount = treeCount;
        this.maxDepth = maxDepth;
        this.percent = percent;
        forest = new ArrayList<>();
        for (int i = 0; i < this.treeCount; ++i) {
            forest.add(new Tree(train.getRandomDataSet(this.percent), maxDepth));
        }
    }

    public double testClassifier(Dataset testData) {
        int cou = 0;
        for (Data test : testData.dataset) {
            if (classify(test) == test.label) {
                cou++;
            }
        }
        return 100.0 * cou / testData.size();
    }

    private int classify (Data test) {
        Map<Integer, Integer> labelCount = new HashMap<>();
        for (Tree t : forest) {
            int l = t.classify(test);
            if (labelCount.containsKey(l)) {
                labelCount.put(l, labelCount.get(l) + 1);
            } else {
                labelCount.put(l, 1);
            }
        }
        int maxCou = Integer.MIN_VALUE;
        int label = 0;
        for (int l : labelCount.keySet()) {
            if (labelCount.get(l) > maxCou) {
                maxCou = labelCount.get(l);
                label = l;
            }
        }
        return label;
    }
}
