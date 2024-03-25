package com.spring.mvc.chap03;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/coffee")
public class CoffeeController {

	// 커피 주문서 양식 페이지 열어주기
	@GetMapping("/order")
	public String order() {
		return "chap03/coffee-form";
	}

	/*
		@request - 커피 주문을 서버에서 처리하는 요청
		@url - /coffee/result : POST
		@response - /chap03/coffee-result.jsp
	*/
	@PostMapping("/result")
	public String result(String menu,
						 // 사용자가 커피를 선택하지 않으면 이벤트가 발생하지 않으므로 price가 ""로 전달됨
						 // ""를 int로 변환하는 과정에서 에러 발생
						 @RequestParam(defaultValue = "3000") int price,
						 Model model) {
		System.out.println("menu = " + menu);
		System.out.println("price = " + price);

		// 적립금 계산 10%
		int bonus = (int) (price * 0.1);

		// 정보를 jsp로 전달하기 위해 Model 객체 사용
		model.addAttribute("m", menu);
		model.addAttribute("p", price);
		model.addAttribute("b", bonus);

		return "chap03/coffee-result";
	}

}
