package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User user1;
    private User user2;

    private ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        user1 = new User(null, "Sergey1", "sergey1@gmail.com");
        user2 = new User(null, "Valery2", "valery2@mail.ru");
        itemRequest = new ItemRequest(null, "вещь", user1, LocalDateTime.now());
    }

    @Test
    void getItemRequestsByRequesterTest() {
        testEntityManager.persist(user1);
        testEntityManager.persist(itemRequest);

        List<ItemRequest> itemsList = itemRequestRepository.getItemRequestsByRequester(user1.getId(), PageRequest.of(0, 10));

        then(itemsList).size().isEqualTo(1);
        then(itemsList).containsExactlyElementsOf(List.of(itemRequest));
    }

    @Test
    void getItemRequestsByRequestersTest() {
        testEntityManager.persist(user1);
        testEntityManager.persist(user2);
        testEntityManager.persist(itemRequest);

        List<ItemRequest> itemsList = itemRequestRepository.getItemRequestsByRequesters(user2.getId(), PageRequest.of(0, 10));

        then(itemsList).size().isEqualTo(1);
        then(itemsList).containsExactlyElementsOf(List.of(itemRequest));
    }
}
