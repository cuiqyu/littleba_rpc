package com.limpid.exception;

/**
 * 业务异常
 *
 * @auther cuiqiongyu
 * @create 2020/9/1 11:02
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 8272780160744263944L;

    protected final String message;

    public BusinessException(String message) {
        this.message = message;
    }

    public BusinessException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
