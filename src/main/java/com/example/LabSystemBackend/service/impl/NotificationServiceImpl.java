package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.NotificationDao;
import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.entity.Order;
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
/**
 * @version 1.0
 * @author Cong Liu, Siyan Li, Sheyang Li
 *
 * Implement of Notification Service
 */
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
        System.out.print(notification);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mHelper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        mHelper.setFrom(sysEmail);
        mHelper.setTo(email);
        mHelper.setSubject(notification.getSubject());
        String content = notification.getContent();
        String htmlMsg = "<body>" + content + "</body>";
        mHelper.setText(htmlMsg, true);
        mailSender.send(mimeMessage);

        return notificationDao.insertNotification(notification);
    }

    @Override
    public int sendNotificationByTemplate(String email, NotificationTemplate template, String userName)
            throws MessagingException {
        System.out.print(userName);
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
    public int sendNotificationByTemplate(String email, NotificationTemplate template, String userName, String opEmail) throws MessagingException {
        Notification notification = new Notification();
        notification.setSenderId(User.ID_OF_SYSTEM);
        if (userService.emailExists(email)) {
            notification.setRecipientId(userService.getUserByEmail(email).getUserId());
        } else {
            notification.setRecipientId(User.ID_OF_UNREGISTERED);
        }
        notification.setSubject(template.getSubject());
        notification.setContent(String.format(template.getContent()
                , userName, opEmail));
        return sendNotification(email, notification);
    }

    @Override
    public int sendNotificationByTemplate(String email, NotificationTemplate template, String userName, Order order) throws MessagingException {
        Notification notification = new Notification();
        notification.setSenderId(User.ID_OF_SYSTEM);
        if (userService.emailExists(email)) {
            notification.setRecipientId(userService.getUserByEmail(email).getUserId());
        } else {
            notification.setRecipientId(User.ID_OF_UNREGISTERED);
        }
        notification.setSubject(template.getSubject());
        notification.setContent(String.format(template.getContent()
                , userName, order.getOrderId(), order.getItemName(), order.getAmount(), order.getItemLink()));
        return sendNotification(email, notification);
    }

    @Override
    public int sendNotificationByTemplate(String email, NotificationTemplate template, String userName, String opEmail, Order order) throws MessagingException {
        Notification notification = new Notification();
        notification.setSenderId(User.ID_OF_SYSTEM);
        if (userService.emailExists(email)) {
            notification.setRecipientId(userService.getUserByEmail(email).getUserId());
        } else {
            notification.setRecipientId(User.ID_OF_UNREGISTERED);
        }
        notification.setSubject(template.getSubject());
        notification.setContent(String.format(template.getContent()
                , userName, opEmail, order.getOrderId(), order.getItemName(), order.getAmount(), order.getItemLink()));
        return sendNotification(email, notification);
    }

    @Override
    public int sendNotificationAddOrDeleteAppointment(String email, NotificationTemplate template, String userName,
                                                      String timeSlot, String date, String operatorName) throws MessagingException {
        Notification notification = new Notification();
        notification.setSenderId(User.ID_OF_SYSTEM);
        if (userService.emailExists(email)) {
            notification.setRecipientId(userService.getUserByEmail(email).getUserId());
        } else {
            notification.setRecipientId(User.ID_OF_UNREGISTERED);
        }
        notification.setSubject(template.getSubject());
        notification.setContent(String.format(template.getContent()
                , userName, timeSlot, date, operatorName));
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
    public int sendToAllAdmin(NotificationTemplate template) throws MessagingException {
        int sendCounter = 0;
        List<User> admins = userService.getAllAdminReceiveBulkEmail();
        for (User admin : admins) {
            String adminEmail = admin.getEmail();
            String adminName = admin.getFullName();
            sendNotificationByTemplate(adminEmail, template
                    , adminName);
            sendCounter++;
        }
        return sendCounter == admins.size() ? 1 : 0;
    }
}
