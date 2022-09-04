package com.liqing.demotest.service;

import com.liqing.demotest.entity.User;
import com.liqing.demotest.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private IUserService userService;

    @Test
    public void reg() {
        try {
            User user = new User();
            user.setUsername("LIQING");
            user.setPassword("123456");
            user.setGender(1);
            user.setPhone("17858802974");
            user.setEmail("lower@tedu.cn");
            user.setAvatar("xxxx");
            userService.reg(user);
            System.out.println("注册成功!");
        } catch (ServiceException e) {
            System.out.println("注册失败!" + e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void  login(){

        try{
            String username = "liqing";
            String passWorld = "123456";
            User login = userService.login(username, passWorld);
            String s = login.getUid().toString();
            System.out.println("用户的id："+s);


        }catch (Exception e)
        {
            System.out.println("用户名或密码不存在!" + e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }

    }
}
