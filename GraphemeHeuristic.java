import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Heuristic that analyzes text using grapheme patterns (letter combinations)
 * to determine if text matches English writing patterns
 * Graphemes are basic units of written language (single letters and common combinations)
 */
public class GraphemeHeuristic implements Heuristic {
    
    // Common English graphemes from linguistic analysis
    private static final Set<String> COMMON_GRAPHEMES = new HashSet<>();
    static {
        // Single letters
        for (char c = 'a'; c <= 'z'; c++) {
            COMMON_GRAPHEMES.add(String.valueOf(c));
        }
        
        // Bigraphs (most common two-letter combinations in English)
        String[] BIGRAPHS = {"th", "he", "in", "er", "an", "re", "on", "at", "en", "nd", "or", "te", "es", "ed", "it", "is", "al", "ar", "st", "to",
                            "nt", "ha", "ou", "ea", "le", "ve", "se", "me", "li", "de", "co", "ra", "ro", "ma", "ne", "ic", "ca", "ta", "si",
                            "no", "lo", "di", "el", "pe", "ri", "be", "ut", "la", "so", "fo"};
        
        for (String bigraph : BIGRAPHS) {
            COMMON_GRAPHEMES.add(bigraph);
        }
        
        // Trigraphs (most common three-letter combinations in English)
        String[] TRIGRAPHS = {"the", "and", "ing", "ent", "ion", "her", "for", "tha", "ter", "est", "his", "nth", "ers", "ate", "ver", "all", "con",
                             "res", "int", "com", "sto", "pro", "per", "ect", "tor", "men", "str", "tro", "tin", "der", "und", "tra", "man", "ple",
                             "cal", "low", "por", "pre", "tio", "tan", "car", "mat", "lat", "sta", "sur", "out", "lat", "sup", "tri", "mis"};
        
        for (String trigraph : TRIGRAPHS) {
            COMMON_GRAPHEMES.add(trigraph);
        }
        
        // Quadrigraphs (common four-letter combinations in English)
        String[] QUADRIGRAPHS = {"tion", "ment", "ther", "ally", "ably", "ence", "that", "with", "from", "ntly", "sion", "tive", "form", "ship", "able",
                                "here", "more", "ness", "over", "self", "ward", "less", "some", "stan", "tant", "hand", "port", "tend", "just", "list",
                                "fore", "ward", "side", "seem", "make", "year", "stat", "come", "rate", "part", "term", "test", "turn", "head",
                                "need", "kind", "case", "open", "true"};
        
        for (String quadrigraph : QUADRIGRAPHS) {
            COMMON_GRAPHEMES.add(quadrigraph);
        }
    }
    
    // Expected frequencies for common graphemes (approximate percentages)
    private static final Map<String, Double> GRAPHEME_FREQUENCIES = new HashMap<>();
    static {
        // Single letters
        GRAPHEME_FREQUENCIES.put("e", 12.0);
        GRAPHEME_FREQUENCIES.put("t", 9.1);
        GRAPHEME_FREQUENCIES.put("a", 8.2);
        GRAPHEME_FREQUENCIES.put("o", 7.5);
        GRAPHEME_FREQUENCIES.put("i", 7.0);
        GRAPHEME_FREQUENCIES.put("n", 6.7);
        GRAPHEME_FREQUENCIES.put("s", 6.3);
        GRAPHEME_FREQUENCIES.put("h", 6.1);
        GRAPHEME_FREQUENCIES.put("r", 6.0);
        
        // Bigraphs
        GRAPHEME_FREQUENCIES.put("th", 3.5);
        GRAPHEME_FREQUENCIES.put("he", 3.0);
        GRAPHEME_FREQUENCIES.put("in", 2.5);
        GRAPHEME_FREQUENCIES.put("er", 2.0);
        GRAPHEME_FREQUENCIES.put("an", 1.8);
        GRAPHEME_FREQUENCIES.put("re", 1.6);
        GRAPHEME_FREQUENCIES.put("on", 1.5);
        GRAPHEME_FREQUENCIES.put("at", 1.4);
        GRAPHEME_FREQUENCIES.put("en", 1.3);
        GRAPHEME_FREQUENCIES.put("nd", 1.2);
        
        // Trigraphs
        GRAPHEME_FREQUENCIES.put("the", 1.8);
        GRAPHEME_FREQUENCIES.put("and", 1.2);
        GRAPHEME_FREQUENCIES.put("ing", 1.5);
        GRAPHEME_FREQUENCIES.put("her", 0.9);
        GRAPHEME_FREQUENCIES.put("for", 0.8);
        GRAPHEME_FREQUENCIES.put("ent", 0.7);
        GRAPHEME_FREQUENCIES.put("ion", 0.6);
        
        // Quadrigraphs
        GRAPHEME_FREQUENCIES.put("tion", 0.8);
        GRAPHEME_FREQUENCIES.put("ment", 0.5);
        GRAPHEME_FREQUENCIES.put("that", 0.4);
        GRAPHEME_FREQUENCIES.put("with", 0.4);
        GRAPHEME_FREQUENCIES.put("ther", 0.3);
    }
    
