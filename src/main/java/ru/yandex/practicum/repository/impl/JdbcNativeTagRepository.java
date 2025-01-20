package ru.yandex.practicum.repository.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.domain.Tag;
import ru.yandex.practicum.repository.TagRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public List<Tag> findTagsByNames(List<String> tagNames) {
        SqlParameterSource parameters = new MapSqlParameterSource("tagNames", tagNames);
        return jdbcTemplate.query("""
                SELECT id, tag
                FROM tags
                WHERE tag in (:tagNames)
                """,
                parameters,
                (rs, rowNum) -> new Tag(
                        0L,
                        rs.getLong("id"),
                        rs.getString("tag")
                )
        );
    }

    @Override
    public void addRelationTagsAndPost(List<Tag> tagsList, Long postId) {
        String sql = "INSERT INTO post_tags(post_id, tag_id) VALUES (?, ?)";
        jdbcTemplate.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Tag tag = tagsList.get(i);
                ps.setLong(1, postId);
                ps.setLong(2, tag.getId());
            }

            @Override
            public int getBatchSize() {
                return tagsList.size();
            }
        });
    }

    @Override
    public void save(String tag) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("tag", tag);
        List<Tag> tagsByNames = findTagsByNames(List.of(tag));
        jdbcTemplate.update("""
                        INSERT INTO tags(tag) values (:tag)
                        """,
                parameters);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<String> findAbsentTags(List<String> tagNames) {
        List<String> result = new ArrayList<>(tagNames);
        SqlParameterSource parameters = new MapSqlParameterSource("tags", tagNames);
        List<String> tags = jdbcTemplate.query("""
                        SELECT tag
                        FROM tags
                        WHERE tag in (:tags)
                        """,
                parameters,
                (rs, rowNum) -> rs.getString("tag"));
        result.removeAll(tags);
        return result;
    }
}
