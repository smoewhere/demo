package org.fan.demo.dynamic.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.fan.demo.dynamic.model.User;

import java.util.List;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.2 13:33
 */
public interface User2Mapper {


    @Select("select * from user")
    public List<User> getUsers();

    @Insert("insert into USER (name,address) values (#{name},#{address});")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(User user);
}
