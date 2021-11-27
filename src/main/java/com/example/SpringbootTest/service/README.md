# _Service_

## UserService

```java
//attribute
private UserDao userDao;

//methods
//获取某一个用户的所有信息
public User getUser(int userId);

//登陆
public User login(String email, String password, boolean isAdmin);

//登出
public User logout(String email);

//注册一个用户
public User register(String email, String password, String name, String vertificationCode);

//重置密码
public User resetPassword(String email, String newPassword, String vertificationCode);

//同意用户的申请
public User confirmUserApplication(int userId);

//拒绝用户申请
public boolean rejectUserApplication(int userId);//拒绝用户申请后，直接在数据库里清除用户的信息

//更改用户姓名
public User changeUserName(int userId, String newName);

//更改用户活跃状态
public User deactiveUser(int userId);
public User activeUser(int userId);

//获取所有用户列表
public List<User> getAllUsers();

//获取所有管理员列表
public List<User> getAllAdministrator();

```
---
## DeviceService

```java
//attribute
private DevicerDao DeviceDao;

//methods
//获取所有设备和数量
public List<Device> getAlldevicesAndAmount();

//新增一个设备
public Device addDevice(String deviceName, int Amount, String links);

//删除一个设备
public Device deleteDevice(int deviceId);

//修改设备数量
public Device changeDeviceAmount(int deviceId, int newAmount);

//合并设备列表
public List<Device> mergeDevice(String deviceName, String link, int submitterId, String targetdevice);
```
---
## OrderService

```java
//attribute
private UserDao userDao;

//methods
//获取此用户的所有orders
public List<Order> getUserOrders(int userId);

//获取此用户的active orders
public List<Order> getUserActiveOrders(int userId);

//删除order
public Order deleteOrder(int orderId);

//获取此用户的 past orders
public List<Order> getUserPastOrders(int userId);

//提交一个 order
public Order submitOrder(String article, int amount, String link, String contact);

//获取所有的 active orders
public List<Order> getAllActiveOrders();

//同意 order
public Order confirmOrder(int orderId);

//拒绝 order
public Order rejectOrder(int orderId);//拒绝订单后订单直接在数据库里清除

```
---
## NewsService

```java
//attribute
private Userservice userService;

//methods
//新建一个消息
public News addNews(int senderId, int receiverId, String content);

//获取所有消息
public List<News> getAllNews();

//获取当前按用户所有收到的消息
public News getUserAllNews(int receiverId);

```
---
## AppointmentService
```java
//attribute
private Userservice userService;

//methods
//获取此用户的所有预约
public List<Appointment> getUserAppointments(String userId);

//返回从 startDate 开始所有的空闲时间.
public List<TimeSlot> getAvailableTimeSlots(Date startDate);

//设置可预约时间段
public List<TimeSlot> setAvailableTimeSlots(Date availableDate, int TimeSlot, int endRepeatAfter);

//查询所有预约
public List<Appointment> getAllAppointments();

//删除一个预约
public Appointment deleteAppointment(int appointmentId);

//用户新建一个预约
public Appointment addAppointment(int userId, int TimeSlot, String email);
```

