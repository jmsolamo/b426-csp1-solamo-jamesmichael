package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Playlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PlaylistRepositoryImpl implements PlaylistRepository {

    private final DbConnection dbConnection;

    public PlaylistRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Playlist> findAllActive() {

        String query = "SELECT playlists.id, playlists.name, playlists.date_created, " +
                "playlists.user_id, users.username, playlists.is_archived " +
                "FROM playlists " +
                "JOIN users ON playlists.user_id = users.id " +
                "WHERE playlists.is_archived = 0 " +
                "ORDER BY playlists.id ASC";

        List<Playlist> playlists = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Playlist playlist = new Playlist(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("date_created"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getBoolean("is_archived")
                );

                playlists.add(playlist);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving active playlists: " + e.getMessage());
        }

        return playlists;
    }

    @Override
    public List<Playlist> findAllArchived() {

        String query = "SELECT playlists.id, playlists.name, playlists.date_created, " +
                "playlists.user_id, users.username, playlists.is_archived " +
                "FROM playlists " +
                "JOIN users ON playlists.user_id = users.id " +
                "WHERE playlists.is_archived = 1 " +
                "ORDER BY playlists.id ASC";

        List<Playlist> playlists = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Playlist playlist = new Playlist(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("date_created"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getBoolean("is_archived")
                );

                playlists.add(playlist);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving archived playlists: " + e.getMessage());
        }

        return playlists;
    }

    @Override
    public boolean save(Playlist playlist) {

        String query = "INSERT INTO playlists (name, user_id) VALUES (?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, playlist.getName());
            statement.setInt(2, playlist.getUserId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error saving playlist: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Playlist playlist) {

        String query = "UPDATE playlists SET name = ?, user_id = ? " +
                "WHERE id = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, playlist.getName());
            statement.setInt(2, playlist.getUserId());
            statement.setInt(3, playlist.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating playlist: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(int id) {

        String query = "DELETE FROM playlists WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting playlist: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean archiveById(int id) {

        String query = "UPDATE playlists SET is_archived = 1 " +
                "WHERE id = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error archiving playlist: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean restoreById(int id) {

        String query = "UPDATE playlists SET is_archived = 0 " +
                "WHERE id = ? AND is_archived = 1";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error restoring playlist: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Playlist> searchByName(String keyword) {

        String query = "SELECT playlists.id, playlists.name, playlists.date_created, " +
                "playlists.user_id, users.username, playlists.is_archived " +
                "FROM playlists " +
                "JOIN users ON playlists.user_id = users.id " +
                "WHERE playlists.name LIKE ? AND playlists.is_archived = 0 " +
                "ORDER BY playlists.id ASC";

        List<Playlist> playlists = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + keyword.trim() + "%");

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Playlist playlist = new Playlist(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("date_created"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("username"),
                            resultSet.getBoolean("is_archived")
                    );

                    playlists.add(playlist);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching playlists: " + e.getMessage());
        }

        return playlists;
    }
}
