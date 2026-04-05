package service;

import exception.TableNotAvailableException;
import model.Reservation;

// This interface defines the contract for managing reservations in the restaurant table reservation system.
public interface ReservationService {
    public void addReservation(int tableNO, Reservation reservation) throws TableNotAvailableException;
    public void cancelReservation(int tableNO);
    public Reservation getReservation(int tableNO);
}
