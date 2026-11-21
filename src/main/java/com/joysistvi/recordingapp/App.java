package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.repository.*;
import com.joysistvi.recordingapp.service.*;
import com.joysistvi.recordingapp.view.AlbumView;
import com.joysistvi.recordingapp.view.ArtistView;
import com.joysistvi.recordingapp.view.UserView;

import java.util.Scanner;


// Entry Point
public class App {

    public static void main(String[] args) {

        DbConnection dbConnection = new DbConnection();
        Scanner scanner = new Scanner(System.in);

        ArtistRepository artistRepository = new ArtistRepositoryImpl(dbConnection);
        ArtistService artistService = new ArtistServiceImpl(artistRepository);
        ArtistView artistView = new ArtistView(artistService, scanner);

        AlbumRepository albumRepository = new AlbumRepositoryImpl(dbConnection);
        AlbumService albumService = new AlbumServiceImpl(albumRepository);
        AlbumView albumView = new AlbumView(albumService, scanner);

        UserRepository userRepository = new UserRepositoryImpl(dbConnection);
        UserService userService = new UserServiceImpl(userRepository);
        UserView userView = new UserView(userService, scanner);


        artistView.displayAllArtists();
        artistView.promptCreateArtist();
        artistView.promptUpdateArtist();
        artistView.promptDeleteArtist();
        artistView.promptArchiveArtist();
        artistView.promptRestoreArtist();
        artistView.promptSearchArtists();

        albumView.displayAllAlbums();
        albumView.promptCreateAlbum();
        albumView.promptUpdateAlbum();
        albumView.promptDeleteAlbum();
        albumView.promptArchiveAlbum();
        albumView.promptRestoreAlbum();
        albumView.promptSearchAlbums();

        userView.promptCreateUser();
        userView.displayAllUsers();
        userView.promptUpdateUser();
        userView.promptSearchUsers();
        userView.promptArchiveUser();
        userView.promptRestoreUser();
        userView.promptDeleteUser();
        userView.promptLoginUser();


    }
}