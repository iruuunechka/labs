package last;

import java.io.File;
import java.io.IOException;

/**
 * @author Irene Petrova
 */
public class BayesianRunner {
    public static void main(String[] args) throws IOException {
        BayesianNetwork bn = BayesianReader.read(new File("bayesian_network"));
        int[] conditions = {0, 3};
        boolean[] conditionVals = {false, true};
        bn.countAposteriori(conditions, conditionVals, 5);

    }

}
