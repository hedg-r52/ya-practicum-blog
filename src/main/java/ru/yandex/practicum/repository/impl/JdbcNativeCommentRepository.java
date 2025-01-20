package ru.yandex.practicum.repository.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.domain.Comment;
import ru.yandex.practicum.repository.CommentRepository;

import java.util.*;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        SqlParameterSource parameters = new MapSqlParameterSource("post_id", postId);
        return jdbcTemplate.query(
                "select post_id, id, text from comments where post_id = :post_id",
                parameters,
                (rs, rowNum) -> new Comment(
                        rs.getLong("post_id"),
                        rs.getLong("id"),
                        rs.getString("text")
                )
        );
    }

    @Override
    public Map<Long, List<Comment>> getCommentsByPostIdList(List<Long> idList) {
        Map<Long, List<Comment>> result = new HashMap<>();

        SqlParameterSource parameters = new MapSqlParameterSource("ids", idList);
        List<Comment> comments = jdbcTemplate.query(
                "select post_id, id, text from comments where post_id in (:ids)",
                parameters,
                (rs, rowNum) -> new Comment(
                        rs.getLong("post_id"),
                        rs.getLong("id"),
                        rs.getString("text")
                )
        );
        comments.forEach(comment -> {
            result.putIfAbsent(comment.getPostId(), new ArrayList<Comment>());
            result.get(comment.getPostId()).add(comment);
        });
        return result;
    }

    @Override
    public Comment getById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(
                "select post_id, id, text from comments where id = :id",
                parameters,
                (rs, rowNum) -> new Comment(
                        rs.getLong("post_id"),
                        rs.getLong("id"),
                        rs.getString("text")
                )
        );
    }

    @Override
    public void save(Comment comment) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("post_id", comment.getPostId());
        parameters.addValue("text", comment.getText());

        jdbcTemplate.update(
                "insert into comments(post_id, text) values(:post_id, :text)",
                parameters
        );
    }

    @Override
    public Comment update(Comment comment) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", comment.getId());
        parameters.addValue("post_id", comment.getPostId());
        parameters.addValue("text", comment.getText());

        jdbcTemplate.update(
                "update comments set post_id = :post_id, text = :text where id = :id",
                parameters
        );
        return comment;
    }

    @Override
    public void delete(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(
                "delete from comments where id = :id",
                parameters
        );
    }
}
