package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Artist;

import java.util.List;

// Repository (DATA tier) -> responsible for data persistence and data access logic
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
