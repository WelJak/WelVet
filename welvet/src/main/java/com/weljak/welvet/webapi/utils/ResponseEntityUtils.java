package com.weljak.welvet.webapi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtils {
    private static ResponseEntityUtils ResponseEntityUtilsInstance = null;

    private ResponseEntityUtils() {
    }

    public static ResponseEntityUtils getInstance() {
        if (ResponseEntityUtilsInstance == null) ResponseEntityUtilsInstance = new ResponseEntityUtils();
        return ResponseEntityUtilsInstance;
    }

    public static <T> ResponseEntity<T> toResponseEntity(T data, HttpStatus status) {
        return new ResponseEntity(data, status);
    }
}
