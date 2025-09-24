/**
 * Utility class for cipher operations
 * Contains the alphabet and common utility functions for cipher implementations
 */
public class CipherUtils {
    
    // The alphabet as specified in the requirements
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.:;'!?";
    
    /**
     * Gets the index of a character in the alphabet
     * @param c The character to find
     * @return The index of the character, or -1 if not found
     */
    public static int getAlphabetIndex(char c) {
        return ALPHABET.indexOf(c);
    }
    
    /**
     * Gets the character at a specific index in the alphabet
     * @param index The index to lookup
     * @return The character at that index
     */
    public static char getAlphabetChar(int index) {
        return ALPHABET.charAt(index);
    }
    
    /**
     * Gets the size of the alphabet
     * @return The size of the alphabet
     */
    public static int getAlphabetSize() {
        return ALPHABET.length();
    }
}