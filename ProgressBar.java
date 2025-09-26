/**
 * Progress bar utility for showing cipher search progress
 * Displays a 60-character progress bar with format [====...====]
 */
public class ProgressBar {
    private final String cipherName;
    private final int totalKeys;
    private int testedKeys;
    private long lastUpdateTime;
    private static final int BAR_WIDTH = 60;
    private static final int UPDATE_INTERVAL_MS = 100;
    private static final int KEY_UPDATE_INTERVAL = 1000;
    
    public ProgressBar(String cipherName, int totalKeys) {
        this.cipherName = cipherName;
        this.totalKeys = totalKeys;
        this.testedKeys = 0;
        this.lastUpdateTime = System.currentTimeMillis();
        
        // Print initial progress bar
        updateDisplay();
    }
    
    /**
     * Update progress with number of keys tested
     * Updates display every 1000 keys or every 100ms, whichever comes first
     */
    public void updateProgress(int newTestedKeys) {
        this.testedKeys = newTestedKeys;
        
        long currentTime = System.currentTimeMillis();
        long timeSinceLastUpdate = currentTime - lastUpdateTime;
        
        // Update if 1000 keys tested or 100ms elapsed
        if (testedKeys % KEY_UPDATE_INTERVAL == 0 || timeSinceLastUpdate >= UPDATE_INTERVAL_MS) {
            updateDisplay();
            lastUpdateTime = currentTime;
        }
    }
    
    /**
     * Force update display (used for final update)
     */
    public void forceUpdate() {
        updateDisplay();
    }
    
    private void updateDisplay() {
        double progress = totalKeys > 0 ? (double) testedKeys / totalKeys : 0.0;
        int filledChars = (int) (progress * BAR_WIDTH);
        
        StringBuilder bar = new StringBuilder();
        bar.append("[");
        
        // Add filled portion
        for (int i = 0; i < filledChars; i++) {
            bar.append("=");
        }
        
        // Add empty portion
        for (int i = filledChars; i < BAR_WIDTH; i++) {
            bar.append(".");
        }
        
        bar.append("]");
        
        // Print progress with carriage return to overwrite previous line
        System.out.printf("\r  %s: %s %d/%d (%.1f%%)", 
                         cipherName, bar.toString(), testedKeys, totalKeys, progress * 100);
        System.out.flush();
        
        // If complete, move to next line
        if (testedKeys >= totalKeys) {
            System.out.println();
        }
    }
}