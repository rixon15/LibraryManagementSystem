package exceptions;

public class BorrowingRecordException extends RuntimeException {
    private String BorrowingRecordId;
    private String reason;

    public BorrowingRecordException() {
        super("The requested item is unavailable");
    }

    public BorrowingRecordException(String message) {
        super(message);
    }

    public BorrowingRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public BorrowingRecordException(String BorrowingRecordId, String message) {
        super(message);
        this.BorrowingRecordId = BorrowingRecordId;
    }

    public BorrowingRecordException(String BorrowingRecordId, String reason, String message) {
        super(message);
        this.BorrowingRecordId = BorrowingRecordId;
        this.reason = reason;
    }

    public BorrowingRecordException(String BorrowingRecordId, String reason, String message, Throwable cause) {
        super(message, cause);
        this.BorrowingRecordId = BorrowingRecordId;
        this.reason = reason;
    }

    public String getBorrowingRecordId() {
        return BorrowingRecordId;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(super.getMessage());
        if (BorrowingRecordId != null && !BorrowingRecordId.isEmpty()) {
            sb.append(" (Item ID: ").append(BorrowingRecordId).append(")");
        }
        if (reason != null && !reason.isEmpty()) {
            sb.append(" - Reason: ").append(reason);
        }
        return sb.toString();
    }
}
