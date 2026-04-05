package view;

import javax.swing.SwingUtilities;

public class TableReservationSystem {
    void main() {
    // Instantiate the backend once
    service.ReservationManager manager = new service.ReservationManager();
    // Launch the UI, passing the backend into it
    SwingUtilities.invokeLater(() -> {
            new MainScreenGUI(manager).setVisible(true);
        });
}
}
