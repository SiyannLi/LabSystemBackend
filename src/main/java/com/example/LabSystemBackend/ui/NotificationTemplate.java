package com.example.LabSystemBackend.ui;

public enum NotificationTemplate {
    VERIFICATION_CODE("Your verification code at TECO", "Hello,<p>Welcome to TECO!</p><p>Your verificationcode "+
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
    ORDER_CONFIRMING("oder confirming","Dear %1$s, <p>We have received your order with orderNr.: %2$d  application " +
            "and the administrators will confirm your oder as soon as possible.</p><P>We will notify you by email when " +
            "the review is complete.</P><p>Thank you for your patience.</P> <p>From your TECO team"),
    ORDER_APPROVED("Order approved", "Dear %1$s, </P>We are happy to inform you that your order with number %2$d" +
            " is approved!</P>" +"<p>We will notify you by email when your order arrives.</P><P>From your TECO team</P>"),
    ORDER_REJECTED("Order rejected", "Dear %s， <P>We regret to inform you that your order with number %d was not " +
            "approved and we apologize for this.<P/><P>If you have any questions please contact your administrator.</p>" +
            "<p>From your TECO team/p>"),
    ORDER_CANCEL("Order cancel", "Dear %s， <P>We regret to inform you that your order with number %d was  " +
            "cancelled.<P/><P>If you have any questions please contact your administrator.</p><p>From your TECO team/p>"),
    APPOINTMENT_SUCCESS("Appointment success","Dear %1$s, </P>We are happy to inform you that the following " +
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
