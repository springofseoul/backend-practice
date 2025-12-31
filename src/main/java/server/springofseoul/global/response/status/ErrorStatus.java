package server.springofseoul.global.response.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import server.springofseoul.global.response.code.BaseErrorCode;
import server.springofseoul.global.response.dto.ErrorReasonDto;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 전역 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"GLOBAL-500", "서버 내부 오류가 발생했습니다. 자세한 사항은 백엔드 팀에 문의하세요."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"GLOBAL-400", "입력 값이 잘못된 요청 입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"GLOBAL-401", "인증이 필요 합니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "GLOBAL-405", "허용되지 않은 요청 메소드입니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "GLOBAL-415", "지원되지 않는 미디어 타입입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .isSuccess(false)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
}
