package org.example.systemDesign.rateLimiter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TokenBucketTest {

    @Test
    void exceeding_bucket() throws InterruptedException {
        TokenBucket tokenBucket = new TokenBucket(5, 10);
        // 0s - 0.
        Assertions.assertFalse(tokenBucket.isRequestAllowed(1));
        Thread.sleep(1000);
        // 1s - 5.
        Assertions.assertTrue(tokenBucket.isRequestAllowed(2));
        // 1s - 3.
        Assertions.assertFalse(tokenBucket.isRequestAllowed(4));
        // 1s - 3.
        Thread.sleep(1000);
        // 2s - 8.
        Assertions.assertTrue(tokenBucket.isRequestAllowed(8));
    }
}