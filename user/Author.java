package user;

import exceptions.AuthorException;
import exceptions.ItemException;
import item.Book;
import item.LibraryItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Author {
    private final String authorId = UUID.randomUUID().toString();
    private final String firstName, lastName;
    private final List<LibraryItem> books = new LinkedList<>();

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public List<LibraryItem> getAllBooks() {
        return new ArrayList<>(books);
    }

    public void addBook(LibraryItem book) {
        if(book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if(books.contains(book)) {
            throw new ItemException(book.getItemId(),  "Book cannot be duplicated", "Book already exists in the list");
        }
        if(book instanceof Book) {
            if(((Book) book).getAuthor() != this) {
                throw new AuthorException(authorId, "The book is written by a different author");
            }
        }

        books.add(book);
    }

    public void removeBook(LibraryItem book) {
        if(book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if(!books.contains(book)) {
           throw new ItemException(book.getItemId(), "Book doesn't exists", "Cannot delete a book that's not part of the list");
        }
        books.remove(book);
    }


}
