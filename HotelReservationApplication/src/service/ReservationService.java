package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import javax.naming.spi.ResolveResult;
import java.util.*;


public class ReservationService {
    private static ReservationService reservationService = new ReservationService();
    private static int defaultStay = 5;

    private Map<String, IRoom> rooms = new HashMap<>();
    private Map<String, Collection<Reservation>> reservations = new HashMap<>();

    public static ReservationService getReservationService() {
        return reservationService;
    }

    public void addRoom(IRoom room) {
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomNumber) {
        return rooms.get(roomNumber);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {

        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        Collection<Reservation> customerReservations = getCustomerReservations(customer);

        if (customerReservations == null) {
            customerReservations = new LinkedList<>();
            reservations.put(customer.getEmail(), customerReservations);
        }
        customerReservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        final Collection<Reservation> allReservations = new LinkedList<>();
        final Collection<IRoom> availableRooms = new LinkedList<>(rooms.values());

        for (Collection<Reservation> reservations : reservations.values()) {
            allReservations.addAll(reservations);
        }

        final Collection<IRoom> unAvailableRooms = new LinkedList<>();
        for (Reservation reservation : allReservations) {
            if (reservation.getCheckInDate().before(checkInDate)
                    && reservation.getCheckOutDate().after(checkOutDate)) {
                unAvailableRooms.add(reservation.getRoom());
            }
        }
        availableRooms.removeAll(unAvailableRooms);
        return availableRooms;
    }

    public Collection<Reservation> getCustomerReservations(Customer customer) {
        return reservations.get(customer.getEmail());
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    public void printReservations() {
        Collection<Reservation> allReservations = new LinkedList<>();
        for (Collection<Reservation> reservations : reservations.values()) {
            allReservations.addAll(reservations);
        }
        if (allReservations.isEmpty()) {
            System.out.println("No reservations found");
        } else {
            for (Reservation reservation : allReservations) {
                System.out.println(reservation + "\n");
            }
        }
    }
}
