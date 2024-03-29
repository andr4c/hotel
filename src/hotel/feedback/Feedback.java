package hotel.feedback;

import hotel.Hotel;
import hotel.client.Client;

public class Feedback {
    private Client client;
    private String text;
    private int rating;
    private Hotel hotel;

    public Feedback(Client client, String text, int rating, Hotel hotel) {
        this.client = client;
        this.text = text;

        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be a number from 1 to 5.");
        }

        this.hotel = hotel;

        client.addFeedback(this);
    }

    public Client getClient() {
        return client;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setText(String text) {
        this.text = text;
    }


    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be a number from 1 to 5.");
        }
    }
}
