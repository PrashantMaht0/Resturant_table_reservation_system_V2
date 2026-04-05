package model;
// Final class representing a 4-seater table in the restaurant
public final class FourSeaterTable extends AbstractTable implements Table {
    
    public final static int CAPACITY = 4;
    private final int customCapacity;

    // Default 4-seater constructor
    public FourSeaterTable(int tableNumber, TableType tableType) {
        this(tableNumber, tableType, CAPACITY);
    }

    // Custom capacity constructor using Flexible Constructor Bodies (JEP 513)
    public FourSeaterTable(int tableNumber, TableType tableType, int customCapacity) {
        
        //  Validate the capacity BEFORE calling super()
        if (customCapacity <= 0) {
            throw new IllegalArgumentException("Custom capacity must be greater than 0");
        }
        if (customCapacity % 2 != 0) {
            throw new IllegalArgumentException("Custom capacity must be even");
        }
        
        super(tableNumber, tableType);
        
        this.customCapacity = customCapacity;
    }

    @Override
    public int getCapacity() {
        return customCapacity;
    }
}