import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * @author Irene Petrova
 */
public class TxtReader {
    private final int numCou = 3;
    private final List<Item<Double, Integer>> data = new ArrayList<>();

    public TxtReader(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filename));
        int dataCou = 0;
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(",");
            if (line.length == 1) break;
            Double[] vals = new Double[numCou - 1];
            for (int i = 0; i < numCou - 1; ++i) {
                vals[i] = Double.valueOf(line[i]);
            }
            data.add(new Item<>(vals, Integer.valueOf(line[numCou - 1])));
            dataCou++;
        }
        normalizeData(data);
        Collections.shuffle(data);
    }

    private void normalizeData(List<Item<Double, Integer>> data) {
        double[] maxParamValues = new double[data.get(0).params.length];
        double[] minParamValues = new double[data.get(0).params.length];
        double[] midParamValues = new double[data.get(0).params.length];

        for (int i = 0; i < maxParamValues.length; ++i) {
            maxParamValues[i] = Double.NEGATIVE_INFINITY;
            minParamValues[i] = Double.POSITIVE_INFINITY;
            midParamValues[i] = 0;
        }
        for (Item<Double, Integer> item : data) {
            for (int i = 0; i < item.params.length; ++i) {
                if (item.params[i] < minParamValues[i]) {
                    minParamValues[i] = item.params[i];
                }
                if (item.params[i] > maxParamValues[i]) {
                    maxParamValues[i] = item.params[i];
                }
                midParamValues[i] += item.params[i];
            }
        }

        for (int i = 0; i < midParamValues.length; ++i) {
            midParamValues[i] /= data.size();
        }
        for (Item<Double, Integer> item : data) {
            for (int i = 0; i < item.params.length; ++i) {
                item.params[i] = (item.params[i] - midParamValues[i]) / (maxParamValues[i] - minParamValues[i]);
            }
        }
    }

//    private void normalizeData(List<Item<Double, Integer>> data) {
//        double [] max = new double[data.get(0).params.length];
//        double [] min = new double[data.get(0).params.length];
//        for (int i = 0; i < max.length; ++i) {
//            max[i] = Double.NEGATIVE_INFINITY;
//            min[i] = Double.POSITIVE_INFINITY;
//        }
//        for (Item<Double, Integer> item : data) {
//            for (int i = 0; i < item.params.length; ++i) {
//                if (item.params[i] < min[i]) {
//                    min[i] = item.params[i];
//                }
//                if (item.params[i] > max[i]) {
//                    max[i] = item.params[i];
//                }
//            }
//        }
//        for (Item<Double, Integer> item : data) {
//            for (int i = 0; i < item.params.length; ++i) {
//                item.params[i] = 2 * (item.params[i] - min[i]) / (max[i] - min[i]) - 1;
//            }
//        }
//    }

    public List<Item<Double, Integer>> getTrainingSet(int percent) {
        return data.subList(0, (int) Math.floor(data.size() * percent / 100));
    }

    public List<Item<Double, Integer>> getTestSet(int percent) {
        return data.subList((int) Math.floor(data.size() * percent / 100), data.size());
    }
}
