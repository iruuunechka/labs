package second.random_forest;

import java.util.List;

/**
 * @author Irene Petrova
 */
public class Data implements Comparable<Data> {
    public static int curFeatureForSort;
    public final List<Integer> features;
    public final int label;

    public Data(List<Integer> features, int label) {
        this.features = features;
        this.label = label;
    }

    @Override
    public int compareTo(Data o) {
        int thisFeature = features.get(curFeatureForSort);
        int oFeature = o.features.get(curFeatureForSort);
        if (thisFeature < oFeature) {
            return -1;
        } else if (thisFeature > oFeature) {
            return 1;
        } else {
            return 0;
        }
    }
}
