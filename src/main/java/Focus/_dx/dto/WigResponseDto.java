package Focus._dx.dto;

import Focus._dx.domain.Wig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * WIG 응답 DTO
 * Entity를 직접 노출하지 않고 API 응답으로 내보내는 객체
 * 클라이언트에게 필요한 정보만 선택적으로 노출
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder // 필드가 많은 객체를 안전하고 가독성있게 생성하는 디자인 패턴
public class WigResponseDto {

    private Long id;
    private String goal;
    private String description;
    // 나중에 추가할 필드들
    //private LocalDateTime createdAt;
    //private LocalDateTime updatedAt;


    /**
     * Entity → DTO 변환 정적 팩토리 메서드
     * Entity에서 DTO를 생성할 때 사용
     */
    public static WigResponseDto from(Wig wig) {
        return WigResponseDto.builder()
                .id(wig.getId())
                .goal(wig.getGoal())
                .description(wig.getDescription())
                .build();
    }
}
