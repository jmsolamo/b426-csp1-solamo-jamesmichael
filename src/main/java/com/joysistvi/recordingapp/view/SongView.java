package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.service.SongService;
import java.util.List;
import java.util.Scanner;

public class SongView {

    private final SongService songService;
    private final Scanner scanner;

    public SongView(SongService songService, Scanner scanner) {
        this.songService = songService;
        this.scanner = scanner;
    }

    public void handleSongMenu() {

        while (true) {
            System.out.println("\n----- Manage Songs -----");
            System.out.println("1. View all songs");
            System.out.println("2. Create new song");
            System.out.println("3. Update song");
            System.out.println("4. Search song");
            System.out.println("5. Archive song");
            System.out.println("6. Restore song");
            System.out.println("7. Delete song");
            System.out.println("0. Back");

            int choice = getIntPrompt("Choose an option: ");
            switch (choice) {
                case 1 -> displayAllSongs();
                case 2 -> promptCreateSong();
                case 3 -> promptUpdateSong();
                case 4 -> promptSearchSongs();
                case 5 -> promptArchiveSong();
                case 6 -> promptRestoreSong();
                case 7 -> promptDeleteSong();
                case 0 -> {return;}
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void displayAllSongs() {

        List<Song> songs = songService.getAllActiveSongs();

        System.out.println("\n----- Active Songs -----");
        System.out.printf("%-5s %-25s %-15s %-10s %-20s%n", "ID", "Title", "Genre", "Length", "Album");

        if (songs == null || songs.isEmpty()) {
            System.out.println("No active songs found.");
            return;
        }

        for (Song song : songs) {
            System.out.printf("%-5d %-25s %-15s %-10s %-20s%n",
                    song.getId(),
                    song.getTitle(),
                    song.getGenre(),
                    song.getLength(),
                    song.getAlbumName());
        }
    }

    public void promptCreateSong() {

        System.out.println("\n----- Create New Song -----");
        String title = getStringPrompt("Enter song title: ");
        String genre = getStringPrompt("Enter song genre: ");
        String length = getStringPrompt("Enter song length (e.g. 3:45): ");
        int albumId = getIntPrompt("Enter album ID: ");
        boolean created = songService.createSong(title, genre, length, albumId);

        if (created) {
            System.out.println("Song created successfully.");
        } else {
            System.out.println("Failed to create song.");
        }
    }

    public void promptUpdateSong() {

        System.out.println("\n----- Update Song -----");
        displayAllSongs();
        int id = getIntPrompt("\nEnter song ID to update: ");
        String title = getStringPrompt("Enter new song title: ");
        String genre = getStringPrompt("Enter new song genre: ");
        String length = getStringPrompt("Enter new song length (e.g. 3:45): ");
        int albumId = getIntPrompt("Enter new album ID: ");
        boolean updated = songService.updateSong(id, title, genre, length, albumId);

        if (updated) {
            System.out.println("Song updated successfully.");
        } else {
            System.out.println("Failed to update song.");
        }
    }

    public void promptDeleteSong() {

        System.out.println("\n----- Delete Song -----");
        displayAllSongs();
        int id = getIntPrompt("\nEnter song ID to delete: ");
        boolean deleted = songService.deleteSong(id);

        if (deleted) {
            System.out.println("Song deleted successfully.");
        } else {
            System.out.println("Failed to delete song.");
        }
    }

    public void promptArchiveSong() {

        System.out.println("\n----- Archive Song -----");
        displayAllSongs();
        int id = getIntPrompt("Enter song ID to archive: ");
        boolean archived = songService.archiveSong(id);

        if (archived) {
            System.out.println("Song archived successfully.");
        } else {
            System.out.println("Failed to archive song.");
        }
    }

    public void promptRestoreSong() {

        System.out.println("\n----- Restore Song -----");
        List<Song> songs = songService.getAllArchivedSongs();
        System.out.printf("%-5s %-25s %-15s %-10s %-20s%n", "ID", "Title", "Genre", "Length", "Album");

        if (songs == null || songs.isEmpty()) {
            System.out.println("No archived songs found.");
            return;
        }

        for (Song song : songs) {
            System.out.printf("%-5d %-25s %-15s %-10s %-20s%n",
                    song.getId(),
                    song.getTitle(),
                    song.getGenre(),
                    song.getLength(),
                    song.getAlbumName());
        }

        int id = getIntPrompt("Enter song ID to restore: ");
        boolean restored = songService.restoreSong(id);

        if (restored) {
            System.out.println("Song restored successfully.");
        } else {
            System.out.println("Failed to restore song.");
        }
    }

    public void promptSearchSongs() {

        System.out.println("\n----- Search Songs -----");
        String keyword = getStringPrompt("Enter song title or keyword: ");
        List<Song> songs = songService.searchSongs(keyword);
        System.out.printf("%-5s %-25s %-15s %-10s %-20s%n", "ID", "Title", "Genre", "Length", "Album");

        if (songs == null || songs.isEmpty()) {
            System.out.println("No matching songs found.");
            return;
        }

        for (Song song : songs) {
            System.out.printf("%-5d %-25s %-15s %-10s %-20s%n",
                    song.getId(),
                    song.getTitle(),
                    song.getGenre(),
                    song.getLength(),
                    song.getAlbumName());
        }
    }

    private String getStringPrompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private int getIntPrompt(String message) {

        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
