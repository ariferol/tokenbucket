package com.example.tokenbucket.test;

import static org.junit.jupiter.api.Assertions.*;

import com.example.tokenbucket.core.TokenBucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TokenBucketUnitTest {

    private TokenBucket bucket;

    @BeforeEach
    void setup() {
        // Kapasite 5, 1 token/saniye refill
        bucket = new TokenBucket(5, 1, 1000);
        System.out.println("TokenBucket başlatıldı: kapasite=5, doldurmaToken=1, doldurmaPeriyoduMs=1000");
    }

    @Test
    void testConsumeTokens() {
        // Başlangıçta 5 token var, 5 kez çekilebilir
        System.out.println("testConsumeTokens çalıştırılıyor...");
        for (int i = 0; i < 5; i++) {
            boolean result = bucket.tryConsume(1);
            System.out.println("Token çekme denemesi " + (i + 1) + ": " + result);
            assertTrue(result);
        }
        // Artık token yok
        boolean noToken = bucket.tryConsume(1);
        System.out.println("Tüketim sonrası token çekme denemesi: " + noToken);
        assertFalse(noToken);
    }

    @Test
    void testRefillTokens() throws InterruptedException {
        // Tüm tokenlar harcandı
        System.out.println("testRefillTokens çalıştırılıyor...");
        for (int i = 0; i < 5; i++) {
            boolean result = bucket.tryConsume(1);
            System.out.println("Token çekme denemesi " + (i + 1) + ": " + result);
            assertTrue(result);
        }
        boolean noToken = bucket.tryConsume(1);
        System.out.println("Tüketim sonrası token çekme denemesi: " + noToken);
        assertFalse(noToken);

        // 1.5 saniye bekle => 1 tokendan fazla refill olmalı
        System.out.println("1.5 saniye doldurma için bekleniyor...");
        Thread.sleep(1500);

        boolean afterRefill = bucket.tryConsume(1); // refill edilmiş token çekilebilir
        System.out.println("Doldurma sonrası token çekme denemesi: " + afterRefill);
        assertTrue(afterRefill);
    }
}