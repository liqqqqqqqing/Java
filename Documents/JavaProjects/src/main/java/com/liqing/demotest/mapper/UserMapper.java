package com.liqing.demotest.mapper;

import com.liqing.demotest.entity.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {

    Integer insert(User user);

    User findByUsername(String username);

}
