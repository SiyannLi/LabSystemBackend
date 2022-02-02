package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.NotificationDao;
import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.NotificationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationDao notificationDao;
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender mailSender;

    private final String sysEmail = "Labsystem22@outlook.com";

    @Override
    public int sendNotification(Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        String recipientEmail = userService.getUser(notification.getRecipientId()).getEmail();
        message.setFrom(sysEmail);
        message.setTo(recipientEmail);
        message.setSubject(notification.getSubject());
        message.setText(notification.getContent());
        mailSender.send(message);

        return notificationDao.insertNotification(notification);
    }

    @Override
    public int sendNotification(String email, Notification notification) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mHelper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        mHelper.setFrom(sysEmail);
        mHelper.setTo(email);
        mHelper.setSubject(notification.getSubject());
        String content = notification.getContent();
        String htmlMsg = "<body style='border:2px solid black'>" + content + "</body>";
        mHelper.setText(htmlMsg, true);
        mailSender.send(mimeMessage);

        return notificationDao.insertNotification(notification);
    }

    @Override
    public int sendNotificationByTemplate(String email, NotificationTemplate template, String userName)
            throws MessagingException {
        Notification notification = new Notification();
        notification.setSenderId(User.ID_OF_SYSTEM);
        if (userService.emailExists(email)) {
            notification.setRecipientId(userService.getUserByEmail(email).getUserId());
        } else {
            notification.setRecipientId(User.ID_OF_UNREGISTERED);
        }
        notification.setSubject(template.getSubject());
        notification.setContent(String.format(template.getContent()
                , userName));
        return sendNotification(email, notification);
    }

    @Override
    public List<Notification> getAllNotification() {
        return notificationDao.getAllNotification();
    }

    @Override
    public List<Notification> getUserAllNotification(int recipientId) {
        return notificationDao.getUserAllNotification(recipientId);
    }

    @Override
    public int sendToAllAdmin(String subject, String content) {
        int sendCounter = 0;
        List<User> admins = userService.getAllAdministrators();
        for (User admin : admins) {
            Notification noti = new Notification();
            noti.setSubject(subject);
            noti.setContent(content);
            noti.setRecipientId(admin.getUserId());
            noti.setSenderId(0);

            sendCounter += sendNotification(noti);
        }
        return sendCounter == admins.size() ? 1 : 0;
    }
}
