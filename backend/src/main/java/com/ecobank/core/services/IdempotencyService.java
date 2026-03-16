package com.ecobank.core.services;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdempotencyService {

    private final Set<String> processedKeys = ConcurrentHashMap.newKeySet();

    public boolean isProcessed(String key) {
        return processedKeys.contains(key);
    }

    public void markProcessed(String key) {
        processedKeys.add(key);
    }
}

