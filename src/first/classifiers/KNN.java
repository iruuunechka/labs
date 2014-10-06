package first.classifiers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Irene Petrova
 */
public class KNN implements Classifier<Double, Integer> {
    private final List<Item<Double, Integer>> trainingData;
    private final int k;

    public KNN(List<Item<Double, Integer>> trainingData) {
        this.trainingData = trainingData;
        int percent = 75;
        this.k = selectK(trainingData.subList(0, percent * trainingData.size() / 100), trainingData.subList(percent * trainingData.size() / 100, trainingData.size()));
    }

    public KNN(List<Item<Double, Integer>> trainingData, int k) {
        this.trainingData = trainingData;
        this.k = k;
    }

    private int selectK(List<Item<Double, Integer>> data, List<Item<Double, Integer>> tests) {
        int maxK = 10;
        int bestK = 1;
        double f = Double.NEGATIVE_INFINITY;

        for (int k = 1; k < maxK; ++k) {
            int tp = 0;
            int fn = 0;
            int fp = 0;
            int tn = 0;
            for (Item<Double, Integer> item : tests) {
                if (classify(data, item.params, k) == item.res) {
                    if (item.res == 0) {
                        tp++;
                    } else {
                        tn++;
                    }
                } else {
                    if (item.res == 0) {
                        fn++;
                    } else {
                        fp++;
                    }
                }
            }
            double curF = Utils.fmeasure(tp, tn, fp, fn);
            if (curF >= f) {
                f = curF;
                bestK = k;
            }
        }
        System.out.println(bestK);
        return bestK;
    }

    public Integer classify(List<Item<Double, Integer>> trainingData, Double[] test, int k) {
        if (test.length != trainingData.get(0).params.length) {
            throw new AssertionError("Size of test isn't compatible with size of training objects");
        }
        List<Pair> neigh = new ArrayList<>();
        for (int i = 0; i < trainingData.size(); ++i) {
            double dist = 0;
            for (int j = 0; j < test.length; ++j) {
                dist += Math.pow(trainingData.get(i).params[j] - test[j], 2);
            }
            dist = Math.sqrt(dist);
            neigh.add(new Pair(dist, trainingData.get(i).res));
        }
        Collections.sort(neigh);
        int zeroCou = 0;
        for (int i = 0; i < k; ++i) {
            if (neigh.get(i).val == 0) {
                zeroCou++;
            }
        }
        if (zeroCou > k / 2) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public Double testClassifier(List<Item<Double, Integer>> tests) {
        int good = 0;
        for (Item<Double, Integer> item : tests) {
            if (classify(trainingData, item.params, k) == item.res) {
                good++;
            }
        }
        return 100.0 * good / tests.size();
    }

    private class Pair implements Comparable<Pair> {
        private Double dist;
        private int val;

        public Pair(Double dist, int val) {
            this.dist = dist;
            this.val = val;
        }

        @Override
        public int compareTo(Pair o) {
            if (dist - o.dist < 0) {
                return -1;
            } else if (dist - o.dist == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
