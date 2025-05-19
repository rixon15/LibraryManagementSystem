package tests.models;

import exceptions.AuthorException;
import exceptions.ItemException;
import user.Author;
import item.Book;
import item.BookGenres;
import item.LibraryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    private Author author;
    private LibraryItem book1;
    private LibraryItem book2;
    private Book bookByMichaelDoe;

    @BeforeEach
    void setUp() {
        author = new Author("Michael", "Doe");
        book1 = new Book("TestBook1", new Author("Book1", "TestAuthor1"), new ArrayList<>(List.of(BookGenres.FANTASY, BookGenres.ROMANCE)), "Book1 for testing");
        book2 = new Book("TestBook2", new Author("Book2", "TestAuthor2"), new ArrayList<>(List.of(BookGenres.CHILDREN, BookGenres.HORROR)), "Book2 for testing");
        bookByMichaelDoe = new Book("MyBook", author, new ArrayList<>(List.of(BookGenres.SCIENCE_FICTION)), "A book by Michael Doe");
    }

    @Test
    @DisplayName("Author ID should be a valid UUID string")
    void getAuthorId() {
        String authorId = author.getAuthorId();

        assertNotNull(authorId, "Author ID should not be null");
        assertDoesNotThrow(() -> UUID.fromString(authorId));
    }

    @Test
    @DisplayName("getFullName should return first name and last name concatenated")
    void getFullName() {
        String fullName = author.getFullName();

        assertEquals("Michael Doe", fullName, "Full name should be correctly concatenated");
    }

    @Test
    @DisplayName("getAllBooks should reflect books added during author/book initialization")
    void getAllBooks_afterSetUp_containsInitialBook() {
        List<LibraryItem> books = author.getAllBooks();
        assertNotNull(books, "Book list should not be null");
        assertEquals(1, books.size(), "Book list should contain one book after setup");
        assertTrue(books.contains(bookByMichaelDoe), "Book list should contain the book added during setup");
    }


    @Test
    @DisplayName("GetAllBooks should return all added books")
    void getAllBooks_returnsAddedBooks() {
        Book anotherBookByMichael = new Book("Another Book", author, new ArrayList<>(List.of(BookGenres.MYSTERY)), "Another book by Michael Doe");

        assertThrows(AuthorException.class, () -> author.addBook(book1), "Should throw AuthorException when adding a book by a different author");

        List<LibraryItem> books = author.getAllBooks();

        assertEquals(2,books.size(), "Should have two books in the list");
        assertTrue(books.contains(bookByMichaelDoe), "List should contain the book written by Michael Doe");
        assertTrue(books.contains(anotherBookByMichael), "List should contain book1 (added explicitly)");
        assertFalse(books.contains(book2), "List should not contain book2 as it was not added to Michael Doe's list");
    }

    @Test
    @DisplayName("getAllBook should return a copy of the internal list not a reference")
    void getAllBooks_returnsCopy() {

        List<LibraryItem> bookList1 = author.getAllBooks();

        assertNotNull(bookList1, "First retrieved list should not be null");
        assertEquals(1, bookList1.size(), "First list should contain one book");

        try {
            bookList1.add(book2);
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }


        List<LibraryItem> bookList2 = author.getAllBooks();
        assertNotNull(bookList2, "Second retrieved list should not be nul");
        assertEquals(1, bookList2.size(), "Internal list should still contain only one book (book1)");
        assertTrue(bookList2.contains(bookByMichaelDoe), "Internal list should still contain the original book only");

        assertNotSame(bookList1, bookList2, "getAllBooks should return a new List instance each time");
    }

    @Test
    @DisplayName("addBook should add a new book successfully")
    void addBook_success_forOwnBook() {
        int initialBookCount = author.getAllBooks().size();
        assertEquals(1, initialBookCount, "Author should have one book from setup");

        Book newBookByAuthor = new Book("New Title", author, new ArrayList<>(List.of(BookGenres.THRILLER)), "New book by Michael Doe");

        List<LibraryItem> books = author.getAllBooks();
        assertEquals(initialBookCount + 1, books.size(), "Book count should increase after adding a new book by the same author");
        assertTrue(books.contains(bookByMichaelDoe), "List should still contain the book from setUp.");
        assertTrue(books.contains(newBookByAuthor), "List should contain the newly added book by the author.");
    }

    @Test
    @DisplayName("addBook should throw AuthorException when adding a book by a different author")
    void addBook_differentAuthor_throwsAuthorException() {
        assertThrows(AuthorException.class, () -> author.addBook(book1), "Adding a book by a different author should throw AuthorException");

        assertEquals(1, author.getAllBooks().size(), "Book list size should remain unchanged");
        assertFalse(author.getAllBooks().contains(book1), "Book list should not contain book1");
    }

    @Test
    @DisplayName("addBook should throw IllegalArgumentException for null book")
    void addBook_nullBook_throwsIllegalArgumentsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> author.addBook(null));
        assertEquals("Book cannot be null", exception.getMessage(), "Exception message mismatch for null book. ");
    }

    @Test
    @DisplayName("addBook should throw ItemException when adding a duplicated book")
    void addBook_duplicatedBook_throwsItemException() {
        ItemException exception = assertThrows(ItemException.class, () -> author.addBook(bookByMichaelDoe));

        String expectedBaseMessage = "Book already exists in the list";
        String expectedReason = "Book cannot be duplicated";

        assertTrue(exception.getMessage().startsWith(expectedBaseMessage), "Exception message should start with: " + expectedBaseMessage);
        assertTrue(exception.getMessage().contains("(Item ID: " + bookByMichaelDoe.getItemId() + ")"), "Exception message should contain the Item ID.");
        assertTrue(exception.getMessage().contains("Reason: " + expectedReason), "Exception message should contain the reason.");
        assertEquals(bookByMichaelDoe.getItemId(), exception.getItemId(), "ItemException should carry the correct itemId.");
        assertEquals(expectedReason, exception.getReason(), "ItemException should carry the correct reason.");

        assertEquals(1, author.getAllBooks().size(), "List size should be 1 after trying to add a duplicated book");
    }

    @Test
    @DisplayName("removeBook should remove an existing book successfully")
    void removeBook_success() {
        assertTrue(author.getAllBooks().contains(bookByMichaelDoe), "Book list should contain bookByMichaelDoe from the setup");
        int initialSize = author.getAllBooks().size();

        author.removeBook(bookByMichaelDoe);

        List<LibraryItem> books = author.getAllBooks();
        assertTrue(initialSize - 1 >= 0, "The size of the book list cannot be negative");
        assertEquals(initialSize - 1, books.size(), "Book list size should decrease after removing bookByMichaelDoe");
        assertFalse(books.contains(bookByMichaelDoe), "book list should not contain bookByMichaelDoe");
        assertTrue(books.isEmpty(), "Book list should be empty if the only book inside was bookByMichaelDoe");

    }

    @Test
    @DisplayName("removeBook should throw ItemException if book does not exist")
    void removeBook_nonExistingBook_throwsItemException() {
        assertFalse(author.getAllBooks().contains(book1), "book1 should not be in the authors list");

        ItemException exception = assertThrows(ItemException.class, () -> author.removeBook(book1));

        String expectedBaseMessage = "Cannot delete a book that's not part of the list";
        String expectedReason = "Book doesn't exists";

        assertTrue(exception.getMessage().startsWith(expectedBaseMessage), "Exception message should start with: " + expectedBaseMessage);
        assertTrue(exception.getMessage().contains("(Item ID: " + book1.getItemId() + ")"), "Exception message should contain the Item ID.");
        assertTrue(exception.getMessage().contains("Reason: " + expectedReason), "Exception message should contain the reason.");
        assertEquals(book1.getItemId(), exception.getItemId(), "ItemException should carry the correct itemId.");
        assertEquals(expectedReason, exception.getReason(), "ItemException should carry the correct reason.");
    }

    @Test
    @DisplayName("removeBook should throw IllegalArgumentException for null book")
    void removeBook_nullBook_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> author.removeBook(null));
        assertEquals("Book cannot be null", exception.getMessage(), "Exception message mismatch for null book removal");
    }

    @Test
    @DisplayName("getAllBooks should return an empty list for a brand new author instance")
    void getAllBooks_brandNewAuthorInstance_isEmpty() {
        Author brandNewAuthor = new Author("Fresh", "Face");
        List<LibraryItem> books = brandNewAuthor.getAllBooks();
        assertNotNull(books, "Books list for brand new author should not be null");
        assertTrue(books.isEmpty(), "Book list for brand new author should be empty");
    }
}