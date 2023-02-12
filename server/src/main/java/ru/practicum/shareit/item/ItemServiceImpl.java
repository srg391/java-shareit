package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public ItemWithBookingDto getItem(long userId, long itemId) {
        ItemWithBookingDto itemWithBookingDto = null;
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь c id=" + itemId + " не существует!"));
        if (!item.getOwner().getId().equals(userId)) {
            return itemMapper.createDtoItemWithBooking(item, null, null);
        }
        itemWithBookingDto = getItemWithBooking(item, userId, itemId);
        return itemWithBookingDto;
    }

    private ItemWithBookingDto getItemWithBooking(Item item, long ownerId, long itemId) {
        List<Booking> lastBookings = bookingRepository.findByItem_owner_idAndItem_idAndEndLessThanAndStatusOrderByStartDesc(ownerId, itemId, LocalDateTime.now(), BookingStatus.APPROVED);
        List<Booking> futureBookings = bookingRepository.findByItem_owner_idAndItem_idAndStartGreaterThanAndStatusOrderByStartDesc(ownerId, itemId, LocalDateTime.now(), BookingStatus.APPROVED);

        Booking last = lastBookings.stream()
                .max(Comparator.comparing(Booking::getEnd))
                .orElse(null);

        Booking next = futureBookings.stream()
                .findFirst()
                .orElse(null);
        return itemMapper.createDtoItemWithBooking(item, last, next);
    }

    @Transactional
    @Override
    public List<ItemWithBookingDto> getAllItemsOfUser(long userId, int from, int size) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Хозяин c id=" + userId + " не существует!"));
        int page = from / size;
        List<Item> items = itemRepository.findAll(PageRequest.of(page, size)).stream()
                .filter(i -> i.getOwner() == owner)
                .collect(Collectors.toList());
        List<ItemWithBookingDto> itemsFinal = new ArrayList<>();
        for (Item item : items) {
            ItemWithBookingDto itemWithBookingDto = getItemWithBooking(item, userId, item.getId());
            itemsFinal.add(itemWithBookingDto);
        }
        return itemsFinal;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getItemsBySearch(String text, int from, int size) {
        int page = from / size;
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> items = itemRepository.findAll(PageRequest.of(page, size)).stream()
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
        return itemMapper.createDtoListItem(items);
    }

    @Transactional
    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Хозяин c id=" + userId + " не существует!"));
        Item item = itemMapper.createItem(itemDto, owner);
        Item itemFromRepository = itemRepository.save(item);
        return itemMapper.createDtoItem(itemFromRepository);
    }

    @Transactional
    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        final Item item = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Вещь c id=" + itemDto.getId() + " не существует!"));
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Хозяин c id=" + userId + " не существует!"));
        if (item.getOwner().getId().equals(owner.getId())) {
            Optional.ofNullable(itemDto.getName()).ifPresent(n -> item.setName(n));
            Optional.ofNullable(itemDto.getDescription()).ifPresent(d -> item.setDescription(d));
            Optional.ofNullable(itemDto.getAvailable()).ifPresent(a -> item.setAvailable(a));
            itemRepository.save(item);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return itemMapper.createDtoItem(item);
    }
}
