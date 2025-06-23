package com.unime.CineVerse.saga;

import java.util.ArrayList;
import java.util.List;

public class SagaExecutor {
    private final List<SagaStep> steps = new ArrayList<>();
    private final List<SagaStep> completedSteps = new ArrayList<>();

    public void addStep(SagaStep step) {
        steps.add(step);
    }

    public void execute() {
        for (SagaStep step : steps) {
            try {
                step.execute();
                completedSteps.add(step);
            } catch (Exception e) {
                System.out.println("Step failed: " + e.getMessage());
                rollback();
                break;
            }
        }
    }

    private void rollback() {
        for (int i = completedSteps.size() - 1; i >= 0; i--) {
            completedSteps.get(i).compensate();
        }
    }
}
