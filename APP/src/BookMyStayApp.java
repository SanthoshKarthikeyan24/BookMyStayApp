import java.util.*;

/**
 * Main Application
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking History & Reporting");

        // Step 1: Simulate confirmed reservations with guest names
        Reservation r1 = new Reservation("Single-1", "Single", "Abhi");
        Reservation r2 = new Reservation("Double-1", "Double", "Subha");
        Reservation r3 = new Reservation("Suite-1", "Suite", "Vanmathi");

        // Step 2: Store in Booking History
        BookingHistory history = new BookingHistory();
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Step 3: Admin views booking history
        System.out.println("\nBooking History Report:");
        for (Reservation r : history.getConfirmedReservations()) {
            System.out.println("Guest: " + r.getGuestName() + ", Room Type: " + r.getRoomType());
        }
    }
}

/**
 * Reservation Entity with Guest Name
 */
class Reservation {
    private final String reservationId;
    private final String roomType;
    private final String guestName;

    public Reservation(String reservationId, String roomType, String guestName) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.guestName = guestName;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getGuestName() {
        return guestName;
    }
}

/**
 * Booking History Class (non-public to allow single file compilation)
 */
class BookingHistory {

    /**
     * List that stores confirmed reservations.
     */
    private List<Reservation> confirmedReservations;

    /**
     * Initializes an empty booking history.
     */
    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    /**
     * Adds a confirmed reservation
     * to booking history.
     *
     * @param reservation confirmed booking
     */
    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    /**
     * Returns all confirmed reservations.
     *
     * @return list of reservations
     */
    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}