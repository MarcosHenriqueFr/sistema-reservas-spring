package com.example.bookingrestaurant.model;

public class Table {

    private int id;
    private String name;
    private int capacity;
    private TableStatus tableStatus = TableStatus.AVAILABLE;

    public Table(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TableStatus getStatus() {
        return tableStatus;
    }

    public void setStatus(TableStatus tableStatus) {
        this.tableStatus = tableStatus;
    }
}
