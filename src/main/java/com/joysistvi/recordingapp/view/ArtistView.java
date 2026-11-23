package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Artist;
import com.joysistvi.recordingapp.service.ArtistService;

import java.util.List;
import java.util.Scanner;

public class ArtistView {

    private final ArtistService artistService;
    private final Scanner scanner;

    public ArtistView(ArtistService artistService,  Scanner scanner) {
        this.artistService = artistService;
        this.scanner = scanner;
    }

    public void handleMenu() {

        while (true) {
            clearScreen();
            System.out.println("\n----- Manage Artists -----");
            System.out.println("1. View All Artists");
            System.out.println("2. Create Artist");
            System.out.println("3. Update Artist");
            System.out.println("4. Search Artist");
            System.out.println("5. Archive Artist");
            System.out.println("6. Restore Artist");
            System.out.println("7. Delete Artist");
            System.out.println("0. Back");

            int choice = getIntPrompt("Choose an option: ");
            switch (choice) {
                case 1 -> displayAllArtists();
                case 2 -> promptCreateArtist();
                case 3 -> promptUpdateArtist();
                case 4 -> promptSearchArtists();
                case 5 -> promptArchiveArtist();
                case 6 -> promptRestoreArtist();
                case 7 -> promptDeleteArtist();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void displayAllArtists() {

        List<Artist> artists = artistService.getAllActiveArtists();

        System.out.println("\n----- Artists -----");
        System.out.printf("%-4s %-20s%n", "ID", "Artist Name");

        if (artists == null || artists.isEmpty()) {
            System.out.println("No active artists found.");
            pressEnterToContinue();
            return;
        }

        for (Artist artist : artists) {
            System.out.printf("%-4s %-20s%n", artist.getId(), artist.getName());
        }
        pressEnterToContinue();
    }

    public void promptCreateArtist() {

        System.out.println("\n----- Create New Artist ----");
        String name = getStringPrompt("Enter artist name: ");
        boolean created = artistService.createArtist(name);

        if (created) {
            System.out.println("Artist " + name.trim() + " created successfully.");
        } else {
            System.out.println("Artist " + name.trim() + " failed to create.");
        }
        pressEnterToContinue();
    }

    public void promptUpdateArtist() {

        System.out.println("\n----- Update Artist -----");

        displayAllArtistsNoWait();

        int id = getIntPrompt("Enter artist ID to update: ");
        String name = getStringPrompt("Enter new artist name: ");
        boolean updated = artistService.updateArtist(id, name);

        if (updated) {
            System.out.println("Artist updated successfully.");
        } else {
            System.out.println("Failed to update artist.");
        }
        pressEnterToContinue();
    }

    public void promptDeleteArtist() {

        System.out.println("\n----- Delete Artist -----");

        displayAllArtistsNoWait();

        int id = getIntPrompt("Enter artist ID to delete: ");
        boolean deleted = artistService.deleteArtist(id);

        if (deleted) {
            System.out.println("Artist deleted successfully.");
        } else {
            System.out.println("Failed to delete artist.");
        }
        pressEnterToContinue();
    }

    public void promptArchiveArtist() {

        System.out.println("\n----- Archive Artist -----");

        displayAllArtistsNoWait();

        int id = getIntPrompt("Enter artist ID to archive: ");
        boolean archived = artistService.archiveArtist(id);

        if (archived) {
            System.out.println("Artist archived successfully.");
        } else {
            System.out.println("Failed to archive artist.");
        }
        pressEnterToContinue();
    }

    public void promptRestoreArtist() {

        System.out.println("\n----- Restore Artist -----");
        List<Artist> artists = artistService.getAllArchivedArtists();
        System.out.printf("%-5s %-25s%n", "ID", "Artist Name");

        if (artists == null || artists.isEmpty()) {
            System.out.println("No archived artists found.");
            pressEnterToContinue();
            return;
        }

        for (Artist artist : artists) {
            System.out.printf("%-5d %-25s%n",
                    artist.getId(),
                    artist.getName());
        }

        int id = getIntPrompt("Enter artist ID to restore: ");
        boolean restored = artistService.restoreArtist(id);

        if (restored) {
            System.out.println("Artist restored successfully.");
        } else {
            System.out.println("Failed to restore artist.");
        }
        pressEnterToContinue();
    }

    public void promptSearchArtists() {

        System.out.println("\n----- Search Artists -----");
        String keyword = getStringPrompt("Enter artist name or keyword: ");
        List<Artist> artists = artistService.searchArtists(keyword);
        System.out.printf("%-5s %-25s%n", "ID", "Artist Name");

        if (artists == null || artists.isEmpty()) {
            System.out.println("No matching artists found.");
            pressEnterToContinue();
            return;
        }

        for (Artist artist : artists) {
            System.out.printf("%-5d %-25s%n",
                    artist.getId(),
                    artist.getName());
        }
        pressEnterToContinue();
    }

    private void displayAllArtistsNoWait() {
        List<Artist> artists = artistService.getAllActiveArtists();
        System.out.printf("%-4s %-20s%n", "ID", "Artist Name");
        if (artists == null || artists.isEmpty()) {
            System.out.println("No active artists found.");
            return;
        }
        for (Artist artist : artists) {
            System.out.printf("%-4s %-20s%n", artist.getId(), artist.getName());
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
            } catch (Exception e) {
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
