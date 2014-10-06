package first;

import first.rules.ArffReader;

import java.io.FileNotFoundException;

/**
 * @author Irene Petrova
 */
public class Runner {
    private static final int percent = 80;

    public static void main(String[] args) throws FileNotFoundException {
//        TxtReader chips = new TxtReader("chips.txt");
//        Classifier<Double, Integer> knn = new KNN(chips.getTrainingSet(percent));
//        System.out.println("KNN on test set: " + knn.testClassifier(chips.getTestSet(percent)));
//        System.out.println("KNN on training set: " + knn.testClassifier(chips.getTrainingSet(percent)));
//
//        TxtReader prices = new TxtReader("prices.txt");
//        Classifier<Double, Integer> linearRegression = new LinearRegression(0.001, 1e-12, prices.getTrainingSet(percent));
//        System.out.println("Linear regression on test set: " + linearRegression.testClassifier(prices.getTestSet(percent)));
//        System.out.println("Linear regression on training set: " + linearRegression.testClassifier(prices.getTrainingSet(percent)));

//          TxtReader chips = new TxtReader("chips.txt");
//          Classifier<Double, Integer> logisticRegression = new LogisticRegression(1e-3, 1e-6, chips.getTrainingSet(percent));
//          System.out.println("Logistic Regression on test set: " + logisticRegression.testClassifier(chips.getTestSet(percent)));
//          System.out.println("Logistic Regression on training set: " + logisticRegression.testClassifier(chips.getTrainingSet(percent)));

        ArffReader supermarket = new ArffReader("supermarket.arff");

    }
}
