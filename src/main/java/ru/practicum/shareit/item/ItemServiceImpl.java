package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public ItemWithBookingDto getItem(long itemId, long userId) {
        final Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь c id=" + itemId + " не существует!"));
        long ownerId = item.getOwner().getId();
        if (ownerId != (userId)) {
            return itemMapper.createDtoItemWithBooking(item, null, null);
        }
        return getItemWithBooking(item, userId, itemId);
    }

    private ItemWithBookingDto getItemWithBooking(Item item, long ownerId, long itemId) {
        List<Booking> lastBookings = bookingRepository.findLastBookings(ownerId, itemId, LocalDateTime.now(), BookingStatus.APPROVED);
        List<Booking> futureBookings = bookingRepository.findFutureBookings(ownerId, itemId, LocalDateTime.now(), BookingStatus.APPROVED);

        Booking last = lastBookings.stream()
                .max(Comparator.comparing(Booking::getEnd))
                .orElse(null);

        Booking next = futureBookings.stream()
                .findFirst()
                .orElse(null);

        return itemMapper.createDtoItemWithBooking(item, last, next);
    }

    @Override
    @Transactional
    public List<ItemWithBookingDto> getAllItemsOfUser(long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Хозяин c id=" + userId + " не существует!"));
        List<Item> items = itemRepository.findAll().stream()
                .filter(i -> i.getOwner() == owner)
                .collect(Collectors.toList());
        List<ItemWithBookingDto> itemsFinal = new ArrayList<>();
        for (Item item: items) {
            ItemWithBookingDto itemWithBookingDto = getItemWithBooking(item, userId, item.getId());
            itemsFinal.add(itemWithBookingDto);
        }
        return itemsFinal;
    }

    @Override
    @Transactional
    public List<ItemDto> getItemsBySearch(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> items = itemRepository.findAll().stream()
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
        return itemMapper.createDtoListItem(items);
    }

    @Override
    @Transactional
    public ItemDto createItem(long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Хозяин c id=" + userId + " не существует!"));
        Item item = itemMapper.createItem(itemDto, owner);
        Item itemFromRepository = itemRepository.save(item);
        return itemMapper.createDtoItem(itemFromRepository);
    }

    @Override
    @Transactional
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        final Item item = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Вещь c id=" + itemDto.getId() + " не существует!"));
        final User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Хозяин c id=" + userId + " не существует!"));
        long ownerItemId = item.getOwner().getId();
        long ownerId = owner.getId();
        if (ownerItemId == ownerId) {
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            itemRepository.save(item);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return itemMapper.createDtoItem(item);
    }
}
