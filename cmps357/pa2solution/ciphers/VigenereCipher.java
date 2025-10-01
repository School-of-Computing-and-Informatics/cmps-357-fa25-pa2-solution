package cmps357.pa2solution.ciphers;

import cmps357.pa2solution.utils.CipherUtils;

/**
 * Implementation of the Vigenère cipher
 * Uses a repeating key to shift characters by different amounts
 */
public class VigenereCipher {
    
    private String key;
    
    /**
     * Creates a Vigenère cipher with the specified key
     * @param key The key to use for encryption/decryption
     */
    public VigenereCipher(String key) {
        this.key = key;
    }
    
    /**
     * Encrypts text using the Vigenère cipher
     * @param plaintext The text to encrypt
     * @return The encrypted text
     */
    public String encrypt(String plaintext) {
        StringBuilder ciphertext = new StringBuilder();
        int keyIndex = 0;
        
        for (char c : plaintext.toCharArray()) {
            if (c == ' ') {
                // Spaces pass through unchanged
                ciphertext.append(c);
            } else {
                int charIndex = CipherUtils.getAlphabetIndex(c);
                if (charIndex != -1) {
                    // Character is in our alphabet, shift it by the key character
                    char keyChar = key.charAt(keyIndex % key.length());
                    int keyShift = CipherUtils.getAlphabetIndex(keyChar);
                    if (keyShift != -1) {
                        int newIndex = (charIndex + keyShift) % CipherUtils.getAlphabetSize();
                        ciphertext.append(CipherUtils.getAlphabetChar(newIndex));
                        keyIndex++;
                    } else {
                        // Key character not in alphabet, treat as no shift
                        ciphertext.append(c);
                    }
                } else {
                    // Character not in alphabet, pass through unchanged
                    ciphertext.append(c);
                }
            }
        }
        
        return ciphertext.toString();
    }
    
    /**
     * Decrypts text using the Vigenère cipher
     * @param ciphertext The text to decrypt
     * @return The decrypted text
     */
    public String decrypt(String ciphertext) {
        StringBuilder plaintext = new StringBuilder();
        int keyIndex = 0;
        
        for (char c : ciphertext.toCharArray()) {
            if (c == ' ') {
                // Spaces pass through unchanged
                plaintext.append(c);
            } else {
                int charIndex = CipherUtils.getAlphabetIndex(c);
                if (charIndex != -1) {
                    // Character is in our alphabet, shift it back by the key character
                    char keyChar = key.charAt(keyIndex % key.length());
                    int keyShift = CipherUtils.getAlphabetIndex(keyChar);
                    if (keyShift != -1) {
                        int newIndex = (charIndex - keyShift + CipherUtils.getAlphabetSize()) % CipherUtils.getAlphabetSize();
                        plaintext.append(CipherUtils.getAlphabetChar(newIndex));
                        keyIndex++;
                    } else {
                        // Key character not in alphabet, treat as no shift
                        plaintext.append(c);
                    }
                } else {
                    // Character not in alphabet, pass through unchanged
                    plaintext.append(c);
                }
            }
        }
        
        return plaintext.toString();
    }
    
    /**
     * Gets the key for this cipher
     * @return The key string
     */
    public String getKey() {
        return key;
    }
}