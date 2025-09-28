/**
 * Implementation of the Playfair cipher
 * Uses a 5x5 key square for encryption/decryption
 * Note: This implementation uses a simplified approach suitable for brute force attacks
 * by limiting the key space to common letter combinations
 */
public class PlayfairCipher {
    
    private String key;
    private char[][] keySquare;
    private static final int SQUARE_SIZE = 5;
    
    /**
     * Creates a Playfair cipher with the specified key
     * @param key The key to generate the 5x5 square
     */
    public PlayfairCipher(String key) {
        this.key = key;
        this.keySquare = generateKeySquare(key);
    }
    
    /**
     * Generates a 5x5 key square from the given key
     * Uses only lowercase letters a-y (j is combined with i for traditional Playfair)
     */
    private char[][] generateKeySquare(String key) {
        char[][] square = new char[SQUARE_SIZE][SQUARE_SIZE];
        boolean[] used = new boolean[26]; // Track used letters (a-z)
        int row = 0, col = 0;
        
        // First add unique letters from the key
        for (char c : key.toLowerCase().toCharArray()) {
            if (c >= 'a' && c <= 'z' && c != 'j' && !used[c - 'a']) {
                square[row][col] = c;
                used[c - 'a'] = true;
                col++;
                if (col == SQUARE_SIZE) {
                    col = 0;
                    row++;
                }
                if (row == SQUARE_SIZE) break;
            }
        }
        
        // Fill remaining positions with unused letters (skip j)
        for (char c = 'a'; c <= 'z' && row < SQUARE_SIZE; c++) {
            if (c != 'j' && !used[c - 'a']) {
                square[row][col] = c;
                used[c - 'a'] = true;
                col++;
                if (col == SQUARE_SIZE) {
                    col = 0;
                    row++;
                }
            }
        }
        
        return square;
    }
    
    /**
     * Finds the position of a character in the key square
     */
    private int[] findPosition(char c) {
        c = Character.toLowerCase(c);
        if (c == 'j') c = 'i'; // j maps to i in traditional Playfair
        
        for (int row = 0; row < SQUARE_SIZE; row++) {
            for (int col = 0; col < SQUARE_SIZE; col++) {
                if (keySquare[row][col] == c) {
                    return new int[]{row, col};
                }
            }
        }
        return null; // Character not found in square
    }
    
