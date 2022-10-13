package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.InvalidEmailException;

import java.util.HashSet;
import java.util.Set;

@Component
public class UniqueEmailsRepository {

    private final Set<String> uniqueEmails = new HashSet<>();

    public void checkEmailForUniquenessAndValidity(String email) {
        if (!email.contains(" ") && email.matches(".+@.+\\.[a-z]+")) {
            if (uniqueEmails.contains(email)) {
                throw new AlreadyExistsException("Пользователь с почтой " + email + " уже существует!");
            } else uniqueEmails.add(email);
        } else throw new InvalidEmailException("Email не соотвествует!");
    }

    public void deleteEmailFromSetStorage(String email) {
        uniqueEmails.remove(email);
    }
}
