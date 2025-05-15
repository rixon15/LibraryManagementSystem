package FinalProject01.item;

import java.util.UUID;

public abstract class LibraryItem implements Borrowable {
    private final String itemId = UUID.randomUUID().toString();
    private String title;
    private boolean available;

    public LibraryItem(String title, boolean available) {
        this.title = title;
        this.available = available;
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
