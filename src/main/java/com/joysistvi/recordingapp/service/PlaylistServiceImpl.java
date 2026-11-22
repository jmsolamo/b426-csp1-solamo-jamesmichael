package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.repository.PlaylistRepository;
import java.util.Collections;
import java.util.List;

public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Override
    public List<Playlist> getAllActivePlaylists() {

        return playlistRepository.findAllActive();
    }

    @Override
    public List<Playlist> getAllArchivedPlaylists() {

        return playlistRepository.findAllArchived();
    }

    @Override
    public boolean createPlaylist(String name, int userId) {

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Validation Error: Playlist name cannot be empty.");
            return false;
        }

        if (userId <= 0) {
            System.out.println("Validation Error: User ID must be greater than zero.");
            return false;
        }

        Playlist playlist = new Playlist(name.trim(), userId);
        return playlistRepository.save(playlist);
    }

    @Override
    public boolean updatePlaylist(int id, String name, int userId) {

        if (id <= 0) {
            System.out.println("Validation Error: Playlist ID must be greater than zero.");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Validation Error: Playlist name cannot be empty.");
            return false;
        }

        if (userId <= 0) {
            System.out.println("Validation Error: User ID must be greater than zero.");
            return false;
        }

        Playlist playlist = new Playlist(id, name.trim(), userId);
        return playlistRepository.update(playlist);
    }

    @Override
    public boolean deletePlaylist(int id) {

        if (id <= 0) {
            System.out.println("Validation Error: Playlist ID must be greater than zero.");
            return false;
        }

        return playlistRepository.deleteById(id);
    }

    @Override
    public boolean archivePlaylist(int id) {

        if (id <= 0) {
            System.out.println("Validation Error: Playlist ID must be greater than zero.");
            return false;
        }

        return playlistRepository.archiveById(id);
    }

    @Override
    public boolean restorePlaylist(int id) {

        if (id <= 0) {
            System.out.println("Validation Error: Playlist ID must be greater than zero.");
            return false;
        }

        return playlistRepository.restoreById(id);
    }

    @Override
    public List<Playlist> searchPlaylists(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Validation Error: Search keyword cannot be empty.");
            return Collections.emptyList();
        }

        return playlistRepository.searchByName(keyword.trim());
    }
}
