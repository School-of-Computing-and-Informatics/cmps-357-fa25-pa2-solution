import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Main program that performs brute force search across all cipher types
 * and finds the best decryptions for all text files in the INPUT directory
 */
public class BruteForceSearch {
    
    /**
     * Generates all possible Caesar cipher keys (all shifts from 1 to alphabet_size-1)
     */
    public static List<DecryptionCandidate> searchCaesar(String fileName, String cipherText) {
        List<DecryptionCandidate> candidates = new ArrayList<>();
        int alphabetSize = CipherUtils.getAlphabetSize();
        
        for (int shift = 1; shift < alphabetSize; shift++) {
            CaesarCipher caesar = new CaesarCipher(shift);
            String decrypted = caesar.decrypt(cipherText);
            
            DecryptionCandidate candidate = new DecryptionCandidate(
                "Caesar", "shift=" + shift, fileName, cipherText, decrypted);
            candidate.evaluate();
            candidates.add(candidate);
        }
        
        return candidates;
    }
    
    /**
     * Generates all possible Vigenère cipher keys (up to 4 characters as specified)
     */
    public static List<DecryptionCandidate> searchVigenere(String fileName, String cipherText) {
        List<DecryptionCandidate> candidates = new ArrayList<>();
        String alphabet = CipherUtils.ALPHABET;
        
        // Single character keys
        for (char c : alphabet.toCharArray()) {
            String key = String.valueOf(c);
            VigenereCipher vigenere = new VigenereCipher(key);
            String decrypted = vigenere.decrypt(cipherText);
            
            DecryptionCandidate candidate = new DecryptionCandidate(
                "Vigenère", "key=" + key, fileName, cipherText, decrypted);
            candidate.evaluate();
            candidates.add(candidate);
        }
        
        // Two character keys (limited set for performance)
        // Use most common English letters for brute force
        String commonLetters = "etaoinshrdlcumwfgypbvkjxqz";
        for (char c1 : commonLetters.toCharArray()) {
            for (char c2 : commonLetters.toCharArray()) {
                String key = "" + c1 + c2;
                VigenereCipher vigenere = new VigenereCipher(key);
                String decrypted = vigenere.decrypt(cipherText);
                
                DecryptionCandidate candidate = new DecryptionCandidate(
                    "Vigenère", "key=" + key, fileName, cipherText, decrypted);
                candidate.evaluate();
                candidates.add(candidate);
            }
        }
        
        // Three character keys (even more limited for performance)
        String veryCommonLetters = "etaoin";  // Top 6 most common letters
        for (char c1 : veryCommonLetters.toCharArray()) {
            for (char c2 : veryCommonLetters.toCharArray()) {
                for (char c3 : veryCommonLetters.toCharArray()) {
                    String key = "" + c1 + c2 + c3;
                    VigenereCipher vigenere = new VigenereCipher(key);
                    String decrypted = vigenere.decrypt(cipherText);
                    
                    DecryptionCandidate candidate = new DecryptionCandidate(
                        "Vigenère", "key=" + key, fileName, cipherText, decrypted);
                    candidate.evaluate();
                    candidates.add(candidate);
                }
            }
        }
        
        // Four character keys (very limited for performance)
        String topLetters = "eta";  // Top 3 most common letters
        for (char c1 : topLetters.toCharArray()) {
            for (char c2 : topLetters.toCharArray()) {
                for (char c3 : topLetters.toCharArray()) {
                    for (char c4 : topLetters.toCharArray()) {
                        String key = "" + c1 + c2 + c3 + c4;
                        VigenereCipher vigenere = new VigenereCipher(key);
                        String decrypted = vigenere.decrypt(cipherText);
                        
                        DecryptionCandidate candidate = new DecryptionCandidate(
                            "Vigenère", "key=" + key, fileName, cipherText, decrypted);
                        candidate.evaluate();
                        candidates.add(candidate);
                    }
                }
            }
        }
        
        return candidates;
    }
    
    /**
     * Generates all possible Affine cipher keys
     */
    public static List<DecryptionCandidate> searchAffine(String fileName, String cipherText) {
        List<DecryptionCandidate> candidates = new ArrayList<>();
        int[] validAKeys = AffineCipher.getValidMultiplicativeKeys();
        int alphabetSize = CipherUtils.getAlphabetSize();
        
        for (int a : validAKeys) {
            for (int b = 0; b < alphabetSize; b++) {
                try {
                    AffineCipher affine = new AffineCipher(a, b);
                    String decrypted = affine.decrypt(cipherText);
                    
                    DecryptionCandidate candidate = new DecryptionCandidate(
                        "Affine", affine.getKey(), fileName, cipherText, decrypted);
                    candidate.evaluate();
                    candidates.add(candidate);
                } catch (IllegalArgumentException e) {
                    // Skip invalid key combinations
                }
            }
        }
        
        return candidates;
    }
    
