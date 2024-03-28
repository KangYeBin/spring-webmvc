package com.spring.mvc.chap05.repository;

import com.spring.mvc.chap05.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {

	private final JdbcTemplate jdbcTemplate;


	@Override
	public List<Board> findAll() {
		String sql = "SELECT * FROM tbl_board ORDER BY board_no DESC";
		List<Board> boardList = jdbcTemplate.query(sql, new BoardMapper());

		return boardList;
	}

	@Override
	public Board findOne(int boardNo) {
		String sql = "SELECT * FROM tbl_board WHERE board_no = ?";
		Board board = null;
		try {
			board = jdbcTemplate.queryForObject(sql, new BoardMapper(), boardNo);
		} catch (DataAccessException e) {
			return null;
		}

		return board;
	}

	@Override
	public void save(Board board) {
		String sql = "INSERT INTO tbl_board " +
				"(title, content, writer) " +
				"VALUES (?,?,?)";

		jdbcTemplate.update(sql, board.getTitle(), board.getContent(), board.getWriter());

	}

	@Override
	public void delete(int boardNO) {
		String sql = "DELETE FROM tbl_board WHERE board_no = ?";

		jdbcTemplate.update(sql, boardNO);
	}

	@Override
	public void updateViewCount(int bno) {
		String sql = "UPDATE tbl_board SET view_count = view_count + 1 WHERE board_no = ?";
		jdbcTemplate.update(sql, bno);
	}

	class BoardMapper implements RowMapper<Board> {

		@Override
		public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
			Board board = new Board(
				rs.getInt("board_no"),
				rs.getString("title"),
				rs.getString("content"),
				rs.getInt("view_count"),
				rs.getTimestamp("reg_date").toLocalDateTime(),
				rs.getString("writer")
			);

			return board;
		}
	}
}