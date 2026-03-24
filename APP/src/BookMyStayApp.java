import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main Application for Concurrent Booking Simulation
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        // Step 1: Initialize shared components
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomAllocationService allocationService = new RoomAllocationService(inventory);

        // Step 2: Add booking requests to queue
        bookingQueue.addBooking(new Reservation("Abhi", "Single"));
        bookingQueue.addBooking(new Reservation("Vanmathi", "Double"));
        bookingQueue.addBooking(new Reservation("Kural", "Suite"));
        bookingQueue.addBooking(new Reservation("Subha", "Single"));

        // Step 3: Create booking processor tasks (threads)
        Thread t1 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService));

        // Step 4: Start concurrent processing
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        // Step 5: Display remaining inventory
        System.out.println("Remaining Inventory:");
        System.out.println("Single: " + inventory.getAvailability("Single"));
        System.out.println("Double: " + inventory.getAvailability("Double"));
        System.out.println("Suite: " + inventory.getAvailability("Suite"));
    }
}

/**
 * Reservation Entity
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
 * Booking Request Queue (shared)
 */
class BookingRequestQueue {
    private final Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addBooking(Reservation reservation) {
        queue.offer(reservation);
    }

    public synchronized Reservation pollBooking() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * Room Inventory (shared)
 */
class RoomInventory {
    private final Map<String, AtomicInteger> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", new AtomicInteger(5));
        inventory.put("Double", new AtomicInteger(3));
        inventory.put("Suite", new AtomicInteger(2));
    }

    public int getAvailability(String roomType) {
        AtomicInteger count = inventory.get(roomType);
        return count != null ? count.get() : 0;
    }

    public boolean allocateRoom(String roomType) {
        AtomicInteger count = inventory.get(roomType);
        if (count != null) {
            synchronized (count) {
                if (count.get() > 0) {
                    count.decrementAndGet();
                    return true;
                }
            }
        }
        return false;
    }
}

/**
 * Room Allocation Service (ensures thread-safe allocation)
 */
class RoomAllocationService {
    private final RoomInventory inventory;
    private final Map<String, Integer> roomCounters = new HashMap<>();

    public RoomAllocationService(RoomInventory inventory) {
        this.inventory = inventory;
        roomCounters.put("Single", 1);
        roomCounters.put("Double", 1);
        roomCounters.put("Suite", 1);
    }

    public synchronized String allocateRoom(Reservation reservation) {
        String type = reservation.getRoomType();
        if (inventory.allocateRoom(type)) {
            int roomId = roomCounters.get(type);
            roomCounters.put(type, roomId + 1);
            return type + "-" + roomId;
        }
        return null;
    }
}

/**
 * Concurrent Booking Processor (Runnable)
 */
class ConcurrentBookingProcessor implements Runnable {

    private final BookingRequestQueue bookingQueue;
    private final RoomInventory inventory;
    private final RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService
    ) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {
        while (true) {
            Reservation reservation;
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) break;
                reservation = bookingQueue.pollBooking();
            }

            String roomId = allocationService.allocateRoom(reservation);
            if (roomId != null) {
                System.out.println("Booking confirmed for Guest: " + reservation.getGuestName() + ", Room ID: " + roomId);
            } else {
                System.out.println("Booking failed for Guest: " + reservation.getGuestName() + " (No availability)");
            }
        }
    }
}