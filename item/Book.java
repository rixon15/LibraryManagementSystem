package item;

import user.Author;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Book extends LibraryItem {
    private final Author author;
    private final List<BookGenres> genres;
    private String description;

    public Book( String title, Author author, List<BookGenres> genres, String description) {
        super(title);
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        this.author = author;
        author.addBook(this);
        this.genres = genres;
        this.description = description;
    }

    public Author getAuthor() {
        return author;
    }

    public List<BookGenres> getGenres() {
        return new ArrayList<>(genres);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description == null) {
            throw new InvalidParameterException("Invalid description");
        }
        this.description = description;
    }

    @Override
    public void displayDetails() {
        System.out.println("Book Id: " + getItemId());
        System.out.println("Title: " + getTitle());
        System.out.println("Author: " + getAuthor());
        System.out.println("Genres: " + getGenres());
        System.out.println("Description: " + getDescription());
    }
}
