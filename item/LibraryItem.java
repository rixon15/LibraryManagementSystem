package item;

import java.util.UUID;

public abstract class LibraryItem implements Borrowable {
    private final String itemId = UUID.randomUUID().toString();
    private String title;
    private boolean available = true;

    public LibraryItem(String title) {
        this.title = title;
    }

    public String getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public abstract void displayDetails();


    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void markAsBorrowed() {
        this.available = false;
    }

    @Override
    public void markAsAvailable() {

        this.available = true;

    }
}
