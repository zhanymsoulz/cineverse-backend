package com.unime.CineVerse.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class BadgeConfigLoader {

    private final List<BadgeConfig> badgeConfigs;

    public BadgeConfigLoader() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getResourceAsStream("/config/badges.json");
            this.badgeConfigs = objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load badge configs", e);
        }
    }

    public List<BadgeConfig> getBadgeConfigs() {
        return badgeConfigs;
    }
}
