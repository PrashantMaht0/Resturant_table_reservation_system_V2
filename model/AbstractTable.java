package model;
// Abstract class representing a table in the restaurant
public abstract class AbstractTable  {
    final int tableNumber;
    final TableType tableType;
    boolean isReserved;

    public AbstractTable(int tableNumber, TableType tableType) {
        if (tableNumber <= 0) {
            throw new IllegalArgumentException("Table number must be greater than 0");
        }
        if (tableType == null) {
            throw new IllegalArgumentException("Table type cannot be null");
        }
        this.tableNumber = tableNumber;
        this.tableType = tableType;
        this.isReserved = false;
    }
    // Abstract method to get the seating capacity of the table, to be implemented by subclasses
    public abstract int getCapacity();

    // Getters for the table properties
    public int getTableNumber() {
        return tableNumber;
    }

    public TableType getTableType() {
        return tableType;
    }

    public boolean isReserved() {
        return isReserved;
    }
    public void release() {
        if (!isReserved) {
            throw new IllegalStateException("Table is not reserved");
        }
        isReserved = false;
    }
    public void reserve() {
        if (isReserved) {
            throw new IllegalStateException("Table is already reserved");
        }
        isReserved = true;
    }
    
}
