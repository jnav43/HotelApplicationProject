package model.room;

import java.util.Objects;

public class Room implements IRoom {

    private final String roomNumber;
    private final double price;
    private final RoomType enumeration;

    public Room(String roomNumber, double price, RoomType enumeration) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration = enumeration;
    }

    @Override
    public String getRoomNumber() { return this.roomNumber; }

    @Override
    public Double getRoomPrice() { return this.price;}

    @Override
    public RoomType getRoomType() { return this.enumeration; }

    @Override
    public boolean isFree() {
        return this.price == 0.0;
    }

    public boolean equals (Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Room)) {
            return false;
        }
        Room r = (Room) o;
        return Objects.equals(this.roomNumber, r.roomNumber);
    }
    public String toString() {
        return "Room Number: " + roomNumber + ", price: " + price + ", type: " + enumeration;
    }
}
