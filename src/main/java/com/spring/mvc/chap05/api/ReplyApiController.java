package com.spring.mvc.chap05.api;

import com.spring.mvc.chap05.common.Page;
import com.spring.mvc.chap05.dto.request.ReplyModifyRequestDTO;
import com.spring.mvc.chap05.dto.request.ReplyPostRequestDTO;
import com.spring.mvc.chap05.dto.response.ReplyDetailResponseDTO;
import com.spring.mvc.chap05.dto.response.ReplyListResponseDTO;
import com.spring.mvc.chap05.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API URL 설계 원칙
 * - CRUD는 URL에 명시하는게 아니라 HTTP method로만 표현해야 함!!
 * => /replies/write      (X)
 * => /replies   :  POST  (O)
 * <p>
 * => /replies/all        (X)   - 전체조회
 * => /replies   :  GET   (O)   - 전체조회
 * => /replies/17  : GET        - 단일조회
 * <p>
 * => /replies/delete?replyNo=3   (X)
 * => /replies/3    :   DELETE    (O)
 */

@RestController    // @Controller + 메서드마다 @ResponseBody를 붙인 것과 동일한 효과
@RequestMapping("/api/v1/replies")
@RequiredArgsConstructor
public class ReplyApiController {

	private final ReplyService replyService;

	// 댓글 목록 조회 요청
	// URL : /api/v1/replies/글번호/page/페이지번호
	@GetMapping("/{boardNo}/page/{pageNo}")
	public ResponseEntity<?> list(@PathVariable int boardNo, @PathVariable int pageNo) {
		System.out.println("/api/v1/replies/" + boardNo + ": GET!!");
		System.out.println("pageNo = " + pageNo);

		Page page = new Page();
		page.setPageNo(pageNo);
		page.setAmount(5);

		ReplyListResponseDTO replies = replyService.getList(boardNo, page);

		return ResponseEntity.ok().body(replies);
	}

	// RequestParam : 동기요청에서 ?뒤에 붙은 파라미터
	// RequestBody : 비동기요청에서 요청 객체 바디 안에 있는 JSON을 파싱
	@PostMapping
	@ResponseBody
	public ResponseEntity<?> create(@Validated @RequestBody ReplyPostRequestDTO dto
			, BindingResult result) { // 검증 결과 메시지를 갖는 객체

		// 입력값 검증에 걸리면 400번 status와 함께 메시지를 클라이언트로 전송
		if (result.hasErrors()) {

			// ResponseEntity는 응답에 관련된 여러가지 정보(상태 코드, 전달할 데이터 등)를
			// 한번에 객체로 포장해서 리턴할 수 있게 하는 Spring에서 제공하는 객체
			return ResponseEntity
					.badRequest()
					.body(result.toString());
			// 제너릭의 타입에 ?를 넣으면 return 타입에 따라 제너릭의 값이 자동으로 정해진다
		}

		System.out.println("/api/v1/replies: POST!!");
		System.out.println("dto = " + dto);

		replyService.register(dto);

		return ResponseEntity.ok("success");
	}


	@PutMapping
	public ResponseEntity<?> update(
			@Validated @RequestBody ReplyModifyRequestDTO dto, BindingResult result) {

		if (result.hasErrors()) {
			return ResponseEntity
					.badRequest()
					.body(result.toString());
		}

		System.out.println("/api/v1/replies : PUT!!");
		System.out.println("dto = " + dto);

		replyService.modify(dto);

		return ResponseEntity.ok().body("modSuccess");
	}


	@DeleteMapping("/{rno}")
	public ResponseEntity<?> delete(@RequestBody @PathVariable Integer rno) {

		if (rno == null) {
			return ResponseEntity
					.badRequest()
					.body("댓글 번호가 전달되지 않음");
		}

		System.out.println("/api/v1/replies/ " + rno + ": DELETE");

		try {
			replyService.delete(rno);
			return ResponseEntity.ok().body("delSuccess");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
