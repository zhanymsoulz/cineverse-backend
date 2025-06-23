package com.unime.CineVerse.saga.steps;

import com.unime.CineVerse.saga.SagaStep;

public class DeleteBadgesStep implements SagaStep {
    private final Long userId;

    public DeleteBadgesStep(Long userId) {
        this.userId = userId;
    }

    @Override
    public void execute() throws Exception {
        System.out.println("Deleting badges for user " + userId);
    }

    @Override
    public void compensate() {
        System.out.println("Restoring badges for user " + userId);
    }
}

