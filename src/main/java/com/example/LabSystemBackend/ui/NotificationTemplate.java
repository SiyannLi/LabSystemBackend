package com.example.LabSystemBackend.ui;

public enum NotificationTemplate {
    //user
    VERIFICATION_CODE("Verification Code", "Dear visitor, <p>Your verification code is: " +
            "%s</p> <p>Please DO NOT share this code with others.</p><p>From your TECO team</p>"),
    RESISTER_FINISHED("Registration Finished", "Dear %s, <p>We have received your registration.</p> <p>You will " +
            "be notified by a separate email once your registration is approved or rejected.</p>   <p>From your TECO team</p>"),
    REGISTER_SUCCESS("Registration Successful", "Dear %s, <p>Your registration has been approved, you " +
            "may now access to the system with your credentials.</p>    <p>From your TECO team</p>"),
    REGISTER_REJECTED("Registration Rejected", "Dear %s， <p>Your registration has been rejected, Should you have any " +
            "further questions, please contact %s.</p>     <p>From your TECO team</p>"),
    PASSWORD_RESET_SUCCESS("Password Reset successfully", "Dear %s, <p>Your password has been " +
            "successfully reset.</p> <p>You may access to the system with new credentials.</p>    <p>From your TECO team</p>"),
    INFO_CHANGED("User Information Changed", "Dear %s, <p>Your account information has been changed by an " +
            "administrator, please consult %s or any administrator for further assistance.</p><p>From your TECO team</p>"),
    BECOME_ADMIN("Become Admin", "Dear %s, <p>You are now an administrator, please use admin login to " +
            "access to the administrator management system.</p> <p>From your TECO team</p>"),
    ADMIN_ROLE_REVOKED("Admin Role Revoked", "Dear %s, <p>Your administrator access has been removed. " +
            "Should you have any further questions, please contact %s.</p> <p>From your TECO team</p>"),
    //appointment

    APPOINTMENT_BOOKED("Appointment Booked","Dear %s, <p>Your appointment at %s on %s has been confirmed.</p> " +
            "<p>This appointment was booked by %s.</p> <p>From your TECO team</p>"),
    APPOINTMENT_CANCELLED("Appointment Cancelled","Dear %s, <p>your appointment at %s on %s has been cancelled.</p> " +
            "<p>This appointment was cancelled by %s.</p> <p>From your TECO team</p>"),
    //order
    ORDER_CONFIRMED("Order Confirmed", "Dear %s, <p>The following order has been confirmed, " +
            "you will be notified separately once your item has arrived and checked into stock.</p>" +
            "<p>ID : %d</p>" +
            "<p>ITEM : %s</p>" +
            "<p>AMOUNT: %d</p>" +
            "<p> LINK : <a href=\"%s\">  For item details, please visit the website</a>  </p>  <p>From your TECO team</p>"),
    ORDER_REJECTED("Order Rejected", "Dear %s, <p>The following order has been rejected, Should you have any " +
            "further questions, please contact %s.</p>" +
            "<p>ID : %d</p>" +
            "<p>ITEM : %s</p>" +
            "<p>AMOUNT: %d</p>" +
            "<p> LINK : <a href=\"%s\">  For item details, please visit the website</a>  </p>   <p>From your TECO team</p>"),
    ORDER_ARRIVED("Order Arrived", "Dear %s，<p>The following order has arrived and item(s) have/has " +
            "been checked into stock.</p>" +
            "<p>ID : %d</p>" +
            "<p>ITEM : %s</p>" +
            "<p>AMOUNT: %d</p>" +
            "<p> LINK : <a href=\"%s\">  For item details, please visit the website</a>  </p>   <p>From your TECO team</p>"),
    ORDER_RECEIVED("Order Received", "Dear %s, <p>The following order has been submitted for auditing, " +
            "you will be notified separately once your order is approved or rejected.</p>" +
            "<p>ID : %d</p>" +
            "<p>ITEM : %s</p>" +
            "<p>AMOUNT: %d</p>" +
            "<p> LINK : <a href=\"%s\">  For item details, please visit the website</a>  </p>    <p>From your TECO team</p>"),
    ORDER_CANCELLED("Order Cancelled", "Dear %s, <p>The following order has been cancelled by yourself.</p>" +
            "<p>ID : %d</p>" +
            "<p>ITEM : %s</p>" +
            "<p>AMOUNT: %d</p>" +
            "<p> LINK : <a href=\"%s\">  For item details, please visit the website</a>  </p> " +
            "<p>From your TECO team</p>"),
    //admin
    NEW_REGISTRATION_REQUEST("New Registration Request", "Dear %s, <p>There is a new registration request " +
            "to be processed.</p></p> <p>From your TECO team</p>"),
    NEW_ORDER_REQUEST("New Order Request", "Dear %s, <p>There is a new order request to be processed.</p>" +
            "</p> <p>From your TECO team</p>");


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
