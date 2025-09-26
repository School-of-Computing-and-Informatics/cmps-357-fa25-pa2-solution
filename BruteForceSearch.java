import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main program that performs brute force search across all cipher types
 * and finds the best decryptions for all text files in the INPUT directory
 */
public class BruteForceSearch {
    /**
     * Returns a string with the timing rounded to the largest appropriate unit:
     * nearest 10 ms, 1 s, 30 s, 2 min, or 10 min (whichever is largest for the value)
     */
    private static String approximateTime(long ms) {
        if (ms >= 10 * 60 * 1000) { // 10 min
            long rounded = Math.round((double) ms / (10 * 60 * 1000)) * 10;
            return rounded + " min";
        } else if (ms >= 2 * 60 * 1000) { // 2 min
            long rounded = Math.round((double) ms / (2 * 60 * 1000)) * 2;
            return rounded + " min";
        } else if (ms >= 30 * 1000) { // 30 s
            long rounded = Math.round((double) ms / (30 * 1000)) * 30;
            return rounded + " s";
        } else if (ms >= 1000) { // 1 s
            long rounded = Math.round((double) ms / 1000);
            return rounded + " s";
        } else if (ms >= 10) { // 10 ms
            long rounded = Math.round((double) ms / 10) * 10;
            return rounded + " ms";
        } else {
            return ms + " ms";
        }
    }
    
    /**
     * Generates all possible Caesar cipher keys (all shifts from 1 to alphabet_size-1)
     */
    public static List<DecryptionCandidate> searchCaesar(String fileName, String cipherText) {
        List<DecryptionCandidate> candidates = new ArrayList<>();
        int alphabetSize = CipherUtils.getAlphabetSize();
        int totalKeys = alphabetSize - 1;
        
        ProgressBar progressBar = new ProgressBar("Caesar", totalKeys);
        
        for (int shift = 1; shift < alphabetSize; shift++) {
            CaesarCipher caesar = new CaesarCipher(shift);
            String decrypted = caesar.decrypt(cipherText);
            
            DecryptionCandidate candidate = new DecryptionCandidate(
                "Caesar", "shift=" + shift, fileName, cipherText, decrypted);
            candidate.evaluate();
            candidates.add(candidate);
            
            // Update progress
            progressBar.updateProgress(shift);
        }
        
        progressBar.forceUpdate();
        return candidates;
    }
    
    /**
     * Generates all possible Vigenère cipher keys (up to 4 characters as specified)
     */
    public static List<DecryptionCandidate> searchVigenere(String fileName, String cipherText) {
        return searchVigenere(fileName, cipherText, 1); // Use single thread by default for compatibility
    }
    
