package com.spring.mvc.chap05.controller;

import com.spring.mvc.chap05.dto.request.LoginRequestDto;
import com.spring.mvc.chap05.dto.request.SignUpRequestDTO;
import com.spring.mvc.chap05.dto.response.LoginUserResponseDTO;
import com.spring.mvc.chap05.entity.Member;
import com.spring.mvc.chap05.service.LoginResult;
import com.spring.mvc.chap05.service.MemberService;
import com.spring.mvc.util.MailSenderService;
import com.spring.mvc.util.upload.FileUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.spring.mvc.util.LoginUtils.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

	// properties 파일에 작성한 값을 가져오는 아노테이션
	@Value("${file.upload.root-path}")
	private String rootPath;
	
	private final MemberService memberService;
	private final MailSenderService mailSenderService;

	// 회원 가입 양식 화면 요청
	// 응답하고자 하는 화면의 경로가 URL과 동일하다면 void로 처리할 수 있다
	@GetMapping("/sign-up")
	public void signUp() {
		log.info("/members/sign-up: GET!!");
	}

	//	public String signUp() {
	//		System.out.println("/members/sign-up: GET");
	//		return "members/sign-up";
	//	}

	// 아이디, 이메일 중복 체크 비동기 요청 처리
	@GetMapping("/check/{type}/{keyword}")
	@ResponseBody
	public ResponseEntity<?> check(@PathVariable String type, @PathVariable String keyword) {
		log.info("/members/check: async GET!!");
		log.debug("type : {}, keyword : {}", type, keyword);

		Boolean checked = memberService.checkDuplicateValue(type, keyword);

		return ResponseEntity.ok().body(checked);
	}

	@PostMapping("/sign-up")
	public String signUp(SignUpRequestDTO dto) {
		log.info("/members/sign-up: POST!!, dto: {}", dto);
		log.info("attached file name : {}", dto.getProfileImage().getOriginalFilename());

		// 서버에 파일 업로드
		String savePath = FileUtils.uploadFile(dto.getProfileImage(), rootPath);
		log.info("save-path : {}", savePath);

		// 일반 방식 (우리 사이트를 통해 회원가입)
		dto.setLoginMethod(Member.LoginMethod.COMMON);

		memberService.join(dto, savePath);

		return "redirect:/board/list";
	}

	@GetMapping("/sign-in")
	public void signIn() {
		log.info("/members/sign-in: GET!!");
	}

	// 로그인 검증 요청
	@PostMapping("/sign-in")
	public String signIn(LoginRequestDto dto,
						 RedirectAttributes ra,
						 HttpServletResponse response,
						 HttpServletRequest request) {

		log.info("/members/sign-in: POST!!, dto : {}", dto);

		// 자동 로그인 서비스를 추가하기 위해 세션과 응답 객체도 함께 전달
		LoginResult result = memberService.authenticate(dto, request.getSession(), response);
		log.info("result : {}", result);

		// model에 담긴 데이터는 리다이렉트시 jsp로 전달되지 못한다
		// 리다이렉트는 응답이 나갔다가 재요청이 들어오므로 그 과정에서
		// 첫 번째 응답이 나가는 순간 모델 소멸
		// Model의 생명주기는 한 번의 요청과 응답 사이에서만 유효
//		model.addAttribute("result", result);
		// 리다이렉트 상황에서는 RedirectAttribute 사용
		ra.addFlashAttribute("result", result);

		if (result == LoginResult.SUCCESS) {	// 로그인 성공 시

			//로그인 했다는 정보를 계속 유지하기 위한 수단으로 쿠키 사용
//			makeLoginCookie(dto, response);

			// 세션으로 로그인 유지
			memberService.maintainLoginState(request.getSession(), dto.getAccount());

			return "redirect:/board/list";
		}

		return "redirect:/members/sign-in";	// 로그인 실패 시

	}

	private void makeLoginCookie(LoginRequestDto dto, HttpServletResponse response) {
		// 쿠키에 로그인 기록을 저장
		// 쿠키 객체 생성 -> 생성자의 매개값으로 쿠키의 이름과 저장할 값을 전달
		// (문자열만 저장되고 용량의 한계가 있다)
		Cookie cookie = new Cookie("login", dto.getAccount());

		// 쿠키 세부 설정
		cookie.setMaxAge(60);	// 쿠키 수명 설정 (초)
		cookie.setPath("/");	// 유효 경로 -> 모든 경로에서 유효한 쿠키

		// 쿠키가 완성되면 응답 객체에 쿠키를 태워서 클라이언트로 전송
		response.addCookie(cookie);
	}


	// 로그아웃 요청 처리
	@GetMapping("/sign-out")
	public String signOut(HttpSession session,
						  HttpServletRequest request,
						  HttpServletResponse response) {
		log.info("/member/sign-out: GET!!");

		// 자동 로그인 중인지 확인
		if (isAutoLogin(request)) {
			// 쿠키를 삭제하고 DB 데이터도 원래대로 돌려놓아야 한다
			memberService.autoLoginClear(request,response);
		}

		// sns 로그인 상태인지 확인
		LoginUserResponseDTO dto = (LoginUserResponseDTO) session.getAttribute(LOGIN_KEY);
		if (dto.getLoginMethod().equals("KAKAO")) {
			memberService.kakaoLogout(dto, session);
		}

		// 세션에서 로그인 정보 기록 삭제
		session.removeAttribute("login");

		// 세션 전체 무효화 (초기화)
		session.invalidate();

		return "redirect:/";
	}

	// 연습용 이메일 폼 화면
	@GetMapping("/email")
	public String emailForm() {
		return "email/email-form";
	}

	// 이메일 인증
	@PostMapping("/email")
	@ResponseBody
	public ResponseEntity<?> mailCheck(@RequestBody String email) {
		log.info("이메일 인증 요청 들어옴: {}", email );

		String authNum = null;
		try {
			authNum = mailSenderService.joinEmail(email);
			return ResponseEntity.ok().body(authNum);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("이메일 전송 과정에서 에러 발생");
		}
	}

}
