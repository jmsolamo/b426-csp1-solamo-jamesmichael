package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Artist;

import java.util.List;

public interface ArtistRepository {

    List<Artist> findAllActive();
    List<Artist> findAllArchived();
    boolean save(Artist artist);
    boolean update(Artist artist);
    boolean deleteById(int id);
    boolean archiveById(int id);
    boolean restoreById(int id);
    List<Artist> searchByName(String keyword);

}
