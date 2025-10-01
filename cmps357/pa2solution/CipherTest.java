package cmps357.pa2solution;

import cmps357.pa2solution.ciphers.*;
import cmps357.pa2solution.utils.*;

/**
 * Main class to test cipher implementations
 * Tests Caesar and Vigenère ciphers with various keys and lorem ipsum text
 */
public class CipherTest {
    
    // Sample lorem ipsum text for testing
    private static final String LOREM_IPSUM = "the quick brown fox jumps over thirteen lazy dogs. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    
    public static void main(String[] args) {
        System.out.println("Cipher Implementation Test");
        System.out.println("=========================");
        System.out.println("Alphabet: " + CipherUtils.ALPHABET);
        System.out.println();
        
        // Test Caesar cipher
        testCaesarCipher();
        System.out.println();
        
        // Test Vigenère cipher
        testVigenereCipher();
    }
    
    /**
     * Tests the Caesar cipher with various shift values
     */
    private static void testCaesarCipher() {
        System.out.println("Testing Caesar Cipher");
        System.out.println("====================");
        
        // Test the example from the README (shift 7)
        System.out.println("Test 1: Caesar cipher with shift 7 (from README example)");
        String testText = "the quick brown fox jumps over thirteen lazy dogs...";
        CaesarCipher caesar7 = new CaesarCipher(7);
        String encrypted = caesar7.encrypt(testText);
        String decrypted = caesar7.decrypt(encrypted);
        
        System.out.println("Original:  " + testText);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
        System.out.println("Match: " + testText.equals(decrypted));
        System.out.println();
        
        // Test with different shift values
        int[] shifts = {1, 5, 13, 25};
        for (int shift : shifts) {
            System.out.println("Test: Caesar cipher with shift " + shift);
            CaesarCipher caesar = new CaesarCipher(shift);
            String enc = caesar.encrypt(LOREM_IPSUM);
            String dec = caesar.decrypt(enc);
            
            System.out.println("Original:  " + LOREM_IPSUM.substring(0, Math.min(50, LOREM_IPSUM.length())) + "...");
            System.out.println("Encrypted: " + enc.substring(0, Math.min(50, enc.length())) + "...");
            System.out.println("Decrypted: " + dec.substring(0, Math.min(50, dec.length())) + "...");
            System.out.println("Match: " + LOREM_IPSUM.equals(dec));
            System.out.println();
        }
    }
    
    /**
     * Tests the Vigenère cipher with various keys
     */
    private static void testVigenereCipher() {
        System.out.println("Testing Vigenère Cipher");
        System.out.println("======================");
        
        String[] keys = {"a", "ab", "key", "Bop1", "test"};
        
        for (String key : keys) {
            System.out.println("Test: Vigenère cipher with key '" + key + "'");
            VigenereCipher vigenere = new VigenereCipher(key);
            String encrypted = vigenere.encrypt(LOREM_IPSUM);
            String decrypted = vigenere.decrypt(encrypted);
            
            System.out.println("Key:       " + key);
            System.out.println("Original:  " + LOREM_IPSUM.substring(0, Math.min(50, LOREM_IPSUM.length())) + "...");
            System.out.println("Encrypted: " + encrypted.substring(0, Math.min(50, encrypted.length())) + "...");
            System.out.println("Decrypted: " + decrypted.substring(0, Math.min(50, decrypted.length())) + "...");
            System.out.println("Match: " + LOREM_IPSUM.equals(decrypted));
            System.out.println();
        }
        
        // Special test with a key that contains characters not in our alphabet
        System.out.println("Test: Vigenère cipher with mixed key 'a@b' (@ not in alphabet)");
        VigenereCipher vigenereMixed = new VigenereCipher("a@b");
        String encMixed = vigenereMixed.encrypt("test message");
        String decMixed = vigenereMixed.decrypt(encMixed);
        System.out.println("Original:  test message");
        System.out.println("Encrypted: " + encMixed);
        System.out.println("Decrypted: " + decMixed);
        System.out.println("Match: " + "test message".equals(decMixed));
        System.out.println();
    }
}