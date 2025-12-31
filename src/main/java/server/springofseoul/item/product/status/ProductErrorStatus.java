package server.springofseoul.item.product.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import server.springofseoul.global.response.code.BaseErrorCode;
import server.springofseoul.global.response.dto.ErrorReasonDto;

@Getter
@RequiredArgsConstructor
public enum ProductErrorStatus implements BaseErrorCode {
    _INVALID_PRODUCT(HttpStatus.BAD_REQUEST, "PRODUCT-001", "유효하지 않은 상품입니다.");

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
