package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.Board;
import org.zerock.b01.repository.search.BoardSearch;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch { //엔티티 타입과 pk데이터유형을 지정해야함

    @Query(value = "select now()", nativeQuery = true)
    String getTime();

    @EntityGraph(attributePaths = {"imageSet"}) //attributePaths : 같이 로딩해야하는 속성 명시 - Board객체 로딩시 imageSet엔티티도 같이 로딩
    @Query("select b from Board b where b.bno =:bno") //JPQL - =: JPQL에서 매개변수 바인딩을 위해 사용하는 문법
    //매개변수로 받은 bno가 =:bno 에 담겨 처리됨
    Optional<Board> findIdByWithImages(Long bno); //Board객체와 BoardImage객체를 같이 조회


}
