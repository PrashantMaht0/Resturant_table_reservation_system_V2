package view;

import java.awt.*;
import javax.swing.*;

public class EditReservationDialog extends JDialog {
    
    private service.ReservationManager manager;
    private int tableToEdit;
    private Runnable onSaveSuccess; // Added callback support here too!

    private JLabel headingLabel, tableNumberLabel, customerNameLabel, phoneNumberLabel, dateLabel, timeLabel;
    private JTextField tableNumberInput, customerNameInput, phoneNumberInput, dateInput, timeInput;
    private JButton saveButton, cancelButton;

    // Constructor 1: With UI refresh callback
    public EditReservationDialog(service.ReservationManager manager, int tableNo, Runnable onSaveSuccess) {
        this.manager = manager;
        this.tableToEdit = tableNo;
        this.onSaveSuccess = onSaveSuccess;
        initUI();
        loadExistingData();
    }

    // Constructor 2: Standard (No callback needed)
    public EditReservationDialog(service.ReservationManager manager, int tableNo) {
        this(manager, tableNo, null);
    }

    private void initUI() {
        setTitle("Edit Reservation");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- NORTH: Heading ---
        headingLabel = new JLabel("Edit Reservation", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Calisto MT", Font.BOLD, 22));
        mainPanel.add(headingLabel, BorderLayout.NORTH);

        // --- CENTER: Form Fields ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.weighty = 1.0;

        Font labelFont = new Font("Calisto MT", Font.BOLD, 14);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 12);

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        tableNumberLabel = new JLabel("Table Number:");
        tableNumberLabel.setFont(labelFont);
        formPanel.add(tableNumberLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        tableNumberInput = new JTextField(String.valueOf(tableToEdit));
        tableNumberInput.setFont(inputFont);
        tableNumberInput.setEditable(false); // Lock this field!
        tableNumberInput.setBackground(Color.LIGHT_GRAY);
        formPanel.add(tableNumberInput, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        customerNameLabel = new JLabel("Customer Name:");
        customerNameLabel.setFont(labelFont);
        formPanel.add(customerNameLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        customerNameInput = new JTextField();
        customerNameInput.setFont(inputFont);
        formPanel.add(customerNameInput, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setFont(labelFont);
        formPanel.add(phoneNumberLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.7;
        phoneNumberInput = new JTextField();
        phoneNumberInput.setFont(inputFont);
        formPanel.add(phoneNumberInput, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        dateLabel = new JLabel("Date:");
        dateLabel.setFont(labelFont);
        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.7;
        dateInput = new JTextField();
        dateInput.setFont(inputFont);
        formPanel.add(dateInput, gbc);

        // Row 4
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        timeLabel = new JLabel("Time:");
        timeLabel.setFont(labelFont);
        formPanel.add(timeLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.7;
        timeInput = new JTextField();
        timeInput.setFont(inputFont);
        formPanel.add(timeInput, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- SOUTH: Buttons ---
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0)); 
        buttonPanel.setPreferredSize(new Dimension(360, 60)); 
        
        saveButton = new JButton("Save Changes");
        saveButton.setBackground(new Color(153, 255, 153));
        saveButton.setFont(new Font("Calisto MT", Font.BOLD, 18));
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(this::saveChangesActionPerformed);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(255, 153, 153));
        cancelButton.setFont(new Font("Calisto MT", Font.BOLD, 24));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

    private void loadExistingData() {
        model.Reservation res = manager.getReservation(tableToEdit);
        if (res != null) {
            customerNameInput.setText(res.customerName());
            phoneNumberInput.setText(res.customerPhone());
            dateInput.setText(res.reservationTime().toLocalDate().toString());
            timeInput.setText(res.reservationTime().toLocalTime().toString());
        } else {
            JOptionPane.showMessageDialog(this, "No active reservation found for Table " + tableToEdit, "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }

    private void saveChangesActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String name = customerNameInput.getText().trim();
            String phone = phoneNumberInput.getText().trim();
            
            java.time.LocalDate date = java.time.LocalDate.parse(dateInput.getText().trim());
            java.time.LocalTime time = java.time.LocalTime.parse(timeInput.getText().trim());
            java.time.LocalDateTime newTime = java.time.LocalDateTime.of(date, time);
            
            // Validate that the new reservation time is not in the past
            if (newTime.isBefore(java.time.LocalDateTime.now())) {
                throw new IllegalArgumentException("You cannot book a table in the past!");
            }
            
            manager.cancelReservation(tableToEdit); // Clear the old one
            
            model.Reservation updatedRes = new model.Reservation(name, phone, newTime, tableToEdit);
            manager.addReservation(tableToEdit, updatedRes);
            
            JOptionPane.showMessageDialog(this, "Reservation updated successfully!");
            
            // Trigger UI refresh if a callback was passed
            if (onSaveSuccess != null) {
                onSaveSuccess.run();
            }
            
            this.dispose();
            
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Date/Time format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating reservation: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}