package ru.yandex.practicum.repository.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.domain.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.util.List;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query(
                "SELECT id, name, image, content, likes FROM posts",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("image"),
                        rs.getString("content"),
                        rs.getInt("likes")
                )
        );
    }

    @Override
    public List<Post> findAllFilteredByTag(String tag) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("tag", tag);
        return jdbcTemplate.query("""
                        SELECT p.id id, p.name name, p.image image, p.content content, p.likes likes 
                        FROM posts p
                            JOIN post_tags pt ON p.id = pt.post_id
                            JOIN tags t ON t.id = pt.tag_id
                        WHERE t.tag = :tag
                        """,
                parameters,
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("image"),
                        rs.getString("content"),
                        rs.getInt("likes")
                )
        );
    }

    @Override
    public Post getPost(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(
                "select id, name, image, content, likes from posts where id = :id",
                parameters,
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("image"),
                        rs.getString("content"),
                        rs.getInt("likes")
                )
        );
    }

    @Override
    public Long save(Post post) {
        KeyHolder holder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", post.getName());
        parameters.addValue("image", post.getImage());
        parameters.addValue("content", post.getContent());
        parameters.addValue("likes", post.getLikes());
        jdbcTemplate.update(
                "insert into posts(name, image, content, likes) values ( :name, :image, :content, :likes )",
                parameters,
                holder
        );
        return holder.getKey().longValue();
    }

    @Override
    public Post update(Post post) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", post.getName());
        parameters.addValue("image", post.getImage());
        parameters.addValue("content", post.getContent());
        parameters.addValue("likes", post.getLikes());
        parameters.addValue("id", post.getId());
        jdbcTemplate.update(
                "update posts set name = :name, image = :image, content = :content, likes = :likes where id = :id",
                parameters
        );
        return post;
    }

    @Override
    public void delete(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(
                "delete from posts where id = :id",
                parameters
        );
    }

    @Override
    public void increaseLikesCount(Long id) {
        SqlParameterSource parametersQuery = new MapSqlParameterSource("id", id);
        int likeCount = jdbcTemplate.query("SELECT likes FROM posts WHERE id = :id LIMIT 1",
                parametersQuery,
                (rs, rowNum) -> rs.getInt("likes")
        ).getFirst();

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        parameters.addValue("likeCount", likeCount + 1);
        jdbcTemplate.update("UPDATE posts set likes = :likeCount WHERE id = :id", parameters);
    }


}
