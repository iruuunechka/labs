package first.classifiers;

/**
 * @author Irene Petrova
 */
public class Utils {
    public static double fmeasure(double tp, double tn, double fp, double fn) {
        double precision = ((tp == 0 ? 0 : tp / (tp + fp)) + (tn == 0 ? 0 : tn / (tn + fn))) / 2;
        double recall = ((tp == 0 ? 0 : tp / (tp + fn)) + (tn == 0 ? 0 : tn / (tn + fp))) / 2;
        double f = (precision + recall == 0) ? 0 : 2 * precision * recall / (precision + recall);
        return f;
    }
}
