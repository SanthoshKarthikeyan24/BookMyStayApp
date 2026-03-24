import java.util.*;

/**
 * Main Application for Booking Validation (Use Case 9)
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        // Display application header
        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        // Initialize required components
        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            // Step 1: Read guest input
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Step 2: Validate booking
            validator.validateRoomType(roomType);
            validator.validateAvailability(roomType, inventory);

            // Step 3: If valid, add to booking queue
            bookingQueue.addBooking(new Reservation(guestName, roomType));

            System.out.println("Booking successful for " + guestName + " in " + roomType + " room.");

        } catch (InvalidBookingException e) {
            // Handle domain-specific validation errors
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

/**
 * Reservation entity
 */
class Reservation {
    private final String guestName;
    private final String roomType;

    public Reservation(String guestName, String roomType) {
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
 * Room Inventory (simple availability tracking)
 */
class RoomInventory {
    private final Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public int getAvailable(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    public void bookRoom(String roomType) {
        roomAvailability.put(roomType, getAvailable(roomType) - 1);
    }
}

/**
 * Reservation Validator for input and system state
 */
class ReservationValidator {

    private static final Set<String> validRoomTypes = new HashSet<>(
            Arrays.asList("Single", "Double", "Suite")
    );

    /**
     * Validates room type input
     */
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!validRoomTypes.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }
    }

    /**
     * Validates room availability
     */
    public void validateAvailability(String roomType, RoomInventory inventory) throws InvalidBookingException {
        if (inventory.getAvailable(roomType) <= 0) {
            throw new InvalidBookingException("Selected room type is fully booked.");
        }
    }
}

/**
 * Booking Request Queue (simulates pending confirmed bookings)
 */
class BookingRequestQueue {
    private final List<Reservation> queue = new ArrayList<>();

    public void addBooking(Reservation reservation) {
        queue.add(reservation);
    }

    public List<Reservation> getBookings() {
        return new ArrayList<>(queue);
    }
}

/**
 * Custom Exception for invalid booking scenarios
 */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}