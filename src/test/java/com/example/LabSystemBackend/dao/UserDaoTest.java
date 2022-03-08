package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;
import com.example.LabSystemBackend.util.DataGenerate;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoTest {

    private String[] fullNames;
    private String[] emails;
    private UserRole[] roles;
    private UserAccountStatus[] status;
    private boolean[] isReceiveRequests;


    @Autowired
    UserDao userDao;

    @BeforeAll
    public void initArray() {
        fullNames = new String[]{"TECO TECO", "Cong Liu", "Siyan Li", "Cong Liu"};
        emails = new String[]{"teco@teco.com"
                , "cong.liu@outlook.de"
                , "siyannLi@outlook.com"
                , "fraucong@gmail.com"};
        roles = new UserRole[]{UserRole.SUPER_ADMIN, UserRole.USER, UserRole.ADMIN, UserRole.USER};
        status = new UserAccountStatus[]{UserAccountStatus.ACTIVE, UserAccountStatus.PENDING
                , UserAccountStatus.ACTIVE, UserAccountStatus.INACTIVE};
        isReceiveRequests = new boolean[]{false, false, false, false};
        Assert.assertEquals("Array init error", fullNames.length, emails.length, roles.length);
        Assert.assertEquals("Array init error", fullNames.length, status.length, isReceiveRequests.length);
    }

    private void assertAllDataInList(List<User> users, List<Integer> idx) {
        Assert.assertEquals(idx.size(), users.size());
        for(int i = 0; i < users.size() && idx.contains(i); i++) {
            Assert.assertEquals("user's data incorrect", users.get(i).getFullName(), fullNames[i]);
            Assert.assertEquals("user's data incorrect", users.get(i).getEmail(), emails[i]);
            Assert.assertEquals("user's data incorrect", users.get(i).getUserRole(), roles[i]);
            Assert.assertEquals("user's data incorrect", users.get(i).getUserAccountStatus(), status[i]);
            Assert.assertEquals("user's data incorrect", users.get(i).isReceiveBulkEmail(), isReceiveRequests[i]);
        }
    }

    private void assertAllDataUser(User user) {
        Assert.assertTrue(user.getUserId() <= fullNames.length);
        int idx = user.getUserId() - 1;
        Assert.assertEquals("user's data incorrect", user.getFullName(), fullNames[idx]);
        Assert.assertEquals("user's data incorrect", user.getEmail(), emails[idx]);
        Assert.assertEquals("user's data incorrect", user.getUserRole(), roles[idx]);
        Assert.assertEquals("user's data incorrect", user.getUserAccountStatus(), status[idx]);
        Assert.assertEquals("user's data incorrect", user.isReceiveBulkEmail(), isReceiveRequests[idx]);


    }

    @Test
    void getAllUsers() {
        List<User> users = userDao.getAllUsers();
        List<Integer> notPendingIdx = new ArrayList<>();
        for(int i = 0; i < fullNames.length; i++) {
            if(!status[i].equals(UserAccountStatus.PENDING)) {
                notPendingIdx.add(i);
            }
        }
        // Does not include users whose status is Pending
        assertAllDataInList(users, notPendingIdx);

    }

    @Test
    void getAllAdministrators() {
        List<Integer> adminIdx = new ArrayList<>();
        for(int i = 0; i < fullNames.length; i++) {
            if(!roles[i].equals(UserRole.USER)) {
                adminIdx.add(i);
            }
        }
        List<User> admins = userDao.getAllAdministrators();
        assertAllDataInList(admins, adminIdx);


    }

    @Test
    void getAllAccountToBeConfirmed() {
        List<Integer> pendingIdx = new ArrayList<>();
        for(int i = 0; i < fullNames.length; i++) {
            if(status[i].equals(UserAccountStatus.PENDING)) {
                pendingIdx.add(i);
            }
        }
        List<User> users = userDao.getAllAccountToBeConfirmed();
        assertAllDataInList(users,pendingIdx);
    }

    @Test
    void getAllAdminReceiveBulkEmail() {
        List<Integer> receiveAdminIdx = new ArrayList<>();
        for(int i = 0; i < fullNames.length; i++) {
            if(!roles[i].equals(UserRole.USER) && isReceiveRequests[i]) {
                receiveAdminIdx.add(i);
            }
        }
        List<User> users = userDao.getAllAdminReceiveBulkEmail();
       assertAllDataInList(users, receiveAdminIdx);

    }

    @Test
    void getUser() {
        int id = new Random().nextInt(fullNames.length) + 1;
        User user = userDao.getUser(id);
        assertAllDataUser(user);
    }

    @Test
    void getUserByEmail() {
        int idx = new Random().nextInt(fullNames.length);
        String email = emails[idx];
        User user = userDao.getUserByEmail(email);
        assertAllDataUser(user);
    }

    @Test
    void deleteUser() {
        int id = new Random().nextInt(fullNames.length) + 1;
        Assert.assertTrue("fail to delete", 1 == userDao.deleteUser(id));

    }

    @Test
    void insertUser() {
        User newUser = DataGenerate.generateUser();
        userDao.insertUser(newUser);
        Assert.assertTrue("Fail to insert", fullNames.length + 1 == newUser.getUserId());

    }

    @Test
    void updatePassword() {
        int id = new Random().nextInt(fullNames.length) + 1;
        Faker faker  = new Faker();
        String newPassword = faker.internet().password();
        Assert.assertTrue("fail to update password", 1 == userDao.updatePassword(id, newPassword));

    }

    @Test
    void updateUserAccountStatus() {
        int id = new Random().nextInt(fullNames.length) + 1;
        int status = new Random().nextInt(UserAccountStatus.values().length);
        UserAccountStatus newStatus = UserAccountStatus.values()[status];
        Assert.assertTrue("fail to update status", 1 == userDao.updateUserAccountStatus(id, newStatus));
    }

    @Test
    void updateName() {
        int id = new Random().nextInt(fullNames.length) + 1;
        Faker faker  = new Faker();
        String[] name;
        do {
            name = faker.harryPotter().character().toLowerCase().split(" ");

        } while (name.length != 2);
        Assert.assertTrue("fail to update name", 1 == userDao.updateName(id, name[0], name[1]));
    }

    @Test
    void updateUserRole() {
        int id = new Random().nextInt(fullNames.length) + 1;
        int role = new Random().nextInt(UserRole.values().length);
        UserRole newRole = UserRole.values()[role];
        Assert.assertTrue("fail to update role", 1 == userDao.updateUserRole(id, newRole));
    }

    @Test
    void updateAdminEmailSetting() {
        int id = new Random().nextInt(fullNames.length) + 1;
        boolean stetting = new Random().nextInt(2) == 1;
        Assert.assertTrue("fail to update email setting", 1 == userDao.updateAdminEmailSetting(id, stetting));

    }
}