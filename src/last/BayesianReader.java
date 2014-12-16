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

    public static BayesianNetwork read(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] names = br.readLine().split(" ");
        Map<Integer, String> varNames = new HashMap<>();
        for (int i = 0; i < names.length; ++i) {
            varNames.put(i, names[i]);
        }

        int vertCount = names.length;
        Node[] graph = new Node[vertCount];
        String s;
        while (!(s = br.readLine()).isEmpty()) {
            String[] vertProb = s.split(" : ");
            Map<Integer, Double> prob = new HashMap<>();
            prob.put(0, 1 - Double.parseDouble(vertProb[1]));
            int vert = Integer.parseInt(vertProb[0]);
            prob.put(1 << vert, Double.parseDouble(vertProb[1]));
            graph[vert] = new Node(new Factor(prob, 1 << vert), null, vert);
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
            parents += 1 << vert;
            while (!s.equals("end")) {
                String[] parentProb = s.split(" : ");
                String[] parent = parentProb[0].split(" ");

                int parentsCond = 0;
                for (int i = 0; i < parent.length; ++i) {
                    if (parent[i].charAt(0) != 172) {
                        parentsCond += 1 << Integer.parseInt(parent[i]);
                    }
                }
                prob.put(parentsCond, 1 - Double.parseDouble(parentProb[1]));
                parentsCond += 1 << vert;
                prob.put(parentsCond, Double.parseDouble(parentProb[1]));
                s = br.readLine();
            }
            graph[vert] = new Node(new Factor(prob, parents), child, vert);
        }
        System.out.println(graph.length);
        return new BayesianNetwork(graph, varNames);
    }
}
