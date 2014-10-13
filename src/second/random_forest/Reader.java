package second.random_forest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Irene Petrova
 */
public class Reader {

    public static Dataset readDataset(File data, File labels) throws IOException {
        BufferedReader brData = new BufferedReader(new FileReader(data));
        BufferedReader brLabel = new BufferedReader(new FileReader(labels));

        String s;
        String l;

        Dataset dataset = new Dataset();
        while (!((s = brData.readLine()) == null)) {
            String[] line = s.split(" ");
            l = brLabel.readLine();
            List<Integer> features = new ArrayList<>();
            for (String i : line) {
                features.add(Integer.valueOf(i));
            }
            dataset.addData(features, Integer.valueOf(l));
        }
        return dataset;
    }


}
