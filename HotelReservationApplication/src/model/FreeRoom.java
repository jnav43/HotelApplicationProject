package model;


import model.room.Room;
import model.room.RoomType;

public class FreeRoom extends Room {
    public FreeRoom(String roomNumber, RoomType enumeration) {
        super(roomNumber, 0.0, enumeration);
    }

    public String toString() {
        return "Free Room: " + super.toString();
    }
}
