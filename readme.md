# **Restaurant Table Reservation System — The Spice India**
### Author : Prashant Mahto (A00336051)
### email : A00336051@student.tus.ie
### *OOPs 2 Assignment*

This project is refactored version of previous Restaurant Table Reservation System [OOPs1 Assignment](https://github.com/PrashantMaht0/Resturant_table_reservation_system). It demonstrates implementation of advance JAVA 25 concepts like Collections, Lambda, Records, Concurrency, NIO2,Flexible constructor bodies, instance main methods, Loaclisation and automatic background cleanup,etc. This Project follows a MVC desgin patterns and use Java Swing library for providing a simple GUI. 

### Project Features: 
- **GUI:** A simple and effective UI for managing table reservation in restaurant using Java Swing library. 
- **Local storage** : A local CSV file stores the reservation which can be viewed and access using the program. 
- **Automatic background clean up**: Using the Java ScheduledExecutorService a cleanup task is performed on Reservations. If Reservation is past 2 hours its deleted and resources are cleaned up automatically. 
- **Local Language Support**: A toogle langauge button switches the language from english to local language(Irish) 

### Project Structure
- Exception
    - TableNotAvailableException: Exception thrown when a table is not available for reservation
- Model - Data Entites
    - AbstractTable: Abstract class representing a table in the restaurant
    - Reservation: record representing a reservation made by a customer
    - Table: A sealead Interface repersenting a table in resturant
    - TwoSeaterTable & FourSeaterTable: Implementing Table inteface with custom seating capatiy of 2 , 4 or more person. 
    - TableType: Enum representing different type of tables available in restaurant including (BOOTH,WINDOW,OUTDOOR,STANDARD)
- Service - Business logic
    - ReservationManager: Provide Core business logic, declaring tables and helper methods for UI
- Util 
    - FileService: Providing service for handling File I/O operations like Loading  and Saving data from CSV file
- View - UI
    - Contains UI file built on Java Swing library providing a light weight and effective UI to program. 
- messages_en.properties & messages_ga.properties files: local language properties files providing local language swtich feature. 
- reservations.csv : A local Database storing Reservations. 

### Refactoring Notes:
- **Dynamic Datatypes**: Pervious version used a static arrays for tables now dynamic Java Collections (Map<Integer, Reservation>, List<AbstractTable>) for scalable, efficient O(1) lookups and flexible in-memory data management is used. 
- **Modern Java Streams**: Replaced traditional for-loops with the Java Stream API (.stream().filter().collect()) for cleaner, more declarative data querying
- **loacl DataStroge**: In pervious all the reservation were lost once the program was closed. Now a *reservation.csv* file is setup to store all the reservation. 
- **Background CleanUp**: Using Java Runnable interface a background cleanup process is setup to clear the reservations past 2 hours. This Runnable process triggers every 60 seconds for reservation and clean them up and reload the reseravtions table. 
- **UI Inconsistencies**: Reservations Table were using buttons in the action column to trigger the OptionPane for viewing table reservation. But now a mouseListener method is used to detect click event on action column and view/book OptionPane is triggered. 

### Run Program. 

**Note!! :** This project is built upon Java 25 LTS 
- Run: 
```
javac -d out view/TableReservationSystem.java
Copy-Item messages*.properties -Destination out\
java -cp out TableReservationSystem
```
