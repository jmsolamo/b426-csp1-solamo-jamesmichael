package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.repository.SongRepository;
import java.util.Collections;
import java.util.List;

public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public List<Song> getAllActiveSongs() {

        return songRepository.findAllActive();
    }

    @Override
    public List<Song> getAllArchivedSongs() {

        return songRepository.findAllArchived();
    }

    @Override
    public boolean createSong(String title, String genre, String length, int albumId) {

        if (title == null || title.trim().isEmpty()) {
            System.out.println("Validation Error: Song title cannot be empty.");
            return false;
        }

        if (genre == null || genre.trim().isEmpty()) {
            System.out.println("Validation Error: Song genre cannot be empty.");
            return false;
        }

        if (length == null || length.trim().isEmpty()) {
            System.out.println("Validation Error: Song length cannot be empty.");
            return false;
        }

        if (albumId <= 0) {
            System.out.println("Validation Error: Album ID must be greater than zero.");
            return false;
        }

        Song song = new Song(title.trim(), genre.trim(), length.trim(), albumId);
        return songRepository.save(song);
    }

    @Override
    public boolean updateSong(int id, String title, String genre, String length, int albumId) {

        if (id <= 0) {
            System.out.println("Validation Error: Song ID must be greater than zero.");
            return false;
        }

        if (title == null || title.trim().isEmpty()) {
            System.out.println("Validation Error: Song title cannot be empty.");
            return false;
        }

        if (genre == null || genre.trim().isEmpty()) {
            System.out.println("Validation Error: Song genre cannot be empty.");
            return false;
        }

        if (length == null || length.trim().isEmpty()) {
            System.out.println("Validation Error: Song length cannot be empty.");
            return false;
        }

        if (albumId <= 0) {
            System.out.println("Validation Error: Album ID must be greater than zero.");
            return false;
        }

        Song song = new Song(id, title.trim(), genre.trim(), length.trim(), albumId);
        return songRepository.update(song);
    }

    @Override
    public boolean deleteSong(int id) {

        if (id <= 0) {
            System.out.println("Validation Error: Song ID must be greater than zero.");
            return false;
        }

        return songRepository.deleteById(id);
    }

    @Override
    public boolean archiveSong(int id) {

        if (id <= 0) {
            System.out.println("Validation Error: Song ID must be greater than zero.");
            return false;
        }

        return songRepository.archiveById(id);
    }

    @Override
    public boolean restoreSong(int id) {

        if (id <= 0) {
            System.out.println("Validation Error: Song ID must be greater than zero.");
            return false;
        }

        return songRepository.restoreById(id);
    }

    @Override
    public List<Song> searchSongs(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Validation Error: Search keyword cannot be empty.");
            return Collections.emptyList();
        }

        return songRepository.searchByTitle(keyword.trim());
    }
}
