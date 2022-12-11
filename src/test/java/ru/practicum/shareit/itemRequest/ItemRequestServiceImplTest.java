package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewestItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemRequestServiceImplTest {

    ItemRequestServiceImpl itemRequestServiceImpl;
    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRequestMapper itemRequestMapper;

    private ItemRequest itemRequest;

    private NewestItemRequestDto newestItemRequestDto;

    private User user;

    @BeforeEach
    void beforeEach() {
        itemRequestServiceImpl = new ItemRequestServiceImpl(itemRequestRepository, userRepository, itemRequestMapper);
        user = new User(1L, "Sergey1", "sergey1@gmail.com");
        itemRequest = new ItemRequest(1L, "вещь", user, LocalDateTime.now());
        newestItemRequestDto = new NewestItemRequestDto("вещь");
    }

    @Test
    void createItemRequestTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRequestMapper.createItemRequest(any(), any(), any()))
                .thenReturn(itemRequest);
        when(itemRequestRepository.save(any()))
                .thenReturn(itemRequest);

        itemRequestServiceImpl.createRequest(user.getId(), newestItemRequestDto);

        verify(itemRequestRepository, times(1)).save(itemRequest);
    }

    @Test
    void getItemRequestsByRequesterTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        final List<ItemRequest> requests = new ArrayList<>(Collections.singletonList(itemRequest));
        when(itemRequestRepository.getItemRequestsByRequester(anyLong(), PageRequest.of(anyInt(), 10)))
                .thenReturn(requests);

        final List<ItemRequestDto> requestDtos = itemRequestServiceImpl.getRequestOfUser(user.getId(), 0, 10);

        assertNotNull(requestDtos);

        verify(itemRequestRepository, times(1)).getItemRequestsByRequester(1L, PageRequest.of(0, 10));
    }

    @Test
    void getItemRequestsByRequestersTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        final List<ItemRequest> requests = new ArrayList<>(Collections.singletonList(itemRequest));
        when(itemRequestRepository.getItemRequestsByRequesters(anyLong(), PageRequest.of(anyInt(), 10)))
                .thenReturn(requests);

        final List<ItemRequestDto> requestDtos = itemRequestServiceImpl.getAllRequestsOfUsers(user.getId(), 0, 10);

        assertNotNull(requestDtos);

        verify(itemRequestRepository, times(1)).getItemRequestsByRequesters(1L, PageRequest.of(0, 10));
    }


    @Test
    void getRequestTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));

        itemRequestServiceImpl.getRequest(user.getId(), itemRequest.getId());
        verify(itemRequestRepository, times(1)).findById(1L);
    }


}
