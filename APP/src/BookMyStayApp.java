import java.util.*;

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        // Inventory setup
        RoomInventory inventory = new RoomInventory();
        inventory.registerRoomType("Single", 2);
        inventory.registerRoomType("Suite", 1);

        // Booking queue (FIFO)
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vanmathi", "Suite"));

        // Booking service
        BookingService service = new BookingService(inventory);

        // Process bookings
        while (queue.hasPendingRequests()) {
            service.processBooking(queue.getNextRequest());
        }
    }
}

/**
 * Reservation (Guest Request)
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
 * FIFO Queue
 */
class BookingRequestQueue {
    private final Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }
}

/**
 * Inventory
 */
class RoomInventory {
    private final Map<String, Integer> availability = new HashMap<>();

    public void registerRoomType(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

/**
 * Booking Service (Core UC6)
 */
class BookingService {

    private final RoomInventory inventory;

    // Track allocated IDs per type
    private final Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Counter per room type → ensures sequential IDs
    private final Map<String, Integer> roomCounters = new HashMap<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation request) {

        String type = request.getRoomType();
        String guest = request.getGuestName();

        // Check availability
        if (inventory.getAvailability(type) <= 0) {
            System.out.println("Booking failed for Guest: " + guest + " (No rooms available)");
            return;
        }

        // Generate sequential room ID
        int count = roomCounters.getOrDefault(type, 0) + 1;
        roomCounters.put(type, count);

        String roomId = type + "-" + count;

        // Store allocation (Set ensures uniqueness)
        allocatedRooms.putIfAbsent(type, new HashSet<>());
        allocatedRooms.get(type).add(roomId);

        // Update inventory immediately
        inventory.reduceRoom(type);

        // Confirm booking
        System.out.println("Booking confirmed for Guest: " + guest + ", Room ID: " + roomId);
    }
}