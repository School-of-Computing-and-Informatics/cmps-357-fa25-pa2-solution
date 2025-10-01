package cmps357.pa2solution;

import cmps357.pa2solution.ciphers.*;

/**
 * File processing example showing how ciphers would work with the INPUT directory
 * This demonstrates the basic framework for the full assignment
 */
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileProcessor {
    
    public static void main(String[] args) {
        System.out.println("File Processing Demonstration");
        System.out.println("============================");
        System.out.println();
        
        try {
            // Process all .txt files in INPUT directory
            Path inputDir = Paths.get("INPUT");
            if (!Files.exists(inputDir)) {
                System.out.println("INPUT directory not found!");
                return;
            }
            
            List<Path> txtFiles = Files.list(inputDir)
                .filter(path -> path.toString().endsWith(".txt"))
                .collect(java.util.stream.Collectors.toList());
                
            for (Path file : txtFiles) {
                System.out.println("Processing file: " + file.getFileName());
                String content = Files.readString(file);
                System.out.println("Original content: " + content);
                
                // Test Caesar cipher encryption/decryption
                CaesarCipher caesar = new CaesarCipher(7);
                String caesarEncrypted = caesar.encrypt(content);
                String caesarDecrypted = caesar.decrypt(caesarEncrypted);
                
                System.out.println("Caesar encrypted: " + caesarEncrypted);
                System.out.println("Caesar decrypted: " + caesarDecrypted);
                System.out.println("Caesar match: " + content.equals(caesarDecrypted));
                
                // Test Vigenère cipher encryption/decryption
                VigenereCipher vigenere = new VigenereCipher("key");
                String vigenereEncrypted = vigenere.encrypt(content);
                String vigenereDecrypted = vigenere.decrypt(vigenereEncrypted);
                
                System.out.println("Vigenère encrypted: " + vigenereEncrypted);
                System.out.println("Vigenère decrypted: " + vigenereDecrypted);
                System.out.println("Vigenère match: " + content.equals(vigenereDecrypted));
                System.out.println();
            }
            
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }
}