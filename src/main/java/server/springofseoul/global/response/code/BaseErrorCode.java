package server.springofseoul.global.response.code;

import server.springofseoul.global.response.dto.ErrorReasonDto;

public interface BaseErrorCode extends BaseCode {
    public ErrorReasonDto getReason();

    public ErrorReasonDto getReasonHttpStatus();
}
