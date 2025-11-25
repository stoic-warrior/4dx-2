package Focus._dx.exception;

/*
DB나 리스트에서 특정 WIG목표 못찾았을때 던지는 예외 (데이터베이스에서 id = X인 WIG을 못 찾으면 터뜨릴 예외.)
RuntimeException을 상속받아 Unchecked Exception으로 만듦 (RuntimeException 기반 → Unchecked(선언/처리 강제 없음))
 */
public class WigNotFoundException extends RuntimeException{ // 보통 “비즈니스 예외”는 Runtime으로 만든

    public WigNotFoundException(Long id) {
        super("WIG을 찾을 수 없습니다. ID: " + id); // super()는 부모 클래스의 생성자를 호출
    }

    public WigNotFoundException(String message) {
        super(message);
    }
}
