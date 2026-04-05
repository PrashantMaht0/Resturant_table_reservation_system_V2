package view;

import javax.swing.JFrame;



public class ViewTablesGUI extends JFrame{

    private javax.swing.JLabel Heading;
    private javax.swing.JLabel activeBookingLabel;
    private javax.swing.JLabel availableTableTypesLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nextArrivalLabel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTable reservationTable;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel subHeading;
    private service.ReservationManager manager;

    public ViewTablesGUI(service.ReservationManager manager) {
        this.manager = manager;
        super("View Reservations");

        Heading = new javax.swing.JLabel();
        subHeading = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        reservationTable = new javax.swing.JTable();
        activeBookingLabel = new javax.swing.JLabel();
        nextArrivalLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        availableTableTypesLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("View Reservations");
        setSize(new java.awt.Dimension(1000, 700));
        refreshData();

        Heading.setFont(new java.awt.Font("Calisto MT", 1, 24)); // NOI18N
        Heading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Heading.setText("The Spice India");
        Heading.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        subHeading.setFont(new java.awt.Font("Calisto MT", 1, 18)); // NOI18N
        subHeading.setText("Reservations");

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(e -> refreshData());

        reservationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Table No", "Table Type", "Capacity", "Status", "Action"
            }
        ));

        // Listen for clicks on the table
        reservationTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Get the exact row and column the user clicked
                int row = reservationTable.rowAtPoint(e.getPoint());
                int col = reservationTable.columnAtPoint(e.getPoint());

                // Column 4 is the "Action" column
                if (col == 4 && row >= 0) {
                    int tableNo = (int) reservationTable.getValueAt(row, 0);
                    // Grab the Action text ("Complete" or "Book") from Column 4
                    String actionText = (String) reservationTable.getValueAt(row, 4);

                    if ("Complete".equals(actionText)) {
                        model.Reservation res = manager.getReservation(tableNo);
                        if (res != null){
                            // Show reservation details and ask for confirmation to mark as completed
                            String details = "Reservation Details for Table " + tableNo + ":\n\n" +
                                            "Customer Name: " + res.customerName() + "\n" +
                                            "Phone Number: " + res.customerPhone() + "\n" +
                                            "Date: " + res.reservationTime().toLocalDate() + "\n" +
                                            "Time: " + res.reservationTime().toLocalTime() + "\n\n" +
                                            "Would you like to mark this table as completed/cleared?";
                        

                            Object[] options = {"Mark as Completed", "Close"};
                            int choice = javax.swing.JOptionPane.showOptionDialog(ViewTablesGUI.this, 
                                details, 
                                "View Reservation", 
                                javax.swing.JOptionPane.YES_NO_OPTION, 
                                javax.swing.JOptionPane.INFORMATION_MESSAGE, 
                                null, 
                                options, 
                                options[1]);
                            
                            if (choice == javax.swing.JOptionPane.YES_OPTION) {
                                manager.cancelReservation(tableNo);
                                refreshData(); 
                            }
                        }else {
                            javax.swing.JOptionPane.showMessageDialog(ViewTablesGUI.this, 
                            "No active reservation details found for Table " + tableNo, 
                            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                    } else if ("Book".equals(actionText)) {
                        // --- BOOK THE TABLE ---
                        AddReservationDialog addDialog = new AddReservationDialog(manager ,() -> refreshData());
                        addDialog.setLocationRelativeTo(ViewTablesGUI.this);
                        addDialog.setVisible(true);
                    }
                }
            }
        });

        jScrollPane1.setViewportView(reservationTable);

        activeBookingLabel.setText("Active Bookings");

        nextArrivalLabel.setText("Next Arrival ");

        statusLabel.setText("Status ");

        availableTableTypesLabel.setText("Available ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subHeading, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(refreshButton, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(activeBookingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(nextArrivalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(availableTableTypesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Heading, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Heading, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subHeading, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refreshButton, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                    .addComponent(activeBookingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nextArrivalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                    .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                    .addComponent(availableTableTypesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }
	
    //method to refresh the data displayed in the GUI, including the reservation table and summary labels
    private void refreshData() {
        
        long activeCount = manager.getTotalActiveReservations();
        activeBookingLabel.setText("Active Bookings: " + activeCount);

        model.Reservation next = manager.getEarliestReservation();
        String arrivalText = (next != null) ? next.reservationTime().toLocalTime().toString() : "None";
        nextArrivalLabel.setText("Next Arrival: " + arrivalText);

        String statusText = manager.isResturantFull() ? "AT FULL CAPACITY" : "ACCEPTING WALK-INS";
        statusLabel.setText("Status: " + statusText);

        long availableCount = manager.getAvailableTables().size();
        String availableTableType = manager.getAvailableTableType().entrySet().stream()
                .filter(entry -> !entry.getValue().equals(0L))
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .reduce((a, b) -> a + " | " + b)
                .orElse("No Reservations");
        availableTableTypesLabel.setText("Available: " + availableTableType + " | ( "+ availableCount + ")");

        
        
        javax.swing.table.DefaultTableModel model = 
            (javax.swing.table.DefaultTableModel) reservationTable.getModel();
        
        // Wipe out the empty default rows
        model.setRowCount(0); 

        // Loop through backend tables and add them to the UI
        manager.getAllTables().forEach(table -> {
            
            // Pattern Matching for switch!
            int capacity = switch(table) {
                case model.TwoSeaterTable t -> t.getCapacity();
                case model.FourSeaterTable f -> f.getCapacity();
                default -> 0;
            };
            
            String status = table.isReserved() ? "RESERVED" : "NOT RESERVED";
            String actionText = table.isReserved() ? "Complete" : "Book";
            
            model.addRow(new Object[]{
                table.getTableNumber(), 
                table.getTableType(), 
                capacity, 
                status, 
                actionText
            });
        });
    }
}
