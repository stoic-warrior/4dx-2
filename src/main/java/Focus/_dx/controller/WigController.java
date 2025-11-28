package Focus._dx.controller;

import Focus._dx.dto.WigRequestDto;
import Focus._dx.dto.WigResponseDto;
import Focus._dx.service.WigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * WIG REST API Controller
 *
 * 역할: HTTP 요청을 받아 Service로 전달하고, 응답을 반환
 * - 요청 검증 (@Valid)
 * - Service 호출
 * - HTTP 상태 코드 설정
 *
 * @RestController = @Controller + @ResponseBody
 * @RequestMapping: 모든 메서드의 기본 경로를 /api/wigs로 설정
 * @RequiredArgsConstructor: final 필드 생성자 주입 (서비스 필드)
 */
@RestController
@RequestMapping("/api/wigs")
@RequiredArgsConstructor
public class WigController {

    private final WigService wigService; // 직접 Repository를 쓰지 않고, WigService에게 일을 맡김.

    /**
     * WIG 생성
     * POST /api/wigs
     *
     * @param requestDto 생성할 WIG 정보
     * @return 201 Created + 생성된 WIG 정보
     */
    @PostMapping
    public ResponseEntity<WigResponseDto> createWig(
            @RequestBody @Valid WigRequestDto requestDto) {
        // @RequestBody → HTTP 요청 바디(JSON)를 WigRequestDto로 변환. (역직렬화)
        // @Valid → DTO에 있는 @NotBlank, @Size 등을 자동 검사.
        //          검증 실패 시 Controller까지 가지 않고 MethodArgumentNotValidException 발생 → GlobalExceptionHandler에서 처리.

        WigResponseDto response = wigService.createWig(requestDto); //Service에게 "이 DTO로 WIG 하나 만들어줘"라고 요청

        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 Created
                .body(response); //  생성된 WIG 정보(WigResponseDto)를 JSON으로 리턴.
    }

    /**
     * 전체 WIG 목록 조회
     * GET /api/wigs
     *
     * @return 200 OK + WIG 목록
     */
    @GetMapping
    public ResponseEntity<List<WigResponseDto>> getAllWigs() {
        List<WigResponseDto> wigs = wigService.getAllWigs(); // Service가 Repository에서 전체 목록 찾아서 List<WigResponseDto>로 변환.
        return ResponseEntity.ok(wigs); // 상태코드: 200 OK,  바디: WIG 리스트(JSON 배열)
    }

    /**
     * ID로 WIG 조회
     * GET /api/wigs/{id}
     *
     * @param id WIG ID
     * @return 200 OK + WIG 정보 (또는 404 Not Found)
     */
    @GetMapping("/{id}")   // 실제 URL: GET /api/wigs/1, GET /api/wigs/10 이런 형식.
    public ResponseEntity<WigResponseDto> getWigById(@PathVariable Long id) {
        // @PathVariable Long id : {id} URL에 들어있는 값이 Long id 파라미터로 들어옴
        WigResponseDto wig = wigService.getWigById(id);
        // Service에서 findById 호출.
        // 못 찾으면 WigNotFoundException 터짐 → GlobalExceptionHandler가 404로 변환.
        return ResponseEntity.ok(wig);
        // 200 OK + 단건(single) WIG DTO 반환.
    }

    /**
     * WIG 수정
     * PUT /api/wigs/{id}
     *
     * @param id 수정할 WIG ID
     * @param requestDto 수정할 내용
     * @return 200 OK + 수정된 WIG 정보
     */
    @PutMapping("/{id}")
    public ResponseEntity<WigResponseDto> updateWig(
            @PathVariable Long id,
            @RequestBody @Valid WigRequestDto requestDto) {

        WigResponseDto updated = wigService.updateWig(id, requestDto);
        /*
        Service가
            해당 WIG 조회 (없으면 예외 → 404)
            필드 수정
            트랜잭션 + JPA 변경감지로 UPDATE 수행
         */
        return ResponseEntity.ok(updated); // 수정된 결과를 200 OK로 응답.
    }

    /**
     * WIG 삭제
     * DELETE /api/wigs/{id}
     *
     * @param id 삭제할 WIG ID
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWig(@PathVariable Long id) {
        wigService.deleteWig(id);
        /*
        Service가 존재 여부 확인 후 삭제.
        존재하지 않으면 WigNotFoundException → 404 응답.
         */
        return ResponseEntity.noContent().build(); // 상태코드: 204 No Content, 바디 없음. (삭제 성공 시 보통 이렇게)
    }

    /**
     * 추가 기능 예시: 키워드 검색
     * GET /api/wigs/search?keyword=운동
     */
    // @GetMapping("/search")
    // public ResponseEntity<List<WigResponseDto>> searchWigs(
    //         @RequestParam String keyword) {
    //     List<WigResponseDto> wigs = wigService.searchWigs(keyword);
    //     return ResponseEntity.ok(wigs);
    // }
}
