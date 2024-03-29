package hotel.client;

import hotel.feedback.Feedback;
import hotel.room.Room;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private String name;
    private int money;
    private List<Room> bookings;
    private List<Feedback> feedbacks;

    public Client(String name, int money) {
        this.name = name;
        this.money = money;
        this.bookings = new ArrayList<>();
        this.feedbacks = new ArrayList<>();
    }

    // Kliendil ülevaade broneeringutest
    public List<Room> getBookings() {
        return bookings;
    }

    // Kliendil ülevaade tagasisidedest
    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public int getMoney() {
        return money;
    }

    public void subtractMoney(int amount) {
        money -= amount;
    }

    public void addBooking(Room room) {
        bookings.add(room);
    }
    public void addFeedback(Feedback feedback) {
        feedbacks.add(feedback);
    }

    public void removeBooking(Room room) {
        bookings.remove(room);
    }

    public boolean hasBooking(Room room) {
        return bookings.contains(room);
    }

    public boolean hasPastBookings(LocalDate currentDate) {
        for (Room room : bookings) {
            List<LocalDate> occupancyDays = room.getOccupancyDays();
            for (LocalDate occupancyDate : occupancyDays) {
                if (occupancyDate.isBefore(currentDate)) {
                    return true;
                }
            }
        }
        return false;
    }
}
