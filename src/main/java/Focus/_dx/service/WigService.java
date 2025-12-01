package Focus._dx.service;


import Focus._dx.domain.Wig;
import Focus._dx.dto.WigRequestDto;
import Focus._dx.dto.WigResponseDto;
import Focus._dx.exception.WigNotFoundException;
import Focus._dx.repository.WigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        Wig savedWig = wigRepository.save(wig); // JPA가 INSERT 쿼리 날리고, id 값 채워진 savedWig 반환.

        // 3. Entity를 DTO로 변환해서 반환
        log.info("WIG 생성 완료 - id: {}", savedWig.getId());
        return WigResponseDto.from(savedWig); // DB 엔티티를 그대로 Client에 노출하지 않고, 응답용 DTO로 감싸서 반환.
    }

    /**
     * 모든 WIG 조회
     * DB에서 Wig 전부 뽑아서, 응답용 DTO 리스트로 변환해서 돌려준다.
     */
    public List<WigResponseDto> getAllWigs() {
        log.info("전체 WIG 목록 조회");

        return wigRepository.findAll() //DB에서 Wig 엔티티 전체를 가져오는 JPA 메서드, 반환 타입: List<Wig>
                .stream() // 리스트를 Stream으로 변환, Stream<Wig>, “흐름”으로 하나씩 처리할 수 있게 만든다.
                .map(WigResponseDto::from)  // map() : stream 요소를 변환하는 메서드. from메서드에 wig를 넣고 반환
                .collect(Collectors.toList()); // Stream → List로 다시 모으는 과정.
    }

    /**
     * ID로 WIG 조회
     * ID로 찾고, 없으면 404 던지고, 있으면 DTO로 감싸서 준다.
     * “조회 + 없으면 커스텀 예외”
     */
    public WigResponseDto getWigById(Long id) {
        log.info("WIG 조회 - id: {}", id);

        Wig wig = wigRepository.findById(id)
                .orElseThrow(() -> new WigNotFoundException(id)); // 해당 ID가 없으면, 커스텀 예외를 터뜨림. 이 예외는 GlobalExceptionHandler가 404로 변환.

        return WigResponseDto.from(wig);
    }

    /**
     * WIG 수정
     * 엔티티 하나 영속 상태로 가져온 뒤, setter로 값만 바꾸고 트랜잭션 끝날 때 JPA가 알아서 UPDATE 날린다. 이게 dirty checking 패턴.
     * 이 패턴은 나중에 도메인 메서드 기반 변경(ex. wig.updateGoalAndDescription(...))으로 발전시킬 수 있음.
     */
    @Transactional
    public WigResponseDto updateWig(Long id, WigRequestDto requestDto) {
        log.info("WIG 수정 시작 - id: {}", id);

        // 1. 기존 WIG 조회
        Wig wig = wigRepository.findById(id) // 조회된 wig는 영속성 컨텍스트가 관리하는 상태
                .orElseThrow(()-> new WigNotFoundException(id));

        // 2. 필드 업데이트
        wig.setGoal(requestDto.getGoal());
        wig.setDescription(requestDto.getDescription());

        // 3. @Transactional + JPA 영속성 컨텍스트 덕분에
        //    save()를 명시적으로 호출하지 않아도 자동으로 UPDATE 쿼리 실행
        //    (Dirty Checking - 변경 감지)

        log.info("WIG 수정 완료 - id: {}", id);
        return WigResponseDto.from(wig);
    }

    /**
     * WIG 삭제
     * 존재 확인 → 없으면 404 → 있으면 삭제. 한 트랜잭션 안에서 깔끔하게 처리
     */
    @Transactional
    public void deleteWig(Long id) {
        log.info("WIG 삭제 시작 - id: {}", id);

        // 1. 존재하는지 확인
        if (!wigRepository.existsById(id)) {
            throw new WigNotFoundException(id);
        }

        // 2. 삭제
        wigRepository.deleteById(id);

        log.info("WIG 삭제 완료 - id: {}", id);
    }
    /**
     * 특정 키워드로 WIG 검색 (추가 기능 예시)
     * Repository에 메서드를 추가해야 함
     */
    // public List<WigResponseDto> searchWigs(String keyword) {
    //     log.info("WIG 검색 - keyword: {}", keyword);
    //
    //     return wigRepository.findByGoalContaining(keyword).stream()
    //             .map(WigResponseDto::from)
    //             .collect(Collectors.toList());
    // }
}
