package cmps357.pa2solution.utils;

/**
 * Timing utility for tracking elapsed time during cipher processing
 */
public class Timer {
    private long startTime;
    
    /**
     * Start or restart the timer
     */
    public void start() {
        startTime = System.currentTimeMillis();
    }
    
    /**
     * Get elapsed time in milliseconds since start
     */
    public long getElapsedMs() {
        return System.currentTimeMillis() - startTime;
    }
}