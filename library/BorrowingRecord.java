package library;

import java.time.LocalDate;
import java.util.UUID;

public class BorrowingRecord {
    private final String memberId;
    private final String itemId;
    private final LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private final String recordId = UUID.randomUUID().toString();

    public BorrowingRecord(String memberId, String itemId, LocalDate borrowDate, LocalDate dueDate) {
        this.memberId = memberId;
        this.itemId = itemId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getItemId() {
        return itemId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
       this.returnDate = returnDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public String getRecordId() {
        return recordId;
    }
}
