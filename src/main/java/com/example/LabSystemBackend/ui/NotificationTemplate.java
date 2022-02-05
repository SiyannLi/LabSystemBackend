package com.example.LabSystemBackend.ui;

public enum NotificationTemplate {
    //user
    VERIFICATION_CODE("Verification Code", "Dear visitor, your verification code is" +
            "%s. Please DO NOT share this code with others."),
    RESISTER_FINISHED("Registration Finished", "Dear %s, we have received your registration. You will " +
            "be notified by a separate email once your registration is approved or rejected    From your TECO team"),
    REGISTER_SUCCESS("Registration Successful", "Dear %s, your registration has been approved, you " +
            "may now access to the system with your credentials.    From your TECO team"),
    REGISTER_REJECTED("Registration Rejected", "Dear %s， your registration has been rejected, Should you have any" +
            "further questions, please contact %s.     From your TECO team"),
    PASSWORD_RESET_SUCCESS("Password Reset successfully", "Dear %s, your password has been " +
            "successfully reset. You may access to the system with new credentials    From your TECO team"),
    INFO_CHANGED("User Information Changed", "Dear %s, your account information has been changed by an " +
            "administrator, please consult %s or any administrator for further assistance.    From your TECO team"),
    BECOME_ADMIN("Become Admin", "Dear %s, you are now an administrator, please use admin login to " +
            "access to the administrator management system."),
    ADMIN_ROLE_REVOKED("Admin Role Revoked", "Dear %s, your administrator access has been removed. " +
            "Should you have any further questions, please contact %s."),
    //appointment

    //order
    ORDER_CONFIRMED("Order Confirmed", "Dear %s, the following order has been confirmed, " +
            "you will be notified separately once your item has arrived and checked into stock." +
            "ID : %d" +
            "ITEM : %s" +
            "AMOUNT: %d" +
            "LINK :%s  From your TECO team"),
    ORDER_REJECTED("Order Rejected", "Dear %s, the following order has been rejected, Should you have any " +
            "further questions, please contact %s." +
            "ID : %d" +
            "ITEM : %s" +
            "AMOUNT: %d" +
            "LINK :%s  From your TECO team"),
    ORDER_ARRIVED("Order Arrived", "Dear %s，the following order has arrived and item(s) have/has " +
            "been checked into stock." +
            "ID : %d" +
            "ITEM : %s" +
            "AMOUNT: %d" +
            "LINK :%s  From your TECO team"),
    ORDER_RECEIVED("Order Received", "Dear %s, the following order has been submitted for auditing , " +
            "you will be notified separately once your order is approved or rejected." +
            "ID : %d" +
            "ITEM : %s" +
            "AMOUNT: %d" +
            "LINK :%s    From your TECO team"),
    ORDER_CANCELLED("Order Cancelled", "Dear %s, the following order has been cancelled by yourself." +
            "ID : %d" +
            "ITEM : %s" +
            "AMOUNT: %d" +
            "LINK :%s"),
    //admin
    NEW_REGISTRATION_REQUEST("New Registration Request", "Dear %s, there is a new registration request " +
            "to be processed."),
    NEW_ORDER_REQUEST("New Order Request", "Dear %s, there is a new order request to be processed.");


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
