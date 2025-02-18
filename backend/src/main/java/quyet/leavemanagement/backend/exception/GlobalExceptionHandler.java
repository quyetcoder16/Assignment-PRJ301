package quyet.leavemanagement.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import quyet.leavemanagement.backend.dto.response.base.ApiResponse;
import org.springframework.security.access.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * handling any exceptions that are not covered by other exception handlers.
     *
     * @param e
     * @param request
     * @return A ResponseEntity containing the error response information.
     */
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<ApiResponse> handlingOtherException(Exception e, HttpServletRequest request) {
//        ApiResponse response = new ApiResponse();
//        response.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
//        response.setStatusCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getErrorCode());
//        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getHttpStatusCode()).body(response);
//    }

    // handling access denied exception (forbidden)
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .statusCode(errorCode.getErrorCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    //handling app exception
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException(AppException e, HttpServletRequest request) {
        ErrorCode errorCode = e.getErrorCode();

        ApiResponse response = new ApiResponse();
        response.setMessage(errorCode.getMessage());
        response.setStatusCode(errorCode.getErrorCode());

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(response);
    }
}
