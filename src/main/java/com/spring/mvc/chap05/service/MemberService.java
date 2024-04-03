package com.spring.mvc.chap05.service;

import com.spring.mvc.chap05.dto.request.LoginRequestDto;
import com.spring.mvc.chap05.dto.request.SignUpRequestDTO;
import com.spring.mvc.chap05.entity.Member;
import com.spring.mvc.chap05.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.spring.mvc.chap05.service.LoginResult.*;

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
	public LoginResult authenticate(LoginRequestDto dto) {

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
}
