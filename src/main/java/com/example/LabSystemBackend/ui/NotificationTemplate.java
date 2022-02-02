package com.example.LabSystemBackend.ui;

public enum NotificationTemplate {
    VERIFICATION_CODE("Your verification code at TECO", "Hello,<p>Welcome to TECO!</p><p>Your verificationcode " +
            "is: %s.</p><p>From your TECO team</p>"),
    RESISTER_CONFIRMING("Register confirming", "Dear %s, <p>We have received your registration application " +
            "and the administrators will confirm your information as soon as possible.<P>We will notify you by email when " +
            "the review is complete. <p>Thank you for your patience. </p>  </P> <p>From your TECO team</P>"),
    REGISTER_SUCCESS("Register success", "Dear %s, <P>welcome to TECO! </P>We are happy to inform you that " +
            "you have successfully registered!</P><P>From your TECO team</P>"),
    REGISTER_FAIL("Fail to register", "Dear %s， <P>We regret to inform you that your registration was not " +
            "approved and we apologize for this.<P/><P>We look forward to your next visit.</p><p>From your TECO team</p>"),
    CHANGE_PASSWORD_SUCCESS("Password changed successfully", "Dear %s, <p>We are happy to inform you that " +
            "you have successfully changed your password!</p><p>From your TECO team</p>"),
    ORDER_CONFIRMED("Your order is confirmed", "Dear %s, <p>Thank you for your patience and we are happy " +
            "to inform you that your order with id %d has been confirmed by the administrator.</p>  <p>We will notify you by email when " +
            "the item you ordered arrives.</p>  <p>From your TECO team</p>"),
    ORDER_REJECTED("Your order is rejected", "Dear %s, <p>Thank you for your patience, but we are sorry to " +
            "inform you that we are unable to accept your order with id %d.</p> <p>We apologize for this and look forward to " +
            "your next order.</p>  <p>From your TECO team</p>"),
    IN_STOCK("Your order is in stock", "Dear %s， <p>We are pleased to inform you that your order with " +
            "id %d has arrived and we welcome you to pick it up anytime during our working hours.</p>  <p>From your TECO team</p>"),
    ORDER_CONFIRMING("Order confirming", "Dear %s, <p>We have received your order with id %d application " +
            "and the administrators will confirm this order as soon as possible.</p><p>We will notify you by email when " +
            "the review is complete.</p> <p>Thank you for your patience.</p>    <p>From your TECO team</p>"),
    ORDER_CANCEL("Order cancel", "Dear %s， <P>We regret to inform you that your order with number %d was  " +
            "cancelled.<P/><P>If you have any questions please contact your administrator.</p><p>From your TECO team/p>"),
    APPOINTMENT_SUCCESS("Appointment success", "Dear %1$s, </P>We are happy to inform you that the following " +
            "appointment has been reserved for you:</p><p>Day: %2$tF</p><p>Time: %3$s </P><P>From your TECO team</P>"),
    APPOINTMENT_CANCEL("Appointment cancel", "Dear %1$s, <p>We regret to cancel your appointment on : " +
            "<p>Day : %2$tF</p> <p>Time: %3$s </p><p>If you still need to make an appointment, please make a new " +
            "appointment in the TECO system.</P><P>From your " +
            "TECO team</P>");


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
