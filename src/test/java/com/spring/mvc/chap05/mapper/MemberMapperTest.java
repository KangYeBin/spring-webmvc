package com.spring.mvc.chap05.mapper;

import com.spring.mvc.chap05.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberMapperTest {

	@Autowired
	MemberMapper memberMapper;

	@Test
	@DisplayName("회원가입에 성공해야한다")
	void save() {
	    // given
		Member member = Member.builder()
				.account("abc1234")
				.password("abc1234")
				.name("김춘식")
				.email("abc1234@naver.com")
				.build();
		// when
		memberMapper.save(member);
	    // then
	}

	@Test
	@DisplayName("아이디가 abc1234인 계정을 조회하면 그 회원의 이름은 김춘식이어야 한다")
	void findMemberTest() {
	    // given
		String account = "abc1234";
	    // when
		Member member = memberMapper.findOne(account);
		// then
		assertEquals("김춘식", member.getName());
	}

	@Test
	@DisplayName("계정이 abc1234일 경우 중복확인 결과값이 true여야 한다.")
	void duplicateTest() {
	    // given
		String account = "abc1234";
	    // when
		Boolean flag = memberMapper.isDuplicate("account", account);
		System.out.println("flag = " + flag);
		// then
		assertTrue(flag);
	}

	@Test
	@DisplayName("이메일이 abc@naver.com일 경우 중복확인 결과값이 false여야 한다.")
	void duplicateEmailTest() {
	    // given
		String email = "abc@naver.com";
	    // when
		Boolean flag = memberMapper.isDuplicate("email", email);
		// then
		assertFalse(flag);
	}
}