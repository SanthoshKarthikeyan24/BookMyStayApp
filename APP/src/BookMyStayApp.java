import java.util.*;

/**
 * Main Application for Booking Cancellation
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking Cancellation");

        // Step 1: Initialize inventory and cancellation service
        RoomInventory inventory = new RoomInventory();
        CancellationService cancellationService = new CancellationService();

        // Step 2: Simulate a confirmed booking
        String reservationId = "Single-1";
        String roomType = "Single";

        // Register booking for later cancellation
        cancellationService.registerBooking(reservationId, roomType);

        // Increment inventory to simulate previous state (for demo, assume 5 initially)
        inventory.setAvailability(roomType, 5);

        // Step 3: Cancel booking
        cancellationService.cancelBooking(reservationId, inventory);

        // Step 4: Show rollback history
        cancellationService.showRollbackHistory();
    }
}

/**
 * Room Inventory
 */
class RoomInventory {
    private final Map<String, Integer> availability = new HashMap<>();

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public void incrementAvailability(String roomType) {
        availability.put(roomType, getAvailability(roomType) + 1);
    }

    public void setAvailability(String roomType, int count) {
        availability.put(roomType, count);
    }
}

/**
 * Cancellation Service
 */
class CancellationService {

    /** Stack that stores recently released room IDs (LIFO rollback) */
    private final Stack<String> releasedRoomIds;

    /** Maps reservation ID to room type */
    private final Map<String, String> reservationRoomTypeMap;

    /** Initializes cancellation tracking structures */
    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    /**
     * Registers a confirmed booking.
     *
     * @param reservationId confirmed reservation ID
     * @param roomType allocated room type
     */
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    /**
     * Cancels a confirmed booking and restores inventory safely.
     *
     * @param reservationId reservation to cancel
     * @param inventory centralized room inventory
     */
    public void cancelBooking(String reservationId, RoomInventory inventory) {
        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Cannot cancel. Reservation does not exist.");
            return;
        }

        // Retrieve room type and restore inventory
        String roomType = reservationRoomTypeMap.get(reservationId);
        inventory.incrementAvailability(roomType);

        // Track released reservation in stack
        releasedRoomIds.push(reservationId);

        // Remove from reservation map (cancellation complete)
        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
        System.out.println("Updated " + roomType + " Room Availability: " + inventory.getAvailability(roomType));
    }

    /**
     * Displays recently cancelled reservations (most recent first)
     */
    public void showRollbackHistory() {
        System.out.println("Rollback History (Most Recent First):");
        while (!releasedRoomIds.isEmpty()) {
            String reservationId = releasedRoomIds.pop();
            System.out.println("Released Reservation ID: " + reservationId);
        }
    }
}