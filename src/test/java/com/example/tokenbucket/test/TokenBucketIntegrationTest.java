package com.example.tokenbucket.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

//@SpringBootTestx
@SpringBootTest(classes = com.example.tokenbucket.TokenBucketApplication.class)
@AutoConfigureMockMvc
public class TokenBucketIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRateLimit() throws Exception {
        /*Gerçek yük testini Postman, JMeter gibi tool lar ile yapsaniz daha iyi olur*/
        // 10 istek yapılabilir (bucket kapasitesi)
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/api/async-task"))
                    .andExpect(status().isOk());
        }

        // 11. istek rate limit tarafından engellenmeli
        mockMvc.perform(get("/api/async-task"))
                .andExpect(status().isTooManyRequests());
    }
}