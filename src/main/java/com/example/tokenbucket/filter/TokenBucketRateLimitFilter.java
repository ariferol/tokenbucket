package com.example.tokenbucket.filter;

import com.example.tokenbucket.core.TokenBucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBucketRateLimitFilter extends OncePerRequestFilter {

    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    // Her IP için 10 token kapasite, 5 token / saniye sonra tekrar dolduruluyor
    private static final long CAPACITY = 10;
    private static final long REFILL_TOKENS = 5;
    private static final long REFILL_PERIOD_MS = 1000;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Bu metot, Spring 5 ve üzeri için kullanılabilir
        String clientIp = request.getRemoteAddr();

        TokenBucket tokenBucket = buckets.computeIfAbsent(clientIp,
                ip -> new TokenBucket(CAPACITY, REFILL_TOKENS, REFILL_PERIOD_MS));

        if (tokenBucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded. Try again later.");
        }
    }
}