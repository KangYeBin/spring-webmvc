package com.spring.mvc.chap05.dto.request;

import com.spring.mvc.chap05.entity.Reply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class ReplyPostRequestDTO {

	// NotNull : null만 안 됨, 빈 문자열은 됨
	// NotBlank : null, 빈 문자열 모두 안 됨

	@NotBlank
	@Size(min = 1, max = 300)
	private String text;    // 댓글 내용

	@NotBlank
	@Size(min = 2, max = 8)
	private String author;    // 댓글 작성자

	@NotNull
	private int bno;        // 원본 글 번호


	// dto를 Entity로 바꾸는 변환 메서드
	public Reply toEntity() {
		return Reply.builder()
				.replyText(text)
				.replyWriter(author)
				.boardNo(bno)
				.build();
	}


}
