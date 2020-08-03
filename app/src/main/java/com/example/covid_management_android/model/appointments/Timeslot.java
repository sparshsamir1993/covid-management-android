package com.example.covid_management_android.model.appointments;

public class Timeslot {
    private Long slot;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    private boolean isAvailable;

    public Timeslot() {

    }

    public Timeslot(Long slot) {
        this.slot = slot;
    }


    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public Long getSlot() {
        return slot;
    }

}
