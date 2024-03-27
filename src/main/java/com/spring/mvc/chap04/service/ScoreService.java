package com.spring.mvc.chap04.service;

/*
	컨트롤러와 레파지토리 사이에 위치하여 중간 로직을 처리하는 역할
	
	컨트롤러 <-> 서비스 <-> 레파지토리
*/

import com.spring.mvc.chap04.dto.ScoreRequestDTO;
import com.spring.mvc.chap04.dto.ScoreResponseDTO;
import com.spring.mvc.chap04.entity.Score;
import com.spring.mvc.chap04.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {

	// 서비스는 레파지토리 계층과 의존 관계가 있으므로 객체가 생성될 때 자동 주입
	private final ScoreRepository repository;

	// 성적 입력 중간 처리
	// 컨트롤러가 dto를 넘김 -> 값을 정제하고 entity로 변환 -> 레파지토리 계층으로 넘김
	public boolean insertScore(ScoreRequestDTO dto) {
		Score score = new Score(dto);
		return repository.save(score);
	}

	/*
		- 목록 조회 중간 처리
		컨트롤러는 데이터베이스에서 성적정보 리스트를 조회해 오기를 원한다.
		그런데 데이터베이스는 민감한 정보까지 모두 조회한다.
		그리고 컬럼명도 그대로 노출하기 때문에 숨기는 처리를 하고 싶다. -> response용 DTO를 생성
	*/

	public List<ScoreResponseDTO> findAll(String sort) {
		List<ScoreResponseDTO> dtoList = new ArrayList<>();
		List<Score> scoreList = repository.findAll(sort);

		for (Score score : scoreList) {
			ScoreResponseDTO dto = new ScoreResponseDTO(score);
			dtoList.add(dto);
		}
		return dtoList;
	}


	public void deleteScore(int stuNum) {
		repository.delete(stuNum);
	}

	public Score findOne(int stuNum) {
		return repository.findOne(stuNum);
	}

	public void updateScore(ScoreRequestDTO dto, int stuNum) {
		// 클라이언트가 수정할 데이터를 보내면
		// 1. 서버에 저장되어있는 기존 데이터를 조회해서 수정
		// 2. 새로운 Score 객체를 생성해서 점수를 세팅하고 총점, 평균, 학점을 계산
		Score changeScore = new Score(dto);
		changeScore.setStuNum(stuNum);
		repository.update(changeScore);
	}
}

