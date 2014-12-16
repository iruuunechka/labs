package last;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Irene Petrova
 */
public class Factor {
    public Factor(Map<Integer, Double> prob, int parents) {
        this.prob = prob;
        this.parents = parents;
    }

    private Map<Integer, Double> prob;
    private int parents;

    public Factor copy() {
        Map<Integer, Double> newProb = new HashMap<>();
        newProb.putAll(prob);
        return new Factor(newProb, parents);
    }

    public void filter(int x, boolean s) {
        for (Iterator<Map.Entry<Integer, Double>> it = prob.entrySet().iterator(); it.hasNext();) {
            int key = it.next().getKey();
            if (getBit(key, x) != s) {
                it.remove();
            }
        }
    }

    private Map<Integer, Double> sumByMask(int mask) {
        Map<Integer, Double> newProb = new HashMap<>();
        for (int k : prob.keySet()) {
            int newKey = k & mask;
            newProb.put(newKey, newProb.getOrDefault(newKey, 0.0) + prob.get(k));
        }
        return newProb;
    }

    public Factor sumByVert(int x, int vertCount) {
        int mask = (1 << vertCount) - 1;
        mask = mask & ~(1 << x);
        return new Factor(sumByMask(mask), parents & mask);
    }

    public boolean isParent(int p) {
        return getBit(parents, p);
    }

    private boolean getBit(int x, int i) {
        return (x | (1 << i)) == x;
    }

    private boolean hasMask(int x, int mask) {
        return (x | mask) == x;
    }

    public void normalize() {
        double sum = 0;
        for (double p : prob.values()) {
            sum += p;
        }
        for (int key : prob.keySet()) {
            prob.put(key, prob.get(key) / sum);
        }
    }

    public Factor multiply(Factor factor) {
        int newParents = parents | factor.parents;
        int intersection = parents & factor.parents;
        Map<Integer, Double> newProb = new HashMap<>();
        for (int parVal1 : prob.keySet()) {
            int mask = intersection & parVal1;
            for (int parval2 : factor.prob.keySet()) {
                if (hasMask(parval2, mask)) {
                    newProb.put(parVal1 + (parval2 & ~mask),
                            prob.get(parVal1) * factor.prob.get(parval2));
                }
            }
        }
        return new Factor(newProb, newParents);
    }

    public void print(int num, int[] reasons, boolean[] reasonsVal, Map<Integer, String> names) {
        StringBuilder sb = new StringBuilder();
        sb.append("Probability of " + num + " ("+ names.get(num) + ") in case of ");
        for (int i = 0; i < reasons.length; i++) {
            if (!reasonsVal[i]) {
                sb.append("no ");
            }
            sb.append(reasons[i] + " ("+ names.get(reasons[i]) + ") ");
        }
        System.out.println(sb);
        for (Map.Entry<Integer, Double> e : prob.entrySet()) {
            if (getBit(e.getKey(), num)) {
                System.out.println(String.format("true | %.4f", e.getValue()));
            } else {
                System.out.println(String.format("false  | %.4f", e.getValue()));
            }
        }
    }
}
