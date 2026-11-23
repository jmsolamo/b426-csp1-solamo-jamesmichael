package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistRepositoryImpl implements ArtistRepository {

    private final DbConnection dbConnection;

    public ArtistRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Artist> findAllActive() {

        List<Artist> artists = new ArrayList<>();
        String query = "SELECT * FROM artists WHERE is_archived = 0";

        try (Connection connection = dbConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                artists.add(new Artist(id, name));
            }
        } catch (SQLException e) {
            System.out.println("Read Artists Error: " + e.getMessage());
        }

        return artists;
    }

    @Override
    public List<Artist> findAllArchived() {

        List<Artist> artists = new ArrayList<>();

        String query = "SELECT * FROM artists WHERE is_archived = 1 ORDER BY id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                artists.add(new Artist(id, name));
            }

        } catch (SQLException e) {
            System.out.println("Read Archived Artists Error: " + e.getMessage());
        }

        return artists;
    }

    @Override
    public boolean save(Artist artist) {

        String query = "INSERT INTO artists (name) VALUES (?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, artist.getName());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Create Artist Error: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(Artist artist) {

        String query = "UPDATE artists SET name = ? WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, artist.getName());
            preparedStatement.setInt(2, artist.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update Artist Error: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean deleteById(int id) {

        String query = "DELETE FROM artists WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete Artist Error: " + e.getMessage());
        }

        return false;
    }

    public boolean archiveById(int id) {

        String query = "UPDATE artists SET is_archived = 1 WHERE id = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Archive Artist Error: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean restoreById(int id) {

        String query = "UPDATE artists SET is_archived = 0 WHERE id = ? AND is_archived = 1";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Restore Artist Error: " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<Artist> searchByName(String keyword) {

        List<Artist> artists = new ArrayList<>();

        String query = "SELECT * FROM artists " +
                "WHERE is_archived = 0 AND name LIKE ? " +
                "ORDER BY id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + keyword.trim() + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");

                    artists.add(new Artist(id, name));
                }
            }

        } catch (SQLException e) {
            System.out.println("Search Artists Error: " + e.getMessage());
        }

        return artists;
    }

}