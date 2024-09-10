package ru.romanov.moneytransferservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.romanov.moneytransferservice.model.dto.UserDto;
import ru.romanov.moneytransferservice.model.entity.User;
import ru.romanov.moneytransferservice.service.UserService;
import ru.romanov.moneytransferservice.service.ValidationService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_Success() {
        UserDto userDto = new UserDto("Doe", "John", "Smith", LocalDate.of(1990, 1, 1), "john.doe@example.com", "1234567890");
        User user = new User();
        when(validationService.isUserDtoValidated(userDto)).thenReturn(true);
        when(userService.createUser(anyString(), anyString(), anyString(), any(LocalDate.class), anyString(), anyString())).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(validationService, times(1)).isUserDtoValidated(userDto);
        verify(userService, times(1)).createUser(userDto.getLastName(), userDto.getFirstName(), userDto.getPatronymicName(), userDto.getBirthDate(), userDto.getEmail(), userDto.getPhoneNumber());
    }

    @Test
    void createUser_BadRequest() {
        UserDto userDto = new UserDto("Doe", "John", "Smith", LocalDate.of(1990, 1, 1), "john.doe@example.com", "1234567890");
        when(validationService.isUserDtoValidated(userDto)).thenReturn(false);

        ResponseEntity<User> response = userController.createUser(userDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(validationService, times(1)).isUserDtoValidated(userDto);
        verify(userService, times(0)).createUser(anyString(), anyString(), anyString(), any(LocalDate.class), anyString(), anyString());
    }

    @Test
    void getUsers_Success() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.getUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).getUsers();
    }

    @Test
    void getUserById_Success() {
        User user = new User();
        when(userService.getUserById(anyLong())).thenReturn(user);

        ResponseEntity<User> response = userController.getUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getUserByUniqueNumber_Success() {
        User user = new User();
        when(userService.getUserByUniqueNumber(anyString())).thenReturn(user);

        ResponseEntity<User> response = userController.getUser("uniqueNumber123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).getUserByUniqueNumber("uniqueNumber123");
    }

    @Test
    void deleteUser_Success() {
        doNothing().when(userService).deleteUser(anyLong());

        ResponseEntity<String> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully.", response.getBody());
        verify(userService, times(1)).deleteUser(1L);
    }
}
