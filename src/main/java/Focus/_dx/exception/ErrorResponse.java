package Focus._dx.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * API 에러 응답 형식을 통일하기 위한 클래스
 * “에러를 항상 같은 모양으로 내보내는 통일된 응답 포맷”
 * 클라이언트가 에러를 일관되게 처리할 수 있도록 함
 *
 * 사용 예:
 * {
 *   "status": 404,
 *   "message": "WIG을 찾을 수 없습니다. ID: 999",
 *   "timestamp": "2025-10-30T15:30:00"
 * }
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {

    /**
     * HTTP 상태 코드
     * 예: 400 (Bad Request), 404 (Not Found), 500 (Internal Server Error)
     */
    private int status;

    /**
     * 에러 메시지
     * 사용자에게 보여줄 친절한 메시지
     */
    private String message;

    /**
     * 에러 발생 시각
     * 로그 추적에 유용
     */
    private LocalDateTime timestamp;

    /**
     * 정적 팩토리 메서드
     * ErrorResponse 객체를 쉽게 생성
     *
     * 사용 예:
     * ErrorResponse error = ErrorResponse.of(404, "리소스를 찾을 수 없습니다");
     */
    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, LocalDateTime.now());
    }
}
