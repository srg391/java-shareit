package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> jacksonTester;

    @Test
    void createDtoCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "коментарий к вещи", 1L, "Sergey1", null);
        JsonContent<CommentDto> result = jacksonTester.write(commentDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.authorName");
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
    }
}
