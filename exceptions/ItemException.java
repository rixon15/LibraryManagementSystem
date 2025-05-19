package exceptions;

public class ItemException extends RuntimeException {

    private String itemId;
    private String reason;

    public ItemException() {
        super("The requested item is unavailable");
    }

    public ItemException(String message) {
        super(message);
    }

    public ItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemException(String itemId, String message) {
        super(message);
        this.itemId = itemId;
    }

    public ItemException(String itemId, String reason, String message) {
        super(message);
        this.itemId = itemId;
        this.reason = reason;
    }

    public ItemException(String itemId, String reason, String message, Throwable cause) {
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
