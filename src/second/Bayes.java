package second;

import java.util.*;

/**
 * @author Irene Petrova
 */
public class Bayes {

    private final double[] prior;
    private final List<Map<String, Double>> condprob;
    private final Set<String> dictionary;
    private final int classCou = 2;

    private Set<String> getDictionary(List<Message> trainingData) {
        Set<String> dictionary = new HashSet<>();
        for (Message msg : trainingData) {
            dictionary.addAll(msg.getTerms());
        }
        return dictionary;
    }

    private List<List<Message>> getMsgByClass(List<Message> trainingData) {
        List<List<Message>> msgByClass = new ArrayList<>();
        msgByClass.add(new ArrayList<Message>());
        msgByClass.add(new ArrayList<Message>());
        for (Message msg : trainingData) {
            if (msg.isSpam()) {
                msgByClass.get(1).add(msg);
            } else {
                msgByClass.get(0).add(msg);
            }
        }
        return msgByClass;
    }

    private int countTerm(String term, List<Message> msgClass) {
        int count = 0;
        for (Message msg : msgClass) {
            count += msg.getTermCount(term);
        }
        return count;
    }

    public Bayes (List<Message> trainingData) {
        dictionary = getDictionary(trainingData);
        int n = trainingData.size();
        List<List<Message>> msgByClass = getMsgByClass(trainingData);
        prior = new double[classCou];
        condprob = new ArrayList<>();
        for (int c = 0; c < classCou; ++c) {
            double nc = msgByClass.get(c).size();
            prior[c] = nc / n;

            Map<String, Integer> tct = new HashMap<>();
            int tctSum = 0;
            for (String term : dictionary) {
                int count = countTerm(term, msgByClass.get(c));
                tct.put(term, count);
                tctSum += count + 1;
            }

            condprob.add(new HashMap<String, Double>());
            for (String term : dictionary) {
                condprob.get(c).put(term, (1.0 + tct.get(term)) / tctSum );
            }
        }
    }

    private boolean classify(Message msg) {
        double[] score = new double[classCou];
        for (int c = 0; c < classCou; ++c) {
            score[c] = Math.log(prior[c]);
            for (String term : msg.getTerms()) {
                score[c] += Math.log(condprob.get(c).containsKey(term) ? condprob.get(c).get(term) : 1);
            }
        }

        double maxC = Double.NEGATIVE_INFINITY;
        int argmaxC = 0;
        for (int c = 0; c < classCou; ++c) {
            if (score[c] > maxC) {
                maxC = score[c];
                argmaxC = c;
            }
        }
        return argmaxC == 1;
    }

    public double testClassifier(List<Message> testData) {
        int good = 0;
        for (Message msg : testData) {
            if (classify(msg) == msg.isSpam()) {
                good++;
            }
        }
        return 100.0 * good / testData.size();
    }
}
