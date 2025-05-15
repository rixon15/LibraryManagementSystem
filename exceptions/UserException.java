package FinalProject01.exceptions;

public class UserException extends RuntimeException {
    private String userId;
    private String reason;

    public UserException() {
        super("The requested item is unavailable");
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(String userId, String message) {
        super(message);
        this.userId = userId;
    }

    public UserException(String userId, String reason, String message) {
        super(message);
        this.userId = userId;
        this.reason = reason;
    }

    public UserException(String userId, String reason, String message, Throwable cause) {
        super(message, cause);
        this.userId = userId;
        this.reason = reason;
    }

    public String getuserId() {
        return userId;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(super.getMessage());
        if (userId != null && !userId.isEmpty()) {
            sb.append(" (Item ID: ").append(userId).append(")");
        }
        if (reason != null && !reason.isEmpty()) {
            sb.append(" - Reason: ").append(reason);
        }
        return sb.toString();
    }
}
