package last;

import java.util.*;

/**
 * @author Irene Petrova
 */
public class BayesianNetwork {
    private Node[] graph;

    public int getVertCou() {
        return vertCou;
    }

    private final int vertCou;
    private final Map<Integer, String> names;

    public BayesianNetwork(Node[] nodes, Map<Integer, String> names) {
        this.names = names;
        this.graph = nodes;
        this.vertCou = nodes.length;
        checkCorrect();
    }

    private void dfsAcyclic(int u, int[] color) {
        color[u] = 1;
        if (graph[u].child != null) {
            for (int v : graph[u].child) {
                if (color[v] == 0) {
                    dfsAcyclic(v, color);
                }
                if (color[v] == 1) {
                    throw new RuntimeException("Bayesian network has cycle");
                }
            }
        }
        color[u] = 3;
    }

    private void dfsConnected(int u, int[] color, List<Set<Integer>> graph) {
        color[u] = 1;
        if (graph.get(u) != null) {
            for (int v : graph.get(u)) {
                if (color[v] == 0) {
                    dfsConnected(v, color, graph);
                }
            }
        }
        color[u] = 3;
    }

    private void checkCorrect() {
        int[] color = new int[graph.length];
        Arrays.fill(color, 0);
        dfsAcyclic(0, color);

        List<Set<Integer>> unDirectedGraph = new ArrayList<>();
        for (Node n : graph) {
            Set<Integer> neigh = new HashSet<>();
            neigh.addAll(n.child);
            unDirectedGraph.add(neigh);
        }
        for (int i = 0; i < graph.length; ++i) {
            for (int child : graph[i].child) {
                unDirectedGraph.get(child).add(i);
            }
        }
        Arrays.fill(color, 0);
        dfsConnected(0, color, unDirectedGraph);
        for (int col : color) {
            if (col == 0) {
                throw new RuntimeException("Bayesian network is not connected");
            }
        }
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
    public Factor countAposteriori(int[] x, boolean[] s, int v) {
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
            if (vwr.var == v) {
                it.remove();
            }
            for (int xVal : x) {
                if (xVal != v && vwr.var == xVal) {
                    it.remove();
                }
            }
        }

        Factor[] factors = copyFactor();
        for (Factor f : factors) {
            for (int i = 0; i < x.length; ++i) {
                if (f.isParent(x[i])) {
                    f.filter(x[i], s[i]);
                }
            }
        }
        Factor curFactor = null;
        boolean[] usedFactor = new boolean[vertCou]; //true if already multiplied
        Arrays.fill(usedFactor, false);
        while (!sortedVarsWithRef.isEmpty()) {
            VarWithRef vr = sortedVarsWithRef.get(0);
            Factor product = null;
            List<Integer> curMultiplied = new ArrayList<>(); //перемноженные на текущем шаге
            for (int r : vr.refs) {
                if (!usedFactor[r]) {
                    if (product == null) {
                        product = factors[r];
                    } else {
                        product = product.multiply(factors[r]);
                    }
                    usedFactor[r] = true;
                    curMultiplied.add(r);
                }
            }
            if (product == null) {
                if (curFactor != null) {
                    curFactor = curFactor.sumByVert(vr.var, vertCou);
                }
            } else if (curFactor == null) {
                curFactor = product;
                curFactor = curFactor.sumByVert(vr.var, vertCou);
            } else {
                curFactor = curFactor.multiply(product);
                curFactor = curFactor.sumByVert(vr.var, vertCou);
            }
            sortedVarsWithRef.remove(vr);
            for (VarWithRef vwr : sortedVarsWithRef) {
                for (Integer toRemove : curMultiplied) {
                    vwr.refs.remove(toRemove);
                }
            }
            Collections.sort(sortedVarsWithRef);
        }
        for (int i = 0; i < usedFactor.length; ++i) {
            if (!usedFactor[i]) {
                if (curFactor == null){
                    curFactor = factors[i];
                } else {
                    curFactor = curFactor.multiply(factors[i]);
                    if (i != v) {
                        curFactor = curFactor.sumByVert(i, vertCou);
                    }
                }
            }
        }
        curFactor.normalize();
        curFactor.print(v, x, s, names);
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
