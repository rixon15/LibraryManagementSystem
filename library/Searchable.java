package library;

import item.LibraryItem;

import java.util.List;

public interface Searchable {
    List<LibraryItem> searchByTitle(String title);
    List<LibraryItem> searchByAuthor(String authorName);
    List<LibraryItem> searchByGenre(String genreName);
}
