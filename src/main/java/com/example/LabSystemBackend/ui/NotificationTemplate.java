package com.example.LabSystemBackend.ui;

public enum NotificationTemplate {
    VERIFICATION_CODE("Your verification code at TECO", "Hello, Welcome to TECO! Your verification code " +
            "is: %s"),
    RESISTER_CONFIRMING("Register confirming", "Dear %s, We have received your registration application " +
            "and the administrators will confirm your information as soon as possible. We will notify you by email when " +
            "the review is complete. Thank you for your patience.    From your TECO team"),
    REGISTER_SUCCESS("Register success", "Dear %s, welcome to TECO!  We are happy to inform you that " +
            "you have successfully registered!    From your TECO team"),
    REGISTER_FAIL("Fail to register", "Dear %s， We regret to inform you that your registration was not " +
            "approved and we apologize for this. We look forward to your next visit.     From your TECO team"),
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
