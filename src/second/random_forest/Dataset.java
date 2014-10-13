package second.random_forest;

import java.util.*;

/**
 * @author Irene Petrova
 */
public class Dataset {
    private static final Random rand = new Random();
    public final Set<Integer> labelSet;
    public final List<Data> dataset;

    public Dataset(List<Data> dataset) {
        this.dataset = dataset;
        this.labelSet = new HashSet<>();
        for (Data d : dataset) {
            this.labelSet.add(d.label);
        }
    }

    public Dataset() {
        this.dataset = new ArrayList<>();
        labelSet = new HashSet<>();
    }

    public void addData(List<Integer> features, int label) {
        dataset.add(new Data(features, label));
        labelSet.add(label);
    }

    public boolean isSameLabel() {
        if (labelSet.size() == 1) {
            return true;
        }
        return false;
    }

    public int getFeatureCount() {
        return dataset.get(0).features.size();
    }

    public int size() {
       return dataset.size();
    }

    public int getSplitVal(int splitIndex, int feature) {
        return dataset.get(splitIndex).features.get(feature);
    }

    public int countLabel(int label) {
        int cou = 0;
        for (Data d : dataset) {
            if (d.label == label) {
                cou++;
            }
        }
        return cou;
    }

    public boolean canSplitByFeature(int feature) {
        int featureVal = dataset.get(0).features.get(feature);
        for (Data d : dataset) {
            if (d.features.get(feature) != featureVal) {
                return true;
            }
        }
        return false;
    }

    public Dataset getRandomDataSet(int percent) {
        List<Data> newDataset = new ArrayList<>();
        int cou = percent * dataset.size() / 100;
        for (int i = 0; i < cou; ++i) {
            newDataset.add(dataset.get(rand.nextInt(dataset.size())));
        }
        return new Dataset(newDataset);
    }

    public int getSplitIndex(int feature){
        sortByFeature(feature);
        int i;
        for (i = dataset.size() / 2; i < dataset.size(); ++i) {
            if (dataset.get(i).features.get(feature) != dataset.get(dataset.size() / 2).features.get(feature)) {
                break;
            }
        }
        i = i - dataset.size() / 2;
        int j;
        for (j = dataset.size() / 2 - 1; j >= 0; --j) {
            if (dataset.get(j).features.get(feature) != dataset.get(dataset.size() / 2).features.get(feature)) {
                break;
            }
        }
        j = dataset.size() / 2 - 1 - j ;
        if (i <= j) {
            return dataset.size() / 2 + i;
        } else {
            return dataset.size() / 2 - j;
        }
    }

    public void sortByFeature(int feature) {
        Data.curFeatureForSort = feature;
        Collections.sort(dataset);
    }

    public Dataset getLeft(int splitIndex) {
        return new Dataset(dataset.subList(0, splitIndex));
    }

    public Dataset getRight(int splitIndex) {
        return new Dataset(dataset.subList(splitIndex, dataset.size()));
    }
}