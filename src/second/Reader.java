package second;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Irene Petrova
 */
public class Reader {
    private final List<Message> dataset = new ArrayList<>();

    public Reader(File datasetDir) throws IOException {
        for (File dataset : datasetDir.listFiles()) {
            for (File messages : dataset.listFiles()) {
                BufferedReader br = new BufferedReader(new FileReader(messages));

                String s;
                Set<String> terms = new HashSet<>();
                Map<String, Integer> msgTokens = new HashMap<>();
                boolean isSpam;

                while (!((s = br.readLine()) == null)) {
                    StringTokenizer st = new StringTokenizer(s);
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken();
                        terms.add(token);
                        if (msgTokens.containsKey(token)) {
                            msgTokens.put(token, msgTokens.get(token) + 1);
                        } else {
                            msgTokens.put(token, 1);
                        }
                    }
                }
                isSpam = messages.getName().contains("spmsg");
                this.dataset.add(new Message(terms, msgTokens, isSpam));
            }
        }
        Collections.shuffle(dataset);
    }

    public List<Message> getTrainingSet(int percent) {
        return dataset.subList(0, (int) Math.floor(dataset.size() * percent / 100));
    }

    public List<Message> getTestSet(int percent) {
        return dataset.subList((int) Math.floor(dataset.size() * percent / 100), dataset.size());
    }
}
