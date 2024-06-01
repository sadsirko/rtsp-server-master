package com.rtsp.rtspserver.service;

import com.rtsp.rtspserver.model.User;
import com.rtsp.rtspserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create a user with a hashed password
    public User addUser(User user) {
        // Check if user already exists
        Optional<User> existingUser = userRepository.findByUserLogin(user.getUserLogin());
        if (existingUser.isPresent()) {
            // Throw an exception or handle according to your business logic
            throw new IllegalStateException("User already exists with login: " + user.getUserLogin());
        }

        // If user does not exist, hash password and add new user
        user.setHashPassword(hashPassword(user.getHashPassword())); // Hash password before saving
        return userRepository.addUser(user);
    }
    public boolean deleteUser(int userId) {
        return userRepository.deleteUser(userId);
    }

    // Retrieve a user by ID
    public User getUser(int userId) {
        return userRepository.findUserById(userId);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    // Update a user and hash the password if it's changed
    public boolean updateUser(int userId, String userLogin, int roleId, String password) {
        User existingUser = userRepository.findUserById(userId);
        if (existingUser != null) {
            existingUser.setUserLogin(userLogin);
            existingUser.setRoleId(roleId);
            if (!BCrypt.checkpw(password, existingUser.getHashPassword())) { // Check if the new password is different from the old one
                existingUser.setHashPassword(hashPassword(password)); // Hash new password
            }
            return userRepository.updateUser(existingUser);
        }
        return false;
    }

    // Helper method to hash passwords
    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    public boolean validateUserPassword(String submittedPassword, String storedHash) {
        return BCrypt.checkpw(submittedPassword, storedHash);
    }
}
