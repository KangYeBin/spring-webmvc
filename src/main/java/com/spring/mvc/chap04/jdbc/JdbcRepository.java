package com.spring.mvc.chap04.jdbc;

import com.spring.mvc.chap04.entity.Person;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcRepository {

	// db 연결 설정 정보
	private String url = "jdbc:mysql://localhost:3306/spring_db?serverTimezone=Asia/Seoul"; // DB url 주소
	private String username = "spring"; // 계정명
	private String password = "spring"; // 비밀번호

	// JDBC 드라이버 로딩
	// 자바 프로그램과 데이터베이스 간에 객체가 드나들 길을 뚫어놓는다고 생각.

	public JdbcRepository() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 데이터베이스 커넥션 얻기 (Connection 객체 얻기 -> 데이터베이스 접속을 담당하는 객체)
	public Connection getConnection() throws SQLException {
		// 위에 준비한 url, username, password를 전달해서 DB 접속을 전담하는 Connection 받아오기
		return DriverManager.getConnection(url, username, password);
	}

	// INSERT 기능 -> 테스트 클래스에서 테스트
	public void save(Person person) {
		Connection conn = null;
		try {
			// 1. DB 연결하고 연결 정보 얻어오기
			conn = getConnection();

			// 2. 트랜잭션 시작
			conn.setAutoCommit(false);

			// 3. 실행할 SQL을 완성 (문자열)
			// 들어갈 값이 위치할 곳에 ?로 셋팅
			String sql = "INSERT INTO person (person_name, person_age) " +
					"VALUES (?, ?)";

			// 4. SQL을 실행시켜주는 객체를 얻어온다
			// prepareStatement의 매개값으로 미완성된 sql을 전달
			PreparedStatement pstmt = conn.prepareStatement(sql);

			// 5. ? 채우기 (순서, 값의 타입을 고려해서 채우기)
			// ?를 채울 때 지목하는 인덱스는 1번부터
			pstmt.setString(1, person.getPersonName());
			pstmt.setInt(2, person.getPersonAge());

			// 6. SQL 실행 명령
			// INSERT, UPDATE, DELETE -> executeUpdate();
			// SELECT -> executeQuery();
			int result = pstmt.executeUpdate();	// 리턴값은 성공한 쿼리문의 개수

			// 7. 트랜잭션 처리
			if (result == 1) conn.commit();
			else conn.rollback();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 8. 접속 해제
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// UPDATE 기능
	public void update(Person person) {
		Connection conn = null;

		try {
			// url, id, pw를 가지고 DB에 접속
			conn = getConnection();

			// 오토 커밋 끄기 (트랜잭션)
			conn.setAutoCommit(false);

			String sql = "UPDATE person SET person_name = ?, person_age = ? WHERE id = ?";

			// sql을 실행하는 객체 받아오기 (작성한 sql을 주면서)
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, person.getPersonName());
			pstmt.setInt(2, person.getPersonAge());
			pstmt.setInt(3, person.getId());

			// sql 실행 명령
			int result = pstmt.executeUpdate();

			if (result == 1) conn.commit();
			else conn.rollback();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// DELETE 기능
	public void delete(int id) {
		Connection conn = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(false);

			String sql = "DELETE FROM person WHERE id = ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);

			int result = pstmt.executeUpdate();

			if (result == 1) conn.commit();
			else conn.rollback();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 전체 회원 조회
	public List<Person> findAll() {
		List<Person> people = new ArrayList<>();

		Connection conn = null;

		try {
			conn = getConnection();

			String sql = "SELECT * FROM person";
			PreparedStatement pstmt = conn.prepareStatement(sql);

			// sql 실행 -> SELECT문에서는 executeQuery()
			// ResultSet : SELECT 조회 결과를 들고 있는 객체
			// -> 조회 내용을 자바 타입으로 변환할 수 있는 기능 제공
			ResultSet rs = pstmt.executeQuery();

			// rs.next() 메서드는 조회 대상이 존재한다면 true를 리턴하면서 한 행을 타겟으로 잡는다
			// 더 이상 조회할 데이터가 없다면 false를 리턴
			// 만약 내가 작성한 sql의 조회 결과 가 여러 행이라면 반복문을 통해 나 행씩
			// 더 이상 조회할 데이터가 없다면 반복문이 종료되도록 설계
			while(rs.next()) {
				// 특정 한 행이 지목된 생타 -> 행의 컬럼을 지목해서 데이터를 가져온다
				int id = rs.getInt("id");
				String personName = rs.getString("person_name");
				int person_age = rs.getInt("person_age");

				Person p = new Person(id, personName, person_age);
				people.add(p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}


		return people;
	}

	// 단일 조회 (pk로 조회)
	public Person findOne(int id) {
		Connection conn = null;
		try {
			conn = getConnection();

			String sql = "SELECT * FROM person WHERE id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();

			// 가져올 데이터가 여러 행이면 반복문으로 처리
			// 지금은 pk로 조회했기 때문에 행이 하나이거나, 없을 수 있다.
			if (rs.next()) {
				int personId = rs.getInt(id);
				String personName = rs.getString("person_name");
				int personAge = rs.getInt("person_age");

				return new Person(personId, personName, personAge);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 조회 결과가 없음
		return null;
	}

}
