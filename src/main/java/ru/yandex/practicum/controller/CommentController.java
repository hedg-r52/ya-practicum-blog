package ru.yandex.practicum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.service.CommentService;

@Controller
@RequestMapping("/comment")
public class CommentController {

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    private final CommentService commentService;

    @PostMapping()
    public String addComment(@ModelAttribute("newComment") CommentDto comment) {
        commentService.save(comment);
        return "redirect:/post/" + comment.getPostId();
    }

    @PostMapping(value = "/{id}/post/{postId}")
    public String deleteComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "id") Long id) {
        commentService.delete(id);
        return "redirect:/post/" + postId;
    }

    @PostMapping(value = "/edit")
    public String editComment(@ModelAttribute("editedComment") CommentDto comment) {
        commentService.update(comment);
        return "redirect:/post/" + comment.getPostId();
    }
}
