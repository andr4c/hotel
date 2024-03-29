package hotel.exceptions;

public class BookingNotFoundException extends Exception {
    public BookingNotFoundException() {
        super("Booking at specified date not found.");
    }
}