    private String lastSummary = "";
    
    @Override
    public double analyze(String text) {
        if (text == null || text.trim().isEmpty()) {
            lastSummary = "No text to analyze";
            return 0.0;
        }
        
        String lowerText = text.toLowerCase();
        Map<String, Integer> graphemeCount = new HashMap<>();
        int totalGraphemes = 0;
        
        // Extract and count graphemes
        // Start with longer graphemes first to avoid overcounting
        for (int length = 3; length >= 1; length--) {
            for (int i = 0; i <= lowerText.length() - length; i++) {
                String candidate = lowerText.substring(i, i + length);
                
                // Only count if it's a valid grapheme and consists of letters
                if (COMMON_GRAPHEMES.contains(candidate) && candidate.matches("[a-z]+")) {
                    // Check if this position hasn't been counted by a longer grapheme
                    boolean alreadyCounted = false;
                    for (int len = length + 1; len <= 3; len++) {
                        int start = Math.max(0, i - len + 1);
                        int end = Math.min(lowerText.length(), i + len);
                        for (int j = start; j < end - len + 1; j++) {
                            if (j <= i && i < j + len) {
                                String longer = lowerText.substring(j, j + len);
                                if (COMMON_GRAPHEMES.contains(longer) && longer.matches("[a-z]+")) {
                                    alreadyCounted = true;
                                    break;
                                }
                            }
                        }
                        if (alreadyCounted) break;
                    }
                    
                    if (!alreadyCounted) {
                        graphemeCount.put(candidate, graphemeCount.getOrDefault(candidate, 0) + 1);
                        totalGraphemes++;
                        i += length - 1; // Skip ahead to avoid overlapping
                    }
                }
            }
        }
        
        if (totalGraphemes == 0) {
            lastSummary = "No valid graphemes found";
            return 0.0;
        }
        
        // Calculate score based on presence of common graphemes
        double score = 0.0;
        int commonGraphemesFound = 0;
        
        for (String grapheme : COMMON_GRAPHEMES) {
            if (graphemeCount.containsKey(grapheme)) {
                commonGraphemesFound++;
                // Weight by expected frequency if available
                double weight = GRAPHEME_FREQUENCIES.getOrDefault(grapheme, 1.0);
                score += weight;
            }
        }
        
        // Normalize score
        score = Math.min(1.0, score / (COMMON_GRAPHEMES.size() * 5.0));
        
        lastSummary = String.format("Found %d common graphemes out of %d total graphemes", 
                                   commonGraphemesFound, totalGraphemes);
        
        return score;
    }
    
    @Override
    public String getName() {
        return "Grapheme Analysis";
    }
    
    @Override
    public String getSummary() {
        return lastSummary;
    }
}