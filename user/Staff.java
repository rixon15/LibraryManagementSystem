package FinalProject01.user;

import FinalProject01.item.Author;
import FinalProject01.item.LibraryItem;
import FinalProject01.library.Library;

import java.util.UUID;

public class Staff extends User {
    private final String employeeId = UUID.randomUUID().toString();
    private final StaffRole role;
    private final Library library;

    public Staff( String name, String email, StaffRole role, Library library) {
        super( name, email);
        this.role = role;
        this.library = library;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public StaffRole getRole() {
        return role;
    }

    public void addAuthor(Author author) {
        if(author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        library.addAuthor(author);
    }

    public void removeAuthor(Author author) {
        if(author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }

        library.removeAuthor(author.getAuthorId());
    }

    public void addItem(LibraryItem item) {
        if(item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        library.addItem(item);
    }

    public void removeItem(LibraryItem item) {
        if(item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        library.removeItem(item.getItemId());
    }

    public void updateItem(LibraryItem item) {
        if(item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        library.updateItem(item);
    }

    @Override
    public void displayUserInfo() {
        System.out.println("Staff User Id: " + getUserId());
        System.out.println("Employee Id: " + getEmployeeId());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Role: " + getRole());
    }
}
