/**
 * Implementation of the Atbash cipher
 * A simple substitution cipher that maps each letter to its reverse in the alphabet
 * (a↔z, b↔y, c↔x, etc.)
 */
public class AtbashCipher {
    
    /**
     * Creates an Atbash cipher (no key needed - it's deterministic)
     */
    public AtbashCipher() {
        // No initialization needed - Atbash is deterministic
    }
    
    /**
     * Encrypts text using the Atbash cipher
     * @param plaintext The text to encrypt
     * @return The encrypted text
     */
    public String encrypt(String plaintext) {
        StringBuilder ciphertext = new StringBuilder();
        int alphabetSize = CipherUtils.getAlphabetSize();
        
        for (char c : plaintext.toCharArray()) {
            if (c == ' ') {
                // Spaces pass through unchanged
                ciphertext.append(c);
            } else {
                int index = CipherUtils.getAlphabetIndex(c);
                if (index != -1) {
                    // Character is in our alphabet, apply Atbash transformation
                    int reversedIndex = alphabetSize - 1 - index;
                    ciphertext.append(CipherUtils.getAlphabetChar(reversedIndex));
                } else {
                    // Character not in alphabet, pass through unchanged
                    ciphertext.append(c);
                }
            }
        }
        
        return ciphertext.toString();
    }
    
    /**
     * Decrypts text using the Atbash cipher
     * Note: Atbash is its own inverse, so decrypt is identical to encrypt
     * @param ciphertext The text to decrypt
     * @return The decrypted text
     */
    public String decrypt(String ciphertext) {
        // Atbash is its own inverse - decryption is identical to encryption
        return encrypt(ciphertext);
    }
    
    /**
     * Gets the key for this cipher
     * @return A descriptive string since Atbash has no key
     */
    public String getKey() {
        return "atbash";
    }
}