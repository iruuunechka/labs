package second;

import java.io.File;
import java.io.IOException;

/**
 * @author Irene Petrova
 */
public class Runner {
    public static final int percent = 70;
    public static void main(String[] args) throws IOException {
        Reader bayesReader = new Reader(new File("pu1"));
        Bayes bayesClassifier = new Bayes(bayesReader.getTrainingSet(percent));
        System.out.println("Bayes classifier on test set: " + bayesClassifier.testClassifier(bayesReader.getTestSet(percent)));
        System.out.println("Bayes classifier on training set: " + bayesClassifier.testClassifier(bayesReader.getTrainingSet(percent)));
    }
}