    /**
     * Generates all possible Vigenère cipher keys (up to 4 characters as specified)
     * Parallelized version that uses multiple threads
     */
    public static List<DecryptionCandidate> searchVigenere(String fileName, String cipherText, int numThreads) {
        List<DecryptionCandidate> candidates = Collections.synchronizedList(new ArrayList<>());
        String alphabet = CipherUtils.ALPHABET;
        String commonLetters = "etaoinshrdlcumwfgypbvkjxqz";
        
        // Calculate total keys
        int totalKeys = alphabet.length() + // Single character
                       (commonLetters.length() * commonLetters.length()) + // Two character
                       (commonLetters.length() * commonLetters.length() * commonLetters.length()) + // Three character
                       (commonLetters.length() * commonLetters.length() * commonLetters.length() * commonLetters.length()); // Four character
        
        ProgressBar progressBar = new ProgressBar("Vigenère", totalKeys);
        AtomicInteger testedKeys = new AtomicInteger(0);
        
        // Create thread pool
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Void>> futures = new ArrayList<>();
        
        // Single character keys - distribute work across threads
        int alphabetSize = alphabet.length();
        int singleCharBatchSize = Math.max(1, alphabetSize / numThreads);
        
        for (int threadId = 0; threadId < numThreads; threadId++) {
            final int startIdx = threadId * singleCharBatchSize;
            final int endIdx = (threadId == numThreads - 1) ? alphabetSize : Math.min((threadId + 1) * singleCharBatchSize, alphabetSize);
            
            if (startIdx < endIdx) {
                futures.add(executor.submit(() -> {
                    for (int i = startIdx; i < endIdx; i++) {
                        char c = alphabet.charAt(i);
                        String key = String.valueOf(c);
                        VigenereCipher vigenere = new VigenereCipher(key);
                        String decrypted = vigenere.decrypt(cipherText);
                        
                        DecryptionCandidate candidate = new DecryptionCandidate(
                            "Vigenère", "key=" + key, fileName, cipherText, decrypted);
                        candidate.evaluate();
                        candidates.add(candidate);
                        
                        progressBar.updateProgress(testedKeys.incrementAndGet());
                    }
                    return null;
                }));
            }
        }
        
        // Two character keys - distribute work across threads
        int twoCharTotal = commonLetters.length() * commonLetters.length();
        int twoCharBatchSize = Math.max(1, twoCharTotal / numThreads);
        
        for (int threadId = 0; threadId < numThreads; threadId++) {
            final int startIdx = threadId * twoCharBatchSize;
            final int endIdx = (threadId == numThreads - 1) ? twoCharTotal : Math.min((threadId + 1) * twoCharBatchSize, twoCharTotal);
            
            if (startIdx < endIdx) {
                futures.add(executor.submit(() -> {
                    for (int idx = startIdx; idx < endIdx; idx++) {
                        int c1Idx = idx / commonLetters.length();
                        int c2Idx = idx % commonLetters.length();
                        
                        char c1 = commonLetters.charAt(c1Idx);
                        char c2 = commonLetters.charAt(c2Idx);
                        String key = "" + c1 + c2;
                        
                        VigenereCipher vigenere = new VigenereCipher(key);
                        String decrypted = vigenere.decrypt(cipherText);
                        
                        DecryptionCandidate candidate = new DecryptionCandidate(
                            "Vigenère", "key=" + key, fileName, cipherText, decrypted);
                        candidate.evaluate();
                        candidates.add(candidate);
                        
                        progressBar.updateProgress(testedKeys.incrementAndGet());
                    }
                    return null;
                }));
            }
        }
        
        // Three character keys - distribute work across threads
        int threeCharTotal = commonLetters.length() * commonLetters.length() * commonLetters.length();
        int threeCharBatchSize = Math.max(1, threeCharTotal / numThreads);
        
        for (int threadId = 0; threadId < numThreads; threadId++) {
            final int startIdx = threadId * threeCharBatchSize;
            final int endIdx = (threadId == numThreads - 1) ? threeCharTotal : Math.min((threadId + 1) * threeCharBatchSize, threeCharTotal);
            
            if (startIdx < endIdx) {
                futures.add(executor.submit(() -> {
                    for (int idx = startIdx; idx < endIdx; idx++) {
                        int c1Idx = idx / (commonLetters.length() * commonLetters.length());
                        int c2Idx = (idx / commonLetters.length()) % commonLetters.length();
                        int c3Idx = idx % commonLetters.length();
                        
                        char c1 = commonLetters.charAt(c1Idx);
                        char c2 = commonLetters.charAt(c2Idx);
                        char c3 = commonLetters.charAt(c3Idx);
                        String key = "" + c1 + c2 + c3;
                        
                        VigenereCipher vigenere = new VigenereCipher(key);
                        String decrypted = vigenere.decrypt(cipherText);
                        
                        DecryptionCandidate candidate = new DecryptionCandidate(
                            "Vigenère", "key=" + key, fileName, cipherText, decrypted);
                        candidate.evaluate();
                        candidates.add(candidate);
                        
                        progressBar.updateProgress(testedKeys.incrementAndGet());
                    }
                    return null;
                }));
            }
        }
        
        // Four character keys - distribute work across threads
        int fourCharTotal = commonLetters.length() * commonLetters.length() * commonLetters.length() * commonLetters.length();
        int fourCharBatchSize = Math.max(1, fourCharTotal / numThreads);
        
        for (int threadId = 0; threadId < numThreads; threadId++) {
            final int startIdx = threadId * fourCharBatchSize;
            final int endIdx = (threadId == numThreads - 1) ? fourCharTotal : Math.min((threadId + 1) * fourCharBatchSize, fourCharTotal);
            
            if (startIdx < endIdx) {
                futures.add(executor.submit(() -> {
                    for (int idx = startIdx; idx < endIdx; idx++) {
                        int c1Idx = idx / (commonLetters.length() * commonLetters.length() * commonLetters.length());
                        int c2Idx = (idx / (commonLetters.length() * commonLetters.length())) % commonLetters.length();
                        int c3Idx = (idx / commonLetters.length()) % commonLetters.length();
                        int c4Idx = idx % commonLetters.length();
                        
                        char c1 = commonLetters.charAt(c1Idx);
                        char c2 = commonLetters.charAt(c2Idx);
                        char c3 = commonLetters.charAt(c3Idx);
                        char c4 = commonLetters.charAt(c4Idx);
                        String key = "" + c1 + c2 + c3 + c4;
                        
                        VigenereCipher vigenere = new VigenereCipher(key);
                        String decrypted = vigenere.decrypt(cipherText);
                        
                        DecryptionCandidate candidate = new DecryptionCandidate(
                            "Vigenère", "key=" + key, fileName, cipherText, decrypted);
                        candidate.evaluate();
                        candidates.add(candidate);
                        
                        progressBar.updateProgress(testedKeys.incrementAndGet());
                    }
                    return null;
                }));
            }
        }
        
        // Wait for all tasks to complete
        try {
            for (Future<Void> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error during parallel execution: " + e.getMessage());
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        
        progressBar.forceUpdate();
        return new ArrayList<>(candidates);
    }
    
    /**
     * Generates all possible Affine cipher keys
     */
    public static List<DecryptionCandidate> searchAffine(String fileName, String cipherText) {
        List<DecryptionCandidate> candidates = new ArrayList<>();
        int[] validAKeys = AffineCipher.getValidMultiplicativeKeys();
        int alphabetSize = CipherUtils.getAlphabetSize();
        
        int totalKeys = validAKeys.length * alphabetSize;
        ProgressBar progressBar = new ProgressBar("Affine", totalKeys);
        int testedKeys = 0;
        
        for (int a : validAKeys) {
            for (int b = 0; b < alphabetSize; b++) {
                try {
                    AffineCipher affine = new AffineCipher(a, b);
                    String decrypted = affine.decrypt(cipherText);
                    
                    DecryptionCandidate candidate = new DecryptionCandidate(
                        "Affine", affine.getKey(), fileName, cipherText, decrypted);
                    candidate.evaluate();
                    candidates.add(candidate);
                    
                    progressBar.updateProgress(++testedKeys);
                } catch (IllegalArgumentException e) {
                    // Skip invalid key combinations
                    testedKeys++;
                    progressBar.updateProgress(testedKeys);
                }
            }
        }
        
        progressBar.forceUpdate();
        return candidates;
    }
    
    /**
     * Process a single file with all cipher types
     */
    public static List<DecryptionCandidate> processFile(String fileName, String content, Map<String, Long> fileTimings, int numThreads) {
        System.out.println("Processing file: " + fileName + " (length: " + content.length() + ")");
        
        Timer fileTimer = new Timer();
        fileTimer.start();
        
        List<DecryptionCandidate> allCandidates = new ArrayList<>();
        
        // Search with Caesar cipher
        List<DecryptionCandidate> caesarResults = searchCaesar(fileName, content);
        allCandidates.addAll(caesarResults);
        
        // Search with Vigenère cipher (parallelized)
        List<DecryptionCandidate> vigenereResults = searchVigenere(fileName, content, numThreads);
        allCandidates.addAll(vigenereResults);
        
        // Search with Affine cipher
        List<DecryptionCandidate> affineResults = searchAffine(fileName, content);
        allCandidates.addAll(affineResults);
        
        long elapsedMs = fileTimer.getElapsedMs();
        fileTimings.put(fileName, elapsedMs);
        
        System.out.println("  Completed in " + elapsedMs + "ms. Total candidates: " + allCandidates.size());
        
        return allCandidates;
    }
    
    /**
     * Export the results for each file to output.md in the specified format
     */
    public static void exportResults(Map<String, List<DecryptionCandidate>> resultsByFile, 
                                   Map<String, Long> fileTimings, long totalElapsedMs) {
        try {
            PrintWriter writer = new PrintWriter("output.md");
            
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
            
            // Add timing summary
            writer.println("# Summary");
            writer.println();
            writer.println("## Timing Results");
            writer.println();
            writer.println("| File Name | Time (ms) | Approximate |");
            writer.println("|-----------|-----------|-------------|");
            int totalFiles = fileTimings.size();
            for (Map.Entry<String, Long> entry : fileTimings.entrySet()) {
                writer.printf("| %s | %d | %s |%n", entry.getKey(), entry.getValue(), approximateTime(entry.getValue()));
            }
            writer.println("| **Total Files** | **" + totalFiles + "** |   |");
            writer.printf("| **Total Elapsed (ms)** | **%d** | **%s** |%n", totalElapsedMs, approximateTime(totalElapsedMs));
            
            writer.close();
            System.out.println("\nResults exported to output.md");
            
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        // Parse command line arguments
        int numThreads = 4; // default value
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--threads") && i + 1 < args.length) {
                try {
                    numThreads = Integer.parseInt(args[i + 1]);
                    if (numThreads < 1) {
                        System.err.println("Number of threads must be at least 1");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number of threads: " + args[i + 1]);
                    return;
                }
            }
        }
        
        System.out.println("Brute Force Cipher Search");
        System.out.println("=========================");
        System.out.println("Using " + numThreads + " threads for parallel processing");
        System.out.println();
        
        Timer totalTimer = new Timer();
        totalTimer.start(); // Record t_total_start
        
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
            
            // Process each file separately and keep track of top 5 results per file
            Map<String, List<DecryptionCandidate>> resultsByFile = new LinkedHashMap<>();
            Map<String, Long> fileTimings = new LinkedHashMap<>();
            int totalCandidates = 0;
            
            // Process each file
            for (Path file : txtFiles) {
                String fileName = file.getFileName().toString();
                String content = Files.readString(file);
                
                List<DecryptionCandidate> fileCandidates = processFile(fileName, content, fileTimings, numThreads);
                totalCandidates += fileCandidates.size();
                
                // Sort candidates for this file by combined score (highest first)
                fileCandidates.sort((a, b) -> Double.compare(b.getCombinedScore(), a.getCombinedScore()));
                
                // Get top 5 results for this file
                List<DecryptionCandidate> topFileResults = fileCandidates.subList(0, Math.min(5, fileCandidates.size()));
                resultsByFile.put(fileName, topFileResults);
                
                System.out.println("  Top 5 results for " + fileName + ":");
                for (int i = 0; i < topFileResults.size(); i++) {
                    DecryptionCandidate candidate = topFileResults.get(i);
                    System.out.printf("    %d. %s [%s]: %.3f\n", 
                                     i + 1, candidate.getCipherName(), candidate.getKey(), 
                                     candidate.getCombinedScore());
                }
                System.out.println();
            }
            
            long totalElapsedMs = totalTimer.getElapsedMs(); // Record t_total_end
            
            System.out.println("Total candidates generated: " + totalCandidates);
            System.out.println("Total execution time: " + totalElapsedMs + "ms");
            
            // Export results organized by file with timing data
            exportResults(resultsByFile, fileTimings, totalElapsedMs);
            
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }
}