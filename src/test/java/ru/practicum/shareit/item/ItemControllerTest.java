package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.CommentService;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ItemService itemServiceImpl;

    @MockBean
    CommentService commentServiceImpl;
    private final ObjectMapper objectMapper = new ObjectMapper();
    ItemDto itemDto;

    @BeforeEach
    void beforeEach() {
        itemDto = new ItemDto(1L, "вещь", "описание вещи", true, 1L);
    }

    @Test
    void createItemTest() throws Exception {
        when(itemServiceImpl.createItem(anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));

        verify(itemServiceImpl, times(1))
                .createItem(1L, itemDto);
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemServiceImpl.updateItem(anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));

        verify(itemServiceImpl, times(1))
                .updateItem(1L, itemDto);
    }

    @Test
    void getItemTest() throws Exception {
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "вещь", "описание вещи", true, 1L, null, null, null);
        itemServiceImpl.createItem(1L, itemDto);

        when(itemServiceImpl.getItem(anyLong(), anyLong()))
                .thenReturn(itemWithBookingDto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())));

        verify(itemServiceImpl, times(1))
                .getItem(1L, 1L);
    }

    @Test
    void getAllItemsOfUserTest() throws Exception {
        when(itemServiceImpl.getAllItemsOfUser(anyLong(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemServiceImpl, times(1))
                .getAllItemsOfUser(1L, 0, 10);
    }

    @Test
    void getItemsBySearchTest() throws Exception {
        when(itemServiceImpl.getItemsBySearch(anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param("text", "home"))
                .andExpect(status().isOk());

        verify(itemServiceImpl, times(1))
                .getItemsBySearch("home", 0, 10);
    }

    @Test
    void createCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "вещь пригодилась", 1L, "Автор отзыва", null);
        when(commentServiceImpl.createComment(any(), anyLong()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));

        verify(commentServiceImpl, times(1))
                .createComment(commentDto, 1L);
    }
}
