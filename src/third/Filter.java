package third;

import second.random_forest.Data;
import second.random_forest.Dataset;

import java.util.*;

/**
 * @author Irene Petrova
 */
public class Filter {

    private final List<FeatureWithRho> sortedFeaturesWithRho;

    public Filter(Dataset traindataset) {
        sortedFeaturesWithRho = filter(traindataset);
    }

    private double xAverage(Dataset dataset, int j) {
        double x = 0;
        for (Data d : dataset.dataset) {
            x += d.features.get(j);
        }
        return x / dataset.size();
    }

    private double yAverage(Dataset dataset) {
        double y = 0;
        for (Data d : dataset.dataset) {
            y += d.label;
        }
        return y / dataset.size();
    }

    private double countYsum(Dataset dataset, double yAve) {
        double sum = 0;
        for (Data d : dataset.dataset) {
            sum += Math.pow(d.label - yAve, 2);
        }
        return sum;
    }

    private double rho(Dataset dataset, int j, double ySum, double yAve) {
        double xAve = xAverage(dataset, j);
        double numer = 0;
        double sum1denom = 0;
        double diff;
        for (Data d : dataset.dataset) {
            diff = d.features.get(j) - xAve;
            numer += diff * (d.label - yAve);
            sum1denom += Math.pow(diff, 2);
        }
        return numer / Math.sqrt(sum1denom * ySum);
    }

    private List<FeatureWithRho> filter(Dataset dataset) {
        double yAve = yAverage(dataset);
        double ySum = countYsum(dataset, yAve);
        List<FeatureWithRho> featuresWithRho = new ArrayList<>(dataset.getFeatureCount());
        for (int j = 0; j < dataset.getFeatureCount(); ++j) {
            featuresWithRho.add(new FeatureWithRho(j, rho(dataset, j, ySum, yAve)));
        }
        Collections.sort(featuresWithRho);
        return featuresWithRho;
    }

    public List<Integer> getFeatures(int featureCou) {
        if (sortedFeaturesWithRho == null) {
            throw new AssertionError("Sorted features haven't been initialized");
        }
        List<Integer> rhos = new ArrayList<>(featureCou);
        for (int i = 0; i < featureCou; ++i) {
            rhos.add(sortedFeaturesWithRho.get(i).feature);
        }
        return rhos;
    }

    public static Dataset generateDatasetWithFeatures(Dataset dataset, List<Integer> sortedFeatures) {
        List<Data> newDataset = new ArrayList<>(dataset.size());
        for (Data d : dataset.dataset) {
            List<Integer> f = new ArrayList<>(sortedFeatures.size());
            for (int i : sortedFeatures) {
                f.add(d.features.get(i));
            }
            newDataset.add(new Data(f, d.label));
        }
        return new Dataset(newDataset);
    }

    private class FeatureWithRho implements Comparable<FeatureWithRho>{
        private final int feature;
        private final double rho;

        private FeatureWithRho(int feature, double rho) {
            this.feature = feature;
            this.rho = rho;
        }

        @Override
        public int compareTo(FeatureWithRho featureWithRho) {
            return Double.compare(rho, featureWithRho.rho);
//            if (rho < featureWithRho.rho) {
//                return -1;
//            } else if (rho > featureWithRho.rho) {
//                return 1;
//            } else {
//                return 0;
//            }
        }
    }
}
