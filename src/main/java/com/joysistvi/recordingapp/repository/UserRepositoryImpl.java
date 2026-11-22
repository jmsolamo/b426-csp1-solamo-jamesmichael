package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final DbConnection dbConnection;

    public UserRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<User> findAllActive() {

        String query = "SELECT * FROM users " +
                "WHERE is_archived = 0 " +
                "ORDER BY id ASC";

        List<User> users = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("role")
                );

                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving active users: " + e.getMessage());
        }

        return users;
    }

    @Override
    public List<User> findAllArchived() {

        String query = "SELECT * FROM users " +
                "WHERE is_archived = 1 " +
                "ORDER BY id ASC";

        List<User> users = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("role")
                );

                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving archived users: " + e.getMessage());
        }

        return users;
    }

    @Override
    public User findByUsername(String username) {

        String query = "SELECT * FROM users " +
                "WHERE username = ? AND is_archived = 0 " +
                "LIMIT 1";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("email"),
                            resultSet.getString("role")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding user: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean save(User user) {

        String query = "INSERT INTO users " +
                "(username, password, email, role) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getRole());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(User user) {

        String query = "UPDATE users SET username = ?, password = ?, " +
                "email = ?, role = ? " +
                "WHERE id = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getRole());
            statement.setInt(5, user.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(int id) {

        String query = "DELETE FROM users WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean archiveById(int id) {

        String query = "UPDATE users SET is_archived = 1 " +
                "WHERE id = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error archiving user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean restoreById(int id) {

        String query = "UPDATE users SET is_archived = 0 " +
                "WHERE id = ? AND is_archived = 1";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error restoring user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<User> searchByUsername(String keyword) {

        String query = "SELECT * FROM users " +
                "WHERE username LIKE ? AND is_archived = 0 " +
                "ORDER BY id ASC";

        List<User> users = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + keyword.trim() + "%");

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    User user = new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("email"),
                            resultSet.getString("role")
                    );

                    users.add(user);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching users: " + e.getMessage());
        }

        return users;
    }
}
