package view;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class AddReservationDialog extends JDialog {
    
    private service.ReservationManager manager;

    private JLabel headingLabel, tableNumberLabel, customerNameLabel, phoneNumberLabel, dateLabel, timeLabel;
    private JTextField tableNumberInput, customerNameInput, phoneNumberInput, dateInput, timeInput;
    private JButton saveButton, cancelButton;
    private Runnable onSaveSuccess;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


    // Constructor 1: Used when you NEED the UI to refresh after saving (e.g., ViewTablesGUI)
    public AddReservationDialog(service.ReservationManager manager, Runnable onSaveSuccess) {
        this.manager = manager;
        this.onSaveSuccess = onSaveSuccess;
        initUI();
    }

    // Constructor 2: Used when you DON'T need the UI to refresh (e.g., MainScreenGUI)
    public AddReservationDialog(service.ReservationManager manager) {
        this(manager, null); // Calls the other constructor and passes null for the runnable
    }

    // All the UI code lives here now to keep things clean
    private void initUI() {
        setTitle("Add Reservation");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setResizable(false);
        
        // Use BorderLayout for the main structure
        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- NORTH: Heading ---
        headingLabel = new JLabel("Add Reservation", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Calisto MT", Font.BOLD, 22));
        mainPanel.add(headingLabel, BorderLayout.NORTH);

        // --- CENTER: Form Fields (Using GridBagLayout for alignment) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5); // Padding around text fields
        gbc.weighty = 1.0;

        Font labelFont = new Font("Calisto MT", Font.BOLD, 14);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 12);

        // Row 0: Table Number
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        tableNumberLabel = new JLabel("Table Number:");
        tableNumberLabel.setFont(labelFont);
        formPanel.add(tableNumberLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        tableNumberInput = new JTextField("Enter Table Number");
        tableNumberInput.setFont(inputFont);
        formPanel.add(tableNumberInput, gbc);

        // Row 1: Customer Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        customerNameLabel = new JLabel("Customer Name:");
        customerNameLabel.setFont(labelFont);
        formPanel.add(customerNameLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        customerNameInput = new JTextField("Enter Customer Name");
        customerNameInput.setFont(inputFont);
        formPanel.add(customerNameInput, gbc);

        // Row 2: Phone Number
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setFont(labelFont);
        formPanel.add(phoneNumberLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.7;
        phoneNumberInput = new JTextField("Enter Customer Phone no");
        phoneNumberInput.setFont(inputFont);
        formPanel.add(phoneNumberInput, gbc);

        // Row 3: Date
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        dateLabel = new JLabel("Date: (YYYY-MM-DD)");
        dateLabel.setFont(labelFont);
        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.7;
        dateInput = new JTextField();
        dateInput.setFont(inputFont);
        LocalDate today = LocalDate.now();
        dateInput.setText(today.format(DATE_FORMATTER));
        formPanel.add(dateInput, gbc);

        // Row 4: Time
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        timeLabel = new JLabel("Time: (HH:MM)");
        timeLabel.setFont(labelFont);
        formPanel.add(timeLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.7;
        timeInput = new JTextField();
        LocalTime now = LocalTime.now();
        timeInput.setText(now.format(TIME_FORMATTER));
        timeInput.setFont(inputFont);
        formPanel.add(timeInput, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- SOUTH: Buttons ---
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0)); // 1 row, 2 cols, 20px gap
        buttonPanel.setPreferredSize(new Dimension(360, 60)); // Makes the buttons tall and chunky
        
        saveButton = new JButton("Save");
        saveButton.setBackground(new Color(153, 255, 153));
        saveButton.setFont(new Font("Calisto MT", Font.BOLD, 24));
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(this::saveButtonActionPerformed);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(255, 153, 153));
        cancelButton.setFont(new Font("Calisto MT", Font.BOLD, 24));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add everything to the frame
        setContentPane(mainPanel);
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int tableNo = Integer.parseInt(tableNumberInput.getText().trim());
            String name = customerNameInput.getText().trim();
            String phone = phoneNumberInput.getText().trim();
            
            java.time.LocalDate date = java.time.LocalDate.parse(dateInput.getText().trim());
            java.time.LocalTime time = java.time.LocalTime.parse(timeInput.getText().trim());
            java.time.LocalDateTime reservationTime = java.time.LocalDateTime.of(date, time);
            
            // Validate that the reservation time is not in the past
            if (reservationTime.isBefore(java.time.LocalDateTime.now())) {
                throw new IllegalArgumentException("You cannot book a table in the past!");
            }
            
            model.Reservation newReservation = new model.Reservation(name, phone, reservationTime, tableNo);
            
            manager.addReservation(tableNo, newReservation);
            
            JOptionPane.showMessageDialog(this, "Reservation successfully added!");
            
            // Trigger the refresh back in the main window!
            if (onSaveSuccess != null) {
                onSaveSuccess.run(); 
            }
            this.dispose(); 
            
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Date/Time format. Please use YYYY-MM-DD and HH:MM 24h format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (exception.TableNotAvailableException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Booking Error", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Table Number must be a valid whole number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Table", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}