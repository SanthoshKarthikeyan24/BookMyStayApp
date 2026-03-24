import java.util.HashMap;
import java.util.Map;

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Room Search\n");

        // Centralized Inventory Initialization
        RoomInventory inventory = new RoomInventory();
        inventory.registerRoomType("Single Room", 5);
        inventory.registerRoomType("Double Room", 3);
        inventory.registerRoomType("Suite Room", 2);

        // Domain Objects
        Room singleRoom = new SingleRoom(1, 250, 1500.0);
        Room doubleRoom = new DoubleRoom(2, 400, 2500.0);
        Room suiteRoom = new SuiteRoom(3, 750, 5000.0);

        // Perform Read-Only Search (No State Change)
        searchAndDisplay(singleRoom, inventory);
        searchAndDisplay(doubleRoom, inventory);
        searchAndDisplay(suiteRoom, inventory);

        System.out.println("Search completed successfully!");
    }

    /**
     * Read-only search method (Use Case 4)
     * Displays room details only if available > 0
     */
    public static void searchAndDisplay(Room room, RoomInventory inventory) {
        int available = inventory.getAvailability(room.getRoomType());

        // Validation: show only available rooms
        if (available > 0) {
            System.out.println(room.getRoomType() + ":");
            System.out.println("Beds: " + room.getBeds());
            System.out.println("Size: " + room.getSize() + " sqft");
            System.out.println("Price per night: " + room.getPrice());
            System.out.println("Available: " + available);
            System.out.println();
        }
    }
}

/**
 * Centralized Inventory (Read-Only Access for Search)
 */
class RoomInventory {

    private final Map<String, Integer> availabilityMap;

    public RoomInventory() {
        this.availabilityMap = new HashMap<>();
    }

    public void registerRoomType(String roomType, int initialAvailability) {
        if (roomType == null || roomType.isEmpty() || initialAvailability < 0) {
            throw new IllegalArgumentException("Invalid input");
        }
        availabilityMap.put(roomType, initialAvailability);
    }

    /**
     * Read-only method
     */
    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }
}

/**
 * Abstract Room class
 */
abstract class Room {
    private final int beds;
    private final int size;
    private final double price;

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

    public abstract String getRoomType();
}

/**
 * Room Types
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

class DoubleRoom extends Room {
    public DoubleRoom(int beds, int size, double price) {
        super(beds, size, price);
    }

    @Override
    public String getRoomType() {
        return "Double Room";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom(int beds, int size, double price) {
        super(beds, size, price);
    }

    @Override
    public String getRoomType() {
        return "Suite Room";
    }
}