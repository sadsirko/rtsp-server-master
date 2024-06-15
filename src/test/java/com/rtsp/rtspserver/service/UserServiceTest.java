package com.rtsp.rtspserver.service;

import com.rtsp.rtspserver.model.User;
import com.rtsp.rtspserver.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void addUser_ShouldCreateUser_WhenUserDoesNotExists() {
        User user = new User(1, "testUser", 1, "password123");
        when(userRepository.findByUserLogin("testUser")).thenReturn(Optional.empty());
        when(userRepository.addUser(any(User.class))).thenReturn(user);

        User createdUser = userService.addUser(user);

        assertNotNull(createdUser);
        assertEquals(user.getUserLogin(), createdUser.getUserLogin());
        verify(userRepository).addUser(user);
    }

    @Test
    void addUser_ShouldThrow_WhenUserAlreadyExists() {
        User user = new User(1, "testUser", 1, "password123");
        when(userRepository.findByUserLogin("testUser")).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () -> {
            userService.addUser(user);
        });
    }

    @Test
    void deleteUser_ShouldReturnTrue_WhenUserExists() {
        when(userRepository.deleteUser(1)).thenReturn(true);

        assertTrue(userService.deleteUser(1));
        verify(userRepository).deleteUser(1);
    }
}
