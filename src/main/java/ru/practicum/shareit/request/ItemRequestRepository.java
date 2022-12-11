package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ru.practicum.shareit.request.model.ItemRequest, Long> {

    @Query("SELECT r FROM ItemRequest r WHERE r.requester.id = ?1 ORDER BY r.dateOfCreation DESC")
    List<ItemRequest> getItemRequestsByRequester(long userId, Pageable pageable);

    @Query("SELECT r FROM ItemRequest r WHERE r.requester.id <> ?1 ORDER BY r.dateOfCreation DESC")
    List<ItemRequest> getItemRequestsByRequesters(long userId, Pageable pageable);
}
