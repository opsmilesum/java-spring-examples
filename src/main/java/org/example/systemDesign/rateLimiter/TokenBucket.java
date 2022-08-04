package org.example.systemDesign.rateLimiter;

import java.time.Instant;

public class TokenBucket {
    // tokens/per-sec.
    private int rate;
    private long maxTokens;
    private long currentTokens;
    private Instant lastRefill;

    TokenBucket(int rate, int maxSize) {
        this.rate = rate;
        this.maxTokens = maxSize;
        currentTokens = 0;
        lastRefill = Instant.now();
    }

    private synchronized void refill() {
        Instant now = Instant.now();

        long interval = now.getEpochSecond() - lastRefill.getEpochSecond();
        long needRefill = interval * rate;
        currentTokens = Math.min(currentTokens + needRefill, maxTokens);
        lastRefill = now;
    }

    public synchronized boolean isRequestAllowed(int askTokens) {
        refill();
        if (currentTokens >= askTokens) {
            currentTokens -= askTokens;
            return true;
        }
        return false;
    }
}
