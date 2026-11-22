package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.service.PlaylistService;
import java.util.List;
import java.util.Scanner;

public class PlaylistView {

    private final PlaylistService playlistService;
    private final Scanner scanner;

    public PlaylistView(PlaylistService playlistService, Scanner scanner) {
        this.playlistService = playlistService;
        this.scanner = scanner;
    }

    public void handleMenu() {

        while (true) {
            System.out.println("\n----- Manage Playlists -----");
            System.out.println("1. View All Playlists");
            System.out.println("2. Create Playlist");
            System.out.println("3. Update Playlist");
            System.out.println("4. Search Playlist");
            System.out.println("5. Archive Playlist");
            System.out.println("6. Restore Playlist");
            System.out.println("7. Delete Playlist");
            System.out.println("0. Back");

            int choice = getIntPrompt("Choose an option: ");
            switch (choice) {
                case 1 -> displayAllActive();
                case 2 -> promptCreate();
                case 3 -> promptUpdate();
                case 4 -> promptSearch();
                case 5 -> promptArchive();
                case 6 -> promptRestore();
                case 7 -> promptDelete();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void displayAllActive() {

        List<Playlist> playlists = playlistService.getAllActivePlaylists();

        System.out.println("\n----- Active Playlists -----");
        System.out.printf("%-5s %-25s %-20s %-15s%n", "ID", "Playlist Name", "Date Created", "Owner");

        if (playlists == null || playlists.isEmpty()) {
            System.out.println("No active playlists found.");
            return;
        }

        for (Playlist playlist : playlists) {
            System.out.printf("%-5d %-25s %-20s %-15s%n",
                    playlist.getId(),
                    playlist.getName(),
                    playlist.getDateCreated(),
                    playlist.getUsername());
        }
    }

    public void promptCreate() {

        System.out.println("\n----- Create New Playlist -----");
        String name = getStringPrompt("Enter playlist name: ");
        int userId = getIntPrompt("Enter user ID (owner): ");
        boolean created = playlistService.createPlaylist(name, userId);

        if (created) {
            System.out.println("Playlist created successfully.");
        } else {
            System.out.println("Failed to create playlist.");
        }
    }

    public void promptUpdate() {

        System.out.println("\n----- Update Playlist -----");
        displayAllActive();
        int id = getIntPrompt("\nEnter playlist ID to update: ");
        String name = getStringPrompt("Enter new playlist name: ");
        int userId = getIntPrompt("Enter new user ID (owner): ");
        boolean updated = playlistService.updatePlaylist(id, name, userId);

        if (updated) {
            System.out.println("Playlist updated successfully.");
        } else {
            System.out.println("Failed to update playlist.");
        }
    }

    public void promptDelete() {

        System.out.println("\n----- Delete Playlist -----");
        displayAllActive();
        int id = getIntPrompt("\nEnter playlist ID to delete: ");
        boolean deleted = playlistService.deletePlaylist(id);

        if (deleted) {
            System.out.println("Playlist deleted successfully.");
        } else {
            System.out.println("Failed to delete playlist.");
        }
    }

    public void promptArchive() {

        System.out.println("\n----- Archive Playlist -----");
        displayAllActive();
        int id = getIntPrompt("Enter playlist ID to archive: ");
        boolean archived = playlistService.archivePlaylist(id);

        if (archived) {
            System.out.println("Playlist archived successfully.");
        } else {
            System.out.println("Failed to archive playlist.");
        }
    }

    public void promptRestore() {

        System.out.println("\n----- Restore Playlist -----");
        List<Playlist> playlists = playlistService.getAllArchivedPlaylists();
        System.out.printf("%-5s %-25s %-20s %-15s%n", "ID", "Playlist Name", "Date Created", "Owner");

        if (playlists == null || playlists.isEmpty()) {
            System.out.println("No archived playlists found.");
            return;
        }

        for (Playlist playlist : playlists) {
            System.out.printf("%-5d %-25s %-20s %-15s%n",
                    playlist.getId(),
                    playlist.getName(),
                    playlist.getDateCreated(),
                    playlist.getUsername());
        }

        int id = getIntPrompt("Enter playlist ID to restore: ");
        boolean restored = playlistService.restorePlaylist(id);

        if (restored) {
            System.out.println("Playlist restored successfully.");
        } else {
            System.out.println("Failed to restore playlist.");
        }
    }

    public void promptSearch() {

        System.out.println("\n----- Search Playlists -----");
        String keyword = getStringPrompt("Enter playlist name or keyword: ");
        List<Playlist> playlists = playlistService.searchPlaylists(keyword);
        System.out.printf("%-5s %-25s %-20s %-15s%n", "ID", "Playlist Name", "Date Created", "Owner");

        if (playlists == null || playlists.isEmpty()) {
            System.out.println("No matching playlists found.");
            return;
        }

        for (Playlist playlist : playlists) {
            System.out.printf("%-5d %-25s %-20s %-15s%n",
                    playlist.getId(),
                    playlist.getName(),
                    playlist.getDateCreated(),
                    playlist.getUsername());
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
