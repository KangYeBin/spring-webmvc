package com.spring.mvc.chap05.dto.response;

import com.spring.mvc.chap05.common.PageMaker;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyListResponseDTO {

	private int count;	// 댓글 수
	private PageMaker pageInfo;	// 페이징 정보
	private List<ReplyDetailResponseDTO> replies;	// 댓글 리스트

}
