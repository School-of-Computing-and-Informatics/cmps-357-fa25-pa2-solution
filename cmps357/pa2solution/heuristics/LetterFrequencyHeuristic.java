package cmps357.pa2solution.heuristics;

import java.util.HashMap;
import java.util.Map;

/**
 * Heuristic that analyzes letter frequency to determine if text matches English patterns
 * Uses chi-squared test to compare observed frequencies with expected English frequencies
 */
public class LetterFrequencyHeuristic implements Heuristic {
    
    // Expected English letter frequencies (case-insensitive)
    private static final Map<Character, Double> ENGLISH_FREQUENCIES = new HashMap<>();
    static {
        // Standard English letter frequencies (percentages)
        ENGLISH_FREQUENCIES.put('a', 8.12);
        ENGLISH_FREQUENCIES.put('b', 1.49);
        ENGLISH_FREQUENCIES.put('c', 2.78);
        ENGLISH_FREQUENCIES.put('d', 4.25);
        ENGLISH_FREQUENCIES.put('e', 12.02);
        ENGLISH_FREQUENCIES.put('f', 2.23);
        ENGLISH_FREQUENCIES.put('g', 2.02);
        ENGLISH_FREQUENCIES.put('h', 6.09);
        ENGLISH_FREQUENCIES.put('i', 6.97);
        ENGLISH_FREQUENCIES.put('j', 0.15);
        ENGLISH_FREQUENCIES.put('k', 0.77);
        ENGLISH_FREQUENCIES.put('l', 4.03);
        ENGLISH_FREQUENCIES.put('m', 2.41);
        ENGLISH_FREQUENCIES.put('n', 6.75);
        ENGLISH_FREQUENCIES.put('o', 7.51);
        ENGLISH_FREQUENCIES.put('p', 1.93);
        ENGLISH_FREQUENCIES.put('q', 0.10);
        ENGLISH_FREQUENCIES.put('r', 5.99);
        ENGLISH_FREQUENCIES.put('s', 6.33);
        ENGLISH_FREQUENCIES.put('t', 9.06);
        ENGLISH_FREQUENCIES.put('u', 2.76);
        ENGLISH_FREQUENCIES.put('v', 0.98);
        ENGLISH_FREQUENCIES.put('w', 2.36);
        ENGLISH_FREQUENCIES.put('x', 0.15);
        ENGLISH_FREQUENCIES.put('y', 1.97);
        ENGLISH_FREQUENCIES.put('z', 0.07);
    }
    
    private String lastSummary = "";
    
    @Override
    public double analyze(String text) {
        if (text == null || text.trim().isEmpty()) {
            lastSummary = "No text to analyze";
            return 0.0;
        }
        
        // Count letter frequencies (case-insensitive)
        Map<Character, Integer> letterCounts = new HashMap<>();
        int totalLetters = 0;
        
        for (char c : text.toCharArray()) {
            char lowerC = Character.toLowerCase(c);
            if (Character.isLetter(lowerC) && lowerC >= 'a' && lowerC <= 'z') {
                letterCounts.put(lowerC, letterCounts.getOrDefault(lowerC, 0) + 1);
                totalLetters++;
            }
        }
        
        if (totalLetters == 0) {
            lastSummary = "No letters found in text";
            return 0.0;
        }
        
        // Calculate chi-squared statistic
        double chiSquared = 0.0;
        int lettersAnalyzed = 0;
        
        for (char letter = 'a'; letter <= 'z'; letter++) {
            int observed = letterCounts.getOrDefault(letter, 0);
            double expectedPercent = ENGLISH_FREQUENCIES.get(letter);
            double expected = (expectedPercent / 100.0) * totalLetters;
            
            if (expected > 0) {
                chiSquared += Math.pow(observed - expected, 2) / expected;
                lettersAnalyzed++;
            }
        }
        
        // Convert chi-squared to a score between 0 and 1
        // Lower chi-squared means better match to English
        // Use a scaling factor to normalize the score
        double score = Math.max(0.0, 1.0 - (chiSquared / (lettersAnalyzed * 10.0)));
        
        // Create summary
        lastSummary = String.format("Analyzed %d letters, chi-squared: %.2f", 
                                   totalLetters, chiSquared);
        
        return score;
    }
    
    @Override
    public String getName() {
        return "Letter Frequency Analysis";
    }
    
    @Override
    public String getSummary() {
        return lastSummary;
    }
}