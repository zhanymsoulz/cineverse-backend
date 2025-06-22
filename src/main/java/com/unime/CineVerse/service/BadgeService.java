package com.unime.CineVerse.service;

import com.unime.CineVerse.config.BadgeConfig;
import com.unime.CineVerse.config.BadgeConfigLoader;
import com.unime.CineVerse.model.Badge;
import com.unime.CineVerse.model.UserBadge;
import com.unime.CineVerse.model.Users;
import com.unime.CineVerse.repository.BadgeRepository;
import com.unime.CineVerse.repository.UserBadgeRepository;

import org.apache.commons.jexl3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BadgeService {

    @Autowired
    private BadgeConfigLoader badgeConfigLoader;

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private UserBadgeRepository userBadgeRepository;

    public void evaluateBadgesForUser(Users user, Map<String, Object> userStats) {
        List<BadgeConfig> configs = badgeConfigLoader.getBadgeConfigs();

        for (BadgeConfig config : configs) {
            if (userAlreadyHasBadge(user, config.getName())) {
                continue;
            }

            if (evaluateRule(config.getRule(), userStats)) {
                Badge badge = findOrCreateBadge(config);
                UserBadge userBadge = UserBadge.builder()
                        .user(user)
                        .badge(badge)
                        .awardedAt(LocalDateTime.now())
                        .build();
                userBadgeRepository.save(userBadge);
            }
        }
    }

    private boolean userAlreadyHasBadge(Users user, String badgeName) {
        return user.getUserBadges().stream()
                .anyMatch(ub -> ub.getBadge().getName().equalsIgnoreCase(badgeName));
    }

    private boolean evaluateRule(String rule, Map<String, Object> context) {
        JexlEngine jexl = new JexlBuilder().create();
        JexlExpression expression = jexl.createExpression(rule);
        JexlContext jexlContext = new MapContext(context);
        try {
            Object result = expression.evaluate(jexlContext);
            return result instanceof Boolean && (Boolean) result;
        } catch (Exception e) {
            System.err.println("Failed to evaluate rule: " + rule);
            return false;
        }
    }

    private Badge findOrCreateBadge(BadgeConfig config) {
        return badgeRepository.findByName(config.getName())
                .orElseGet(() -> badgeRepository.save(Badge.builder()
                        .name(config.getName())
                        .rule(config.getRule())
                        .description(config.getDescription())
                        .build()));
    }
}
