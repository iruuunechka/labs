package last;

import java.io.File;
import java.io.IOException;

/**
 * @author Irene Petrova
 */
public class BayesianRunner {
    public static void main(String[] args) throws IOException {
        BayesianReader.read(new File("bayesian_network"));
    }
}
