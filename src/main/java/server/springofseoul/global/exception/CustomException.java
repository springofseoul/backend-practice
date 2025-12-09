package server.springofseoul.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.springofseoul.global.response.code.BaseErrorCode;

@Getter
public class CustomException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public CustomException(BaseErrorCode errorCode) {
        super(errorCode.getMessage()); // RuntimeException(message) 세팅
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    public String getCode() {
        return errorCode.getCode();
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
