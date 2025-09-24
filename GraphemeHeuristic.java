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
    
    // Common English graphemes (single letters and digraphs/trigraphs)
    private static final Set<String> COMMON_GRAPHEMES = new HashSet<>();
    static {
        // Single letters
        for (char c = 'a'; c <= 'z'; c++) {
            COMMON_GRAPHEMES.add(String.valueOf(c));
        }
        
        // Common digraphs (two-letter combinations)
        String[] digraphs = {
            "th", "he", "in", "er", "an", "re", "ed", "nd", "on", "en", "at", "ou", "ea", "ha",
            "es", "or", "ti", "to", "it", "st", "ar", "hi", "as", "te", "et", "ng", "of", "al",
            "de", "se", "le", "sa", "si", "ar", "ve", "ra", "ld", "ur", "ch", "sh", "wh", "ph",
            "gh", "ck", "qu", "oo", "ee", "ll", "ss", "ff", "pp", "tt", "nn", "mm", "dd", "bb",
            "cc", "gg", "rr", "zz", "ai", "ay", "ei", "ey", "ie", "oe", "ue", "ui", "au", "aw",
            "ew", "ow", "oy", "oi"
        };
        
        for (String digraph : digraphs) {
            COMMON_GRAPHEMES.add(digraph);
        }
        
        // Common trigraphs (three-letter combinations)
        String[] trigraphs = {
            "the", "and", "ing", "ion", "tio", "ent", "ous", "all", "are", "ere", "her", "his",
            "ate", "est", "for", "ght", "cha", "che", "chi", "tch", "dge", "sch"
        };
        
        for (String trigraph : trigraphs) {
            COMMON_GRAPHEMES.add(trigraph);
        }
    }
    
    // Expected frequencies for common graphemes (rough estimates)
    private static final Map<String, Double> GRAPHEME_FREQUENCIES = new HashMap<>();
    static {
        GRAPHEME_FREQUENCIES.put("e", 12.0);
        GRAPHEME_FREQUENCIES.put("t", 9.1);
        GRAPHEME_FREQUENCIES.put("a", 8.1);
        GRAPHEME_FREQUENCIES.put("o", 7.5);
        GRAPHEME_FREQUENCIES.put("i", 7.0);
        GRAPHEME_FREQUENCIES.put("n", 6.7);
        GRAPHEME_FREQUENCIES.put("s", 6.3);
        GRAPHEME_FREQUENCIES.put("h", 6.1);
        GRAPHEME_FREQUENCIES.put("r", 6.0);
        GRAPHEME_FREQUENCIES.put("th", 3.5);
        GRAPHEME_FREQUENCIES.put("he", 3.0);
        GRAPHEME_FREQUENCIES.put("in", 2.5);
        GRAPHEME_FREQUENCIES.put("er", 2.0);
        GRAPHEME_FREQUENCIES.put("an", 1.8);
        GRAPHEME_FREQUENCIES.put("ing", 1.5);
        GRAPHEME_FREQUENCIES.put("the", 1.2);
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