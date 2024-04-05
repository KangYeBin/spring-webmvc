package com.spring.mvc.interceptor;

import com.spring.mvc.chap05.entity.Member;
import com.spring.mvc.chap05.mapper.MemberMapper;
import com.spring.mvc.chap05.service.MemberService;
import com.spring.mvc.util.LoginUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import java.time.LocalDateTime;

import static com.spring.mvc.util.LoginUtils.*;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AutoLoginInterceptor implements HandlerInterceptor {

	public final MemberMapper memberMapper;
	public final MemberService memberService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// 1. 사용자가 사이트에 들어오면 자동 로그인 쿠키를 가진 클라이언트인지 체크
		Cookie autoLoginCookie = WebUtils.getCookie(request, AUTO_LOGIN_COOKIE);

		// 2. 자동 로그인 쿠키가 있다면
		if (autoLoginCookie != null) {

			// 3. 지금 읽어들인 쿠키가 갖고 있는 session_id를 꺼낸다
			String sessionId = autoLoginCookie.getValue();

			// 4. DB에서 쿠키가 가진 session_id와 동일한 값을 가진 회원 조회
			Member member = memberMapper.findOneByCookie(sessionId);

			// 5. 회원이 정상적으로 조회가 됐다면 and 자동 로그인 만료시간 이전이면 로그인 수행
			if (member != null && LocalDateTime.now().isBefore(member.getLimitTime())) {
				memberService.maintainLoginState(request.getSession(), member.getAccount());
			}
		}

		// 자동 로그인 여부에 상관없이 요청은 무조건 컨트롤러로 전달되어야하므로 return 값을 true로 고정
		return true;
	}
}
