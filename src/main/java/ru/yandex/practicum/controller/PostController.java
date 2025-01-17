package ru.yandex.practicum.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostShortDto;
import ru.yandex.practicum.service.PostService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/post")
public class PostController {
    private final Long LIMIT = 5L;
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String blogList(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

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
                         @RequestParam("limit") Optional<Long> optionalLimit,
                         @RequestParam("offset") Optional<Long> optionalOffset) {
        if (tag.isBlank()) {
            return "redirect:/post";
        }
        long offset = optionalOffset.orElse(0L);
        long limit = optionalLimit.orElse(LIMIT);
        List<PostShortDto> posts = postService.findAllFilteredByTag(tag, limit, offset);
        model.addAttribute("posts", posts);
        model.addAttribute("name", tag);

        return "posts";
    }
}
