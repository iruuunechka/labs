package last;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Irene Petrova
 */
public class BayesianReader {
    private static class Node {
        private Factor factor;
        private int[] child;

        public Node(Factor factor, int[] child) {
            this.factor = factor;
            this.child = child;
        }

        public void setChild(int[] child) {
            this.child = child;
        }

    }
    private static class Factor {
        public Factor(Map<Integer, Double> prob, int parents) {
            this.prob = prob;
            this.parents = parents;
        }

        private Map<Integer, Double> prob;


        private int parents;

        private void sumByMask(int mask) {
            for (int k : prob.keySet()) {
                int newKey = k & mask;
                prob.put(newKey, prob.getOrDefault(newKey, 0.0) + prob.get(k));
            }
        }

        public void sumByVert(int x, int vertCount) {
            int mask = 1 << vertCount - 1;
            mask = mask & ~(1 << x);
        }

        private boolean getBit(int x, int i) {
            return (x | (1 << i)) == x;
        }

        private boolean hasMask(int x, int mask) {
            return (x | mask) == x;
        }

        private Factor multiply(Factor factor) {
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
    }

    public static void read(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] names = br.readLine().split(" ");
        int vertCount = names.length;
        Node[] graph = new Node[vertCount];
        String s;
        while (!(s = br.readLine()).isEmpty()) {
            String[] vertProb = s.split(" : ");
            Map<Integer, Double> prob = new HashMap<>();
            prob.put(0, Double.parseDouble(vertProb[1]));
            graph[Integer.parseInt(vertProb[0])] = new Node(new Factor(prob, 0), null);
        }
        while (!((s = br.readLine()) == null)) {
            String[] vertChild =  s.split(":");
            int vert = Integer.parseInt(vertChild[0].trim());
            int[] child = null;
            if (!(vertChild.length <= 1 || vertChild[1].trim().isEmpty())) {
                String[] childs = vertChild[1].trim().split(" ");
                child = new int[childs.length];
                for (int i = 0; i < childs.length; ++i) {
                    child[i] = Integer.parseInt(childs[i]);
                }
            }
            s = br.readLine();
            if (s.equals("end")) {
                graph[vert].setChild(child);
                continue;
            }
            Map<Integer, Double> prob = new HashMap<>();
            String[] parentsNums = s.split(":")[0].split(" ");
            int parents = 0; //first line without "not"
            for (int i = 0; i < parentsNums.length; ++i) {
                parents += 1 << Integer.parseInt(parentsNums[i]);
            }
            while (!s.equals("end")) {
                String[] parentProb = s.split(" : ");
                String[] parent = parentProb[0].split(" ");

                int parentsCond = 0;
                for (int i = 0; i < parent.length; ++i) {
                    if (parent[i].charAt(0) != 172) {
                        parentsCond += 1 << Integer.parseInt(parent[i]);
                    }
                }
                prob.put(parentsCond, Double.parseDouble(parentProb[1]));
                s = br.readLine();
            }
            graph[vert] = new Node(new Factor(prob, parents), child);
        }
        System.out.println(graph.length);
    }
}
