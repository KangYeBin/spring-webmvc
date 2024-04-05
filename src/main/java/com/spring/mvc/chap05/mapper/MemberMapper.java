package com.spring.mvc.chap05.mapper;

import com.spring.mvc.chap05.dto.request.AutoLoginRequestDTO;
import com.spring.mvc.chap05.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

	// 회원 가입
	void save(Member member);

	// 회원 정보 단일 조회
	Member findOne(String account);

	/**
	 * 중복 체크(account, email) 기능
	 * @param type - 중복을 검사할 내용 (account, email)
	 * @param keyword - 중복 검사 입력값 (ex: abc1234@naver.com)
	 * @return 중복이면 true, 중복이 아니면 false
	 */
	// 중복 체크
	Boolean isDuplicate(@Param("type") String type, @Param("keyword") String keyword);

	// 자동 로그인 세션 아이디, 만료 시간 업데이트
	void saveAutoLogin(AutoLoginRequestDTO build);

	// 쿠키값(세션 아이디)으로 회원 정보 조회
	Member findOneByCookie(String sessionId);
}
