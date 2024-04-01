package com.spring.mvc.chap05.service;

import com.spring.mvc.chap05.dto.request.ReplyPostRequestDTO;
import com.spring.mvc.chap05.dto.response.ReplyListResponseDTO;
import com.spring.mvc.chap05.entity.Reply;
import com.spring.mvc.chap05.mapper.ReplyMapper;
import lombok.RequiredArgsConstructor;
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

	public List<ReplyListResponseDTO> getList(int boardNo) {
		List<ReplyListResponseDTO> dtoList = new ArrayList<>();
		List<Reply> replyList = mapper.findAll(boardNo);

		for (Reply reply : replyList) {
			dtoList.add(new ReplyListResponseDTO(reply));
		}

		return dtoList;
	}


}
