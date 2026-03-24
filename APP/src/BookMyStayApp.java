import java.util.LinkedList;
import java.util.Queue;

public class BookMyStayApp {

    public static void main(String[] args) {

        // Display application header
        System.out.println("Booking Request Queue");

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Create booking requests
        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Double");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        // Add requests to queue (FIFO order)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Process requests in FIFO order
        while (bookingQueue.hasPendingRequests()) {
            Reservation request = bookingQueue.getNextRequest();
            System.out.println("Processing booking for Guest: "
                    + request.getGuestName()
                    + ", Room Type: "
                    + request.getRoomType());
        }

        System.out.println("\nAll booking requests processed fairly!");
    }
}

/**
 * Represents a booking request (Guest intent)
 */
class Reservation {
    private final String guestName;
    private final String roomType;

    public Reservation(String guestName, String roomType) {
        if (guestName == null || guestName.isEmpty() ||
                roomType == null || roomType.isEmpty()) {
            throw new IllegalArgumentException("Invalid reservation details");
        }
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * Booking Request Queue (FIFO)
 */
class BookingRequestQueue {

    private final Queue<Reservation> queue;

    public BookingRequestQueue() {
        this.queue = new LinkedList<>();
    }

    /**
     * Add request to queue
     */
    public void addRequest(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        queue.offer(reservation); // FIFO insertion
    }

    /**
     * Get next request (FIFO removal)
     */
    public Reservation getNextRequest() {
        return queue.poll(); // removes head of queue
    }

    /**
     * Check if queue has pending requests
     */
    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }
}