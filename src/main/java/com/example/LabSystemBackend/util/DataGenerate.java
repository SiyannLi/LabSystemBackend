package com.example.LabSystemBackend.util;

import com.example.LabSystemBackend.entity.*;
import com.github.javafaker.Faker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;


public class DataGenerate {

    private static final Faker faker = new Faker();

    public static User generateUser() {
        String[] name;
        do {
            name = faker.harryPotter().character().toLowerCase().split(" ");

        } while (name.length != 2);
        User user = new User();
        user.setUserPassword(faker.internet().password());
        user.setFirstName(name[0]);
        user.setLastName(name[1]);
        int role = new Random().nextInt(UserRole.values().length);
        user.setUserRole(UserRole.values()[role]);
        user.setEmail(faker.internet().emailAddress(name[0].toLowerCase() + name[1].toLowerCase()));
        int status = new Random().nextInt(UserAccountStatus.values().length);
        user.setUserAccountStatus(UserAccountStatus.values()[status]);
        boolean receiveEmail = new Random().nextInt(2) == 1 ? true : false;
        user.setReceiveBulkEmail(receiveEmail);
        return user;
    }

    public static Item generateItem() {
        Item item = new Item();
        item.setItemName(faker.food().spice());
        item.setAmount(faker.random().nextInt(1, 100));
        item.setItemDescri(faker.ancient().primordial());
        return item;
    }

    public static TimeSlot generateTimeSlot() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setSlot(faker.random().nextInt(1, 6));

        timeSlot.setTimeSlotDate(sdf.parse(faker.business().creditCardExpiry()));
        int status = new Random().nextInt(TimeSlotStatus.values().length);
        timeSlot.setTimeSlotStatus(TimeSlotStatus.values()[status]);
        return timeSlot;
    }

    public static Appointment generateAppointment() throws ParseException {
        Appointment appointment = new Appointment();
        appointment.setUserId(faker.random().nextInt(1, 100));
        appointment.setTimeSlotId(faker.random().nextInt(6));
        return appointment;
    }

    public static Order generateOrder() {
        Order order = new Order();
        order.setUserId(faker.random().nextInt(1, 100));
        order.setAmount(faker.random().nextInt(1, 100));
        int status = new Random().nextInt(OrderStatus.values().length);
        order.setOrderStatus(OrderStatus.values()[status]);
        return order;
    }

    public static Notification generateNotification() {
        Notification notification = new Notification();
        notification.setContent(faker.harryPotter().quote());
        notification.setSubject(faker.aquaTeenHungerForce().character());
        notification.setRecipientId(faker.random().nextInt(1, 100));
        notification.setSenderId(faker.random().nextInt(1, 100));
        return notification;
    }
}
