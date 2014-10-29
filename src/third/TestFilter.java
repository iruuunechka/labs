package third;

import second.random_forest.Dataset;
import second.random_forest.Forest;
import second.random_forest.Reader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Irene Petrova
 */
public class TestFilter {
    private static final int maxDepth = 100;
    public static final int percent = 100;
    public static final int treeCount = 3001;

    public static void main(String[] args) throws IOException {
        Dataset train = Reader.readDataset(new File("random_forest/arcene_train.data"), new File("random_forest/arcene_train.labels"));
        Dataset test = Reader.readDataset(new File("random_forest/arcene_valid.data"), new File("random_forest/arcene_valid.labels"));
        Filter filter = new Filter(train);
        PrintWriter trainPW = new PrintWriter("train.txt");
        PrintWriter testPW = new PrintWriter("test.txt");
        for (int featureCou = 50; featureCou > 0; featureCou -= 1) {
            List<Integer> sortedFeatures = filter.getFeatures(featureCou);
            Dataset curTrain = Filter.generateDatasetWithFeatures(train, sortedFeatures);
            Dataset curTest = Filter.generateDatasetWithFeatures(test, sortedFeatures);
//          Tree tree = new Tree(curTrain, maxDepth);
//          System.out.println(tree.testClassifier(curTrain));
//          System.out.println(tree.testClassifier(curTest));

            Forest forest = new Forest(curTrain, treeCount, maxDepth, percent);
            double trainRes = forest.testClassifier(curTrain);
            double testRes = forest.testClassifier(curTest);


            System.out.println("Features: " + featureCou);
            System.out.println("Classifier on train: " + trainRes);
            System.out.println("Classifier on test: " + testRes);
            System.out.println("================================");
            trainPW.println(featureCou + " " + trainRes);
            testPW.println(featureCou + " " + testRes);
            trainPW.flush();
            testPW.flush();
        }
//
    }
}
