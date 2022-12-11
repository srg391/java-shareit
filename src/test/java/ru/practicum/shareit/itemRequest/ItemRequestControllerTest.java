package ru.practicum.shareit.itemRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewestItemRequestDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ItemRequestServiceImpl itemRequestServiceImpl;
    private ItemRequestDto itemRequestDto;
    private NewestItemRequestDto newestItemRequestDto;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        itemRequestDto = new ItemRequestDto(1L, "вещь", LocalDateTime.now(), null);
        newestItemRequestDto = new NewestItemRequestDto("вещь");
    }

    @Test
    void createRequestTest() throws Exception {
        when(itemRequestServiceImpl.createRequest(anyLong(), any()))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(newestItemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class));
    }

    @Test
    void getRequestOfUserTest() throws Exception {
        when(itemRequestServiceImpl.getRequest(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription()), String.class));

        verify(itemRequestServiceImpl, times(1))
                .getRequest(1L, 1L);
    }

    @Test
    void getAllRequestsOfUserTest() throws Exception {
        when(itemRequestServiceImpl.getRequestOfUser(anyLong(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemRequestServiceImpl, times(1))
                .getRequestOfUser(1L, 0, 10);
    }

    @Test
    void getAllRequestsOfUsersTest() throws Exception {
        when(itemRequestServiceImpl.getAllRequestsOfUsers(anyLong(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemRequestServiceImpl, times(1))
                .getAllRequestsOfUsers(1L, 0, 10);
    }
}
