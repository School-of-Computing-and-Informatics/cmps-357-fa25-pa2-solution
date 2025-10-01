package cmps357.pa2solution.heuristics;

/**
 * Interface for text analysis heuristics used to evaluate decrypted text quality
 * Higher scores indicate better matches to expected patterns
 */
public interface Heuristic {
    
    /**
     * Analyzes text and returns a score indicating how well it matches expected patterns
     * @param text The text to analyze
     * @return A score between 0.0 and 1.0, where 1.0 indicates a perfect match
     */
    double analyze(String text);
    
    /**
     * Gets the name of this heuristic
     * @return A descriptive name for this heuristic
     */
    String getName();
    
    /**
     * Gets a summary of the analysis performed on the last analyzed text
     * @return A human-readable summary of the analysis results
     */
    String getSummary();
}