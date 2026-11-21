package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Album;
import com.joysistvi.recordingapp.service.AlbumService;
import java.util.List;
import java.util.Scanner;

public class AlbumView {

    private final AlbumService albumService;
    private final Scanner scanner;

    public AlbumView(AlbumService albumService, Scanner scanner) {
        this.albumService = albumService;
        this.scanner = scanner;
    }

    public void displayAllAlbums() {

        List<Album> albums = albumService.getAllActiveAlbums();

        System.out.println("\n----- Active Albums -----");
        System.out.printf("%-5s %-25s %-8s %-12s%n", "ID", "Album Name", "Year", "Artist");

        if (albums == null || albums.isEmpty()) {
            System.out.println("No active albums found.");
            return;
        }

        for (Album album : albums) {
            System.out.printf("%-5d %-25s %-8d %-12s%n",
                    album.getId(),
                    album.getName(),
                    album.getYear(),
                    album.getArtistName());
        }
    }

    public void promptCreateAlbum() {

        System.out.println("\n----- Create New Album -----");
        String name = getStringPrompt("Enter album name: ");
        int year = getIntPrompt("Enter release year: ");
        int artistId = getIntPrompt("Enter artist ID: ");
        boolean created = albumService.createAlbum(name, year, artistId);

        if (created) {
            System.out.println("Album created successfully.");
        } else {
            System.out.println("Failed to create album.");
        }
    }

    public void promptUpdateAlbum() {

        System.out.println("\n----- Update Album -----");
        displayAllAlbums();
        int id = getIntPrompt("\nEnter album ID to update: ");
        String name = getStringPrompt("Enter new album name: ");
        int year = getIntPrompt("Enter new release year: ");
        int artistId = getIntPrompt("Enter new artist ID: ");
        boolean updated = albumService.updateAlbum(id, name, year, artistId);

        if (updated) {
            System.out.println("Album updated successfully.");
        } else {
            System.out.println("Failed to update album.");
        }
    }

    public void promptDeleteAlbum() {

        System.out.println("\n----- Delete Album -----");
        displayAllAlbums();
        int id = getIntPrompt("\nEnter album ID to delete: ");
        boolean deleted = albumService.deleteAlbum(id);

        if (deleted) {
            System.out.println("Album deleted successfully.");
        } else {
            System.out.println("Failed to delete album.");
        }
    }

    public void promptArchiveAlbum() {

        System.out.println("\n----- Archive Album -----");
        displayAllAlbums();
        int id = getIntPrompt("\nEnter album ID to archive: ");
        boolean archived = albumService.archiveAlbum(id);

        if (archived) {
            System.out.println("Album archived successfully.");
        } else {
            System.out.println("Failed to archive album.");
        }
    }

    public void promptRestoreAlbum() {

        System.out.println("\n----- Restore Album -----");
        List<Album> albums = albumService.getAllArchivedAlbums();
        System.out.printf("%-5s %-25s %-8s %-12s%n", "ID", "Album Name", "Year", "Artist");

        if (albums == null || albums.isEmpty()) {
            System.out.println("No archived albums found.");
            return;
        }

        for (Album album : albums) {
            System.out.printf("%-5d %-25s %-8d %-12s%n",
                    album.getId(),
                    album.getName(),
                    album.getYear(),
                    album.getArtistName());
        }

        int id = getIntPrompt("\nEnter album ID to restore: ");
        boolean restored = albumService.restoreAlbum(id);

        if (restored) {
            System.out.println("Album restored successfully.");
        } else {
            System.out.println("Failed to restore album.");
        }
    }

    public void promptSearchAlbums() {

        System.out.println("\n----- Search Albums -----");
        String keyword = getStringPrompt("Enter album name or keyword: ");
        List<Album> albums = albumService.searchAlbums(keyword);
        System.out.printf("%-5s %-25s %-8s %-12s%n", "ID", "Album Name", "Year", "Artist");
        if (albums == null || albums.isEmpty()) {
            System.out.println("No matching albums found.");
            return;
        }

        for (Album album : albums) {
            System.out.printf("%-5d %-25s %-8d %-12s%n",
                    album.getId(),
                    album.getName(),
                    album.getYear(),
                    album.getArtistName());
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