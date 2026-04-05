package model;
// Final class representing a 2-seater table in the restaurant
public final class TwoSeaterTable extends AbstractTable implements Table {
    
    // Constant to represent the seating capacity of a two-seater table
    public final static int CAPACITY = 2;

    // Constructor to initialize the two-seater table with a table number and table type
    public TwoSeaterTable(int tableNumber, TableType tableType) {
        super(tableNumber, tableType);
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }
    
}
