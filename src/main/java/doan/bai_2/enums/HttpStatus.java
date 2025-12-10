package doan.bai_2.enums;

public enum HttpStatus {
    OK(200, "OK"),
    CREATED(201, "Success"),
    NO_CONTENT(204, "No Content"),
    BAD_REQUEST(400, "Access Denied"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error");

    private final int code;
    private final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
