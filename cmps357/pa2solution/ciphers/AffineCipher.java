package cmps357.pa2solution.ciphers;

import cmps357.pa2solution.utils.CipherUtils;

/**
 * Implementation of the Affine cipher
 * Uses the formula: E(x) = (ax + b) mod m
 * And the decryption formula: D(y) = a^(-1) * (y - b) mod m
 * where a and m must be coprime for the cipher to work
 */
public class AffineCipher {
    
    private int a;  // Multiplicative key
    private int b;  // Additive key
    private int modulus;
    private int aInverse;
    
    /**
     * Creates an Affine cipher with the specified keys
     * @param a The multiplicative key (must be coprime with alphabet size)
     * @param b The additive key
     */
    public AffineCipher(int a, int b) {
        this.modulus = CipherUtils.getAlphabetSize();
        this.a = a;
        this.b = b % modulus;
        
        // Calculate multiplicative inverse of a modulo m
        this.aInverse = modularInverse(a, modulus);
        if (aInverse == -1) {
            throw new IllegalArgumentException("The key 'a' must be coprime with alphabet size " + modulus);
        }
    }
    
    /**
     * Encrypts text using the Affine cipher
     * @param plaintext The text to encrypt
     * @return The encrypted text
     */
    public String encrypt(String plaintext) {
        StringBuilder ciphertext = new StringBuilder();
        
        for (char c : plaintext.toCharArray()) {
            if (c == ' ') {
                // Spaces pass through unchanged
                ciphertext.append(c);
            } else {
                int index = CipherUtils.getAlphabetIndex(c);
                if (index != -1) {
                    // Character is in our alphabet, apply affine transformation
                    int newIndex = (a * index + b) % modulus;
                    ciphertext.append(CipherUtils.getAlphabetChar(newIndex));
                } else {
                    // Character not in alphabet, pass through unchanged
                    ciphertext.append(c);
                }
            }
        }
        
        return ciphertext.toString();
    }
    
    /**
     * Decrypts text using the Affine cipher
     * @param ciphertext The text to decrypt
     * @return The decrypted text
     */
    public String decrypt(String ciphertext) {
        StringBuilder plaintext = new StringBuilder();
        
        for (char c : ciphertext.toCharArray()) {
            if (c == ' ') {
                // Spaces pass through unchanged
                plaintext.append(c);
            } else {
                int index = CipherUtils.getAlphabetIndex(c);
                if (index != -1) {
                    // Character is in our alphabet, apply inverse affine transformation
                    int newIndex = (aInverse * (index - b + modulus)) % modulus;
                    plaintext.append(CipherUtils.getAlphabetChar(newIndex));
                } else {
                    // Character not in alphabet, pass through unchanged
                    plaintext.append(c);
                }
            }
        }
        
        return plaintext.toString();
    }
    
    /**
     * Calculates the modular multiplicative inverse using Extended Euclidean Algorithm
     * @param a The number to find inverse for
     * @param m The modulus
     * @return The multiplicative inverse, or -1 if it doesn't exist
     */
    private int modularInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1;  // Inverse doesn't exist
    }
    
    /**
     * Checks if two numbers are coprime (GCD = 1)
     * @param a First number
     * @param b Second number
     * @return True if the numbers are coprime
     */
    public static boolean areCoprime(int a, int b) {
        return gcd(a, b) == 1;
    }
    
    /**
     * Calculates the Greatest Common Divisor using Euclidean algorithm
     * @param a First number
     * @param b Second number
     * @return The GCD of a and b
     */
    private static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }
    
    /**
     * Gets valid multiplicative keys for the Affine cipher
     * @return Array of valid 'a' values that are coprime with the alphabet size
     */
    public static int[] getValidMultiplicativeKeys() {
        int modulus = CipherUtils.getAlphabetSize();
        java.util.List<Integer> validKeys = new java.util.ArrayList<>();
        
        for (int a = 1; a < modulus; a++) {
            if (areCoprime(a, modulus)) {
                validKeys.add(a);
            }
        }
        
        return validKeys.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * Gets the multiplicative key
     * @return The 'a' key value
     */
    public int getMultiplicativeKey() {
        return a;
    }
    
    /**
     * Gets the additive key
     * @return The 'b' key value
     */
    public int getAdditiveKey() {
        return b;
    }
    
    /**
     * Gets the key as a string representation
     * @return String representation of the key pair (a, b)
     */
    public String getKey() {
        return "(" + a + ", " + b + ")";
    }
}