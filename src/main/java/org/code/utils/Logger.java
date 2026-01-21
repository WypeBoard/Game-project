package org.code.utils;

import java.util.HashMap;
import java.util.Map;

public final class Logger {

    private static final boolean DEBUG_ENABLED = true;

    // Track last log time for each key to prevent spam
    private static final Map<String, Long> lastLogTime = new HashMap<>();
    private static final long MIN_LOG_INTERVAL_MS = 1000; // Only log same message once per second

    // Track value changes to only log when something actually changes
    private static final Map<String, String> lastLogValue = new HashMap<>();

    private Logger() {
        // Utility class
    }

    /**
     * Log a debug message (always prints)
     */
    public static void debug(Class<?> clazz, String message) {
        if (DEBUG_ENABLED) {
            System.out.print(printMessage(clazz, message));
        }
    }

    private static String printMessage(Class<?> clazz, String message) {
        return String.format("[DEBUG] [%s] - %s%n", clazz.getSimpleName(), message);
    }

    /**
     * Log only once per second for the same key (prevents spam in game loop)
     */
    public static void debugThrottled(Class<?> clazz, String key, String message) {
        if (!DEBUG_ENABLED) return;

        long now = System.currentTimeMillis();
        Long lastTime = lastLogTime.get(clazz.getSimpleName() + key);

        if (lastTime == null || (now - lastTime) >= MIN_LOG_INTERVAL_MS) {
            System.out.print(printMessage(clazz, message));
            lastLogTime.put(clazz.getSimpleName() + key, now);
        }
    }

    /**
     * Log only when the value changes (perfect for tracking state)
     */
    public static void debugOnChange(Class<?> clazz, String key, Object value) {
        if (!DEBUG_ENABLED) return;

        String valueStr = String.valueOf(value);
        String lastValue = lastLogValue.get(clazz.getSimpleName() + key);

        if (!valueStr.equals(lastValue)) {
            System.out.print(printMessage(clazz, key + ": " + valueStr));
            lastLogValue.put(clazz.getSimpleName() + key, valueStr);
        }
    }

    /**
     * Log only when the value changes, with custom message
     */
    public static void debugOnChange(Class<?> clazz, String key, String message, Object value) {
        if (!DEBUG_ENABLED) return;

        String valueStr = String.valueOf(value);
        String lastValue = lastLogValue.get(clazz.getSimpleName() + key);

        if (!valueStr.equals(lastValue)) {
            System.out.print(printMessage(clazz, message + ": " + valueStr));
            lastLogValue.put(clazz.getSimpleName() + key, valueStr);
        }
    }

    /**
     * Log error message (always prints)
     */
    public static void error(Class<?> clazz, String message) {
        System.err.println(printMessage(clazz, message));
    }

    /**
     * Log error with exception
     */
    public static void error(Class<?> clazz, String message, Exception e) {
        System.err.println(printMessage(clazz, message));
        e.printStackTrace();
    }

    /**
     * Clear all throttle/change tracking (useful when changing states)
     */
    public static void clearTracking() {
        lastLogTime.clear();
        lastLogValue.clear();
    }
}
