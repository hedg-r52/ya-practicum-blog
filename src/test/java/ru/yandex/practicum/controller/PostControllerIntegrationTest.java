package ru.yandex.practicum.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.DatabaseHelper;
import ru.yandex.practicum.config.DataSourceConfiguration;
import ru.yandex.practicum.config.WebConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig({DataSourceConfiguration.class, WebConfiguration.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class PostControllerIntegrationTest {

    @Autowired
    private PostController postController;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DatabaseHelper databaseHelper;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
