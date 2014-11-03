package third.pca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Irene Petrova
 */
public class Reader {
    public static List<List<Double>> readDataset(File data) throws IOException {
        BufferedReader brData = new BufferedReader(new FileReader(data));
        String s;
        List<List<Double>> dataset = new ArrayList<>();
        while (!((s = brData.readLine()) == null)) {
            String[] line = s.split(" ");
            List<Double> features = new ArrayList<>();
            for (String i : line) {
                features.add(Double.valueOf(i));
            }
            dataset.add(features);
        }
        return dataset;
    }
}
