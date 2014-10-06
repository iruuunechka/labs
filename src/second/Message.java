package second;

import java.util.Map;
import java.util.Set;

/**
 * @author Irene Petrova
 */
public class Message {
    public Set<String> getTerms() {
        return terms;
    }

    public boolean isSpam() {
        return isSpam;
    }

    public int getTermCount (String term) {
        if (!terms.contains(term)) {
            return 0;
        }
        return msgTokens.get(term);
    }

    private final Set<String> terms;
    private final Map<String, Integer> msgTokens;
    private final boolean isSpam;

    public Message(Set<String> terms, Map<String, Integer> msgTokens, boolean isSpam) {
        this.terms = terms;
        this.msgTokens = msgTokens;
        this.isSpam = isSpam;
    }


}
