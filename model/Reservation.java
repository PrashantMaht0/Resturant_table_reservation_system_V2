package model;
//Reservation record representing a reservation made by a customer
import java.time.LocalDateTime;

public record Reservation(String customerName, String customerPhone , LocalDateTime reservationTime, int tableNumber) {
    public Reservation {
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be null or blank");
        }
        if (customerPhone == null || customerPhone.isBlank()) {
            throw new IllegalArgumentException("Customer phone cannot be null or blank");
        }
        if (reservationTime == null) {
            throw new IllegalArgumentException("Reservation time cannot be null");
        }
        if (tableNumber <= 0) {
            throw new IllegalArgumentException("Table number must be greater than 0");
        }
    }
}
