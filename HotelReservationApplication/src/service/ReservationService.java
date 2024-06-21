package service;

import model.Customer;
import model.room.IRoom;
import model.Reservation;

import java.util.Calendar;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;


public class ReservationService {
    private static final ReservationService reservationService = new ReservationService();
    private static final int additionalDays = 7;

    private final Map<String, IRoom> rooms = new HashMap<>();
    private final Map<String, Collection<Reservation>> reservations = new HashMap<>();

    private ReservationService() {}

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

        final Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        Collection<Reservation> customerReservations = getCustomerReservations(customer);

        if (customerReservations == null) {
            customerReservations = new LinkedList<>();
        }
        customerReservations.add(reservation);
        reservations.put(customer.getEmail(), customerReservations);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkIn, Date checkOut) {
        return findAvailableRooms(checkIn, checkOut);
    }

    private Collection<IRoom> findAvailableRooms(Date checkIn, Date checkOut) {
        final Collection<Reservation> allReservations = getAllReservations();
        Collection<IRoom> unavailableRooms = new LinkedList<>();

        for (Reservation reservation : allReservations) {
            if (doubleReserved(reservation, checkIn, checkOut)) {
                unavailableRooms.add(reservation.getRoom());
            }
        }
        return rooms.values().stream()
                .filter(room -> unavailableRooms.stream().noneMatch(unavailableRoom -> unavailableRoom.equals(room)))
                .collect(Collectors.toList());

    }

    public Collection<IRoom> findAlternativeRooms(final Date checkInDate, final Date checkOutDate) {
        final Date altCheckIn = alternateDates(checkInDate);
        final Date altCheckOut = alternateDates(checkOutDate);
        return findAvailableRooms(altCheckIn, altCheckOut);
    }

    public Date alternateDates(final Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, additionalDays);
        return calendar.getTime();
    }

    public Collection<Reservation> getCustomerReservations(Customer customer) {
        return reservations.get(customer.getEmail());
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    private boolean doubleReserved(Reservation reservation, final Date checkIn, final Date checkOut) {
        return checkIn.before(reservation.getCheckOutDate()) && checkOut.after(reservation.getCheckInDate());
    }

    private Collection<Reservation> getAllReservations() {
        final Collection<Reservation> allReservations = new LinkedList<>();

        for (Collection<Reservation> reservations : reservations.values()) {
            allReservations.addAll(reservations);
        }
        return allReservations;
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
