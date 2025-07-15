package com.example.tokenbucket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api")
public class AsyncController {

    private final Executor executor = Executors.newFixedThreadPool(10);

    @GetMapping("/async-task")
    public CompletableFuture<String> asyncTask() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Asenkron Task Tamamlandi";
        }, executor);
    }
}
