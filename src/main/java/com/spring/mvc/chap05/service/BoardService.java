package com.spring.mvc.chap05.service;

import com.spring.mvc.chap05.common.Search;
import com.spring.mvc.chap05.dto.request.BoardWriteRequestDTO;
import com.spring.mvc.chap05.dto.response.BoardDetailResponseDTO;
import com.spring.mvc.chap05.dto.response.BoardListResponseDTO;
import com.spring.mvc.chap05.entity.Board;
import com.spring.mvc.chap05.mapper.BoardMapper;
import com.spring.mvc.util.LoginUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.spring.mvc.util.LoginUtils.*;

@Service
@RequiredArgsConstructor
public class BoardService {

//	mybatis가 우리가 만든 xml을 클래스로 변환해서 객체를 주입
	private final BoardMapper mapper;

	public void register(BoardWriteRequestDTO dto, HttpSession session) {
		Board board = new Board(dto);
		// 이제 화면단에서 작성자가 전달되지 않으므로
		// 세션에서 현재 로그인 중인 사용자의 아이디를 얻어와서 세팅
		board.setWriter(getCurrentLoginMemberAccount(session));
		mapper.save(board);
	}

	// mapper로부터 전달받은 entity List를 DTO List로 변환해서 컨트롤러에게 리턴
	public List<BoardListResponseDTO> getList(Search page) {
		List<BoardListResponseDTO> dtoList = new ArrayList<>();
		List<Board> boardList = mapper.findAll(page);

		System.out.println("boardList = " + boardList);
		for (Board board : boardList) {
			BoardListResponseDTO dto = new BoardListResponseDTO(board);
			dtoList.add(dto);
		}
		return dtoList;
	}

	public BoardDetailResponseDTO getDetail(int bno) {
		// 상세 보기니까 조회수를 하나 올려주는 처리
		mapper.updateViewCount(bno);

		Board board = mapper.findOne(bno);
		return new BoardDetailResponseDTO(board);
	}


	public void delete(int bno) {
		mapper.delete(bno);
	}

	public int getCount(Search page) {
		return mapper.getCount(page);
	}
}
