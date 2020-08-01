package com.example.covid_management_android.model.appointments;

public class Timeslot {
    private Long slot;

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
