package com.spring.mvc.chap05.service;

import com.spring.mvc.chap05.common.Page;
import com.spring.mvc.chap05.common.PageMaker;
import com.spring.mvc.chap05.dto.request.ReplyModifyRequestDTO;
import com.spring.mvc.chap05.dto.request.ReplyPostRequestDTO;
import com.spring.mvc.chap05.dto.response.ReplyDetailResponseDTO;
import com.spring.mvc.chap05.dto.response.ReplyListResponseDTO;
import com.spring.mvc.chap05.entity.Reply;
import com.spring.mvc.chap05.mapper.ReplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

	private final ReplyMapper mapper;

	public void register(ReplyPostRequestDTO dto) {
		mapper.save(dto.toEntity());
	}

	public ReplyListResponseDTO getList(int boardNo, Page page) {
		List<ReplyDetailResponseDTO> dtoList = new ArrayList<>();
		// DB에서 댓글 정보 조회
		List<Reply> replyList = mapper.findAll(boardNo, page);

		for (Reply reply : replyList) {
			dtoList.add(new ReplyDetailResponseDTO(reply));
		}

		// DB에서 총 댓글 개수 조회
		int count = mapper.count(boardNo);

		// 댓글 목록을 페이징 해야 하기 때문에 좀 더 여러개의 정보를 화면단으로 넘겨야 한다.
		// 그래서 DTO를 새롭게 생성함. PageMaker도 함께 보내고 있다.
		return ReplyListResponseDTO.builder()
				.replies(dtoList)
				.count(count)
				.pageInfo(new PageMaker(page, count)) // 객체 생성시 page, count를 전달하면 페이징 알고리즘이 돌아감
				.build();
	}

	public void modify(ReplyModifyRequestDTO dto) {
		Reply reply = dto.toEntity();

		mapper.modify(reply);
	}

	public void delete(int rno) {
		mapper.delete(rno);
	}
}
