package hotel.exceptions;

public class RoomAlreadyExistsException extends Exception {
    public RoomAlreadyExistsException() {
        super("Room already exists in the database.");
    }
}
