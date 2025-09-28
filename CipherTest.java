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
        System.out.println();
        
        // Test Atbash cipher
        testAtbashCipher();
        System.out.println();
        
        // Test Playfair cipher
        testPlayfairCipher();
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
    
    /**
     * Tests the Atbash cipher
     */
    private static void testAtbashCipher() {
        System.out.println("Testing Atbash Cipher");
        System.out.println("====================");
        
        // Test basic functionality
        System.out.println("Test 1: Basic Atbash cipher test");
        String testText = "hello world";
        AtbashCipher atbash = new AtbashCipher();
        String encrypted = atbash.encrypt(testText);
        String decrypted = atbash.decrypt(encrypted);
        
        System.out.println("Original:  " + testText);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
        System.out.println("Match: " + testText.equals(decrypted));
        System.out.println();
        
        // Test with the README example
        System.out.println("Test 2: Atbash with README example");
        String readmeText = "the quick brown fox jumps over thirteen lazy dogs...";
        String encReadme = atbash.encrypt(readmeText);
        String decReadme = atbash.decrypt(encReadme);
        
        System.out.println("Original:  " + readmeText);
        System.out.println("Encrypted: " + encReadme);
        System.out.println("Decrypted: " + decReadme);
        System.out.println("Match: " + readmeText.equals(decReadme));
        System.out.println();
        
        // Test with full lorem ipsum
        System.out.println("Test 3: Atbash with lorem ipsum");
        String encLorem = atbash.encrypt(LOREM_IPSUM);
        String decLorem = atbash.decrypt(encLorem);
        
        System.out.println("Original:  " + LOREM_IPSUM.substring(0, Math.min(50, LOREM_IPSUM.length())) + "...");
        System.out.println("Encrypted: " + encLorem.substring(0, Math.min(50, encLorem.length())) + "...");
        System.out.println("Decrypted: " + decLorem.substring(0, Math.min(50, decLorem.length())) + "...");
        System.out.println("Match: " + LOREM_IPSUM.equals(decLorem));
        System.out.println();
    }
    
    /**
     * Tests the Playfair cipher with various keys
     */
    private static void testPlayfairCipher() {
        System.out.println("Testing Playfair Cipher");
        System.out.println("======================");
        
        String[] keys = {"keyword", "test", "playfair", "hello"};
        String testText = "hello world test message";
        
        for (String key : keys) {
            System.out.println("Test: Playfair cipher with key '" + key + "'");
            PlayfairCipher playfair = new PlayfairCipher(key);
            String encrypted = playfair.encrypt(testText);
            String decrypted = playfair.decrypt(encrypted);
            
            System.out.println("Key:       " + key);
            System.out.println("Original:  " + testText);
            System.out.println("Encrypted: " + encrypted);
            System.out.println("Decrypted: " + decrypted);
            System.out.println("Match: " + testText.equals(decrypted));
            System.out.println();
        }
        
        // Test with a longer text sample
        System.out.println("Test: Playfair with longer text");
        String longerText = "the quick brown fox jumps over the lazy dog";
        PlayfairCipher playfairLong = new PlayfairCipher("cipher");
        String encLong = playfairLong.encrypt(longerText);
        String decLong = playfairLong.decrypt(encLong);
        
        System.out.println("Key:       cipher");
        System.out.println("Original:  " + longerText);
        System.out.println("Encrypted: " + encLong);
        System.out.println("Decrypted: " + decLong);
        System.out.println("Match: " + longerText.equals(decLong));
        System.out.println();
    }
}