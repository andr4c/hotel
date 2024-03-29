package hotel.exceptions;

public class AlreadyOccupiedException extends Exception {
    public AlreadyOccupiedException() {
        super("Room is already occupied at that date.");
    }
}
