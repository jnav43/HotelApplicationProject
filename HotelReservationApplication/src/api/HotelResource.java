package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class HotelResource {

    private static final HotelResource hotelResource = new HotelResource();
    private final CustomerService customerService = CustomerService.getCustomerService();
    private final ReservationService reservationService = ReservationService.getReservationService();

    private HotelResource() {}

    public static HotelResource getHotelResource() { return hotelResource; }

    public Customer getCustomer(String email) { return customerService.getCustomer(email); }

    public void createCustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String email, IRoom room, Date checkInDate, Date checkOutDate) {
        return reservationService.reserveARoom(getCustomer(email), room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String email) {
        final Customer customer = getCustomer(email);
        if (customer == null) {
            return Collections.emptyList();
        }
        return reservationService.getCustomerReservations(getCustomer(email));
    }
}
