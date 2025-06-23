package com.unime.CineVerse.saga;

public interface SagaStep {
    void execute() throws Exception;
    void compensate();
}
