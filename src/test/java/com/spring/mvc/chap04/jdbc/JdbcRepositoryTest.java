package com.spring.mvc.chap04.jdbc;

import com.spring.mvc.chap04.entity.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JdbcRepositoryTest {

	@Autowired
	JdbcRepository repository;

	@Test
	@DisplayName("Person 객체 정보를 DB에 정상 삽입해야 한다")
		// 테스트의 목적 작성
	void saveTest() {
		Person p = new Person(1, "김춘식", 40);
		repository.save(p);
	}

	@Test
	@DisplayName("회원번호가 2번인 회원의 이름과 나이를 수정해야 한다")
	void updateTest() {

		// given : 테스트를 위해 데이터를 세팅
		int id = 2;
		String newName = "개굴이";
		int newAge = 15;
		Person p = new Person(id, newName, newAge);

		// when : 테스트 목표를 확인하여 실제 테스트가 진행되는 구간
		repository.update(p);

		// then : 테스트 결과를 확인하는 구간 (단언 기법 -> Assertion)

	}

	@Test
	@DisplayName("회원번호가 2번인 회원을 삭제한다")
	void deleteTest() {
		int id = 2;

		repository.delete(id);
	}

	@Test
	@DisplayName("더미 데이터 10개를 삽입한다")
	void vulkInsert() {
		for (int i = 0; i < 10; i++) {
			Person p = new Person(0, "랄라라" + i, i + 10);
			repository.save(p);
		}
	}

	@Test
	@DisplayName("전체 회원을 조회하면 회원 리스트의 사이즈는 11일 것이다")
	void findAllTest() {

		List<Person> people = repository.findAll();

		for (Person person : people) {
			System.out.println(person);
		}

		// people.size()가 11과 같을 것임을 단언
		Assertions.assertEquals(11, people.size());
	}
	
	@Test
	@DisplayName("특정 아이디 회원을 조회하면 하나으 ㅣ회원만 조회될 것이고," +
			"없는  id를 전달하면 null이 리턴될 것이다")
	void findOneTest() {
		int id = 1;

		Person person = repository.findOne(id);
		System.out.println("person = " + person);

		assertNotNull(person);
	}
}