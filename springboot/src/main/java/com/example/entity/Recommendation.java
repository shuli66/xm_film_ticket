package com.example.entity;

public class Recommendation {
    private Long userId;
    private double similarity;

    public Recommendation(Long userId, double similarity) {
        this.userId = userId;
        this.similarity = similarity;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}
