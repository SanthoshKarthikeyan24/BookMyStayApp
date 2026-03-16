public class BookMyStayApp {

    // Static variables representing room availability
    static int singleRoomAvailability = 5;
    static int doubleRoomAvailability = 3;
    static int suiteRoomAvailability = 2;

    public static void main(String[] args) {

        System.out.println("Hotel Room Initialization\n");

        // Create room objects using polymorphism
        Room singleRoom = new SingleRoom(1, 250, 1500.0);
        Room doubleRoom = new DoubleRoom(2, 400, 2500.0);
        Room suiteRoom = new SuiteRoom(3, 750, 5000.0);

        // Display room details and availability
        displayRoomDetails(singleRoom, singleRoomAvailability);
        displayRoomDetails(doubleRoom, doubleRoomAvailability);
        displayRoomDetails(suiteRoom, suiteRoomAvailability);

        System.out.println("Application executed successfully!");
    }

    // Helper method to print room details and availability
    public static void displayRoomDetails(Room room, int availability) {
        System.out.println(room.getRoomType() + ":");
        System.out.println("Beds: " + room.getBeds());
        System.out.println("Size: " + room.getSize() + " sqft");
        System.out.println("Price per night: " + room.getPrice());
        System.out.println("Available: " + availability);
        System.out.println();
    }
}

/**
 * Abstract Room class representing common properties of all room types.
 */
abstract class Room {
    private int beds;
    private int size;      // size in sqft
    private double price;  // price per night

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