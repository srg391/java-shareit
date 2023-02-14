package ru.gateway.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.gateway.user.UserClient;
import ru.practicum.gateway.user.UserController;
import org.springframework.http.MediaType;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest(classes = UserController.class)
@AutoConfigureMockMvc
class GatewayUserControllerTest {
    @MockBean
    UserClient userClient;
    @Autowired
    private MockMvc mvc;


    @Test
    void testGetUserByWrongId() throws Exception {
        MvcResult response = mvc.perform(get("/users/null")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        System.out.println(message);
        assertTrue(message.contains("Failed"));
    }

    @Test
    void testDeleteWrongId() throws Exception {
        MvcResult response = mvc.perform(delete("/users/null")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        System.out.println(message);
        assertTrue(message.contains("Failed"));
    }

}
