package cvut.ear.hotelreservationsystem.model;

public enum RoomType {
    STANDARD("TYPE_STANDARD"), DELUXE("TYPE_DELUXE"), APPARTEMENT("TYPE_APPARTEMENT");

    private final String name;

    RoomType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
