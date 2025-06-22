package com.unime.CineVerse.listener;

import com.unime.CineVerse.event.ReviewPostedEvent;
import com.unime.CineVerse.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BadgeEventListener {

    @Autowired
    private BadgeService badgeService;

    @EventListener
    public void handleReviewPosted(ReviewPostedEvent event) {
        badgeService.evaluateBadgesForUser(event.getUser(), event.getStats());
    }
}
