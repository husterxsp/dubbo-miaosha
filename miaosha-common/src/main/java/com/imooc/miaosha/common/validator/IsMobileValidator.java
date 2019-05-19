package com.imooc.miaosha.common.validator;

import com.imooc.miaosha.common.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author xushaopeng
 * @date 2018/10/02
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (!required && StringUtils.isEmpty(value)) {
            return true;
        }

        return ValidatorUtil.isMobile(value);
    }

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.require();
    }
}
