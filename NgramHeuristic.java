import java.util.HashMap;
import java.util.Map;

/**
 * Stub implementation for N-gram analysis heuristic
 * This would analyze sequences of N characters/words to determine text quality
 * Currently provides a basic implementation that can be extended for full N-gram analysis
 */
public class NgramHeuristic implements Heuristic {
    
    private int ngramSize;
    private String lastSummary = "";
    
    /**
     * Creates an N-gram heuristic with the specified N-gram size
     * @param ngramSize The size of N-grams to analyze (e.g., 2 for bigrams, 3 for trigrams)
     */
    public NgramHeuristic(int ngramSize) {
        this.ngramSize = Math.max(1, ngramSize);
    }
    
    /**
     * Creates a default N-gram heuristic with trigrams (N=3)
     */
    public NgramHeuristic() {
        this(3);
    }
    
    @Override
    public double analyze(String text) {
        if (text == null || text.trim().isEmpty()) {
            lastSummary = "No text to analyze";
            return 0.0;
        }
        
        // Basic implementation: count unique N-grams and compare to total possible
        // This is a stub - a full implementation would compare against expected English N-gram frequencies
        
        String cleanText = text.toLowerCase().replaceAll("[^a-zA-Z\\s]", "");
        Map<String, Integer> ngramCounts = new HashMap<>();
        int totalNgrams = 0;
        
        // Extract N-grams
        for (int i = 0; i <= cleanText.length() - ngramSize; i++) {
            String ngram = cleanText.substring(i, i + ngramSize);
            // Only count if it contains at least one letter and no spaces in the middle
            if (ngram.matches(".*[a-z].*") && !ngram.trim().contains(" ")) {
                ngramCounts.put(ngram, ngramCounts.getOrDefault(ngram, 0) + 1);
                totalNgrams++;
            }
        }
        
        if (totalNgrams == 0) {
            lastSummary = String.format("No valid %d-grams found", ngramSize);
            return 0.0;
        }
        
        // Simple scoring: diversity of N-grams suggests more natural text
        // A completely random text would have many unique N-grams
        // A highly repetitive text would have few unique N-grams
        // Natural English falls somewhere in between
        double diversity = (double) ngramCounts.size() / totalNgrams;
        
        // Score based on diversity - too high or too low suggests non-natural text
        double score;
        if (diversity < 0.3) {
            // Too repetitive
            score = diversity / 0.3;
        } else if (diversity > 0.8) {
            // Too random
            score = (1.0 - diversity) / 0.2;
        } else {
            // Good range
            score = 1.0;
        }
        
        score = Math.max(0.0, Math.min(1.0, score));
        
        lastSummary = String.format("Found %d unique %d-grams out of %d total (diversity: %.2f)", 
                                   ngramCounts.size(), ngramSize, totalNgrams, diversity);
        
        return score;
    }
    
    @Override
    public String getName() {
        return String.format("%d-gram Analysis", ngramSize);
    }
    
    @Override
    public String getSummary() {
        return lastSummary;
    }
    
    /**
     * Gets the N-gram size used by this heuristic
     * @return The N-gram size
     */
    public int getNgramSize() {
        return ngramSize;
    }
}