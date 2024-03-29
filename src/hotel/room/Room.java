package hotel.room;

import hotel.Hotel;
import hotel.exceptions.AlreadyOccupiedException;
import hotel.exceptions.BookingNotFoundException;
import java.time.LocalDate;
import java.util.List;

public class Room {
    private static int lastAssignedId = 0;
    private int id; // Toal erinev number
    private Type type;
    private Hotel hotel; // Üks tuba vaid ühes hotellis
    private int price;
    private List<LocalDate> occupancyDays; // Saab broneerida kindlaks päevaks + näitab kas vaba

    // Erinevad tüübid millest sõltub hind
    public enum Type {
        LUXURY(100),
        MIDDLECLASS(50),
        BASIC(25);

        private final int price;

        Type(int price) {
            this.price = price;
        }

        public int getPrice() {
            return price;
        }
    }

    public Room(Type type, Hotel hotel) {
        this.id = ++lastAssignedId;
        this.type = type;
        this.price = type.getPrice();
        this.hotel = hotel;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public List<LocalDate> getOccupancyDays() {
        return occupancyDays;
    }

    public void addOccupancyDay(LocalDate date) throws AlreadyOccupiedException {
        if (occupancyDays.contains(date)) {
            throw new AlreadyOccupiedException();
        } else {
            occupancyDays.add(date);
        }
    }

    public void removeOccupancyDay(LocalDate date) throws BookingNotFoundException {
        boolean removed = occupancyDays.remove(date);
        if (!removed) {
            throw new BookingNotFoundException();
        }
    }

    public void setHotel(Hotel hotel) {
        if (this.hotel == null) {
            this.hotel = hotel;
        } else {
            throw new IllegalArgumentException("Room has already been assigned to a hotel.");
        }

    }
}
