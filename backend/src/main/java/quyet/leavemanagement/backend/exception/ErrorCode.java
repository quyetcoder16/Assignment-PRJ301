package quyet.leavemanagement.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //    General Errors from 1000 -> 1999
    UNCATEGORIZED_EXCEPTION(1999, "Uncategorized Exception error!", HttpStatus.INTERNAL_SERVER_ERROR),
    TOKEN_CANNOT_CREATE_EXCEPTION(1001, "Token cannot generate!", HttpStatus.INTERNAL_SERVER_ERROR),

    //   Authentication Errors from 2000->2999
    UNAUTHENTICATED(2000, "Unauthenticated error!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2001, "You do not have permission!", HttpStatus.FORBIDDEN),
    EMAIL_OR_PASSWORD_NOT_MATCH(2002, "Email or Password does not match!", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(2003, "User not found!", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED_EXCEPTION(1002, "Token expired!", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_STILL_VALID(1003, "Access token still valid!", HttpStatus.UNAUTHORIZED),


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
