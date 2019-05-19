package com.imooc.miaosha.common.vo;

import com.imooc.miaosha.common.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author xushaopeng
 * @date 2018/09/19
 */
@Data
public class LoginVo {

    // @NotNull 注解是框架已经定义好的
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
