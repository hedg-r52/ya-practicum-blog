package ru.yandex.practicum.repository.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.domain.Comment;
import ru.yandex.practicum.domain.Tag;
import ru.yandex.practicum.repository.TagRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcNativeTagRepository implements TagRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcNativeTagRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> findTagsByPostId(Long postId) {
        SqlParameterSource parameters = new MapSqlParameterSource("id", postId);
        return jdbcTemplate.query("""
                        SELECT post_id, t.id id, t.tag tag
                        FROM post_tags pt
                            JOIN tags t ON t.id = pt.tag_id
                        WHERE post_id = :id
                        """,
                parameters,
                (rs, rowNum) -> new Tag(
                        rs.getLong("post_id"),
                        rs.getLong("id"),
                        rs.getString("tag")
                )
        );
    }

    @Override
    public Map<Long, List<Tag>> findTagsByPostIdList(List<Long> idList) {
        Map<Long, List<Tag>> result = new HashMap<>();

        SqlParameterSource parameters = new MapSqlParameterSource("ids", idList);
        List<Tag> tags = jdbcTemplate.query("""
                        SELECT post_id, t.id id, t.tag tag
                        FROM post_tags pt
                            JOIN tags t ON t.id = pt.tag_id
                        WHERE post_id in (:ids)
                        """,
                parameters,
                (rs, rowNum) -> new Tag(
                        rs.getLong("post_id"),
                        rs.getLong("id"),
                        rs.getString("tag")
                )
        );
        tags.forEach(tag -> {
            result.putIfAbsent(tag.getPostId(), new ArrayList<>());
            result.get(tag.getPostId()).add(tag);
        });

        return result;
    }

    @Override
    public void save(Tag tag) {

    }

    @Override
    public void delete(Long id) {

    }
}
