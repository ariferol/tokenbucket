package com.example.tokenbucket.core;

import java.util.concurrent.atomic.AtomicLong;

public class TokenBucket {

    private final long capacity;        // Maks token sayısı (bucket büyüklüğü)
    private final long refillTokens;    // Yenileme miktarı
    private final long refillPeriodMs;  // Yenileme süresi (ms)

    private AtomicLong availableTokens;
    private volatile long lastRefillTimestamp;

    public TokenBucket(long capacity, long refillTokens, long refillPeriodMs) {
        this.capacity = capacity;
        this.refillTokens = refillTokens;
        this.refillPeriodMs = refillPeriodMs;
        this.availableTokens = new AtomicLong(capacity);
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    // Token yenileme işlemi
    private synchronized void refill() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTimestamp;

        if (elapsed > refillPeriodMs) {
            long periods = elapsed / refillPeriodMs;
            long tokensToAdd = periods * refillTokens;

            long newTokenCount = Math.min(capacity, availableTokens.get() + tokensToAdd);
            availableTokens.set(newTokenCount);
            lastRefillTimestamp += periods * refillPeriodMs;
        }
    }

    // Token çekmeye çalış
    public boolean tryConsume(long tokens) {
        refill();

        while (true) {
            long currentTokens = availableTokens.get();
            if (currentTokens < tokens) {
                return false; // Yeterli token yok
            }
            long updatedTokens = currentTokens - tokens;
            if (availableTokens.compareAndSet(currentTokens, updatedTokens)) {
                return true; // Başarıyla token çekildi
            }
            // CAS başarısızsa döngü devam eder
        }
    }
}
