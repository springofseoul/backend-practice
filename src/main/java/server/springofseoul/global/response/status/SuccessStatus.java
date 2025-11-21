package server.springofseoul.global.response.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import server.springofseoul.global.response.code.BaseCode;

@Getter
@RequiredArgsConstructor
public enum SuccessStatus implements BaseCode {

    OK(HttpStatus.OK, "GLOBAL-200", "요청 응답에 성공했습니다."),
    CREATED(HttpStatus.CREATED, "GLOBAL-201", "생성에 성공했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public boolean isSuccess() {
        return true;
    }
}
