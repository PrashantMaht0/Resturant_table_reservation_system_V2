package service;
import exception.TableNotAvailableException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import model.AbstractTable;
import model.FourSeaterTable;
import model.Reservation;
import model.TableType;
import model.TwoSeaterTable;


// ReservationManager class that implements the ReservationService interface and manages restaurant reservations
public class ReservationManager implements ReservationService {

    private final List<AbstractTable> tables = new ArrayList<>();
    private final Map<Integer, Reservation> activeReservations = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final util.FileService fileService = new util.FileService();


    public ReservationManager() {
        initializetables();
        
        activeReservations.putAll(fileService.loadReservations());

        boolean removedStaleOnStartup = activeReservations.entrySet().removeIf(entry -> {
            Reservation reservation = entry.getValue();
            return reservation.reservationTime().plusHours(2).isBefore(java.time.LocalDateTime.now());
        });

        if (removedStaleOnStartup) {
            fileService.saveReservations(activeReservations);
        }

        activeReservations.keySet().forEach(tableNo -> {
            model.AbstractTable t = findTable(tableNo);
            if (t != null) t.reserve();
        });

        startBackgroundCleanupTask();
    }

    //background task to clean up expired reservations every two hours
    private void startBackgroundCleanupTask() {
        Runnable cleanupTask = () -> {
            // removeIf returns true if at least one element was removed
            boolean wasAnythingRemoved = activeReservations.entrySet().removeIf(entry -> {
                Reservation reservation = entry.getValue();
                if(reservation.reservationTime().plusHours(2).isBefore(java.time.LocalDateTime.now())) {
                    model.AbstractTable table = findTable(entry.getKey());
                    if (table != null) {
                        table.release();
                    }
                    return true; 
                }
                return false; 
            });

            if (wasAnythingRemoved) {
                fileService.saveReservations(activeReservations); 
            }
        };
        scheduler.scheduleAtFixedRate(cleanupTask, 1, 1, TimeUnit.MINUTES);
    }

    private void initializetables() {
        tables.add(new TwoSeaterTable(1, TableType.WINDOW));
        tables.add(new FourSeaterTable(2, TableType.BOOTH));
        tables.add(new FourSeaterTable(3, TableType.STANDARD, 6));
        tables.add(new TwoSeaterTable(4, TableType.STANDARD));
        tables.add(new TwoSeaterTable(5, TableType.WINDOW));
        tables.add(new TwoSeaterTable(6, TableType.WINDOW));
        tables.add(new TwoSeaterTable(7, TableType.STANDARD));
        tables.add(new TwoSeaterTable(8, TableType.OUTDOOR));
        tables.add(new FourSeaterTable(9, TableType.STANDARD));
        tables.add(new FourSeaterTable(10, TableType.STANDARD));
        tables.add(new FourSeaterTable(11, TableType.BOOTH));
        tables.add(new FourSeaterTable(12, TableType.BOOTH));
        tables.add(new FourSeaterTable(13, TableType.OUTDOOR));
        tables.add(new FourSeaterTable(14, TableType.OUTDOOR));
        tables.add(new FourSeaterTable(15, TableType.WINDOW));
        tables.add(new FourSeaterTable(16, TableType.STANDARD, 6));
        tables.add(new FourSeaterTable(17, TableType.STANDARD, 6));
        tables.add(new FourSeaterTable(18, TableType.OUTDOOR, 8));
        tables.add(new FourSeaterTable(19, TableType.BOOTH, 8));
        tables.add(new FourSeaterTable(20, TableType.STANDARD, 8));
    }

    

    //get a list of available tables that are not currently reserved
    public List<AbstractTable> getAvailableTables() {
        return tables.stream()
                .filter(table -> !activeReservations.containsKey(table.getTableNumber()))
                .toList();
    }

    //get the earliest reservation from the active reservations . Implemented Java Comparator
    public Reservation getEarliestReservation(){
        LocalDateTime currentTime = LocalDateTime.now(); 
    
        return activeReservations.values().stream()
            // Filter out any reservations where the time is before right now
            .filter(reservation -> !reservation.reservationTime().isBefore(currentTime))
            // Find the earliest one among the remaining future reservations
            .min(Comparator.comparing(Reservation::reservationTime))
            // Return null if there are no future reservations left
            .orElse(null);
    }

    //check if all tables are reserved, indicating that the restaurant is full . Implemented Java predicates
    public boolean isResturantFull() {
        return tables.stream()
                .allMatch(AbstractTable::isReserved);
    }

    //get the total number of active reservations currently in the system
    public long getTotalActiveReservations() {
        return activeReservations.size();
    }

    //get a list of unique customer names from the active reservations, eliminating duplicates
    public List<String> getUniqueCustomerNames(){
        return activeReservations.values().stream()
                .map(Reservation::customerName)
                .distinct()
                .toList();
    }

    //group active reservations by their associated table type, creating a mapping of table types to lists of reservations
    public Map<String, Long> getAvailableTableType(){
            return tables.stream()
                .filter(table -> !table.isReserved()) 
                .collect(Collectors.groupingBy(
                        table -> table.getTableType().toString(), // Use .toString() or just .getTableType() if it already returns a String
                        Collectors.counting()
                ));
        };    
        
    
    

    // Helper method to find a table by its number
    private AbstractTable findTable(int tableNo) {
        return tables.stream()
            .filter(t -> t.getTableNumber() == tableNo)
            .findFirst()
            .orElse(null);
    }

    //method to get a list of all reservations 
    public List<Reservation> getAllReservations() {
        // Implementation for retrieving all reservations
        return activeReservations.values().stream().toList();
    }

    //method to get all tables 
    public List<AbstractTable> getAllTables() {
        return tables;
    }

    @Override
    public void addReservation(int tableNO, Reservation reservation) throws TableNotAvailableException {
        // Implementation for adding a reservation to a specific table number
        AbstractTable table = findTable(tableNO);
        if (table == null) {
            throw new IllegalArgumentException("Table " + tableNO + " does not exist.");
        }
        if (activeReservations.containsKey(tableNO)) {
            throw new TableNotAvailableException("Table " + tableNO + " is already reserved.");
        }
        // Add the reservation to the active reservations map
        activeReservations.put(tableNO, reservation);
        // Mark the table as reserved in the model
        table.reserve();

        // Save to CSV immediately!
        fileService.saveReservations(activeReservations);
    }

    @Override
    public void cancelReservation(int tableNO) {
        // Implementation for canceling a reservation for a specific table number
        AbstractTable table = findTable(tableNO);
        if (table != null) {
            table.release();
        }
        activeReservations.remove(tableNO);

        // Save to CSV immediately!
        fileService.saveReservations(activeReservations);
    }

    @Override
    public Reservation getReservation(int tableNO) {
        // Implementation for retrieving a reservation for a specific table number
        return activeReservations.get(tableNO);
    }
       
}