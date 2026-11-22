package com.joysistvi.recordingapp.model;

public class Playlist {

    private int id;
    private String name;
    private String dateCreated;
    private int userId;
    private String username;
    private boolean archived;

    // Constructor for creating a new playlist
    public Playlist(String name, int userId) {
        this.name = name;
        this.userId = userId;
    }

    // Constructor for retrieving a playlist without join
    public Playlist(int id, String name, int userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    // Constructor for retrieving a playlist with joined user information
    public Playlist(int id, String name, String dateCreated, int userId, String username, boolean archived) {
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.userId = userId;
        this.username = username;
        this.archived = archived;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
