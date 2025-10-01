package cmps357.pa2solution.ciphers;

import cmps357.pa2solution.utils.CipherUtils;

/**
 * Implementation of the Caesar cipher
 * Shifts each character by a fixed amount in the alphabet
 */
public class CaesarCipher {
    
    private int shift;
    
    /**
     * Creates a Caesar cipher with the specified shift value
     * @param shift The number of positions to shift each character
     */
    public CaesarCipher(int shift) {
        this.shift = shift;
    }
    
    /**
     * Encrypts text using the Caesar cipher
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
                    // Character is in our alphabet, shift it
                    int newIndex = (index + shift) % CipherUtils.getAlphabetSize();
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
     * Decrypts text using the Caesar cipher
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
                    // Character is in our alphabet, shift it back
                    int newIndex = (index - shift + CipherUtils.getAlphabetSize()) % CipherUtils.getAlphabetSize();
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
     * Gets the shift value for this cipher
     * @return The shift value
     */
    public int getShift() {
        return shift;
    }
}