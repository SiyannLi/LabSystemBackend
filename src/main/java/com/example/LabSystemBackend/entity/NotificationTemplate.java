package com.example.LabSystemBackend.entity;

public enum NotificationTemplate {
    VERIFICATION_CODE("Your verification code at TECO", "Hello, Welcome to TECO! Your verification code ist: %s");

    private final String subject;
    private final String content;

    NotificationTemplate(String subject, String content) {
        this.content = content;
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getContent() {
        return this.content;
    }


}
