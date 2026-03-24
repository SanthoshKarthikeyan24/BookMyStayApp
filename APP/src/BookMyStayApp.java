import java.util.*;

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Add-On Service Selection");

        // Step 1: Simulate an already confirmed reservation (from UC6)
        String reservationId = "Single-1";

        // Step 2: Create Add-On Services
        AddOnService wifi = new AddOnService("WiFi", 500.0);
        AddOnService breakfast = new AddOnService("Breakfast", 1000.0);

        // Step 3: Add services to reservation
        AddOnServiceManager manager = new AddOnServiceManager();
        manager.addService(reservationId, wifi);
        manager.addService(reservationId, breakfast);

        // Step 4: Calculate total add-on cost
        double totalCost = manager.calculateTotalCost(reservationId);

        // Step 5: Display result
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Total Add-On Cost: " + totalCost);
    }
}

/**
 * Add-On Service (Value-added feature)
 */
class AddOnService {
    private final String serviceName;
    private final double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

/**
 * Manages mapping: Reservation → List of Services
 */
class AddOnServiceManager {

    // Map<ReservationID, List<Services>>
    private final Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    /**
     * Add a service to a reservation
     */
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    /**
     * Calculate total cost of services for a reservation
     */
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());

        double total = 0.0;
        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }
}