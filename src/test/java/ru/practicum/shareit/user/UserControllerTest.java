package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserServiceImpl userServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    private UserDto userDto;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        userDto = new UserDto(1L, "Sergey1", "sergey1@gmail.com");
    }

    @Test
    void getAllUsersTest() throws Exception {
        when(userServiceImpl.getAllUsers())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(userServiceImpl, times(1))
                .getAllUsers();
    }

    @Test
    void getUserTest() throws Exception {
        userServiceImpl.createUser(userDto);
        when(userServiceImpl.getUser(1L))
                .thenReturn(userDto);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDto.getName())));

        verify(userServiceImpl, times(1))
                .getUser(1L);
    }


    @Test
    void createUserTest() throws Exception {
        when(userServiceImpl.createUser(any()))
                .thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userServiceImpl, times(1))
                .createUser(userDto);
    }

    @Test
    void updateUserTest() throws Exception {
        when(userServiceImpl.updateUser(anyLong(), any()))
                .thenReturn(userDto);

        mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userServiceImpl, times(1))
                .updateUser(1L, userDto);
    }

    @Test
    void deleteByUserIdTest() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
        verify(userServiceImpl, times(1))
                .deleteByUserId(1);
    }
}
