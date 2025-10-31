package Focus._dx.repository;

import Focus._dx.domain.Wig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WigRepository extends JpaRepository<Wig,Long> { // Jpa가 자동으로 구현체 주입, CRUD기능 제공,  Wig엔티티를 Long타입 PK로 관리하는 레포지토리 인터페이스
}
