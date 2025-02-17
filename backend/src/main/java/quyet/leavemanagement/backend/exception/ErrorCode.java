package quyet.leavemanagement.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //    General Errors from 1000 -> 1999
    UNCATEGORIZED_EXCEPTION(1999, "Uncategorized Exception error!", HttpStatus.INTERNAL_SERVER_ERROR),
    //   Authentication Errors from 2000->2999
    UNAUTHENTICATED(2000, "Unauthenticated error!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2001, "You do not have permission!", HttpStatus.FORBIDDEN),

    // leave request errors from 3000->3999
    LEAVE_REQUEST_NOT_FOUND(3000, "Leave request not found!", HttpStatus.NOT_FOUND),

    ;
    private int errorCode;
    private String message;
    private HttpStatus httpStatusCode;

    ErrorCode(int errorCode, String message, HttpStatus httpStatusCode) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
