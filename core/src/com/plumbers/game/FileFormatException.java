package com.plumbers.game;

public class FileFormatException extends Exception {
    private static final long serialVersionUID = 6444554379261290003L;

    public FileFormatException() {
        super();
    }

    public FileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileFormatException(String message) {
        super(message);
    }

    public FileFormatException(Throwable cause) {
        super(cause);
    }

}
