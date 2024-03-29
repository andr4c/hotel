package hotel.exceptions;

public class RoomNotFoundException extends Exception {
    public RoomNotFoundException() {
        super("Room not found in the database.");
    }
}
