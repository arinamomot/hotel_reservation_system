package cvut.ear.hotelreservationsystem.model;

public enum ReservationStatus {
    APPROVED("STATUS_APPROVED"), DECLINED("STATUS_DECLINED"), WAITING("STATUS_WAITING"), DELETED("STATUS_DELETED");

    private final String name;

    ReservationStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
