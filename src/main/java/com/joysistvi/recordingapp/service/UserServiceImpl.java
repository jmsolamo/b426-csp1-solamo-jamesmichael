package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Collections;
import java.util.List;

public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllActiveUsers() {

        return userRepository.findAllActive();
    }

    @Override
    public List<User> getAllArchivedUsers() {

        return userRepository.findAllArchived();
    }

    @Override
    public boolean createUser(String username, String password, String email) {

        if (username == null || username.trim().isEmpty()) {
            System.out.println("Validation Error: Username cannot be empty.");
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            System.out.println("Validation Error: Email cannot be empty.");
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Validation Error: Please enter a valid email address.");
            return false;
        }

        if (password == null || password.length() < 6) {
            System.out.println("Validation Error: Password must contain at least 6 characters.");
            return false;
        }

        if (userRepository.findByUsername(username.trim()) != null) {
            System.out.println("Validation Error: Username is already taken.");
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username.trim(), hashedPassword, email.trim(), "USER");
        return userRepository.save(user);
    }

    @Override
    public boolean updateUser(int id, String username, String email) {

        if (id <= 0) {
            System.out.println("Validation Error: User ID must be greater than zero.");
            return false;
        }

        if (username == null || username.trim().isEmpty()) {
            System.out.println("Validation Error: Username cannot be empty.");
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            System.out.println("Validation Error: Email cannot be empty.");
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Validation Error: Please enter a valid email address.");
            return false;
        }

        User existingUser = null;

        for (User user : userRepository.findAllActive()) {
            if (user.getId() == id) {
                existingUser = user;
                break;
            }
        }

        if (existingUser == null) {
            System.out.println("Validation Error: User not found.");
            return false;
        }

        existingUser.setUsername(username.trim());
        existingUser.setEmail(email.trim());
        return userRepository.update(existingUser);
    }

    @Override
    public boolean deleteUser(int id) {

        if (id <= 0) {
            System.out.println("Validation Error: User ID must be greater than zero.");
            return false;
        }

        return userRepository.deleteById(id);
    }

    @Override
    public boolean archiveUser(int id) {

        if (id <= 0) {
            System.out.println("Validation Error: User ID must be greater than zero.");
            return false;
        }

        return userRepository.archiveById(id);
    }

    @Override
    public boolean restoreUser(int id) {

        if (id <= 0) {
            System.out.println("Validation Error: User ID must be greater than zero.");
            return false;
        }

        return userRepository.restoreById(id);
    }

    @Override
    public List<User> searchUsers(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Validation Error: Search keyword cannot be empty.");
            return Collections.emptyList();
        }

        return userRepository.searchByUsername(keyword.trim());
    }

    @Override
    public User loginUser(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            System.out.println("Validation Error: Username cannot be empty.");
            return null;
        }

        if (password == null || password.isEmpty()) {
            System.out.println("Validation Error: Password cannot be empty.");
            return null;
        }

        User user = userRepository.findByUsername(username.trim());

        if (user == null) {
            System.out.println("Invalid username or password.");
            return null;
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            System.out.println("Invalid username or password.");
            return null;
        }

        System.out.println("Login successful. Welcome, " + user.getUsername() + "!");
        return user;
    }
}
