package com.imooc.miaosha.common.exception;


import com.imooc.miaosha.common.result.CodeMsg;

/**
 * @author xushaopeng
 * @date 2018/10/02
 */
public class GlobalException extends RuntimeException {

    private CodeMsg cm;
    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
