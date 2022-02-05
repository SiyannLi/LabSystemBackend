package com.example.LabSystemBackend.ui;

public class OutputMessage {
    public static final String BLANK = " ";
    //user
    public static final String SUCCEED = "succeed";
    public static final String USER_EXISTS = "This user already exists";
    public static final String USER_NOT_EXISTS = "This user does not exist";
    public static final String ERROR = "error";
    public static final String ACCOUNT_INACTIVE = "This account is an inactive account";
    public static final String INCORRECT_PASSWORD = "Incorrect password";
    public static final String NOT_ADMIN = "Not an administrator account";
    //order
    public static final String ORDER_NOT_PENDING = "This order has been confirmed or the order has ended";
    public static final String ORDER_NOT_EXIST = "This order does not exist";
    public static final String NO_ACTIVE_ORDERS = "No active order";
    public static final String NO_PAST_ORDERS = "No past order";
    public static final String NOT_CONFIRMED_ORDER = "This order ist not a confirmed order";
    public static final String IN_STOCK_ITEM_NAME_NOT_EXIST = "Item Name does not exist, lease create this item first.";


}
