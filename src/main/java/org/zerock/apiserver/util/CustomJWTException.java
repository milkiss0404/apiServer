package org.zerock.apiserver.util;

public class CustomJWTException extends RuntimeException {
    public CustomJWTException(String exception){
        super(exception);
    }

}
