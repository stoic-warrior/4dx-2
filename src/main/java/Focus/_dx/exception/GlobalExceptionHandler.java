package Focus._dx.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 핸들러
 * 모든 Controller에서 발생하는 예외를 한 곳에서 처리
 * “컨트롤러 전체에 공통 방패를 씌우고, 예외마다 다른 방패를 들이대는 클래스”
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 */
@RestControllerAdvice // 프로젝트 전체 Controller에서 발생하는 예외를 가로채, JSON응답
public class GlobalExceptionHandler {

    /**
     * WigNotFoundException 처리
     * WIG을 찾을 수 없을 때 404 Not Found 응답
     */
    @ExceptionHandler(WigNotFoundException.class) // Controller 어느 곳에서든 WigNotFoundException 터지면 → 이 메서드가 자동 실행됨
    public ResponseEntity<ErrorResponse> handleWigNotFound(WigNotFoundException ex) {
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);

    }

    /**
     * Validation 실패 처리
     * @Valid 어노테이션으로 검증 실패 시 400 Bad Request 응답
     *
     * 예: @Size(min=5) 위반 시
     */
    @ExceptionHandler(MethodArgumentNotValidException.class) // @Valid 검증 실패
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("message", "입력값 검증 실패");

        // 각 필드별 에러 메시지 수집
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        errors.put("errors", fieldErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    /**
     * 그 외 모든 예외 처리
     * 예상하지 못한 에러 발생 시 500 Internal Server Error 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "서버 내부 오류가 발생했습니다: " + ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
