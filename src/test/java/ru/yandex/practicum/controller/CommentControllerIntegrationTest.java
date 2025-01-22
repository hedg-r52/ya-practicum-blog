package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.DatabaseHelper;
import ru.yandex.practicum.config.DataSourceConfiguration;
import ru.yandex.practicum.config.WebConfiguration;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig({DataSourceConfiguration.class, WebConfiguration.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class CommentControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DatabaseHelper databaseHelper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

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
        mockMvc.perform(post("/comment/1/post/1")
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
