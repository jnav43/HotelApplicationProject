package model;

import model.room.IRoom;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Reservation {
    private final Customer customer;
    private final IRoom room;
    private final Date checkInDate;
    private final Date checkOutDate;

    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() { return this.customer; }
    public IRoom getRoom() { return this.room; }
    public Date getCheckInDate() { return this.checkInDate; }
    public Date getCheckOutDate() { return this.checkOutDate; }

    public String toString() {
        return "Reservation {"+
                customer +'\'' +
                ", Room: '" + room +'\'' +
                ", Check In Date: '" + checkInDate + '\''+
                ", Check Out Date: '" + checkOutDate + '\'';
    }

}
