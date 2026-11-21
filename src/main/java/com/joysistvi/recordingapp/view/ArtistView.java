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

    public void displayAllArtists() {

        List<Artist> artists = artistService.getAllActiveArtists();

        System.out.println("\n----- Artists -----");
        System.out.printf("%-4s %-20s%n", "ID", "Artist Name");

        if (artists == null || artists.isEmpty()) {
            System.out.println("No active artists found.");
            return;
        }

        for (Artist artist : artists) {
            System.out.printf("%-4s %-20s%n", artist.getId(), artist.getName());
        }
    }

    public void promtCreateArtist() {

        System.out.println("\n----- Create New Artist ----");
        String name = getStringPrompt("Enter artist name: ");
        boolean created = artistService.createArtist(name);

        if (created) {
            System.out.println("Artist" + name.trim() + "created successfully.");
        } else {
            System.out.println("Artist" + name.trim() + "failed to create.");
        }
    }

    public void promptUpdateArtist() {

        System.out.println("\n----- Update Artist -----");

        displayAllArtists();

        int id = getIntPrompt("Enter artist ID to update: ");
        String name = getStringPrompt("Enter new artist name: ");
        boolean updated = artistService.updateArtist(id, name);

        if (updated) {
            System.out.println("Artist updated successfully.");
        } else {
            System.out.println("Failed to update artist.");
        }
    }

    public void promptDeleteArtist() {

        System.out.println("\n----- Delete Artist -----");

        displayAllArtists();

        int id = getIntPrompt("Enter artist ID to delete: ");
        boolean deleted = artistService.deleteArtist(id);

        if (deleted) {
            System.out.println("Artist deleted successfully.");
        } else {
            System.out.println("Failed to delete artist.");
        }
    }

    public void promptArchiveArtist() {

        System.out.println("\n----- Archive Artist -----");

        displayAllArtists();

        int id = getIntPrompt("Enter artist ID to archive: ");
        boolean archived = artistService.archiveArtist(id);

        if (archived) {
            System.out.println("Artist archived successfully.");
        } else {
            System.out.println("Failed to archive artist.");
        }
    }

    public void promptRestoreArtist() {

        System.out.println("\n----- Restore Artist -----");
        List<Artist> artists = artistService.getAllArchivedArtists();
        System.out.printf("%-5s %-25s%n", "ID", "Artist Name");

        if (artists == null || artists.isEmpty()) {
            System.out.println("No archived artists found.");
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
    }

    public void promptSearchArtists() {

        System.out.println("\n----- Search Artists -----");
        String keyword = getStringPrompt("Enter artist name or keyword: ");
        List<Artist> artists = artistService.searchArtists(keyword);
        System.out.printf("%-5s %-25s%n", "ID", "Artist Name");

        if (artists == null || artists.isEmpty()) {
            System.out.println("No matching artists found.");
            return;
        }

        for (Artist artist : artists) {
            System.out.printf("%-5d %-25s%n",
                    artist.getId(),
                    artist.getName());
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
}
