package util;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import model.Reservation;

// Service class for handling file operations related to reservations
public class FileService {
    private static final String reservationFilePath = "reservations.csv";

    public Map<Integer, Reservation> loadReservations() {
        // Implementation for loading reservations from a file
        Path filepath = Paths.get(reservationFilePath);

        if (!Files.exists(filepath)) {
            return new ConcurrentHashMap<>(); 
        }

        try(Stream<String> res = Files.lines(filepath)) {
            return res
                    .skip(1) // Skip header line
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length == 4)
                    .collect(Collectors.toMap(
                            parts -> Integer.parseInt(parts[3].trim()), // Table number as key  
                            parts -> new Reservation(
                                    parts[0].trim(), // Customer name
                                    parts[1].trim(), // Customer phone
                                    java.time.LocalDateTime.parse(parts[2].trim()), // Reservation time
                                    Integer.parseInt(parts[3].trim()) // Table number
                            )
                        ));
        } catch (Exception e) {
            System.out.println("Error occurred while loading reservations." + e.getMessage()); 
        }
        return new ConcurrentHashMap<>(); // Return empty map if file doesn't exist
    }

    public void saveReservations(Map<Integer, Reservation> reservations) {
        // Implementation for saving reservations to a file
        Path filepath = Paths.get(reservationFilePath);

        //check if file exists if not create it
        if(!Files.exists(filepath)) {
            try {
                Files.createFile(filepath);
            } catch (Exception e) {
                System.out.println("Error occurred while creating reservation file." + e.getMessage());
                return;
            }
        }
        // Convert reservations to CSV format and write to file
        try{
        Stream<String> dataStream = reservations.values().stream()
                .map(reservation -> String.format("%s,%s,%s,%d",
                        reservation.customerName(),
                        reservation.customerPhone(),
                        reservation.reservationTime(),
                        reservation.tableNumber()));
        List<String> alllines = Stream.concat(
            Stream.of("CustomerName,Phone,Time,TableNumber"),dataStream).toList();
        
        Files.write(filepath, alllines,StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);    

        }catch(Exception e){
            System.out.println("Error occurred while saving reservations." + e.getMessage());
        }               
    } 
}
