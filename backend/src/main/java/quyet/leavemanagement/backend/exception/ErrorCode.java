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
    USER_NOT_FOUND(2003, "User not found!", HttpStatus.NOT_FOUND),
    TOKEN_EXPIRED_EXCEPTION(1002, "Token expired!", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_STILL_VALID(1003, "Access token still valid!", HttpStatus.UNAUTHORIZED),
    EMAIL_NOT_FOUND(1004, "Email not found!", HttpStatus.NOT_FOUND),
    INVALID_OTP(1005, "Invalid OTP!", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(1006, "OTP expired!", HttpStatus.BAD_REQUEST),
    EMAIL_SEND_FAILED(1007, "Email send failed!", HttpStatus.INTERNAL_SERVER_ERROR),

    // leave request errors from 3000->3999
    LEAVE_REQUEST_NOT_FOUND(3000, "Leave request not found!", HttpStatus.NOT_FOUND),
    REQUEST_STATUS_NOT_FOUND(3001, "Request status not found!", HttpStatus.NOT_FOUND),
    TYPE_LEAVE_NOT_FOUND(3002, "Type leave not found!", HttpStatus.NOT_FOUND),
    START_DATE_INVALID(3003, "Start date must be from today onwards", HttpStatus.BAD_REQUEST),
    END_DATE_INVALID(3004, "End date must be greater than or equal to start date", HttpStatus.BAD_REQUEST),
    LEAVE_DATE_ALREADY_EXISTS(3005, "Leave date already exists!", HttpStatus.BAD_REQUEST),
    REQUEST_NOT_FOUND(3006, "Request not found!", HttpStatus.NOT_FOUND),
    INVALID_ACTION(3007, "Invalid action!", HttpStatus.BAD_REQUEST),
    DO_NOT_EDIT_LEAVE_IN_THE_PASS(3008, "Cannot edit past leave request!", HttpStatus.BAD_REQUEST),
    REQUEST_ALREADY_PROCESSED(3009, "Request already processed!", HttpStatus.BAD_REQUEST),
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
