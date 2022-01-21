package com.example.LabSystemBackend.ui;

public enum NotificationTemplate {
    VERIFICATION_CODE("Your verification code at TECO", "Hello, Welcome to TECO! Your verification code " +
            "is: %s"),
    REGISTER_SUCCESS("Register success", "Dear %s, welcome to TECO!  We are happy to inform you that " +
            "you have successfully registered!    From your TECO team"),
    CHANGE_PASSWORD_SUCCESS("Password changed successfully", "Dear %s, We are happy to inform you that " +
            "You have successfully changed your password!    From your TECO team");

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
