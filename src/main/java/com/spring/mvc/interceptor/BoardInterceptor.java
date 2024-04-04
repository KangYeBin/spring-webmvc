package com.spring.mvc.interceptor;

import com.spring.mvc.chap05.entity.Board;
import com.spring.mvc.chap05.mapper.BoardMapper;
import com.spring.mvc.util.LoginUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

import static com.spring.mvc.util.LoginUtils.*;

/*
	인터셉터 : 컨트롤러에 요청이 들어가기 전, 후에 공통적으로 처리할 코드나 검사할 일들을 정의하는 클래스
*/

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BoardInterceptor implements HandlerInterceptor {

	private final BoardMapper boardMapper;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		HttpSession session = request.getSession();

		// 로그인을 안 했다면 글 쓰기, 글 수정, 글 삭제 요청을 하지 못하도록 한다
		if (!isLogin(session)) {
			log.info("권한 없음! 요청 거부! - {}", request.getRequestURI());
			response.sendRedirect("/members/sign-in");
			return false;
		}

		// 삭제 요청이 들어올 때 서버에서 다시 한 번 내가 쓴 글인지 체크

		// 현재 요청이 삭제 요청인지 확인
		String uri = request.getRequestURI();
		if (uri.contains("delete")) {

			// 관리자라면? 삭제 가능
			if (isAdmin(session)) return true;

			// 삭제 요청이 들어온 글 번호 확인 -> 쿼리 파라미터로 글 번호가 넘어옴
			String bno = request.getParameter("bno");

			// 글 번호를 통해 해당 글을 누가 작성했는지 알아낸다
			String writer = boardMapper.findOne(Integer.parseInt(bno)).getWriter();

			// 만약 내가 쓴 글이 아니라면 접근 권한이 없다는 피드백 필요
			if (!isMine(session, writer)) {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter w = response.getWriter();
				String htmlCode = "<script>\n" +
						"    alert('본인이 작성한 게시글만 삭제가 가능합니다.');\n" +
						"    location.href='/board/list';\n" +
						"</script>";
				w.write(htmlCode);
				w.flush();
				return false;
			}
		}
		return true;
	}
}