    /**
     * Encrypts text using the Playfair cipher
     * @param plaintext The text to encrypt
     * @return The encrypted text
     */
    public String encrypt(String plaintext) {
        StringBuilder result = new StringBuilder();
        StringBuilder letters = new StringBuilder();
        
        // Extract only letters for Playfair processing
        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                letters.append(Character.toLowerCase(c));
            }
        }
        
        // Process pairs of letters
        String letterString = letters.toString();
        for (int i = 0; i < letterString.length(); i += 2) {
            char first = letterString.charAt(i);
            char second = (i + 1 < letterString.length()) ? letterString.charAt(i + 1) : 'x';
            
            // If the two letters are the same, insert 'x'
            if (first == second) {
                second = 'x';
                i--; // Process the repeated letter in the next iteration
            }
            
            String encrypted = encryptPair(first, second);
            result.append(encrypted);
        }
        
        // Now reconstruct the text preserving original structure
        StringBuilder finalResult = new StringBuilder();
        int letterIndex = 0;
        String encryptedLetters = result.toString();
        
        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                if (letterIndex < encryptedLetters.length()) {
                    char encryptedChar = encryptedLetters.charAt(letterIndex);
                    // Preserve original case
                    if (Character.isUpperCase(c)) {
                        encryptedChar = Character.toUpperCase(encryptedChar);
                    }
                    finalResult.append(encryptedChar);
                    letterIndex++;
                }
            } else {
                // Non-letter characters (spaces, punctuation) pass through
                finalResult.append(c);
            }
        }
        
        return finalResult.toString();
    }
    
    /**
     * Encrypts a pair of letters using Playfair rules
     */
    private String encryptPair(char first, char second) {
        int[] pos1 = findPosition(first);
        int[] pos2 = findPosition(second);
        
        if (pos1 == null || pos2 == null) {
            // If characters not in square, return as-is
            return "" + first + second;
        }
        
        int row1 = pos1[0], col1 = pos1[1];
        int row2 = pos2[0], col2 = pos2[1];
        
        if (row1 == row2) {
            // Same row: move right, wrap around
            col1 = (col1 + 1) % SQUARE_SIZE;
            col2 = (col2 + 1) % SQUARE_SIZE;
        } else if (col1 == col2) {
            // Same column: move down, wrap around
            row1 = (row1 + 1) % SQUARE_SIZE;
            row2 = (row2 + 1) % SQUARE_SIZE;
        } else {
            // Rectangle: swap columns
            int temp = col1;
            col1 = col2;
            col2 = temp;
        }
        
        return "" + keySquare[row1][col1] + keySquare[row2][col2];
    }
    
    /**
     * Decrypts text using the Playfair cipher
     * @param ciphertext The text to decrypt
     * @return The decrypted text
     */
    public String decrypt(String ciphertext) {
        StringBuilder result = new StringBuilder();
        StringBuilder letters = new StringBuilder();
        
        // Extract only letters for Playfair processing
        for (char c : ciphertext.toCharArray()) {
            if (Character.isLetter(c)) {
                letters.append(Character.toLowerCase(c));
            }
        }
        
        // Process pairs of letters
        String letterString = letters.toString();
        for (int i = 0; i < letterString.length(); i += 2) {
            char first = letterString.charAt(i);
            char second = (i + 1 < letterString.length()) ? letterString.charAt(i + 1) : 'x';
            
            String decrypted = decryptPair(first, second);
            result.append(decrypted);
        }
        
        // Now reconstruct the text preserving original structure
        StringBuilder finalResult = new StringBuilder();
        int letterIndex = 0;
        String decryptedLetters = result.toString();
        
        for (char c : ciphertext.toCharArray()) {
            if (Character.isLetter(c)) {
                if (letterIndex < decryptedLetters.length()) {
                    char decryptedChar = decryptedLetters.charAt(letterIndex);
                    // Preserve original case
                    if (Character.isUpperCase(c)) {
                        decryptedChar = Character.toUpperCase(decryptedChar);
                    }
                    finalResult.append(decryptedChar);
                    letterIndex++;
                }
            } else {
                // Non-letter characters (spaces, punctuation) pass through
                finalResult.append(c);
            }
        }
        
        return finalResult.toString();
    }
    
    /**
     * Decrypts a pair of letters using Playfair rules (reverse of encryption)
     */
    private String decryptPair(char first, char second) {
        int[] pos1 = findPosition(first);
        int[] pos2 = findPosition(second);
        
        if (pos1 == null || pos2 == null) {
            // If characters not in square, return as-is
            return "" + first + second;
        }
        
        int row1 = pos1[0], col1 = pos1[1];
        int row2 = pos2[0], col2 = pos2[1];
        
        if (row1 == row2) {
            // Same row: move left, wrap around
            col1 = (col1 - 1 + SQUARE_SIZE) % SQUARE_SIZE;
            col2 = (col2 - 1 + SQUARE_SIZE) % SQUARE_SIZE;
        } else if (col1 == col2) {
            // Same column: move up, wrap around
            row1 = (row1 - 1 + SQUARE_SIZE) % SQUARE_SIZE;
            row2 = (row2 - 1 + SQUARE_SIZE) % SQUARE_SIZE;
        } else {
            // Rectangle: swap columns (same as encryption)
            int temp = col1;
            col1 = col2;
            col2 = temp;
        }
        
        return "" + keySquare[row1][col1] + keySquare[row2][col2];
    }
    
    /**
     * Gets the key for this cipher
     * @return The key string
     */
    public String getKey() {
        return key;
    }
}