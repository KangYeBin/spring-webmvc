package com.spring.mvc.chap04.repository;

import com.spring.mvc.chap04.entity.Grade;
import com.spring.mvc.chap04.entity.Score;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository    // Bean으로 등록 해둬야 Service에 주입 가능
@RequiredArgsConstructor
public class ScoreRepositoryImpl implements ScoreRepository {

	// Spring-jdbc의 핵심 객체인 jdbcTemplate의 의존성 주입 (생성자 주입)
	// 데이터베이스 접속 객체(Connection)을 바로 활용하는 것이 가능 -> 미리 세팅 끝
	private final JdbcTemplate jdbcTemplate;

	@Override
	public boolean save(Score score) {
		String sql = "INSERT INTO tbl_score " +
				"(stu_name, kor, eng, math, total, average, grade) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?)";

		int result = jdbcTemplate.update(sql, score.getName(), score.getKor(), score.getEng(),
				score.getMath(), score.getTotal(), score.getAverage(), score.getGrade().toString());

		return result == 1 ? true : false;
	}

	@Override
	public List<Score> findAll(String sort) {
		String sql = "SELECT * FROM tbl_score";

		switch (sort) {
			case "num":
				sql += " ORDER BY stu_num";
				break;
			case "name":
				sql += " ORDER BY stu_name";
				break;
			case "avg":
				sql += " ORDER BY average DESC";
		}
		List<Score> scoreList = jdbcTemplate.query(sql, new ScoreMapper());
		return scoreList;
	}

	@Override
	public boolean delete(int stuNum) {
		String sql = "DELETE FROM tbl_score WHERE stu_num = ?";

		int result = jdbcTemplate.update(sql, stuNum);
		return result == 1 ? true : false;
	}

	@Override
	public Score findOne(int stuNum) {
		String sql = "SELECT * FROM tbl_score WHERE stu_num = ?";

		try {
			// queryForObject는 조회 결과가 없을 시 예외가 발생
			Score score = jdbcTemplate.queryForObject(sql, new ScoreMapper(), stuNum);
			return score;
		} catch (DataAccessException e) {
			// 조회 결과가 없다면 catch문이 실행됨 -> null을 리턴
			return null;
		}
	}

	@Override
	public void update(Score changeScore) {
		String sql = "UPDATE tbl_score SET " +
				"kor=?, eng=?, math=?, total=?, average=?, grade=? WHERE stu_num = ?";

		jdbcTemplate.update(sql,
				changeScore.getKor(),
				changeScore.getEng(),
				changeScore.getMath(),
				changeScore.getTotal(),
				changeScore.getAverage(),
				changeScore.getGrade().toString(),
				changeScore.getStuNum());
	}



	// 내부(중첩) 클래스 (inner class)
	// 두 클래스가 굉장히 긴밀한 관계가 있을 때 주로 선언.
	// 해당 클래스 안에서만 사용할 클래스는 굳이 새 파일로 선언하지 않고 만들 수 있다.
	class ScoreMapper implements RowMapper<Score> {
		@Override
		public Score mapRow(ResultSet rs, int rowNum) throws SQLException {
			Score score = new Score(
					rs.getString("stu_name"),
					rs.getInt("kor"),
					rs.getInt("eng"),
					rs.getInt("math"),
					rs.getInt("stu_num"),
					rs.getInt("total"),
					rs.getDouble("average"),
					Grade.valueOf(rs.getString("grade"))
			);

			return score;
		}
	}
}
