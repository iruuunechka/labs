package first.classifiers;

import java.util.List;

/**
 * @author Irene Petrova
 */
public interface Classifier<P, R> {
    public Double testClassifier(List<Item<P, R>> tests);
}
