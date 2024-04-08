package com.spring.mvc.chap05.api;

import com.spring.mvc.chap05.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/weather")
public class WeatherApiController {

	public final WeatherService weatherService;

	@GetMapping("/view")
	public ModelAndView viewPage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("api-test/api-form");
		return mv;
	}

	@GetMapping("/api-req/{area1}/{area2}")
	public void apiRequest(@PathVariable String area1, @PathVariable String area2) {
		log.info("/api-req: GET, area1: {}, area2: {}", area1, area2);

		weatherService.getShortTermForecast(area1, area2);
	}
}
