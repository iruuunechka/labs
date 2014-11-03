package third.pca;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Irene Petrova
 */
public class TestPCA {
    private static final double eps = 0.01;

    public static void main(String[] args) throws IOException {
        List<List<Double>> nb1 = Reader.readDataset(new File("pca/newBasis1"));
        List<List<Double>> nb2 = Reader.readDataset(new File("pca/newBasis2"));
        List<List<Double>> nb3 = Reader.readDataset(new File("pca/newBasis3"));
        PrintWriter pw1 = new PrintWriter("pca/newBasis1Res");
        PrintWriter pw2 = new PrintWriter("pca/newBasis2Res");
        PrintWriter pw3 = new PrintWriter("pca/newBasis3Res");
        PCA pca = new PCA(eps);
        pca.pca(nb1, pw1);
        pca.pca(nb2, pw2);
        pca.pca(nb3, pw3);
    }
}
