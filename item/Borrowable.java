package item;

public interface Borrowable {
    boolean isAvailable();
    void markAsBorrowed();
    void markAsAvailable();
}
