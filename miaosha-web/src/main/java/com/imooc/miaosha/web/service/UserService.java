package com.imooc.miaosha.web.service;

import com.imooc.miaosha.web.dao.UserDao;
import com.imooc.miaosha.common.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xushaopeng
 * @date 2018/09/16
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(int id) {
        return userDao.getById(id);
    }

    // 测试事务
    @Transactional
    public boolean tx() {

        User u1 = new User();
        u1.setId(100);
        userDao.insert(u1);

        User u2 = new User();
        u2.setId(100);
        userDao.insert(u2);
        return true;
    }

}
