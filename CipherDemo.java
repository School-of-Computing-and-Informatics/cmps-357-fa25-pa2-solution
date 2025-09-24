/**
 * Demonstration program showing cipher encrypt and decrypt operations
 */
public class CipherDemo {
    
    public static void main(String[] args) {
        System.out.println("Cipher Demonstration");
        System.out.println("===================");
        System.out.println();
        
        // Demonstrate Caesar cipher with the README example
        String plaintext = "the quick brown fox jumps over thirteen lazy dogs...";
        System.out.println("Testing Caesar Cipher (shift 7) with README example:");
        System.out.println("Plaintext: " + plaintext);
        
        CaesarCipher caesar = new CaesarCipher(7);
        String encrypted = caesar.encrypt(plaintext);
        String decrypted = caesar.decrypt(encrypted);
        
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
        System.out.println("Match: " + plaintext.equals(decrypted));
        System.out.println();
        
        // Demonstrate Vigenère cipher
        String vigText = "hello world";
        String vigKey = "key";
        System.out.println("Testing Vigenère Cipher:");
        System.out.println("Plaintext: " + vigText);
        System.out.println("Key: " + vigKey);
        
        VigenereCipher vigenere = new VigenereCipher(vigKey);
        String vigEncrypted = vigenere.encrypt(vigText);
        String vigDecrypted = vigenere.decrypt(vigEncrypted);
        
        System.out.println("Encrypted: " + vigEncrypted);
        System.out.println("Decrypted: " + vigDecrypted);
        System.out.println("Match: " + vigText.equals(vigDecrypted));
        System.out.println();
        
        // Test with alphabet characters
        String alphabetTest = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.:;'!?";
        System.out.println("Testing with full alphabet:");
        System.out.println("Original: " + alphabetTest);
        
        CaesarCipher caesarFull = new CaesarCipher(1);
        String encryptedFull = caesarFull.encrypt(alphabetTest);
        String decryptedFull = caesarFull.decrypt(encryptedFull);
        
        System.out.println("Caesar(1): " + encryptedFull);
        System.out.println("Decrypted: " + decryptedFull);
        System.out.println("Match: " + alphabetTest.equals(decryptedFull));
    }
}