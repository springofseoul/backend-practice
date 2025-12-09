package server.springofseoul.global.response.code;

import org.springframework.http.HttpStatus;

public interface BaseCode {
    HttpStatus getHttpStatus();
    boolean isSuccess();
    String getCode();
    String getMessage();
}
