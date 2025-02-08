package ru.yandex.practicum.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.DatabaseHelper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostControllerIntegrationTest {

    @Autowired
    private DatabaseHelper databaseHelper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        databaseHelper.resetDatabase();
    }

    @Test
    @Order(1)
    void whenFindAllPosts_thenSuccess() throws Exception {
        mockMvc.perform(get("/post"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("pageNumbers"));
    }

    @Test
    @Order(2)
    void whenFindPostById_thenSuccess() throws Exception {
        mockMvc.perform(get("/post/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"));

    }

    @Test
    @Order(3)
    void whenFilterPosts_thenSuccess() throws Exception {
        mockMvc.perform(post("/post/search")
                        .param("name", "it")
                        .param("page", "1")
                        .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("pageNumbers"));
    }

    @Test
    @Order(4)
    void whenSavePost_thenSuccess() throws Exception {
        mockMvc.perform(post("/post/add")
                        .param("name", "Test")
                        .param("image", "Image")
                        .param("text", "Text")
                        .param("likes", "5")
                        .param("tags", "it")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post"));
    }

    @Test
    @Order(5)
    void whenEditPost_thenSuccess() throws Exception {
        mockMvc.perform(post("/post/2/edit")
                        .param("id", "2")
                        .param("name", "Updated")
                        .param("image", "Image5")
                        .param("text", "New text")
                        .param("likes", "15")
                        .param("tags", "social")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post"));
    }

    @Test
    @Order(6)
    void whenDeletePost_thenSuccess() throws Exception {
        mockMvc.perform(post("/post/1")
                        .param("_method", "delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post"));
    }

    @Test
    @Order(7)
    void whenIncreaseLikes_thenSuccess() throws Exception {
        mockMvc.perform(post("/post/2/like"))
                .andExpect(status().isOk());
    }
}
