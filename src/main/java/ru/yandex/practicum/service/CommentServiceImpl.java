package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.repository.CommentRepository;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public List<CommentDto> findByPostId(Long postId) {
        return commentRepository.getCommentsByPostId(postId).stream()
                .map(commentMapper::toCommentDto)
                .toList();
    }

    @Override
    public CommentDto findById(Long id) {
        return commentMapper.toCommentDto(commentRepository.getById(id));
    }

    @Override
    public CommentDto save(CommentDto commentDto) {
        commentRepository.save(commentMapper.toComment(commentDto));
        return commentDto;
    }

    @Override
    public CommentDto update(CommentDto commentDto) {
        commentRepository.update(commentMapper.toComment(commentDto));
        return commentDto;
    }

    @Override
    public void delete(Long id) {
        commentRepository.delete(id);
    }
}
