package com.weljak.welvet.webapi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WelVetResponseUtils {
    private WelVetResponseUtils() {
    }

    private static Clock clock = Clock.systemUTC();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static ResponseEntity<WelVetResponse> success(String endpoint, Object any, String message, HttpStatus httpStatus) {
        WelVetResponse response = successWelVetResponse(endpoint, any, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<WelVetResponse> error(String endpoint, String error, String message, HttpStatus httpStatus) {
        WelVetResponse response = errorWelVetResponse(endpoint, error, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<WelVetResponse> noContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static WelVetResponse successWelVetResponse(String endpoint, Object any, String message, HttpStatus httpStatus) {
        return WelVetResponse.builder()
                .timeStamp(timeStamp())
                .path(endpoint)
                .responseCode(httpStatus.value())
                .message(message)
                .payload(any)
                .success(true)
                .status(httpStatus.getReasonPhrase())
                .build();
    }

    public static WelVetResponse errorWelVetResponse(String endpoint, String error, String message, HttpStatus httpStatus) {
        return WelVetResponse.builder()
                .timeStamp(timeStamp())
                .path(endpoint)
                .responseCode(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .success(false)
                .message(message)
                .error(error)
                .build();
    }

    private static String timeStamp() {
        LocalDateTime now = LocalDateTime.now(clock);
        return formatter.format(now);
    }
}
