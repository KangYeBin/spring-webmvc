package com.spring.mvc.chap05.controller;

import com.spring.mvc.chap05.dto.request.LoginRequestDto;
import com.spring.mvc.chap05.dto.request.SignUpRequestDTO;
import com.spring.mvc.chap05.service.LoginResult;
import com.spring.mvc.chap05.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	// 회원 가입 양식 화면 요청
	// 응답하고자 하는 화면의 경로가 URL과 동일하다면 void로 처리할 수 있다
	@GetMapping("/sign-up")
	public void signUp() {
		System.out.println("/members/sign-up: GET!!");
	}

	//	public String signUp() {
	//		System.out.println("/members/sign-up: GET");
	//		return "members/sign-up";
	//	}

	// 아이디, 이메일 중복 체크 비동기 요청 처리
	@GetMapping("/check/{type}/{keyword}")
	@ResponseBody
	public ResponseEntity<?> check(@PathVariable String type, @PathVariable String keyword) {
		System.out.println("/members/check: async GET!!");
		System.out.println("type = " + type);
		System.out.println("keyword = " + keyword);

		Boolean checked = memberService.checkDuplicateValue(type, keyword);

		return ResponseEntity.ok().body(checked);
	}

	@PostMapping("/sign-up")
	public String signUp(SignUpRequestDTO dto) {
		System.out.println("/members/sign-up: POST!!");
		System.out.println("dto = " + dto);

		memberService.join(dto);

		return "redirect:/board/list";
	}

	@GetMapping("/sign-in")
	public void signIn() {
		System.out.println("/members/sign-in: GET!!");
	}
	
	// 로그인 검증 요청
	@PostMapping("/sign-in")
	public String signIn(LoginRequestDto dto, RedirectAttributes ra) {
		System.out.println("/members/sign-in: POST!!");
		System.out.println("dto = " + dto);

		LoginResult result = memberService.authenticate(dto);
		System.out.println("result = " + result);

		// model에 담긴 데이터는 리다이렉트시 jsp로 전달되지 못한다
		// 리다이렉트는 응답이 나갔다가 재요청이 들어오므로 그 과정에서
		// 첫 번째 응답이 나가는 순간 모델 소멸
		// Model의 생명주기는 한 번의 요청과 응답 사이에서만 유효
//		model.addAttribute("result", result);
		// 리다이렉트 상황에서는 RedirectAttribute 사용
		ra.addFlashAttribute("result", result);
		
		if (result == LoginResult.SUCCESS) {	// 로그인 성공 시
			return "redirect:/board/list";
		}

		return "redirect:/members/sign-in";	// 로그인 실패 시

	}
}
