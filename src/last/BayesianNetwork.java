package last;

import java.util.*;

/**
 * @author Irene Petrova
 */
public class BayesianNetwork {
    private Node[] graph;
    private final int vertCou;
    private final Map<Integer, String> names;

    public BayesianNetwork(Node[] nodes, Map<Integer, String> names) {
        this.names = names;
        this.graph = nodes;
        this.vertCou = nodes.length;
    }

    private Factor[] copyFactor() {
        Factor[] newFactors = new Factor[vertCou];
        for (int i = 0; i < vertCou; ++i) {
            newFactors[i] = graph[i].factor.copy();
        }
        return newFactors;
    }

    /**
     *
     * @param x condition
     * @param s condition value
     * @param v vertice we count for
     */
    public Factor countAposteriori(int x, boolean s, int v) {
        List<VarWithRef> sortedVarsWithRef = new ArrayList<>(); // в скольких факторах встречается каждая вершина
        for (int i = 0; i < vertCou; ++i) {
            sortedVarsWithRef.add(new VarWithRef(i));
            for (Node n : graph) {
                if (n.factor.isParent(i)) {
                    sortedVarsWithRef.get(i).addRef(n.val);
                }
            }
        }
        Collections.sort(sortedVarsWithRef); //отсортировали
        Iterator<VarWithRef> it = sortedVarsWithRef.iterator();
        while (it.hasNext()) {
            VarWithRef vwr = it.next();
            if (vwr.var == x  || vwr.var == v) {
               it.remove();
            }
        }

        Factor[] factors = copyFactor();
        for (Factor f : factors) {
            if (f.isParent(x)) {
                f.filter(x, s);
            }
        }
        Factor curFactor = null;
        boolean[] usedFactor = new boolean[vertCou]; //true if already multiplied
        Arrays.fill(usedFactor, false);
        for (VarWithRef vr : sortedVarsWithRef) {
            Factor product = null;

            for (int r : vr.refs) {
                if (!usedFactor[r]) {
                    if (product == null) {
                        product = factors[r];
                    } else {
                        product = product.multiply(factors[r]);
                    }
                    usedFactor[r] = true;
                }
            }
            if (product == null) {
                continue;
            }
            if (curFactor == null) {
                curFactor = product;
                curFactor = curFactor.sumByVert(vr.var, vertCou);
                continue;
            }
            curFactor = curFactor.multiply(product);
            curFactor = curFactor.sumByVert(vr.var, vertCou);
        }
        for (int i = 0; i < usedFactor.length; ++i) {
            if (!usedFactor[i]) {
                if (curFactor == null){
                    curFactor = factors[i];
                } else {
                    curFactor.multiply(factors[i]);
                    curFactor = curFactor.sumByVert(i, vertCou);
                }
            }
        }
        curFactor.normalize();
        curFactor.print(v, names.get(v), x, names.get(x));
        return curFactor;
    }

    private class VarWithRef implements Comparable<VarWithRef> {
        private int var;
        private List<Integer> refs;

        private VarWithRef(int var) {
            this.var = var;
            this.refs = new ArrayList<>();
        }

        private void addRef(int ref) {
            refs.add(ref);
        }

        @Override
        public int compareTo(VarWithRef o) {
            if (refs.size() < o.refs.size()) {
                return -1;
            } else if (refs.size() > o.refs.size()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
