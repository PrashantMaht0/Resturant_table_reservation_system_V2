package view;
import javax.swing.JFrame;

public class MainScreenGUI extends JFrame {

    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton ToggleLangButton;
    private javax.swing.JLabel studentInfo;
    private java.awt.Label subTitleName;
    private java.awt.Label titleName;
    private javax.swing.JButton viewButton;
    private service.ReservationManager manager;
    private java.util.ResourceBundle messages;
    private java.util.Locale currentLocale;

    public MainScreenGUI(service.ReservationManager manager) {
        this.manager = manager;
        super("Home Screen");
        currentLocale = java.util.Locale.ENGLISH;
        messages = java.util.ResourceBundle.getBundle("messages", currentLocale);

        titleName = new java.awt.Label();
        subTitleName = new java.awt.Label();
        viewButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        studentInfo = new javax.swing.JLabel();
        ToggleLangButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Table Reservation System");
        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(204, 204, 204));
        setSize(new java.awt.Dimension(1000, 700));

        titleName.setAlignment(java.awt.Label.CENTER);
        titleName.setFont(new java.awt.Font("Calisto MT", 1, 58)); // NOI18N
        titleName.setText("The Spice India ");

        subTitleName.setAlignment(java.awt.Label.CENTER);
        subTitleName.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        subTitleName.setFont(new java.awt.Font("Calisto MT", 1, 24)); // NOI18N
        subTitleName.setText("Table Reservation System");

        viewButton.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        viewButton.setText("View Reservations ");
        viewButton.addActionListener(e -> {
            ViewTablesGUI viewTablesGUI = new ViewTablesGUI(this.manager);
            viewTablesGUI.setVisible(true);
        });

        addButton.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        addButton.setText("Add Reservations");
        addButton.addActionListener(e -> {
            AddReservationDialog addDialog = new AddReservationDialog(this.manager);
            addDialog.setLocationRelativeTo(this);
            addDialog.setVisible(true);
        });

        editButton.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        editButton.setText("Edit Reservations");
        editButton.addActionListener(e -> {
            // Ask for the table number first
            String input = javax.swing.JOptionPane.showInputDialog(this, "Enter Table Number to Edit:");
            
            if (input != null && !input.trim().isEmpty()) {
                try {
                    int tableNo = Integer.parseInt(input.trim());
                    
                    // Check if the reservation exists BEFORE opening the window
                    if (manager.getReservation(tableNo) == null) {
                        javax.swing.JOptionPane.showMessageDialog(this, 
                            "No active reservation found for Table " + tableNo, 
                            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    
                    // If it exists, open the edit dialog with the correct table number
                    EditReservationDialog editDialog = new EditReservationDialog(this.manager, tableNo);
                    editDialog.setLocationRelativeTo(this);
                    editDialog.setVisible(true);
                    
                } catch (NumberFormatException ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Please enter a valid number.");
                }
            }
        });

        deleteButton.setFont(new java.awt.Font("Calisto MT", 1, 14)); // NOI18N
        deleteButton.setText("Delete Reservations");
        deleteButton.addActionListener(e -> {
            String input = javax.swing.JOptionPane.showInputDialog(this, "Enter Table Number to Delete/Complete:");
            
            if (input != null && !input.trim().isEmpty()) {
                try {
                    int tableNo = Integer.parseInt(input.trim());
                    
                    // Check if it exists before trying to delete
                    if (manager.getReservation(tableNo) == null) {
                        javax.swing.JOptionPane.showMessageDialog(this, 
                            "No active reservation found on Table " + tableNo, 
                            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Clear it
                    manager.cancelReservation(tableNo);
                    javax.swing.JOptionPane.showMessageDialog(this, "Table " + tableNo + " has been successfully cleared.");
                    
                } catch (NumberFormatException ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Please enter a valid table number.");
                }
            }
        });

        studentInfo.setBackground(new java.awt.Color(255, 255, 255));
        studentInfo.setFont(new java.awt.Font("Calisto MT", 3, 12)); // NOI18N
        studentInfo.setForeground(new java.awt.Color(255, 102, 51));
        studentInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        studentInfo.setText("Student Name : Prashant Mahto (A00336051)");

        ToggleLangButton.setText("Toggle Language");
        ToggleLangButton.addActionListener(e -> {
            // Swap the locale
            if (currentLocale.equals(java.util.Locale.ENGLISH)) {
                currentLocale = new java.util.Locale("ga", "IE"); // Switch to Irish
            } else {
                currentLocale = java.util.Locale.ENGLISH; // Switch to English
            }

            // Reload the bundle
            messages = java.util.ResourceBundle.getBundle("messages", currentLocale);

            // Update all the text on the screen dynamically
            setTitle(messages.getString("app.title"));
            titleName.setText(messages.getString("app.title"));
            viewButton.setText(messages.getString("menu.view"));
            addButton.setText(messages.getString("menu.add"));
            editButton.setText(messages.getString("menu.edit"));
            deleteButton.setText(messages.getString("menu.remove"));
            ToggleLangButton.setText(messages.getString("btn.switch_lang"));
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(studentInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ToggleLangButton)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(173, 173, 173)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(titleName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(viewButton, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                            .addComponent(editButton,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                            .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(154, 154, 154))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subTitleName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(titleName, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subTitleName, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                .addGap(127, 127, 127)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(viewButton, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                .addGap(119, 119, 119)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(studentInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(ToggleLangButton)))
        );

    }
    
}
