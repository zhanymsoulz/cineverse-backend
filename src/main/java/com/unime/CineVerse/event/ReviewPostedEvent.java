package com.unime.CineVerse.event;

import com.unime.CineVerse.model.Users;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

@Getter
public class ReviewPostedEvent extends ApplicationEvent {

    private final Users user;
    private final Map<String, Object> stats;

    public ReviewPostedEvent(Object source, Users user, Map<String, Object> stats) {
        super(source);
        this.user = user;
        this.stats = stats;
    }
}
