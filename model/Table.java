package model;
// Interface representing a table in the restaurant
public sealed interface Table permits TwoSeaterTable, FourSeaterTable {
    
}
