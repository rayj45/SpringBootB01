package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;

public interface BoardSearch {

    Page<Board> search1(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable); //검색유형으로는 여러개가 올수있으므로 배열처리

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);

//    Page<BoardListReplyCountDTO> searchWithALl(String[] types, String keyword, Pageable pageable);
    Page<BoardListAllDTO> searchWithALl(String[] types, String keyword, Pageable pageable);

}
