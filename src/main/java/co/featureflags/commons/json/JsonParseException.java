package co.featureflags.commons.json;

public class JsonParseException extends RuntimeException {
    public JsonParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonParseException(String message) {
        super(message);
    }
}
