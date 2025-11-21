package server.springofseoul.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import server.springofseoul.global.response.ApiResponse;
import server.springofseoul.global.response.status.ErrorStatus;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 커스텀 예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        logError("CustomException", e);
        return ApiResponse.onFailure(e.getErrorCode());
    }

    // Security 인증 관련 (java.lang.SecurityException)
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiResponse<Void>> handleSecurityException(SecurityException e) {
        logError("SecurityException", e);
        return ApiResponse.onFailure(ErrorStatus.UNAUTHORIZED);
    }

    // 내부 서버 에러 (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        logError("Unhandled exception", e);
        return ApiResponse.onFailure(ErrorStatus.INTERNAL_SERVER_ERROR);
    }

    // ====== Validation / 요청 형식 관련 필수 예외 ======

    // @Valid RequestBody 필드 검증 실패
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String combinedErrors = extractFieldErrors(ex.getBindingResult().getFieldErrors());
        logError("Validation error", combinedErrors);
        return toObjectResponse(ApiResponse.onFailure(ErrorStatus.BAD_REQUEST, combinedErrors));
    }

    // @Validated 쿼리 파라미터/PathVariable 검증 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationParameterError(ConstraintViolationException ex) {
        String errorMessage = ex.getMessage();
        logError("ConstraintViolationException", errorMessage);
        return ApiResponse.onFailure(ErrorStatus.BAD_REQUEST, errorMessage);
    }

    // 필수 쿼리 파라미터 누락
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String errorMessage = "필수 파라미터 '" + ex.getParameterName() + "'가 없습니다.";
        logError("MissingServletRequestParameterException", errorMessage);
        return toObjectResponse(ApiResponse.onFailure(ErrorStatus.BAD_REQUEST, errorMessage));
    }

    // JSON 파싱/역직렬화 실패 (enum 값 포함)
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        if (ex.getCause() instanceof InvalidFormatException formatException) {
            if (formatException.getTargetType().isEnum()) {
                String errorMessage =
                        "올바르지 않은 enum 값입니다. 허용되지 않은 값: " + formatException.getValue();
                logError("Invalid enum value", errorMessage);
                return toObjectResponse(
                        ApiResponse.onFailure(ErrorStatus.BAD_REQUEST, errorMessage)
                );
            }
        }
        String errorMessage = "요청 본문이 잘못되었습니다.";
        logError("HttpMessageNotReadableException", errorMessage);
        return toObjectResponse(ApiResponse.onFailure(ErrorStatus.BAD_REQUEST, errorMessage));
    }

    // 지원하지 않는 HTTP 메소드
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String errorMessage = "지원하지 않는 HTTP 메소드 요청입니다: " + ex.getMethod();
        logError("HttpRequestMethodNotSupportedException", errorMessage);
        return toObjectResponse(ApiResponse.onFailure(ErrorStatus.METHOD_NOT_ALLOWED, errorMessage));
    }

    // 지원하지 않는 Media Type
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String errorMessage = "지원하지 않는 미디어 타입입니다: " + ex.getContentType();
        logError("HttpMediaTypeNotSupportedException", errorMessage);
        return toObjectResponse(ApiResponse.onFailure(ErrorStatus.UNSUPPORTED_MEDIA_TYPE, errorMessage));
    }

    // ====== private 유틸 메서드 ======

    private String extractFieldErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }

    private void logError(String message, Object errorDetails) {
        if (errorDetails instanceof Throwable t) {
            log.error("{}: {}", message, t.getMessage());
        } else {
            log.error("{}: {}", message, errorDetails);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseEntity<Object> toObjectResponse(ResponseEntity<ApiResponse<T>> response) {
        // ResponseEntity<ApiResponse<T>> -> ResponseEntity<Object> 로 캐스팅
        return (ResponseEntity<Object>) (ResponseEntity<?>) response;
    }
}
