
import java.util.HashMap;
import java.util.Map;

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Hotel Room Initialization with Centralized Inventory\n");

        // Create RoomInventory and register room types with initial availability
        RoomInventory inventory = new RoomInventory();
        inventory.registerRoomType("Single Room", 5);
        inventory.registerRoomType("Double Room", 3);
        inventory.registerRoomType("Suite Room", 2);

        // Create room objects (domain modeling)
        Room singleRoom = new SingleRoom(1, 250, 1500.0);
        Room doubleRoom = new DoubleRoom(2, 400, 2500.0);
        Room suiteRoom = new SuiteRoom(3, 750, 5000.0);

        // Display room details along with their availability from centralized inventory
        displayRoomDetails(singleRoom, inventory);
        displayRoomDetails(doubleRoom, inventory);
        displayRoomDetails(suiteRoom, inventory);

        // Example: Update availability after a booking
        System.out.println("\nBooking 1 Single Room...");
        boolean booked = inventory.bookRoom("Single Room");
        System.out.println(booked ? "Booking successful!" : "Booking failed. No rooms available.");

        // Show updated availability
        System.out.println("\nUpdated Inventory:");
        displayRoomDetails(singleRoom, inventory);

        System.out.println("\nApplication executed successfully!");
    }

    // Helper method to print room details with inventory availability
    public static void displayRoomDetails(Room room, RoomInventory inventory) {
        System.out.println(room.getRoomType() + ":");
        System.out.println("Beds: " + room.getBeds());
        System.out.println("Size: " + room.getSize() + " sqft");
        System.out.println("Price per night: " + room.getPrice());
        System.out.println("Available: " + inventory.getAvailability(room.getRoomType()));
        System.out.println();
    }
}

/**
 * Class responsible for managing room availability using a centralized HashMap.
 */
class RoomInventory {

    private final Map<String, Integer> availabilityMap;

    public RoomInventory() {
        this.availabilityMap = new HashMap<>();
    }

    /**
     * Registers a new room type with its initial availability.
     *
     * @param roomType Room type name
     * @param initialAvailability Number of available rooms
     */
    public void registerRoomType(String roomType, int initialAvailability) {
        if (roomType == null || roomType.isEmpty() || initialAvailability < 0) {
            throw new IllegalArgumentException("Invalid room type or availability");
        }
        availabilityMap.put(roomType, initialAvailability);
    }

    /**
     * Retrieves current availability of a room type.
     *
     * @param roomType Room type name
     * @return Number of available rooms or 0 if none registered
     */
    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    /**
     * Attempts to book a room by reducing availability by one if possible.
     *
     * @param roomType Room type name
     * @return true if booking succeeded, false if no rooms available
     */
    public boolean bookRoom(String roomType) {
        int available = availabilityMap.getOrDefault(roomType, 0);
        if (available > 0) {
            availabilityMap.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    /**
     * Method to add rooms to the inventory (e.g., cancellations or new rooms).
     *
     * @param roomType Room type name
     * @param count Number of rooms to add
     */
    public void addRooms(String roomType, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        int current = availabilityMap.getOrDefault(roomType, 0);
        availabilityMap.put(roomType, current + count);
    }
}

/**
 * Abstract Room class representing common properties of all room types.
 */
abstract class Room {
    private final int beds;
    private final int size;      // size in sqft
    private final double price;  // price per night

    public Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    // Abstract method to get room type
    public abstract String getRoomType();
}

/**
 * Concrete class for Single Room.
 */
class SingleRoom extends Room {
    public SingleRoom(int beds, int size, double price) {
        super(beds, size, price);
    }

    @Override
    public String getRoomType() {
        return "Single Room";
    }
}

/**
 * Concrete class for Double Room.
 */
class DoubleRoom extends Room {
    public DoubleRoom(int beds, int size, double price) {
        super(beds, size, price);
    }

    @Override
    public String getRoomType() {
        return "Double Room";
    }
}

/**
 * Concrete class for Suite Room.
 */
class SuiteRoom extends Room {
    public SuiteRoom(int beds, int size, double price) {
        super(beds, size, price);
    }

    @Override
    public String getRoomType() {
        return "Suite Room";
    }
}