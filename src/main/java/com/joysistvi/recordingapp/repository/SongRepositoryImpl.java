package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SongRepositoryImpl implements SongRepository {

    private final DbConnection dbConnection;

    public SongRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Song> findAllActive() {

        String query = "SELECT songs.id, songs.title, songs.genre, songs.length, " +
                "songs.album_id, albums.name AS album_name, songs.is_archived " +
                "FROM songs " +
                "JOIN albums ON songs.album_id = albums.id " +
                "WHERE songs.is_archived = 0 " +
                "ORDER BY songs.id ASC";

        List<Song> songs = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Song song = new Song(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("genre"),
                        resultSet.getString("length"),
                        resultSet.getInt("album_id"),
                        resultSet.getString("album_name"),
                        resultSet.getBoolean("is_archived")
                );

                songs.add(song);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving active songs: " + e.getMessage());
        }

        return songs;
    }

    @Override
    public List<Song> findAllArchived() {

        String query = "SELECT songs.id, songs.title, songs.genre, songs.length, " +
                "songs.album_id, albums.name AS album_name, songs.is_archived " +
                "FROM songs " +
                "JOIN albums ON songs.album_id = albums.id " +
                "WHERE songs.is_archived = 1 " +
                "ORDER BY songs.id ASC";

        List<Song> songs = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Song song = new Song(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("genre"),
                        resultSet.getString("length"),
                        resultSet.getInt("album_id"),
                        resultSet.getString("album_name"),
                        resultSet.getBoolean("is_archived")
                );

                songs.add(song);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving archived songs: " + e.getMessage());
        }

        return songs;
    }

    @Override
    public boolean save(Song song) {

        String query = "INSERT INTO songs (title, genre, length, album_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, song.getTitle());
            statement.setString(2, song.getGenre());
            statement.setString(3, song.getLength());
            statement.setInt(4, song.getAlbumId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error saving song: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Song song) {

        String query = "UPDATE songs SET title = ?, genre = ?, length = ?, album_id = ? " +
                "WHERE id = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, song.getTitle());
            statement.setString(2, song.getGenre());
            statement.setString(3, song.getLength());
            statement.setInt(4, song.getAlbumId());
            statement.setInt(5, song.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating song: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(int id) {

        String query = "DELETE FROM songs WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting song: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean archiveById(int id) {

        String query = "UPDATE songs SET is_archived = 1 " +
                "WHERE id = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error archiving song: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean restoreById(int id) {

        String query = "UPDATE songs SET is_archived = 0 " +
                "WHERE id = ? AND is_archived = 1";

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error restoring song: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Song> searchByTitle(String keyword) {

        String query = "SELECT songs.id, songs.title, songs.genre, songs.length, " +
                "songs.album_id, albums.name AS album_name, songs.is_archived " +
                "FROM songs " +
                "JOIN albums ON songs.album_id = albums.id " +
                "WHERE songs.title LIKE ? AND songs.is_archived = 0 " +
                "ORDER BY songs.id ASC";

        List<Song> songs = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + keyword.trim() + "%");

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Song song = new Song(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("genre"),
                            resultSet.getString("length"),
                            resultSet.getInt("album_id"),
                            resultSet.getString("album_name"),
                            resultSet.getBoolean("is_archived")
                    );

                    songs.add(song);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching songs: " + e.getMessage());
        }

        return songs;
    }
}
