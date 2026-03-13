package com.ecobank.core.services;

import com.ecobank.core.Repository.PlayerRepository;
import com.ecobank.core.models.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class HmacVerificationService {

    private static final String HMAC_ALGO = "HmacSHA256";

    private final PlayerRepository playerRepository;
    private final ObjectMapper objectMapper;

    public HmacVerificationService(
            PlayerRepository playerRepository,
            ObjectMapper objectMapper) {
        this.playerRepository = playerRepository;
        this.objectMapper = objectMapper;
    }

    public void verify(String receivedSignature, Object payload, Long partnerId) {

        Player player = playerRepository.findById(partnerId)
                .orElseThrow(() -> new SecurityException("Invalid partner"));

        String secret = player.getHmacSecret();

        String calculatedSignature = generateHmac(payload, secret);

        if (!calculatedSignature.equals(receivedSignature)) {
            throw new SecurityException("Invalid HMAC signature");
        }
    }

    private String generateHmac(Object payload, String secret) {

        try {
            // Convert request body to JSON
            String payloadJson = objectMapper.writeValueAsString(payload);

            Mac mac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec key =
                    new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);

            mac.init(key);

            byte[] rawHmac =
                    mac.doFinal(payloadJson.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(rawHmac);

        } catch (Exception e) {
            throw new RuntimeException("Failed to verify HMAC", e);
        }
    }
}

