package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.dto.ItemDto;
import ru.practicum.shareit.dto.ItemWithBookingDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemMapper {

    private final CommentMapper commentMapper;

    public ItemDto createDtoItem(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId()
        );
    }

    public List<ItemDto> createDtoListItem(List<Item> items) {
        return items.stream()
                .map(this::createDtoItem)
                .collect(Collectors.toList());
    }


    public static Item createItem(ItemDto itemDto, User owner) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                itemDto.getRequestId()
        );
    }

    public ItemWithBookingDto createDtoItemWithBooking(Item item, Booking lastBooking, Booking nextBooking) {
        if (lastBooking == null && nextBooking == null) {
            return new ItemWithBookingDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequestId(),
                    commentMapper.createDtoListComment(item.getComments()),
                    null,
                    null);
        } else if (lastBooking == null) {
            return new ItemWithBookingDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequestId(),
                    commentMapper.createDtoListComment(item.getComments()),
                    null,
                    new ItemWithBookingDto.Booking(nextBooking.getId(), nextBooking.getBooker().getId()));
        } else if (nextBooking == null) {
            return new ItemWithBookingDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequestId(),
                    commentMapper.createDtoListComment(item.getComments()),
                    new ItemWithBookingDto.Booking(lastBooking.getId(), lastBooking.getBooker().getId()),
                    null);
        } else {
            return new ItemWithBookingDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequestId(),
                    commentMapper.createDtoListComment(item.getComments()),
                    new ItemWithBookingDto.Booking(lastBooking.getId(), lastBooking.getBooker().getId()),
                    new ItemWithBookingDto.Booking(nextBooking.getId(), nextBooking.getBooker().getId())
            );
        }
    }
}
