package last;

import java.io.File;
import java.io.IOException;

/**
 * @author Irene Petrova
 */
public class BayesianRunner {
    private static final String output = "bayesian_graphviz";
    public static void main(String[] args) throws IOException {
        BayesianNetwork bn = BayesianReader.read(new File("bayesian_network"));
        bn.drawNet(new File(output));
        int[] conditions = {0};
        boolean[] conditionVals = {false};
        for (int i = 0; i < bn.getVertCou(); ++i) {
            bn.countAposteriori(conditions, conditionVals, i);
        }
    }
}
