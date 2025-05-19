package exceptions;

public class SearchException extends RuntimeException {

    private String reason;

    public SearchException() {
        super("The requested item is unavailable");
    }

    public SearchException(String message) {
        super(message);
    }

    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchException(String reason, String message) {
        super(message);
        this.reason = reason;
    }

    public SearchException(String reason, String message, Throwable cause) {
        super(message, cause);
        this.reason = reason;
    }



    public String getReason() {
        return reason;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(super.getMessage());
        if (reason != null && !reason.isEmpty()) {
            sb.append(" - Reason: ").append(reason);
        }
        return sb.toString();
    }
}
