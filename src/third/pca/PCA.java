package third.pca;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Irene Petrova
 */
public class PCA {
    private final double eps;

    public PCA(double eps) {
        this.eps = eps;
    }

    private double[][] central(List<List<Double>> dataset) {
        double[][] centeredData = new double[dataset.size()][dataset.get(0).size()];
        double[] av = new double[dataset.get(0).size()];
        Arrays.fill(av, 0);
        for (List<Double> data : dataset) {
            for (int i = 0; i < data.size(); ++i) {
                av[i] += data.get(i) / dataset.size();
            }
        }
        for (int i = 0; i < dataset.size(); ++i) {
            for (int j = 0; j < dataset.get(0).size(); ++j) {
                centeredData[i][j] = dataset.get(i).get(j) - av[j];
            }
        }
        return centeredData;
    }

    private double[][] normalize(double[][] dataset) {
        int div = dataset.length - 1;
        for (int j = 0; j < dataset[0].length; ++j) {
            double sumCol = 0;
            for (int i = 0; i < dataset.length; ++i) {
                sumCol += Math.pow(dataset[i][j], 2) / div;
            }
            sumCol = Math.sqrt(sumCol);
            for (int i = 0; i < dataset.length; ++i) {
                dataset[i][j] /= sumCol;
            }
        }
        return dataset;
    }

    private EigenDecomposition getEigenDecomposition(double[][] centeredData) {
        RealMatrix f = new Array2DRowRealMatrix(centeredData);
        EigenDecomposition ed = new EigenDecomposition(f.transpose().multiply(f));
        return ed;
    }

    private List<Integer> selectMFeatures(EigenDecomposition ed) {
        double[] lambdas = ed.getRealEigenvalues();
        List<Integer> lambdaInd = Stream.iterate(0, i -> i + 1).limit(lambdas.length)
                                        .sorted((i, j) -> Double.compare(lambdas[j], lambdas[i]))
                                        .collect(Collectors.toList());
        int m = 0;
        double lambdaSum = 0;
        for (double l : lambdas) {
            lambdaSum += l;
        }
        double nomin = lambdaSum;
        double em = 1;
        while (em > eps && m <= lambdaInd.size()) {
            nomin -= lambdas[lambdaInd.get(m)];
            em = nomin / lambdaSum;
            m++;
        }
        return lambdaInd.subList(0, Math.min(m, lambdas.length));
    }

    private double[][] getU(EigenDecomposition ed, List<Integer> lambdaInd) {
        List<double[]> matrixU = new ArrayList<>();
        for (int i : lambdaInd) {
            matrixU.add(ed.getEigenvector(i).toArray());
        }
        double[][] res = new double[matrixU.size()][lambdaInd.size()];
        return new Array2DRowRealMatrix(matrixU.toArray(res)).transpose().getData();
    }

    public void pca(List<List<Double>> dataset, PrintWriter pw) {
        double[][] preprocessedData = central(dataset);
        preprocessedData = normalize(preprocessedData);
        EigenDecomposition ed = getEigenDecomposition(preprocessedData);
        List<Integer> features = selectMFeatures(ed);
        double[][] Umatrix = getU(ed, features);
        printMatrix(Umatrix, pw, "U matrix");
        System.out.println("New dim: " + Umatrix[0].length);
        double[][] Gmatrix = new Array2DRowRealMatrix(preprocessedData).multiply(new Array2DRowRealMatrix(Umatrix)).getData();
        printMatrix(Gmatrix, pw, "G matrix");
    }

    private void printMatrix(double[][] matrix, PrintWriter pw, String name) {
        pw.println(name);
        pw.println("==========================================");
        for (double[] row : matrix) {
            for (int j = 0; j < matrix[0].length; ++j) {
                pw.print(row[j] + " ");
            }
            pw.println();
        }
        pw.flush();
    }
}
