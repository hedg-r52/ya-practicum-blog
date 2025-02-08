package ru.yandex.practicum.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.domain.Tag;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostShortDto;
import ru.yandex.practicum.domain.Comment;
import ru.yandex.practicum.domain.Post;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.PostService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    public PostServiceImpl(
            PostRepository postRepository,
            CommentRepository commentRepository,
            TagRepository tagRepository,
            PostMapper postMapper, CommentMapper commentMapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.tagRepository = tagRepository;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public Page<PostShortDto> findAll(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<PostShortDto> posts = postRepository.findAll().stream()
                .map(postMapper::toPostShortDto)
                .toList();
        List<Long> idList = posts.stream().map(PostShortDto::getId).toList();
        enrichPostShortDtos(idList, posts);

        return getPagedPostShortDtoList(pageSize, currentPage, startItem, posts);
    }

    private Page<PostShortDto> getPagedPostShortDtoList(int pageSize, int currentPage, int startItem, List<PostShortDto> posts) {
        List<PostShortDto> list;
        if (posts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, posts.size());
            list = posts.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), posts.size());
    }

    @Override
    public Page<PostShortDto> findAllFilteredByTag(String tag, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<PostShortDto> posts = postRepository.findAllFilteredByTag(tag).stream()
                .map(postMapper::toPostShortDto)
                .toList();
        List<Long> idList = posts.stream().map(PostShortDto::getId).toList();
        enrichPostShortDtos(idList, posts);

        return getPagedPostShortDtoList(pageSize, currentPage, startItem, posts);
    }

    private void enrichPostShortDtos(List<Long> idList, List<PostShortDto> posts) {
        Map<Long, List<Comment>> commentMap = commentRepository.getCommentsByPostIdList(idList);
        Map<Long, List<Tag>> tagMap = tagRepository.findTagsByPostIdList(idList);
        posts.forEach(post -> enrichPost(post, commentMap, tagMap));
    }

    private static void enrichPost(PostShortDto post, Map<Long, List<Comment>> commentMap, Map<Long, List<Tag>> tagMap) {
        if (commentMap.containsKey(post.getId())) {
            post.setComments(commentMap.get(post.getId()).size());
        }
        if (tagMap.containsKey(post.getId())) {
        post.setTags(tagMap.get(post.getId()).stream()
                .map(Tag::getTag)
                .collect(Collectors.joining(",")));
        }
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.getPost(id);
        PostDto postDto = postMapper.toPostDto(post);
        List<CommentDto> comments = commentRepository.getCommentsByPostId(id).stream()
                .map(commentMapper::toCommentDto)
                .toList();
        String tags = tagRepository.findTagsByPostId(id).stream()
                .map(Tag::getTag)
                .collect(Collectors.joining(","));
        postDto.setComments(comments);
        postDto.setTags(tags);
        return postDto;
    }

    @Override
    @Transactional
    public PostDto save(PostDto postDto) {
        Long id = postRepository.save(postMapper.toPost(postDto));
        postDto.setId(id);
        return postDto;
    }

    @Override
    @Transactional
    public PostDto update(PostDto postDto) {
        postRepository.update(postMapper.toPost(postDto));
        return postDto;
    }

    @Override
    public void delete(Long id) {
        postRepository.delete(id);
    }

    @Override
    @Transactional
    public void increaseLikeCount(Long id) {
        postRepository.increaseLikesCount(id);
    }
}
