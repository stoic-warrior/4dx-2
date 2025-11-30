package Focus._dx.service;


import Focus._dx.domain.Wig;
import Focus._dx.dto.WigRequestDto;
import Focus._dx.dto.WigResponseDto;
import Focus._dx.repository.WigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * WIG 비즈니스 로직을 처리하는 Service 계층
 *
 * WigService는 스프링이 관리하는 비즈니스 로직 클래스고, 기본은 읽기 전용 트랜잭션, DB 접근은 WigRepository 한 놈으로만, 의존성은 생성자로 주입, 로그는 log로 찍는다.
 *
 * @Service: 스프링이 이 클래스를 빈으로 관리
 * @Transactional: 메서드 실행 중 예외 발생 시 자동 롤백
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성 (의존성 주입)
 * @Slf4j: 로그 기능 사용 (log.info(), log.error() 등)
 */
@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용 (성능 최적화), 쓰기 작업이 필요한 메서드만 위에 @Transactional 다시 붙여서 readOnly 해제.
@RequiredArgsConstructor
@Slf4j
public class WigService {

    private final WigRepository wigRepository;

    /**
     * WIG 생성
     * 트랜잭션 하나 열고, DTO를 Wig 엔티티로 만들고, DB에 저장하고, 다시 DTO로 포장해서 내보낸다.
     *
     * @Transactional: 쓰기 작업이므로 readOnly = false (기본값)
     *
     */
    @Transactional
    public WigResponseDto createWig(WigRequestDto requestDto) {
        log.info("WIG 생성 시작 - goal: {}", requestDto.getGoal()); // 콘솔에 로그 뜸, logback 설정하면 파일로도 남음, 문제 추적할때 필요

        // 1. DTO를 Entity로 변환
        Wig wig = requestDto.toEntity();

        // 2. DB에 저장
        Wig savedWig = wigRepository.save(wig);

        // 3. Entity를 DTO로 변환해서 반환
        log.info("WIG 생성 완료 - id: {}", savedWig.getId());
        return WigResponseDto.from(savedWig);
    }
}
