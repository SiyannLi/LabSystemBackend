# ***Controller***
## _UserController_

```java
private Userservice userService;

@RequestMapping("/users")
//获取某一个用户的所有信息
@GetMapping("get")
public Result getUsers(int userId);

//登陆
@PostMapping("login")
public Result login(String email, String password, boolean isAdmin);

//登出
@PostMapping("logout")
public Result logout(String email);

//注册一个用户
@PostMapping("register")
public Result register(String email, String password, String name, String vertificationCode);

//重置密码
@PostMapping("resetPassword")
public Result resetPassword(String email, String newPassword, String vertificationCode);

//同意用户的申请
@PostMapping("confirmUserApplication")
public Result confirmUserApplication(int userId);

//拒绝用户申请
@PostMappin("rejectUserApplication")
public Result rejectUserApplication(int userId);

//更改用户姓名
@PostMappin("changeUserName")
public Result changeUserName(int userId, String newName);

//更改用户活跃状态
@PostMappin("deactiveUser")
public Result deactiveUser(int userId);
@PostMappin("activeUser")
public Result activeUser(int userId);

//获取所有用户列表
@GetMapping("getAllUsers")
public Result getAllUsers();

//获取所有管理员列表
@GetMapping("getAllAdministrator")
public Result getAllAdministrator();
```


---
## _DeviceController_

```java
@RequestMapping("/devices")
private Userservice userService;

//获取所有设备和数量
@GetMapping("getAlldevicesAndAmount")
public Result getAlldevicesAndAmount();

//新增一个设备
@PostMapping("addDevice")
public Result addDevice(String deviceName, int Amount, String links);

//删除一个设备
@DeleteMapping("deleteDevice")
public Result deleteDevice(int deviceId);

//修改设备数量
@PostMapping("changeDeviceAmount")
public Result changeDeviceAmount(int deviceId, int newAmount);

//合并设备列表
@PostMapping("mergeDevice")
public Result mergeDevice(String deviceName, String link, int submitterId, String targetdevice);
```


---
## _OrderController_
```java
@RequestMapping("/orders")

private Userservice userService;

//获取此用户的所有orders
public Result getUserOrders(int userId);

//获取此用户的active orders
public Result getUserActiveOrders(int userId);

//删除order
public Result deleteOrder(int orderId);

//获取此用户的 past orders
public Result getUserPastOrders(int userId);

//提交一个 order
public Result submitOrder(String article, int amount, String link, String contact);

//获取所有的 active orders
public Result getAllActiveOrders();

//同意 order
public Result confirmOrder(int orderId);

//拒绝 order
public Result rejectOrder(int orderId);
```


---
## _NewsController_
```java
@RequestMapping("/news")

private Userservice userService;

//新建一个消息
public Result addNews(int senderId, int receiverId, String content);

//获取所有消息
public Result getAllNews();

//获取当前按用户所有收到的消息
public Result getUserAllNews(int receiverId);
```


---
## _AppointmentController_

```java
@RequestMapping("/appointments")

private Userservice userService;

//获取此用户的所有预约
public Result getUserAppointments(String userId);

//返回从 startDate 开始所用的空闲时间.
public Result getAvailableTimeSlots(Date startDate);

//设置可预约时间段
public Result setAvailableTimeSlots(Date availableDate, int TimeSlot, int endRepeatAfter);

//查询所有预约
public Result getAllAppointments();

//删除一个预约
public Result deleteAppointment(int appointmentId);

//用户新建一个预约
public Result addAppointment(int userId, int TimeSlot, String email);
```
