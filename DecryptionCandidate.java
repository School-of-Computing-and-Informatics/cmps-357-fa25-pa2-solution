/**
 * Represents a decryption candidate with its score and metadata
 */
public class DecryptionCandidate {
    private String cipherName;
    private String key;
    private String fileName;
    private String cipherText;
    private String decryptedText;
    private double combinedScore;
    private double letterFrequencyScore;
    private double dictionaryScore;
    private double graphemeScore;
    private String summary;
    
    public DecryptionCandidate(String cipherName, String key, String fileName, 
                              String cipherText, String decryptedText) {
        this.cipherName = cipherName;
        this.key = key;
        this.fileName = fileName;
        this.cipherText = cipherText;
        this.decryptedText = decryptedText;
        this.combinedScore = 0.0;
        this.letterFrequencyScore = 0.0;
        this.dictionaryScore = 0.0;
        this.graphemeScore = 0.0;
        this.summary = "";
    }
    
    /**
     * Evaluates this candidate using all available heuristics
     */
    public void evaluate() {
        LetterFrequencyHeuristic letterHeuristic = new LetterFrequencyHeuristic();
        DictionaryHeuristic dictHeuristic = new DictionaryHeuristic();
        GraphemeHeuristic graphemeHeuristic = new GraphemeHeuristic();
        
        this.letterFrequencyScore = letterHeuristic.analyze(decryptedText);
        this.dictionaryScore = dictHeuristic.analyze(decryptedText);
        this.graphemeScore = graphemeHeuristic.analyze(decryptedText);
        
        // Combined score is the average of all heuristics
        this.combinedScore = (letterFrequencyScore + dictionaryScore + graphemeScore) / 3.0;
        
        // Create summary combining all heuristic summaries
        this.summary = String.format("Letter frequency: %.3f (%s), Dictionary: %.3f (%s), Grapheme: %.3f (%s)", 
                                    letterFrequencyScore, letterHeuristic.getSummary(),
                                    dictionaryScore, dictHeuristic.getSummary(),
                                    graphemeScore, graphemeHeuristic.getSummary());
    }
    
    // Getters
    public String getCipherName() { return cipherName; }
    public String getKey() { return key; }
    public String getFileName() { return fileName; }
    public String getCipherText() { return cipherText; }
    public String getDecryptedText() { return decryptedText; }
    public double getCombinedScore() { return combinedScore; }
    public double getLetterFrequencyScore() { return letterFrequencyScore; }
    public double getDictionaryScore() { return dictionaryScore; }
    public double getGraphemeScore() { return graphemeScore; }
    public String getSummary() { return summary; }
    
    @Override
    public String toString() {
        return String.format("%s [%s] %s: %.3f", cipherName, key, fileName, combinedScore);
    }
}