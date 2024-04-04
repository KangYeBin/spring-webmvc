package com.spring.mvc.chap05.service;

import com.spring.mvc.chap05.dto.request.AutoLoginDTO;
import com.spring.mvc.chap05.dto.request.LoginRequestDto;
import com.spring.mvc.chap05.dto.request.SignUpRequestDTO;
import com.spring.mvc.chap05.dto.response.LoginUserResponseDTO;
import com.spring.mvc.chap05.entity.Member;
import com.spring.mvc.chap05.mapper.MemberMapper;
import com.spring.mvc.util.LoginUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.spring.mvc.chap05.service.LoginResult.*;
import static com.spring.mvc.util.LoginUtils.*;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder encoder;

	// 회원 가입 처리 서비스
	public void join(SignUpRequestDTO dto) {

		// 클라이언트가 보낸 회원가입 데이터를
		// 패스워드 인코딩하여 엔터티로 변환해서 전달
		//		String encodedPw = encoder.encode(dto.getPassword());
		//		dto.setPassword(encodedPw);
		memberMapper.save(dto.toEntity(encoder));
	}

	// 로그인 검증 처리
	public LoginResult authenticate(LoginRequestDto dto, HttpSession session, HttpServletResponse response) {

		Member foundMember = memberMapper.findOne(dto.getAccount());

		if (foundMember == null) {    // 회원가입 안 한 상태
			System.out.println(dto.getAccount() + "는 없는 아이디!");
			return NO_ACC;
		}

		// 비밀번호 일치검사
		String inputPassword = dto.getPassword();
		String realPassword = foundMember.getPassword();

		// matches(입력 비번, 암호화 비번) -> 일치하면 true, 일치하지 않으면 false
		// equals로 비교하면 안됨
		if (!encoder.matches(inputPassword, realPassword)) {
			System.out.println("비밀번호가 다릅니다");
			return NO_PW;
		}

		System.out.println(dto.getAccount() + "님 로그인 성공");
		return SUCCESS;
	}

	public Boolean checkDuplicateValue(String type, String keyword) {
		return memberMapper.isDuplicate(type, keyword);
	}

	public void maintainLoginState(HttpSession session, String account) {

		// 세션은 서버에서만 유일하게 보관되는 데이터로서
		// 로그인 유지 등 상태 유지가 필요할 때 사용되는 내장 객체
		// 세션은 쿠키와 달리 모든 데이터를 저장할 수 있고 크기 제한도 없다
		// 세션의 수명은 기본 1800초 -> 원하는 만큼 수명을 조절할 수 있다
		// 브라우저가 종료되면 남은 수명에 상관없이 세션 데이터는 소멸된다

		// 현재 로그인한 회원의 모든 정보 조회
		Member foundMember = memberMapper.findOne(account);

		// DB 데이터를 보여줄 것만 정제
		LoginUserResponseDTO dto = LoginUserResponseDTO.builder()
				.account(foundMember.getAccount())
				.name(foundMember.getName())
				.email(foundMember.getEmail())
				.auth(foundMember.getAuth().getDescription())
				.build();

		// 세션에 로그인한 회원 정보를 저장
		session.setAttribute(LOGIN_KEY, dto);

		// 세션 수명 설정
		session.setMaxInactiveInterval(60 * 60);	// 1시간
	}
}
