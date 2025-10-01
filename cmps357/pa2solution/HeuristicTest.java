package cmps357.pa2solution;

import cmps357.pa2solution.heuristics.*;

/**
 * Test class to demonstrate the heuristics functionality
 * Shows how each heuristic analyzes different types of text
 *
 * Chi-squared interpretation:
 *   - The chi-squared statistic measures how closely the letter frequency of a text matches expected English frequencies.
 *   - Lower chi-squared values indicate a better match to English (i.e., the text is more likely to be English-like).
 *   - Higher chi-squared values suggest the text is less like English (e.g., random or heavily scrambled text).
 *   - In this program, the score is normalized so that a value closer to 1 means a better match to English, and closer to 0 means a poor match.
 *   - Use the chi-squared value and the normalized score to compare how "English-like" different texts are.
 */
public class HeuristicTest {
    
    public static void main(String[] args) {
        System.out.println("Heuristic Analysis Test");
        System.out.println("======================");
        System.out.println();
        
        // Create test texts
        String englishText = "the quick brown fox jumps over the lazy dog";
        String scrambledText = "ahe qtick brnwo fxo jmpus ovre teh layaz dgo";
        String randomText = "xqz vwk plm jyx hgf rtu abn ced poi lkj mhg";
        String caesarText = "Aol xBpjr iyvDu mvE qBtwz vCly Aol shGF kvn";
        
        // Create heuristics
        Heuristic[] heuristics = {
            new LetterFrequencyHeuristic(),
            new DictionaryHeuristic(), 
            new GraphemeHeuristic(),
            new NgramHeuristic(3)  // trigrams
        };
        
        // Test texts
        String[] texts = {englishText, scrambledText, randomText, caesarText};
        String[] textNames = {"English Text", "Scrambled Text", "Random Text", "Caesar Cipher Text"};
        
        for (int i = 0; i < texts.length; i++) {
            System.out.println("Analyzing: " + textNames[i]);
            System.out.println("Text: \"" + texts[i] + "\"");
            System.out.println();
            
            for (Heuristic heuristic : heuristics) {
                double score = heuristic.analyze(texts[i]);
                System.out.printf("%-25s Score: %.3f - %s%n", 
                                heuristic.getName() + ":", score, heuristic.getSummary());
            }
            System.out.println();
        }
        
        // Test with INPUT files
        testInputFiles(heuristics);
    }
    
    private static void testInputFiles(Heuristic[] heuristics) {
        System.out.println("Testing with INPUT files:");
        System.out.println("========================");
        
        try {
            java.io.File inputDir = new java.io.File("INPUT");
            if (!inputDir.exists()) {
                System.out.println("INPUT directory not found");
                return;
            }
            
            java.io.File[] files = inputDir.listFiles();
            if (files != null) {
                for (java.io.File file : files) {
                    if (file.getName().endsWith(".txt")) {
                        System.out.println("File: " + file.getName());
                        
                        try {
                            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
                            System.out.println("Content: \"" + content.trim() + "\"");
                            
                            for (Heuristic heuristic : heuristics) {
                                double score = heuristic.analyze(content);
                                System.out.printf("%-25s Score: %.3f - %s%n", 
                                                heuristic.getName() + ":", score, heuristic.getSummary());
                            }
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Error reading file: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error accessing INPUT directory: " + e.getMessage());
        }
    }
}