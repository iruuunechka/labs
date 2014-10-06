import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Irene Petrova
 */
public class ArffReader {

    private final  Map<Long, Boolean[]> buckets = new HashMap<>();
    private final double minSupport = 0.01;
    private final double minConfidence = 0.01;
    private final List<Rule> allRules = new ArrayList<>();
    private final int maxName;
    private final List<List<Pair>> candidatesWithSupport = new ArrayList<>();
    private final Map<String, Integer> numByName = new HashMap<>();
    private final Map<Integer, String> nameByNum = new HashMap<>();

    public ArffReader(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filename));
        int dataCou = 0;
        sc.nextLine();       //@relation QueryResult
        sc.nextLine();       //""
        sc.nextLine();       //@attribute product_name
//        String[] line = sc.nextLine().split(",");
//        line[0] = line[0].replace("@attribute product_name {", "");
//        line[line.length - 1] = line[line.length - 1].replace("}", "");
//
//        for (int i = 0; i < line.length; i++) {
//            nameByNum.put(i, line[i]);
//            numByName.put(line[i], i);
//        }
//
//        maxName = line.length;

        String[] line = sc.nextLine().split(",");
        line[0] = line[0].replace("@attribute product_category {", "");
        line[line.length - 1] = line[line.length - 1].replace("}", "");

        for (int i = 0; i < line.length; i++) {
            nameByNum.put(i, line[i]);
            numByName.put(line[i], i);
        }

        maxName = line.length;

        //sc.nextLine();   //@attribute product_category
        sc.nextLine();   //@attribute product_department
        sc.nextLine();   //@attribute basket_id numeric
        sc.nextLine();   //""
        sc.nextLine();   //@data

        while (sc.hasNextLine()) {
            line = sc.nextLine().split(",");
            long bucketNum = Long.valueOf(line[3]);
            if (!buckets.containsKey(bucketNum)) {
                buckets.put(bucketNum, new Boolean[maxName]);
                Arrays.fill(buckets.get(bucketNum), false);
            }
            buckets.get(bucketNum)[numByName.get(line[1])] = true;
        }

        List<List<int[]>> allCandidates = getSets();
        generateAllRules();
        for (Rule r : allRules) {
            for (int i : r.left) {
                System.out.print(nameByNum.get(i) + " ");
            }
            System.out.print("=>");
            for (int i : r.right) {
                System.out.print(nameByNum.get(i) + " ");
            }
            System.out.println( " (confidence greater than or equal to " + r.confidence + ")");
//            System.out.println();
        }
