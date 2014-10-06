package first.classifiers;

import first.classifiers.Classifier;
import first.classifiers.Item;

import java.util.List;

/**
 * @author Irene Petrova
 */
public class LogisticRegression implements Classifier<Double, Integer> {
    private final double alpha;
    private final double eps;
    private final List<Item<Double, Integer>> trainingData;
    private double o0, o1, o2;
    private double sum = 0;

    private double g(double tetaTX) {
        return 1.0 / (1 + Math.exp(-tetaTX));
    }
    private double dfdx0(double x1, double x2, int y) {
        return (g(o0 + o1 * x1 + o2 * x2) - y) / trainingData.size();
    }

    private double dfdx1(double x1, double x2, int y) {
        return (g(o0 + o1 * x1 + o2 * x2) - y) * x1 / trainingData.size();
    }

    private double dfdx2(double x1, double x2, int y) {
        return (g(o0 + o1 * x1 + o2 * x2) - y) * x2 / trainingData.size();
    }

    private boolean stopCond() {
        double sumNew = 0;
        for (Item<Double, Integer> item : trainingData) {
            double h = g(o0 + o1 * item.params[0] + o2 * item.params[1]);
            sumNew += (-item.res * Math.log(h) - (1 - item.res) * Math.log(1 - h)) / trainingData.size();
        }
        //sumNew = Math.sqrt(sumNew);

        boolean answer = Math.abs(sumNew - sum) < eps;
        sum = sumNew;
        return answer;
    }


    public LogisticRegression(double alpha, double eps, List<Item<Double, Integer>> trainingData) {
        this.alpha = alpha;
        this.eps = eps;
        this.trainingData = trainingData;
        //normalizeData(this.trainingData);
        o0 = 1;
        o1 = 1;
        o2 = 1;

        while (!stopCond()) {
            double tmp0 = 0;
            double tmp1 = 0;
            double tmp2 = 0;
            for (Item<Double, Integer> item : trainingData) {
                double dfdx0 = dfdx0(item.params[0], item.params[1], item.res);
                double dfdx1 = dfdx1(item.params[0], item.params[1], item.res);
                double dfdx2 = dfdx2(item.params[0], item.params[1], item.res);
                tmp0 += alpha * dfdx0;
                tmp1 += alpha * dfdx1;
                tmp2 += alpha * dfdx2;
            }
            o0 -= tmp0;
            o1 -= tmp1;
            o2 -= tmp2;
        }
    }

    @Override
    public Double testClassifier(List<Item<Double, Integer>> tests) {
        int correct = 0;
        //normalizeData(tests);
        for (Item<Double, Integer> item : tests) {
//            System.out.println(Math.round(g(o0 + o1 * item.params[0] + o2 * item.params[1])) + " " + (Math.round(g(o0 + o1 * item.params[0] + o2 * item.params[1])) == item.res));
            if (Math.round(g(o0 + o1 * item.params[0] + o2 * item.params[1])) == item.res) {
                correct++;
            }
        }
        return 100.0 * correct / tests.size();
    }
}
