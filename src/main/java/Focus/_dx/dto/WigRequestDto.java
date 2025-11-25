package Focus._dx.dto;

/*
WIG 생성/수정 요청 DTO
클라이언트 → 서버 간 데이터 전달을 담당
Entity는 DB와 직접 연결되므로 위험, DTO로 요청을 받고 검증한 후에 Entity로 변환
 */

import Focus._dx.domain.Wig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * WIG 생성/수정 요청 DTO
 * Entity를 직접 노출하지 않고 API 요청을 받는 객체
 */
@Getter // 캡슐화(private 사용)
@NoArgsConstructor // 기본 생성자 자동 생성. JSON 역직렬화에 필요. HTTP로 전달받은 json을 자바 인스턴스의 필드로 주입. 잭슨이 리플렉션을 활용
@AllArgsConstructor // 모든 필드를 포함한 생성자 자동 생성, 테스트나 직접 객체 생성 시 유용
public class WigRequestDto {

    @NotBlank(message = "목표는 필수입니다")
    @Size(max=100, message = "목표는 100자 이하여야 합니다")
    private String goal;

    @Size(max=500, message = "설명은 500자 이하여야 합니다")
    private String description;

    /*
    컨트롤러의 요청 DTO → JPA 엔티티 변환
     */
    public Wig toEntity() {
        Wig wig = new Wig();
        wig.setGoal(this.goal);
        wig.setDescription(this.description); // this = 이 인스턴스의 필드
        return wig;
    }
}
