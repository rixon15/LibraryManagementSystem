package FinalProject01.exceptions;

public class AuthorException extends RuntimeException {
    private String authorId;
    private String reason;

    public AuthorException() {
        super("The requested item is unavailable");
    }

    public AuthorException(String message) {
        super(message);
    }

    public AuthorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorException(String authorId, String message) {
        super(message);
        this.authorId = authorId;
    }

    public AuthorException(String authorId, String reason, String message) {
        super(message);
        this.authorId = authorId;
        this.reason = reason;
    }

    public AuthorException(String authorId, String reason, String message, Throwable cause) {
        super(message, cause);
        this.authorId = authorId;
        this.reason = reason;
    }

    public String getauthorId() {
        return authorId;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(super.getMessage());
        if (authorId != null && !authorId.isEmpty()) {
            sb.append(" (Item ID: ").append(authorId).append(")");
        }
        if (reason != null && !reason.isEmpty()) {
            sb.append(" - Reason: ").append(reason);
        }
        return sb.toString();
    }
}
