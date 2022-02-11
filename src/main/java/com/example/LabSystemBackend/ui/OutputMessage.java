package com.example.LabSystemBackend.ui;

/**
 * @version 1.0
 * @author Cong Liu
 *
 * Output messages
 */
public class OutputMessage {
    public static final String BLANK = " ";
    public static final String POINT = ".";
    public static final String COLON = ":";
    public static final String SLASH = "/";
    public static final String DOUBLE_SLASH = "//";
    //user
    public static final String SUCCEED = "succeed";
    public static final String USER_EXISTS = "This user already exists";
    public static final String USER_NOT_EXISTS = "This user does not exist";
    public static final String ERROR = "error";
    public static final String ACCOUNT_INACTIVE = "This account is an inactive account";
    public static final String INCORRECT_PASSWORD = "Incorrect password";
    public static final String NOT_ADMIN = "Not an administrator account";
    public static final String INVALID_VERIFY_CODE = "Invalid verification code";
    public static final String NO_USER_TO_CONFIRM = "No users to be confirmed";
    public static final String USER_NOT_PENDING = "User status is not pending";
    public static final String INPUT_ERROR = "Input error";
    public static final String ALREADY_ADMIN = "This account is already an administrator account.";
    public static final String ALREADY_USER = "This account is already an visitor account.";
    public static final String CANNOT_REVOKE_SUPER_ADMIN = "This account is super admin account, can not be revoked";
    //order
    public static final String ORDER_NOT_PENDING = "This order has been confirmed or the order has ended";
    public static final String ORDER_NOT_EXIST = "This order does not exist";
    public static final String NO_ACTIVE_ORDERS = "No active order";
    public static final String NO_PAST_ORDERS = "No past order";
    public static final String NOT_CONFIRMED_ORDER = "This order ist not a confirmed order";
    public static final String IN_STOCK_ITEM_NAME_NOT_EXIST = "Item Name does not exist, lease create this item first.";
    //appointment
    public static final String[] SLOTS ={"8:00-10:00","10:00-12:00","12:00-14:00","14:00-16:00","16:00-18:00","18:00-20:00"};
    public static final String TIME_SLOT_NOT_FREE = "time slot not free";
    public static final String CANCEL_OTHERS_APPOINTMENT = "you can not cancel other's appointment.";
    //item
    public static final String ITEM_EXIST = "Item already exists";
    public static final String ITEM_NOT_EXIST = "Item not exists";
    //slot
    public static final String NOT_ALL_TIME_SET_TO_NA = "only %d time slot set to not available.";
    public static final String NOT_ALL_TIME_SET_TO_FREE = "only %d time slot set to free.";


}
