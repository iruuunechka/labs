package last;

import java.io.File;
import java.io.IOException;

/**
 * @author Irene Petrova
 */
public class BayesianRunner {
    public static void main(String[] args) throws IOException {
        BayesianNetwork bn = new BayesianNetwork(BayesianReader.read(new File("bayesian_network")));
        bn.countAposteriori(0, true, 3);

    }

}
