package com.example.predictcgpa;

public class ProfileStatusPosoClass {
    private String currentCGPA;
    private String creditComplete;
    private String creditLeft;

    public ProfileStatusPosoClass() {
    }

    public ProfileStatusPosoClass(String currentCGPA, String creditComplete, String creditLeft) {
        this.currentCGPA = currentCGPA;
        this.creditComplete = creditComplete;
        this.creditLeft = creditLeft;
    }

    public String getCurrentCGPA() {
        return currentCGPA;
    }

    public void setCurrentCGPA(String currentCGPA) {
        this.currentCGPA = currentCGPA;
    }

    public String getCreditComplete() {
        return creditComplete;
    }

    public void setCreditComplete(String creditComplete) {
        this.creditComplete = creditComplete;
    }

    public String getCreditLeft() {
        return creditLeft;
    }

    public void setCreditLeft(String creditLeft) {
        this.creditLeft = creditLeft;
    }
}
