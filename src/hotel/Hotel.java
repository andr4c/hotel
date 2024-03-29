package hotel;

import hotel.client.Client;
import hotel.exceptions.AlreadyOccupiedException;
import hotel.exceptions.BookingNotFoundException;
import hotel.exceptions.ClientNotFound;
import hotel.exceptions.RoomNotFoundException;
import hotel.feedback.Feedback;
import hotel.room.Room;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Hotel {
    private final Map<Integer, Room> rooms; // Hotellis on toad
    private final Map<Client, List<Feedback>> feedbacks;
    private List<Client> clients;

    public Hotel() {
        this.rooms = new HashMap<>();
        this.feedbacks = new HashMap<>();
        this.clients = new ArrayList<>();
    }

    // Kindel päev
    public String bookRoom(Client client, Room.Type type, LocalDate date) throws AlreadyOccupiedException {
        try {
            Optional<Room> optionalRoom = getAvailableRooms(type, date).stream().findFirst();

            if (optionalRoom.isPresent()) {
                Room room = optionalRoom.get();
                int price = room.getPrice();
                int clientMoney = client.getMoney();

                if (clientMoney >= price) { // Kliendil piisavalt raha
                    client.subtractMoney(price);
                    room.addOccupancyDay(date);
                    client.addBooking(room);
                    clients.add(client);
                    return "Room has been booked successfully";
                } else {
                    return "Not enough money for this type of room.";
                }
            } else {
                return "No rooms of this type are available for specified date.";
            }
        } catch (RoomNotFoundException e) {
            return "No rooms of this type are available for specified date.";
        }
    }

    // Broneeringu saab tühistada
    public String cancelRoomBooking(Client client, int roomId, LocalDate date) throws BookingNotFoundException {
        try {
            Room room = getRoomById(roomId);
            if (client.hasBooking(room)) {
                room.removeOccupancyDay(date);
                client.removeBooking(room);
                clients.remove(client);

                removeClientsWithPastBookings();

                return "Room booking cancelled successfully.";
            } else {
                return "Client does not have a booking for this room.";
            }
        } catch (RoomNotFoundException e) {
            return "Room not found.";
        }
    }

    public void giveFeedback(Client client, String text, int rating) throws ClientNotFound {
        if (!clients.contains(client)) {
            throw new ClientNotFound();
        } else {
            Feedback feedback = new Feedback(client, text, rating, this);
            addFeedback(feedback);
        }
    }

    // otsida vabu tube vastavalt kuupäevale ja või tüübile
    public List<Room> getAvailableRooms(Room.Type type, LocalDate date) throws RoomNotFoundException {
        List<Room> availableRooms = rooms.values().stream()
                .filter(room ->
                        (type == null || room.getType() == type) &&
                                (date == null || !room.getOccupancyDays().contains(date)))
                .collect(Collectors.toList());

        if (availableRooms.isEmpty()) {
            throw new RoomNotFoundException();
        }

        return availableRooms;
    }

    public Room getRoomById(int id) throws RoomNotFoundException {
        if (!rooms.containsKey(id)) {
            throw new RoomNotFoundException();
        }
        return rooms.get(id);
    }

    public void removeClientsWithPastBookings() {
        LocalDate currentDate = LocalDate.now();
        clients.removeIf(client -> client.hasPastBookings(currentDate));
    }

    // Ülevaade klientidest hotellil
    public List<Client> getClients() {
        return clients;
    }

    public void addFeedback(Feedback feedback) {
        Client client = feedback.getClient();
        List<Feedback> clientFeedbacks = feedbacks.getOrDefault(client, new ArrayList<>());
        clientFeedbacks.add(feedback);
        feedbacks.put(client, clientFeedbacks);
    }

    // Ülevaade ruumidest hotellil
    public Map<Integer, Room> getRooms() {
        return rooms;
    }

    public List<Room> getBookedRooms() {
        return rooms.values().stream()
                .filter(room -> !room.getOccupancyDays().isEmpty())
                .collect(Collectors.toList());
    }

    // Ülevaade hinnangutest
    public Map<Client, List<Feedback>> getFeedbacks() {
        return feedbacks;
    }

    // Ülevaade keskmisest hinnangust
    public double getAverageRating() {
        if (feedbacks.isEmpty()) {
            return -1;
        }

        int totalRating = 0;
        int feedbackCount = 0;

        for (List<Feedback> clientFeedbacks : feedbacks.values()) {
            for (Feedback feedback : clientFeedbacks) {
                totalRating += feedback.getRating();
                feedbackCount++;
            }
        }

        return (double) totalRating / feedbackCount;
    }

    public List<Client> sortClientsBookingOrRating() {
        return clients.stream()
                .sorted(Comparator.comparingInt((Client client) -> client.getBookings().size()).reversed()
                        .thenComparingDouble(client -> {
                            List<Feedback> clientFeedbacks = feedbacks.get(client);
                            if (clientFeedbacks != null && !clientFeedbacks.isEmpty()) {
                                return clientFeedbacks.stream().mapToDouble(Feedback::getRating).average().orElse(0);
                            }
                            return 0;
                        }).reversed())
                .collect(Collectors.toList());
    }

    public void addRoom(Room room) {
        int roomId = room.getId();
        room.setHotel(this);

        rooms.put(roomId, room);
    }

    public void removeRoom(int roomId) {
        rooms.remove(roomId);
    }
}
