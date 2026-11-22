package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.repository.*;
import com.joysistvi.recordingapp.service.*;
import com.joysistvi.recordingapp.view.AlbumView;
import com.joysistvi.recordingapp.view.ArtistView;
import com.joysistvi.recordingapp.view.PlaylistView;
import com.joysistvi.recordingapp.view.SongView;
import com.joysistvi.recordingapp.view.UserView;
import java.util.Scanner;

// Entry Point
public class App {

    DbConnection dbConnection = new DbConnection();
    Scanner scanner = new Scanner(System.in);

    ArtistRepository artistRepository = new ArtistRepositoryImpl(dbConnection);
    ArtistService artistService = new ArtistServiceImpl(artistRepository);
    ArtistView artistView = new ArtistView(artistService, scanner);

    AlbumRepository albumRepository = new AlbumRepositoryImpl(dbConnection);
    AlbumService albumService = new AlbumServiceImpl(albumRepository);
    AlbumView albumView = new AlbumView(albumService, scanner);

    SongRepository songRepository = new SongRepositoryImpl(dbConnection);
    SongService songService = new SongServiceImpl(songRepository);
    SongView songView = new SongView(songService, scanner);

    PlaylistRepository playlistRepository = new PlaylistRepositoryImpl(dbConnection);
    PlaylistService playlistService = new PlaylistServiceImpl(playlistRepository);
    PlaylistView playlistView = new PlaylistView(playlistService, scanner);

    UserRepository userRepository = new UserRepositoryImpl(dbConnection);
    UserService userService = new UserServiceImpl(userRepository);
    UserView userView = new UserView(userService, scanner);

    public static void main(String[] args) {

        App app = new App();
        app.run();
    }

    public void run() {

        while (true) {
            User currentUser = showAuthMenuAndAuthenticate();
            if (currentUser == null) {
                System.out.println("\nExiting Recording Studio App. Good Bye!");
                break;
            }

            System.out.println("\nWelcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            dashboard();
            System.out.println("\nLogout " + currentUser.getUsername() + ".");
        }
    }

    private void dashboard() {

        while (true) {
            System.out.println("\n----- Dashboard -----");
            System.out.println("1. Song Management");
            System.out.println("2. Artist Management");
            System.out.println("3. Album Management");
            System.out.println("4. Playlist Management");
            System.out.println("5. User Management");
            System.out.println("0. Logout");

            int choice = getIntPrompt("Choose an option: ");

            switch (choice) {
                case 1 -> songView.handleMenu();
                case 2 -> artistView.handleMenu();
                case 3 -> albumView.handleMenu();
                case 4 -> playlistView.handleMenu();
                case 5 -> userView.handleMenu();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private User showAuthMenuAndAuthenticate() {

        while (true) {
            System.out.println("\n----- Recording Studio App -----");
            System.out.println("1. Login");
            System.out.println("2. Register Account");
            System.out.println("0. Exit");

            int choice = getIntPrompt("Choose an option: ");
            switch (choice) {
                case 1:
                    User loggedInUser = userView.promptLoginUser();

                    if (loggedInUser != null) {
                        return loggedInUser;
                    }
                    break;

                case 2:
                    userView.promptCreateUser();
                    break;

                case 0:
                    return null;

                default:
            }

        }
    }

    private int getIntPrompt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
