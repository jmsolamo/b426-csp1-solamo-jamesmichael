package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Album;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AlbumRepositoryImpl implements AlbumRepository {

    private final DbConnection dbConnection;

    public AlbumRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Album> findAllActive() {

        String query = "SELECT albums.id, albums.name, albums.year, " +
                "albums.artist_id, artists.name AS artist_name, albums.is_archived " +
                "FROM albums " +
                "JOIN artists ON albums.artist_id = artists.id " +
                "WHERE albums.is_archived = 0 " +
                "ORDER BY albums.id ASC";

        List<Album> albums = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Album album = new Album(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("year"),
                        resultSet.getInt("artist_id"),
                        resultSet.getString("artist_name"),
                        resultSet.getBoolean("is_archived")
                );

                albums.add(album);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving active albums: " + e.getMessage());
        }

        return albums;
    }

    @Override
    public List<Album> findAllArchived() {

        String query = "SELECT albums.id, albums.name, albums.year, " +
                "albums.artist_id, artists.name AS artist_name, albums.is_archived " +
                "FROM albums " +
                "JOIN artists ON albums.artist_id = artists.id " +
                "WHERE albums.is_archived = 1 " +
                "ORDER BY albums.id ASC";

        List<Album> albums = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Album album = new Album(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("year"),
                        resultSet.getInt("artist_id"),
                        resultSet.getString("artist_name"),
                        resultSet.getBoolean("is_archived")
                );

                albums.add(album);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving archived albums: " + e.getMessage());
        }

        return albums;
    }

    @Override
    public boolean save(Album album) {

        String query = "INSERT INTO albums (name, year, artist_id) VALUES (?, ?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, album.getName());
            statement.setInt(2, album.getYear());
            statement.setInt(3, album.getArtistId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error saving album: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Album album) {

        String query = "UPDATE albums SET name = ?, year = ?, artist_id = ? " +
                "WHERE id = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, album.getName());
            statement.setInt(2, album.getYear());
            statement.setInt(3, album.getArtistId());
            statement.setInt(4, album.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating album: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(int id) {

        String query = "DELETE FROM albums WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting album: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean archiveById(int id) {

        String query = "UPDATE albums SET is_archived = 1 " +
                "WHERE id = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error archiving album: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean restoreById(int id) {

        String query = "UPDATE albums SET is_archived = 0 " +
                "WHERE id = ? AND is_archived = 1";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error restoring album: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Album> searchByName(String keyword) {

        String query = "SELECT albums.id, albums.name, albums.year, " +
                "albums.artist_id, artists.name AS artist_name, albums.is_archived " +
                "FROM albums " +
                "JOIN artists ON albums.artist_id = artists.id " +
                "WHERE albums.name LIKE ? AND albums.is_archived = 0 " +
                "ORDER BY albums.id ASC";

        List<Album> albums = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + keyword.trim() + "%");

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Album album = new Album(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("year"),
                            resultSet.getInt("artist_id"),
                            resultSet.getString("artist_name"),
                            resultSet.getBoolean("is_archived")
                    );

                    albums.add(album);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching albums: " + e.getMessage());
        }

        return albums;
    }
}
