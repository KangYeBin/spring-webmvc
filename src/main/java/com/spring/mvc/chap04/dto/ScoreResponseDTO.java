package com.spring.mvc.chap04.dto;


import com.spring.mvc.chap04.entity.Grade;
import com.spring.mvc.chap04.entity.Score;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor    // final 필드 초기화하는 생성자
@Getter
@ToString
@EqualsAndHashCode
public class ScoreResponseDTO {

	// 응답용이므로 더 이상 데이터가 변하지 않도록 final로 선언, setter 구현 X
	private final int stuNum;
	private final String maskingName;
	private final double average;
	private final Grade grade;

	public ScoreResponseDTO(Score score) {
		this.stuNum = score.getStuNum();
		this.maskingName = makeMaskingName(score.getName());
		this.average = score.getAverage();
		this.grade = score.getGrade();
	}

	private String makeMaskingName(String name) {
		// 학생의 성을 제외한 나머지 이름을 *로 가려주는 기능

		String maskingName = String.valueOf(name.charAt(0));    // 성만 추출

		for (int i = 0; i < name.length() - 1; i++) {
			maskingName += "*";
		}
		return maskingName;
	}


}
