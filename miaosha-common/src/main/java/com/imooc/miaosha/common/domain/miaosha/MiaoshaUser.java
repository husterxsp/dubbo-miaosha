package com.imooc.miaosha.common.domain.miaosha;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xushaopeng
 * @date 2018/09/19
 */
@Data
public class MiaoshaUser implements Serializable {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;

    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
