package com.imooc.miaosha.common.domain;

import lombok.Data;

@Data
public class User {
    private long id;
    private String name;

    public User() {

    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
