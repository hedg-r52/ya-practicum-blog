package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.domain.Tag;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostShortDto;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.domain.Comment;
import ru.yandex.practicum.domain.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.TagRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;

    public PostServiceImpl(
            PostRepository postRepository,
            CommentRepository commentRepository,
            TagRepository tagRepository,
            PostMapper postMapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.tagRepository = tagRepository;
        this.postMapper = postMapper;
    }

    @Override
    public Page<PostShortDto> findAll(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<PostShortDto> list;
        List<Post> posts = postRepository.findAll();
        List<Long> idList = posts.stream().map(Post::getId).toList();
        enrichPosts(idList, posts);

        if (posts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, posts.size());
            list = posts.subList(startItem, toIndex).stream()
                    .map(postMapper::toPostShortDto)
                    .toList();
        }

        Page<PostShortDto> postPage = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), posts.size());
        return postPage;
    }

    @Override
    public Page<PostShortDto> findAllFilteredByTag(String tag, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<PostShortDto> list;
        List<Post> posts = postRepository.findAllFilteredByTag(tag);
        List<Long> idList = posts.stream().map(Post::getId).toList();
        enrichPosts(idList, posts);

        if (posts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, posts.size());
            list = posts.subList(startItem, toIndex).stream()
                    .map(postMapper::toPostShortDto)
                    .toList();
        }

        Page<PostShortDto> postPage = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), posts.size());
        return postPage;
    }

    private void enrichPosts(List<Long> idList, List<Post> posts) {
        Map<Long, List<Comment>> commentMap = commentRepository.getCommentsByPostIdList(idList);
        Map<Long, List<Tag>> tagMap = tagRepository.findTagsByPostIdList(idList);
        posts.forEach(post -> {
            post.setComments(commentMap.get(post.getId()));
            post.setTags(tagMap.get(post.getId()));
        });
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.getPost(id);
        List<Comment> comments = commentRepository.getCommentsByPostId(id);
        List<Tag> tags = tagRepository.findTagsByPostId(id);
        post.setComments(comments);
        post.setTags(tags);
        return postMapper.toPostDto(post);
    }

    @Override
    public PostDto save(PostDto postDto) {
        Long id = postRepository.save(postMapper.toPost(postDto));
        postDto.setId(id);
        return postDto;
    }

    @Override
    public PostDto update(PostDto postDto) {
        postRepository.update(postMapper.toPost(postDto));
        return postDto;
    }

    @Override
    public void delete(Post post) {
        post.getComments().forEach(comment -> commentRepository.delete(comment.getId()));
        postRepository.delete(post.getId());
    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.getPost(id);
        post.getComments().forEach(comment -> commentRepository.delete(comment.getId()));
        postRepository.delete(post.getId());
    }

    @Override
    public void increaseLikeCount(Long id) {
        postRepository.increaseLikesCount(id);
    }
}