    /**
     * Process a single file with all cipher types
     */
    public static List<DecryptionCandidate> processFile(String fileName, String content) {
        System.out.println("Processing file: " + fileName + " (length: " + content.length() + ")");
        
        List<DecryptionCandidate> allCandidates = new ArrayList<>();
        
        // Search with Caesar cipher
        System.out.print("  Searching Caesar cipher...");
        List<DecryptionCandidate> caesarResults = searchCaesar(fileName, content);
        allCandidates.addAll(caesarResults);
        System.out.println(" " + caesarResults.size() + " candidates");
        
        // Search with Vigenère cipher
        System.out.print("  Searching Vigenère cipher...");
        List<DecryptionCandidate> vigenereResults = searchVigenere(fileName, content);
        allCandidates.addAll(vigenereResults);
        System.out.println(" " + vigenereResults.size() + " candidates");
        
        // Search with Affine cipher
        System.out.print("  Searching Affine cipher...");
        List<DecryptionCandidate> affineResults = searchAffine(fileName, content);
        allCandidates.addAll(affineResults);
        System.out.println(" " + affineResults.size() + " candidates");
        
        return allCandidates;
    }
    
    /**
     * Export the top results to output.md in the specified format
     */
    public static void exportResults(List<DecryptionCandidate> topResults) {
        try {
            PrintWriter writer = new PrintWriter("output.md");
            
            // Group results by filename
            Map<String, List<DecryptionCandidate>> resultsByFile = new LinkedHashMap<>();
            for (DecryptionCandidate candidate : topResults) {
                String fileName = candidate.getFileName();
                resultsByFile.computeIfAbsent(fileName, k -> new ArrayList<>()).add(candidate);
            }
            
            // Write results organized by file
            for (Map.Entry<String, List<DecryptionCandidate>> entry : resultsByFile.entrySet()) {
                String fileName = entry.getKey();
                List<DecryptionCandidate> candidates = entry.getValue();
                
                writer.println("# " + fileName);
                writer.println();
                
                int decryptionNum = 1;
                for (DecryptionCandidate candidate : candidates) {
                    writer.println("## Decryption " + decryptionNum + ": " + candidate.getCipherName() + " Cipher");
                    writer.println("- Cipher: " + candidate.getCipherName());
                    writer.println("- Key: " + candidate.getKey());
                    writer.println("- Combined Score: " + String.format("%.3f", candidate.getCombinedScore()));
                    writer.println("- Letter Frequency Score: " + String.format("%.3f", candidate.getLetterFrequencyScore()));
                    writer.println("- Dictionary Score: " + String.format("%.3f", candidate.getDictionaryScore()));
                    writer.println("- Grapheme Score: " + String.format("%.3f", candidate.getGraphemeScore()));
                    writer.println("- Summary: " + candidate.getSummary());
                    writer.println();
                    writer.println("```");
                    writer.println(candidate.getDecryptedText());
                    writer.println("```");
                    writer.println();
                    
                    decryptionNum++;
                }
            }
            
            writer.close();
            System.out.println("\nResults exported to output.md");
            
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Brute Force Cipher Search");
        System.out.println("=========================");
        System.out.println();
        
        try {
            // Find all .txt files in INPUT directory
            Path inputDir = Paths.get("INPUT");
            if (!Files.exists(inputDir)) {
                System.out.println("INPUT directory not found!");
                return;
            }
            
            List<Path> txtFiles = Files.list(inputDir)
                .filter(path -> path.toString().endsWith(".txt"))
                .sorted()
                .collect(java.util.stream.Collectors.toList());
                
            if (txtFiles.isEmpty()) {
                System.out.println("No .txt files found in INPUT directory!");
                return;
            }
            
            System.out.println("Found " + txtFiles.size() + " text files");
            
            List<DecryptionCandidate> allCandidates = new ArrayList<>();
            
            // Process each file
            for (Path file : txtFiles) {
                String fileName = file.getFileName().toString();
                String content = Files.readString(file);
                
                List<DecryptionCandidate> fileCandidates = processFile(fileName, content);
                allCandidates.addAll(fileCandidates);
            }
            
            System.out.println("\nTotal candidates generated: " + allCandidates.size());
            
            // Sort by combined score (highest first)
            allCandidates.sort((a, b) -> Double.compare(b.getCombinedScore(), a.getCombinedScore()));
            
            // Get top 5 results
            List<DecryptionCandidate> topResults = allCandidates.subList(0, Math.min(5, allCandidates.size()));
            
            System.out.println("\nTop 5 decryption candidates:");
            for (int i = 0; i < topResults.size(); i++) {
                DecryptionCandidate candidate = topResults.get(i);
                System.out.printf("%d. %s [%s] %s: %.3f\n", 
                                 i + 1, candidate.getCipherName(), candidate.getKey(), 
                                 candidate.getFileName(), candidate.getCombinedScore());
            }
            
            // Export results
            exportResults(topResults);
            
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }
}