package ru.yandex.practicum.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.domain.Comment;
import ru.yandex.practicum.domain.Post;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostShortDto;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.TagService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/post")
public class PostController {
    private final int LIMIT = 5;
    private final PostService postService;
    private final TagService tagService;
    private final CommentService commentService;

    public PostController(PostService postService, TagService tagService, CommentService commentService) {
        this.postService = postService;
        this.tagService = tagService;
        this.commentService = commentService;
    }

    @GetMapping
    public String blogList(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(LIMIT);

        Page<PostShortDto> posts = postService.findAll(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("posts", posts);
        int totalPages = posts.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "posts";
    }



    @GetMapping("/{id}")
    public String postDetail(@PathVariable("id") Long id, Model model) {
        PostDto post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "post";
    }

    @PostMapping(value = "/{id}", params = "_method=delete")
    public String delete(@PathVariable(name = "id") Long id) {
        postService.delete(id);
        return "redirect:/post";
    }

    @PostMapping("/search")
    public String search(Model model,
                         @RequestParam("name") String tag,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        if (tag.isBlank()) {
            return "redirect:/post";
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(LIMIT);

        Page<PostShortDto> posts = postService.findAllFilteredByTag(tag, PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("posts", posts);
        int totalPages = posts.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("name", tag);
        return "posts";
    }

    @GetMapping("/add")
    public String addPostPage(Model model) {
        return "add-post";
    }

    @PostMapping(value = "/add")
    public String add(@ModelAttribute("post") PostDto post) {
        PostDto saved = postService.save(post);
        tagService.saveTags(post.getTags(), saved.getId());
        return "redirect:/post";
    }

    @PostMapping("/comment")
    public String addComment(@ModelAttribute("newComment") CommentDto comment) {
        commentService.save(comment);
        return "redirect:/post/" + comment.getParentId();
    }
}
