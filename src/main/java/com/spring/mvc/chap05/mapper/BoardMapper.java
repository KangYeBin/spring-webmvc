package com.spring.mvc.chap05.mapper;

import com.spring.mvc.chap05.common.Search;
import com.spring.mvc.chap05.entity.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// 마이바티스의 SQL 실행을 위한 인터페이스임을 명시
@Mapper
public interface BoardMapper {

	// 목록 조회
	List<Board> findAll(Search page);

	// 상세 조회
	Board findOne(int boardNo);

	// 게시물 등록
	void save(Board board);

	// 게시물 삭제
	void delete(int boardNo);

	// 조회수 처리
	void updateViewCount(int bno);

	// 총 게시물의 개수 리턴
	int getCount(Search page);

}