package ru.romanov.moneytransferservice.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.romanov.moneytransferservice.exception.UserNotFoundException;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.repository.AccountRepository;
import ru.romanov.moneytransferservice.repository.UserRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_Success() {
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser("Doe", "John", "Middle", LocalDate.of(1990, 1, 1), "john.doe@example.com", "1234567890");

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUsers_Success() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_Success() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_UserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getUserByUniqueNumber_Success() {
        User user = new User();
        when(userRepository.findByUniqueNumber("UNIQUE12-UNIQUE12-UNIQUE12")).thenReturn(Optional.of(user));

        User result = userService.getUserByUniqueNumber("UNIQUE12-UNIQUE12-UNIQUE12");

        assertNotNull(result);
        verify(userRepository, times(1)).findByUniqueNumber("UNIQUE12-UNIQUE12-UNIQUE12");
    }

    @Test
    void getUserByUniqueNumber_UserNotFoundException() {
        when(userRepository.findByUniqueNumber("UNIQUE12-UNIQUE12-UNIQUE12")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByUniqueNumber("UNIQUE12-UNIQUE12-UNIQUE12"));
    }

    @Test
    void updateUser_Success() {
        User user = new User();
        userService.updateUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setUniqueNumber("UNIQUE12-UNIQUE12-UNIQUE12");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findByOwnerUniqueNumber("UNIQUE12-UNIQUE12-UNIQUE12")).thenReturn(List.of());

        userService.deleteUser(1L);

        verify(accountRepository, times(1)).findByOwnerUniqueNumber("UNIQUE12-UNIQUE12-UNIQUE12");
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_UserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void generateUniqueNumber_Success() {
        User user = new User();
        user.setUniqueNumber("UNIQUE12-UNIQUE12-UNIQUE12");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String uniqueNumber = userService.createUser("Doe", "John", "Middle", LocalDate.of(1990, 1, 1), "john.doe@example.com", "1234567890").getUniqueNumber();

        assertNotNull(uniqueNumber);
        assertEquals("UNIQUE12-UNIQUE12-UNIQUE12", uniqueNumber);
        assertTrue(uniqueNumber.matches("[A-Z0-9/]{8}-[A-Z0-9/]{8}-[A-Z0-9/]{8}"));
    }
}
