package com.imooc.miaosha.web.dao;

/**
 * @author xushaopeng
 * @date 2018/09/19
 */

import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface MiaoshaUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    MiaoshaUser getById(@Param("id") long id);

    @Update("update miaosha_user set password = #{password} where id = #{id}")
    void update(MiaoshaUser toBeUpdate);

    @Insert("insert into miaosha_user(id, password, nickname, salt, register_date)values(#{id}, #{password}, #{nickname}, #{salt}, #{registerDate})")
    int insert(MiaoshaUser user);
}
