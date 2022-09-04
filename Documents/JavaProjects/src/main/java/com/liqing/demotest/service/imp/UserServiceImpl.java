package com.liqing.demotest.service.imp;

import com.liqing.demotest.entity.User;
import com.liqing.demotest.mapper.UserMapper;
import com.liqing.demotest.service.IUserService;
import com.liqing.demotest.service.ex.InsertException;
import com.liqing.demotest.service.ex.PasswordNotMatchException;
import com.liqing.demotest.service.ex.UserNotFoundException;
import com.liqing.demotest.service.ex.UsernameDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     * @param user 用户数据
     */
    @Override
    public void reg(User user) {
        // 根据参数user对象获取注册的用户名
        String username = user.getUsername();
        // 调用持久层的User findByUsername(String username)方法，根据用户名查询用户数据
        User result = userMapper.findByUsername(username);
        // 判断查询结果是否不为null
        if(result != null){
            // 是：表示用户名已被占用，则抛出UsernameDuplicateException异常
            throw new UsernameDuplicateException("尝试注册的用户名[" + username + "]已经被占用");
        }

        //创建当前时间对象
        Date now = new Date();
        // 补全数据：加密后的密码（toUpperCase将小写字符转换成大写）
        String salt = UUID.randomUUID().toString().toUpperCase();
        String md5Password = getMd5Password(user.getPassword(), salt);
        user.setPassword(md5Password);
        // 补全数据：盐值
        user.setSalt(salt);
        // 补全数据：isDelete(0)
        user.setIsDelete(0);
        // 补全数据：4项日志属性
        user.setCreatedUser(username);
        user.setCreatedTime(now);
        user.setModifiedUser(username);
        user.setModifiedTime(now);

        Integer insertResult = userMapper.insert(user);
        if (insertResult != 1) {
            // 是:插入数据时出现某种错误，则抛出InsertException异常
            throw new InsertException("添加用户数据出现未知错误，请联系系统管理员"); }

    }
    /**
     * 执行密码加密
     * @param password 原始密码
     * @param salt 盐值
     * @return 加密后的密文
     */
    private String getMd5Password(String password, String salt) {
        /*
         * 加密规则：
         * 1、无视原始密码的强度
         * 2、使用UUID作为盐值，在原始密码的左右两侧拼接
         * 3、循环加密3次
         */
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }

    /**
     * 铜壶登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public User login(String username, String password) {
        //登录前 查询该用户是否存在
        User result = userMapper.findByUsername(username);
        if (result == null || result.getIsDelete() == 1) {
            // 结果为空 或者 盐值=1（表示该用户删除）   抛出错误
            throw new UserNotFoundException("没有该账户");

        }
        //还原密码 进行对比
        String salt = result.getSalt();
        String md5Password = getMd5Password(password, salt);
        // 判断查询结果中的密码，与以上加密得到的密码是否不一致
        if (!result.getPassword().equals(md5Password)) {
            // 是:抛出PasswordNotMatchException异常
            throw new PasswordNotMatchException("密码验证失败的错误");
        }
        //返回需要展示的用互信息
        User user = new User();
        // 将查询结果中的uid、username、avatar封装到新的user对象中
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setAvatar(result.getAvatar());
        return user;
    }

}
