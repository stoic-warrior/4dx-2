package Focus._dx.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // 이 클래스는 JPA가 관리하는 엔티티라는 뜻. 필드가 칼럼, 인스턴스가 로우가 된다.
@Getter @Setter // 각 필드에 대해 getter setter 메서드 자동 생성. DTO가 아니면 Setter은 위험해서 나중에 수정
@NoArgsConstructor // 기본성생자(매개변수 없음)를 생성. jpa는 프록시객체 사용하므로 이게 필수
@AllArgsConstructor // 모든 파라미터 존재하는 생성자. id는 DB에서 생성하므로 사실 생성자에선 빼야됨. 그러나 프로토타입에선 그냥 빨리 쓰고 나중에 수정하자
public class Wig {

    @Id // 이 필드를 PK로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id 자동증가
    private Long id;

    @Column(nullable = false, length = 100) // DB컬럼 세부지정,null값 불허, 길이 100자이하
    private String goal;

    @Column(length = 500) // 길이 500자 이하
    private String description;

}
