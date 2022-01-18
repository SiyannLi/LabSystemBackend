# ***Controller***

## _UserController_

```java
private Userservice userService;

@RequestMapping("/users")
//Get all info of one user
@GetMapping("get")
public Result getUsers(int userId);

//login
@PostMapping("login")
public Result login(String email, String password, boolean isAdmin);

//log out
@PostMapping("logout")
public Result logout(String email);

//register one account
@PostMapping("register")
public Result register(String email, String password, String name, String vertificationCode);

//reset password
@PostMapping("resetPassword")
public Result resetPassword(String email, String newPassword, String vertificationCode);

//confirm the application of user to create a new account
@PostMapping("confirmUserApplication")
public Result confirmUserApplication(int userId);

//reject the application of user to create a new account
@PostMappin("rejectUserApplication")
public Result rejectUserApplication(int userId);

//change username
@PostMappin("changeUserName")
public Result changeUserName(int userId, String newName);

//activate or deactivate account
@PostMappin("deactivateUser")
public Result deactiveUser(int userId);
@PostMappin("activateUser")
public Result activeUser(int userId);

//get a list of all users
@GetMapping("getAllUsers")
public Result getAllUsers();

//get a list of all admins
@GetMapping("getAllAdministrators")
public Result getAllAdministrator();
```

---

## _DeviceController_

```java
@RequestMapping("/devices")
private Userservice userService;

//get a list of all devices and their amounts
@GetMapping("getAlldevicesAndAmount")
public Result getAlldevicesAndAmount();

//add a new item
@PostMapping("addDevice")
public Result addDevice(String deviceName, int Amount, String links);

//delete a item from database
@DeleteMapping("deleteDevice")
public Result deleteDevice(int deviceId);

//change the amount of one item
@PostMapping("changeDeviceAmount")
public Result changeDeviceAmount(int deviceId, int newAmount);

//merge two item and sum the amounts
@PostMapping("mergeDevice")
public Result mergeDevice(String deviceName, String link, int submitterId, String targetdevice);
```

---

## _OrderController_

```java
@RequestMapping("/orders")

private Userservice userService;

//get a list of all orders of this user
public Result getUserOrders(int userId);

//get all active orders of this user
public Result getUserActiveOrders(int userId);

//delete one order
public Result deleteOrder(int orderId);

//get all past orders of this user
public Result getUserPastOrders(int userId);

//submit an order with user account
public Result submitOrder(int userId, String article, int amount, String link, String contact);

//get all active orders
public Result getAllActiveOrders();

//confirm order application
public Result confirmOrder(int orderId);

//reject one order application
public Result rejectOrder(int orderId);
```

---

## _NewsController_

```java
@RequestMapping("/notification")

private Userservice userService;

//create a notification
public Result addNews(int senderId, int receiverId, String content);

//get list of all notification
public Result getAllNews();

//get list of all notification of this user
public Result getUserAllNews(int receiverId);
```

---

## _AppointmentController_

```java
@RequestMapping("/appointments")

private Userservice userService;

//get list of all appointments of this user
public Result getUserAppointments(String userId);

//get a list of all available Time slots from start date.
public Result getAvailableTimeSlots(Date startDate);

//set one available time slot
public Result setAvailableTimeSlots(Date availableDate, int TimeSlot, int endRepeatAfter);

//get list of all appointments
public Result getAllAppointments();

//delete one appointment
public Result deleteAppointment(int appointmentId);

//User create one new appointment
public Result addAppointment(int userId, int TimeSlot, String email);
```
