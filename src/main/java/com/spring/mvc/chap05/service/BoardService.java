package com.spring.mvc.chap05.service;

import com.spring.mvc.chap05.dto.request.BoardWriteRequestDTO;
import com.spring.mvc.chap05.dto.response.BoardDetailResponseDTO;
import com.spring.mvc.chap05.dto.response.BoardListResponseDTO;
import com.spring.mvc.chap05.entity.Board;
import com.spring.mvc.chap05.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository repository;

	public void register(BoardWriteRequestDTO dto) {
		Board board = new Board(dto);
		repository.save(board);
	}

	// repository로부터 전달받은 entity List를 DTO List로 변환해서 컨트롤러에게 리턴
	public List<BoardListResponseDTO> getList() {
		List<BoardListResponseDTO> dtoList = new ArrayList<>();
		List<Board> boardList = repository.findAll();

		for (Board board : boardList) {
			BoardListResponseDTO dto = new BoardListResponseDTO(board);
			dtoList.add(dto);
		}
		return dtoList;
	}

	public BoardDetailResponseDTO getDetail(int bno) {
		// 상세 보기니까 조회수를 하나 올려주는 처리
		repository.updateViewCount(bno);

		Board board = repository.findOne(bno);
		return new BoardDetailResponseDTO(board);
	}

	public void delete(int bno) {
		repository.delete(bno);
	}
}
