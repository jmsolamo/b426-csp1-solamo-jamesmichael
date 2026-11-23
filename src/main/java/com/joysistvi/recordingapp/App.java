package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.repository.*;
import com.joysistvi.recordingapp.service.*;
import com.joysistvi.recordingapp.view.*;

import java.util.Scanner;

public class App {

    private final Scanner scanner;
    private final UserView userView;
    private final ArtistView artistView;
    private final AlbumView albumView;
    private final SongView songView;
    private final PlaylistView playlistView;

    public App(Scanner scanner, UserView userView, ArtistView artistView, AlbumView albumView, SongView songView,
            PlaylistView playlistView) {
        this.scanner = scanner;
        this.userView = userView;
        this.artistView = artistView;
        this.albumView = albumView;
        this.songView = songView;
        this.playlistView = playlistView;
    }

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

        SongRepository songRepository = new SongRepositoryImpl(dbConnection);
        SongService songService = new SongServiceImpl(songRepository);
        SongView songView = new SongView(songService, scanner);

        PlaylistRepository playlistRepository = new PlaylistRepositoryImpl(dbConnection);
        PlaylistService playlistService = new PlaylistServiceImpl(playlistRepository);
        PlaylistView playlistView = new PlaylistView(playlistService, songService, scanner);

        App app = new App(scanner, userView, artistView, albumView, songView, playlistView);
        app.run();

        scanner.close();

    }

    public void run() {

        while (true) {
            clearScreen();
            User currentUser = showAuthMenuAndAuthenticate();
            if (currentUser == null) {
                System.out.println("\nExiting Recording Studio App. Goodbye!");
                break;
            }

            System.out.println("\nWelcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")!");

            if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                adminDashboard();
            } else {
                userDashboard(currentUser);
            }
            System.out.println("\nLogged out " + currentUser.getUsername() + ".");
            pressEnterToContinue();
        }
    }

    private void adminDashboard() {
        while (true) {
            clearScreen();
            System.out.println("\n----- Admin Dashboard -----");
            System.out.println("1. Song Management");
            System.out.println("2. Artist Management");
            System.out.println("3. Album Management");
            System.out.println("4. Users Management");
            System.out.println("0. Logout");

            int choice = promptForInt("Choice: ");
            switch (choice) {
                case 1 -> songView.handleMenu();
                case 2 -> artistView.handleMenu();
                case 3 -> albumView.handleMenu();
                case 4 -> userView.handleMenu();
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void userDashboard(User currentUser) {

        while (true) {
            clearScreen();
            System.out.println("\n----- User Dashboard -----");
            System.out.println("1. Browse Artists, Albums, Songs");
            System.out.println("2. Search Songs");
            System.out.println("3. Manage Playlists");
            System.out.println("4. Add / Remove Songs in Playlists");
            System.out.println("0. Logout");

            int choice = promptForInt("Choice: ");
            switch (choice) {
                case 1 -> browseCatalog();
                case 2 -> songView.handleMenu();
                case 3 -> playlistView.handleMenu(currentUser.getId());
                case 4 -> playlistView.handleManageSongsInPlaylist(currentUser.getId());
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void browseCatalog() {
        clearScreen();
        System.out.println("\n----- Browse Catalog -----");
        System.out.println("1. View Artists");
        System.out.println("2. View Albums");
        System.out.println("3. View Songs");
        System.out.println("0. Back");

        int choice = promptForInt("Choice: ");

        switch (choice) {
            case 1 -> artistView.handleMenu();
            case 2 -> albumView.handleMenu();
            case 3 -> songView.handleMenu();
            case 0 -> {
                return;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private User showAuthMenuAndAuthenticate() {

        while (true) {
            clearScreen();
            System.out.println("\n----- Recording Studio App -----");
            System.out.println("1. Login");
            System.out.println("2. Register Account");
            System.out.println("0. Exit");

            int choice = promptForInt("Choose an option: ");
            switch (choice) {
                case 1:
                    User loggedInUser = userView.promptLoginUser();

                    if (loggedInUser != null) {
                        return loggedInUser;
                    }
                    pressEnterToContinue();
                    break;

                case 2:
                    userView.promptCreateUser();
                    break;
                case 0:
                    return null;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private int promptForInt(String promptMessage) {
        while (true) {
            System.out.print(promptMessage);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
