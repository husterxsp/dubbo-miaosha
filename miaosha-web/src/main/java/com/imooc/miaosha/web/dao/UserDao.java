package com.imooc.miaosha.web.dao;

import com.imooc.miaosha.common.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author xushaopeng
 * @date 2018/09/16
 */
@Component
@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    User getById(@Param("id") int id);

    @Insert("insert into user(id, name)values(#{id},#{name})")
    int insert(User user);
}
