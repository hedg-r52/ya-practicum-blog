package ru.yandex.practicum.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.DatabaseHelper;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentControllerIntegrationTest {

    @Autowired
    private DatabaseHelper databaseHelper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        databaseHelper.resetDatabase();
    }

    @Test
    public void addComment_shouldAddComment() throws Exception {
        mockMvc.perform(post("/comment")
                        .param("postId", "1")
                        .param("text", "Test")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/1"));
    }

    @Test
    public void deleteComment_shouldDeleteComment() throws Exception {
        mockMvc.perform(post("/comment/1")
                        .param("content", "content")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/1"));
    }

    @Test
    public void editComment_shouldEditComment() throws Exception {
        mockMvc.perform(post("/comment/edit")
                        .param("id", "2")
                        .param("postId", "1")
                        .param("text", "Updated")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/1"));
    }

}
