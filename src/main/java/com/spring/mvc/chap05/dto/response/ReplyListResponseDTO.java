package com.spring.mvc.chap05.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.mvc.chap05.entity.Reply;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyListResponseDTO {

	private int rno;
	private String text;
	private String writer;

	// 나중에 DTO가 JSON으로 변환될 때 원하는 Format 형식으로 자동 변환
	@JsonFormat(pattern = "yyyy년 MM월 dd일 HH:mm")
	private LocalDateTime regDate;


	// entity를 DTO로 바꿔주는 생성자
	public ReplyListResponseDTO(Reply reply) {
		this.rno = reply.getReplyNo();
		this.text = reply.getReplyText();
		this.writer = reply.getReplyWriter();
		this.regDate = reply.getReplyDate();
	}
}
