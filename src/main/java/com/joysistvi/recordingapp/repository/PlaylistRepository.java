package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.Song;

import java.util.List;

public interface PlaylistRepository {
    List<Playlist> findAllActive();
    List<Playlist> findAllArchived();

    boolean save(Playlist playlist);
    boolean update(Playlist playlist);
    boolean deleteById(int id);
    boolean archiveById(int id);
    boolean restoreById(int id);
    List<Playlist> searchByName(String keyword);


    List<Song> findSongsByPlaylistId(int playlistId);

    List<Playlist> findByUserId(int userId);

    boolean addSongToPlaylist(int playlistId, int songId);
    boolean removeSongFromPlaylist(int playlistId, int songId);

}
