package hotel.exceptions;

public class ClientNotFound extends Exception {
    public ClientNotFound() {
        super("The person isn't a client at this hotel.");
    }
}
