package com.spring.mvc.chap04.dto;

import lombok.*;

/**
	 * 서버가 클라이언트에 데이터를 전달할 때
	 * 데이터베이스에 있는 모든 데이터를 전달하면
	 * 민감한 정보나 보안상의 정보가 같이 전달될 가능성이 있다.
	 * 그래서 클라이언트에 보여줄 데이터만 선별해서 응답하도록
 	 * 클라이언트에서 요청과 함께 보내준 데이터만 정리해서
	 * 이 클래스를 사용한다.
 */

@Getter @Setter @ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ScoreRequestDTO {

	// form에서 넘어오는 데이터만 깔끔하게 받아 객체화
	// DTO 클래스만 봐도 이떤 데이터가 클라인트로 전달되는지 명확화
	private String name;
	private int kor, eng,math;
}
