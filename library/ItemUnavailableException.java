package FinalProject01.library;

public class ItemUnavailableException extends RuntimeException {

    private String itemId;
    private String reason;

    public ItemUnavailableException() {
        super("The requested item is unavailable");
    }

    public ItemUnavailableException(String message) {
        super(message);
    }

    public ItemUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemUnavailableException(String itemId, String message) {
        super(message);
        this.itemId = itemId;
    }

    public ItemUnavailableException(String itemId, String reason, String message) {
        super(message);
        this.itemId = itemId;
        this.reason = reason;
    }

    public ItemUnavailableException(String itemId, String reason, String message, Throwable cause) {
        super(message, cause);
        this.itemId = itemId;
        this.reason = reason;
    }

    public String getItemId() {
        return itemId;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(super.getMessage());
        if (itemId != null && !itemId.isEmpty()) {
            sb.append(" (Item ID: ").append(itemId).append(")");
        }
        if (reason != null && !reason.isEmpty()) {
            sb.append(" - Reason: ").append(reason);
        }
        return sb.toString();
    }
}
