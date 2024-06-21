package model.room;

public enum RoomType {
    SINGLE("1"), DOUBLE("2");

    public final String roomSize;

    RoomType(String roomSize) {
        this.roomSize = roomSize;
    }

    public static RoomType getRoomSize(String roomSize) {
        for (RoomType type : RoomType.values()) {
            if (type.roomSize.equals(roomSize)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid room size");
    }
}
