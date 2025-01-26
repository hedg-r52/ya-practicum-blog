package ru.yandex.practicum.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostShortDto;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.TagService;

import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/post")
public class PostController {
    private final int DEFAULT_PAGE_SIZE = 10;
    private final PostService postService;
    private final TagService tagService;

    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping
    public String blogList(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);

        Page<PostShortDto> posts = postService.findAll(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("posts", posts);
        int totalPages = posts.getTotalPages();
        if (totalPages > 0) {
            long pageNumbers = IntStream.rangeClosed(1, totalPages).count();
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
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);

        Page<PostShortDto> posts = postService.findAllFilteredByTag(tag, PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("posts", posts);
        int totalPages = posts.getTotalPages();
        if (totalPages > 0) {
            long pageNumbers = IntStream.rangeClosed(1, totalPages).count();
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

    @GetMapping("/{id}/edit")
    public String editPostPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return "edit-post";
    }

    @PostMapping(value = "/{id}/edit")
    public String edit(@PathVariable("id") Long id, @ModelAttribute("post") PostDto post) {
        postService.update(post);
        tagService.saveTags(post.getTags(), id);
        return "redirect:/post";
    }

    @PostMapping(value = "/{id}/like")
    @ResponseStatus(value = HttpStatus.OK)
    public void increaseLikeCounter(@PathVariable(name = "id") Long id) {
        postService.increaseLikeCount(id);
    }
}
