package com.spring.mvc.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

import static com.spring.mvc.util.LoginUtils.*;

@Configuration
public class AfterLoginInterceptor implements HandlerInterceptor {

	// 로그인 한 이후 비회원만 접근할 수 있는 페이지 접근 차단
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		// 세션 받아오기
		HttpSession session = request.getSession();
		
		// 세션 데이터 중 'login'이라는 이름의 데이터가 있는지 확인 후
		// 존재한다면 요청이 컨트롤러로 들어가지 못하게 막는다
		if (isLogin(session)) {

			response.setContentType("text/html; charset=UTF-8");
			PrintWriter w = response.getWriter();
			String htmlCode = "<!DOCTYPE html>\n" +
					"<html lang=\"ko\">\n" +
					"<head>\n" +
					"  <meta charset=\"UTF-8\">\n" +
					"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
					"  <title>Document</title>\n" +
					"</head>\n" +
					"<body>\n" +
					"\n" +
					"  <script>\n" +
					"    alert('이미 로그인 한 회원입니다.');\n" +
					"    location.href='/';\n" +
					"  </script>\n" +
					"  \n" +
					"</body>\n" +
					"</html>";
			w.write(htmlCode);
			w.flush();

			return false;	// 컨트롤러로 들어가는 요청을 막음
		}
		
		return true;	// 로그인 하지 않았다면 통과
	}
}
