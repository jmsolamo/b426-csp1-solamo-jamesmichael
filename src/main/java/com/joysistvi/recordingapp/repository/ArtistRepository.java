package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Artist;

import java.util.List;

// Repository (DATA tier) -> responsible for data persistence and data access logic
public interface ArtistRepository {

    public List<Artist> findAllActive();
}
