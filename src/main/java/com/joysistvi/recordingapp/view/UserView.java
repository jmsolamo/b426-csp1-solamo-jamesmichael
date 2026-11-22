package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.service.UserService;
import java.util.List;
import java.util.Scanner;


public class UserView {

    private final UserService userService;
    private final Scanner scanner;

    public UserView(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    public void handleMenu() {

        while (true) {
            System.out.println("\n----- Manage Users -----");
            System.out.println("1. View All Users");
            System.out.println("2. Register User");
            System.out.println("3. Update User");
            System.out.println("4. Search User");
            System.out.println("5. Archive User");
            System.out.println("6. Restore User");
            System.out.println("7. Delete User");
            System.out.println("0. Back");

            int choice = getIntPrompt("Choose an option: ");
            switch (choice) {
                case 1 -> displayAllUsers();
                case 2 -> promptCreateUser();
                case 3 -> promptUpdateUser();
                case 4 -> promptSearchUsers();
                case 5 -> promptArchiveUser();
                case 6 -> promptRestoreUser();
                case 7 -> promptDeleteUser();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayUserTable(List<User> users) {

        if (users == null || users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.printf("%-8s %-25s %-35s %-15s %-10s%n", "ID", "USERNAME", "EMAIL", "ROLE", "STATUS");

        for (User user : users) {
            System.out.printf("%-8d %-25s %-35s %-15s %-10s%n",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    user.isArchived() ? "Archived" : "Active");
        }
    }

    public void promptCreateUser() {

        System.out.println("\n----- User Registration -----");
        String username = getStringPrompt("Enter Username: ");
        String email = getStringPrompt("Enter Email: ");
        String password = getStringPrompt("Enter Password: ");

        boolean created = userService.createUser(username, password, email);

        if (created) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Failed to register user.");
        }
    }

    public void displayAllUsers() {

        System.out.println("\n----- Active Users -----");

        List<User> users = userService.getAllActiveUsers();

        displayUserTable(users);
    }

    public void promptUpdateUser() {

        System.out.println("\n----- Update User -----");

        displayAllUsers();

        int id = getIntPrompt("Enter User ID to update: ");

        if (id <= 0) {
            System.out.println("Invalid User ID.");
            return;
        }

        String username = getStringPrompt("Enter New Username: ");
        String email = getStringPrompt("Enter New Email: ");

        boolean updated = userService.updateUser(id, username, email);

        if (updated) {
            System.out.println("User updated successfully!");
        } else {
            System.out.println("Failed to update user.");
        }
    }

    public void promptSearchUsers() {

        System.out.println("\n----- Search Users -----");

        String keyword = getStringPrompt("Enter Username Keyword: ");

        List<User> users = userService.searchUsers(keyword);

        displayUserTable(users);
    }

    public void promptArchiveUser() {

        System.out.println("\n----- Archive Users -----");
        List<User> users = userService.getAllActiveUsers();
        displayUserTable(users);
        int id = getIntPrompt("Enter User ID to archive: ");

        if (id <= 0) {
            System.out.println("Invalid User ID.");
            return;
        }

        String confirmation = getStringPrompt(
                "Are you sure you want to archive this user? (Y/N): "
        );

        if (!confirmation.equalsIgnoreCase("Y")) {
            System.out.println("Archive operation cancelled.");
            return;
        }

        boolean archived = userService.archiveUser(id);

        if (archived) {
            System.out.println("User archived successfully!");
        } else {
            System.out.println("Failed to archive user.");
        }
    }

    public void promptRestoreUser() {

        System.out.println("\n----- Restore User ------");

        List<User> archivedUsers = userService.getAllArchivedUsers();

        displayUserTable(archivedUsers);

        if (archivedUsers == null || archivedUsers.isEmpty()) {
            return;
        }

        int id = getIntPrompt("Enter User ID to restore: ");

        if (id <= 0) {
            System.out.println("Invalid User ID.");
            return;
        }

        boolean restored = userService.restoreUser(id);

        if (restored) {
            System.out.println("User restored successfully!");
        } else {
            System.out.println("Failed to restore user.");
        }
    }

    public void promptDeleteUser() {

        System.out.println("\n----- Delete User ------");

        displayAllUsers();

        int id = getIntPrompt("Enter User ID to permanently delete: ");

        if (id <= 0) {
            System.out.println("Invalid User ID.");
            return;
        }

        String confirmation = getStringPrompt(
                "Are you sure you want to permanently delete this user? (Y/N): "
        );

        if (!confirmation.equalsIgnoreCase("Y")) {
            System.out.println("Delete operation cancelled.");
            return;
        }

        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            System.out.println("User deleted successfully!");
        } else {
            System.out.println("Failed to delete user.");
        }
    }

    public User promptLoginUser() {

        System.out.println("\n----- User Login -----");

        String username = getStringPrompt("Enter Username: ");
        String password = getStringPrompt("Enter Password: ");

        User user = userService.loginUser(username, password);

        if (user != null) {
            System.out.println("\n\n----- Login Details -----");
            System.out.println("User ID : " + user.getId());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email   : " + user.getEmail());
            System.out.println("Role    : " + user.getRole());
        }
        return user;
    }

    private String getStringPrompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private int getIntPrompt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
