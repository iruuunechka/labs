package second.random_forest;

import java.io.File;
import java.io.IOException;

/**
 * @author Irene Petrova
 */
public class Runner {
    private static final int maxDepth = 10;
    public static final int percent = 90;
    public static final int treeCount = 9;

    public static void main(String[] args) throws IOException {
        Dataset train = Reader.readDataset(new File("random_forest/arcene_train.data"), new File("random_forest/arcene_train.labels"));
        Dataset test = Reader.readDataset(new File("random_forest/arcene_valid.data"), new File("random_forest/arcene_valid.labels"));
//        Tree tree = new Tree(train, maxDepth);
//        System.out.println(tree.testClassifier(train));
//        System.out.println(tree.testClassifier(test));
        Forest forest = new Forest(train, treeCount, maxDepth, percent);
        System.out.println(forest.testClassifier(train));
        System.out.println(forest.testClassifier(test));

    }
}