//        for (List<int[]> set : allCandidates) {
//            for (int[] candidate : set) {
//                for (int name : candidate) {
//                    System.out.println(nameByNum.get(name) + ' ');
//                }
//            }
//        }
    }

    public List<List<int[]>> getSets() {
        int[] f1 = new int[maxName];
        Arrays.fill(f1, 0);
        for (Boolean[] bucket : buckets.values()) {
            for (int i = 0; i < bucket.length; ++i) {
                if (bucket[i]) {
                    f1[i]++;
                }
            }
        }

        List<Pair> suppCand = new ArrayList<>();
        double support = minSupport * buckets.size();
        List<int[]> newCandidates = new ArrayList<>();
        for (int i = 0; i < f1.length; ++i) {
            if (f1[i] > support) {
                int[] name = {i};
                newCandidates.add(name);
                suppCand.add(new Pair(name, f1[i]));
            }
        }
        candidatesWithSupport.add(suppCand);

        int k = 1;
        List<List<int[]>> allCandidates = new ArrayList<>();
        while (!newCandidates.isEmpty()) {
            printCandidates(newCandidates);
            allCandidates.add(newCandidates);
            newCandidates = generateNewCandidates(newCandidates, k);
            k++;
        }
        return allCandidates;
    }

    private void printCandidates(List<int[]> candidates) {
        for (int[] candidate : candidates) {
            for (int name : candidate) {
                System.out.print(nameByNum.get(name) + ' ');
            }
            System.out.println();
        }
    }

    private List<int[]> generateNewCandidates(List<int[]> candidates, int k) {
        System.out.println("Generating candidates for k = " + (k + 1));
        List<int[]> newCandidates = new ArrayList<>();
        for (int i = 0; i < candidates.size(); ++i) {
            for (int j = i + 1; j < candidates.size(); ++j) {
                boolean isPQ = true;
                for (int n = 0; n < k - 1; ++n) {
                    if (candidates.get(i)[n] != candidates.get(j)[n]) {
                        isPQ = false;
                        break;
                    }
                }
                if (isPQ) {
                    int[] newCandidate;
                    if (candidates.get(i)[k - 1] < candidates.get(j)[k - 1]) {
                        newCandidate = Arrays.copyOf(candidates.get(i), k + 1);
                        newCandidate[k] = candidates.get(j)[k - 1];
                    } else {
                        newCandidate = Arrays.copyOf(candidates.get(j), k + 1);
                        newCandidate[k] = candidates.get(i)[k - 1];
                    }
                    newCandidates.add(newCandidate);
                }
            }
        }
        System.out.println("Generated " + newCandidates.size() + " candidates");
        filterCandidates(newCandidates, candidates);
        System.out.println("After filter " + newCandidates.size() + " candidate");
        selectCandidates(newCandidates);
        System.out.println("After selection " + newCandidates.size() + " candidates");
        return newCandidates;
    }

    private void filterCandidates(List<int[]> newCandidates, List<int[]> oldCandidates) {
        for (Iterator<int[]> it = newCandidates.iterator(); it.hasNext();) {
            int[] candidate = it.next();
            for (int i = 0; i < candidate.length; ++i) {
                int[] subCandidate = new int[candidate.length - 1];
                System.arraycopy(candidate, 0, subCandidate, 0, i);
                System.arraycopy(candidate, i + 1, subCandidate, i, candidate.length - i - 1);
                boolean often = false;
                for (int[] oldCandidate : oldCandidates) {
                    if (Arrays.equals(oldCandidate, subCandidate)) {
                        often = true;
                        break;
                    }
                }
                if (!often) {
                    it.remove();
                    break;
                }
            }
        }
    }

    private void selectCandidates(List<int[]> newCandidates) {
        //int num = 0;
        List<Pair> suppCand = new ArrayList<>();
        for (Iterator<int[]> it = newCandidates.iterator(); it.hasNext();) {
            //System.out.println(num++);
            int[] candidate = it.next();
            int cou = 0;
            for (Boolean[] bucket : buckets.values()) {
                boolean contains = true;
                for (int name : candidate) {
                    if (!bucket[name]) {
                        contains = false;
                        break;
                    }
                }
                if (contains) {
                    cou++;
                }
            }
            double support = minSupport * buckets.size();
            if (cou < support) {
                it.remove();
            } else {
                suppCand.add(new Pair(candidate, cou));
            }
        }
        candidatesWithSupport.add(suppCand);
    }

    private Rule generateRule(Rule r, int name) {
        int[] newLeft = Arrays.copyOf(r.left, r.left.length + 1);
        newLeft[newLeft.length - 1] = name;
        Arrays.sort(newLeft);
        int[] newRight = new int[r.right.length - 1];
        int cur = 0;
        for (int i = 0; i < r.right.length; ++i) {
            if (r.right[i] != name) {
                newRight[cur++] = r.right[i];
            }
        }
        return new Rule(newLeft, newRight, r.confidence);
    }

    private void generateGoodRules(Rule r, List<Rule> currRules) {
        if (r.right.length <= 1) {
            return;
        }
        for (int name : r.right) {
            currRules.add(generateRule(r, name));
            generateGoodRules(currRules.get(currRules.size() - 1), currRules);
        }
    }

    private void generateRules(Rule r, double suppF, List<Rule> currRules) {
        if (r.right.length <= 1) {
            return;
        }
        for (int name : r.right) {
            Rule newRule = generateRule(r, name);
            boolean dupl = false;
            for (Rule rule : currRules) {
                dupl = true;
                if (rule.left.length == newRule.left.length) {
                    for (int i = 0; i < rule.left.length; ++i) {
                        if (rule.left[i] != newRule.left[i]) {
                            dupl = false;
                            break;
                        }
                    }
                }
                if (dupl) {
                    break;
                }
            }
            if (dupl) {
                continue;
            }
            newRule.setConfidence(suppF / getSupport(newRule.left));
            if (newRule.confidence >= minConfidence) {
                currRules.add(newRule);
                generateGoodRules(newRule, currRules);
            }
            generateRules(newRule, suppF, currRules);
        }
    }

    private double getSupport (int[] candidate) {
        for (Pair cand : candidatesWithSupport.get(candidate.length - 1)) {
            if (Arrays.equals(candidate, cand.candidate)) {
                return cand.support;
            }
        }
        return 0;
    }

    private void generateAllRules() {
        for (List<Pair> set : candidatesWithSupport) {
            for (Pair candidate : set) {
                List<Rule> currRules = new ArrayList<>();
                generateRules(new Rule(new int[0], candidate.candidate, 0), candidate.support, currRules);
                allRules.addAll(currRules);
            }
        }
    }

    private class Rule {
        private final int[] left;
        private final int[] right;

        private void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        private double confidence;

        private Rule(int[] left, int[] right, double confidence) {
            this.left = left;
            this.right = right;
            this.confidence = confidence;
        }
    }

    private class Pair {
        private final int[] candidate;
        private final int support;

        private Pair(int[] candidate, int support) {
            this.candidate = candidate;
            this.support = support;
        }
    }
}
