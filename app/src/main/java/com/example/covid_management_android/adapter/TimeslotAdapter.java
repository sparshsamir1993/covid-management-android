package com.example.covid_management_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_management_android.R;
import com.example.covid_management_android.model.appointments.Timeslot;

import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;

public class TimeslotAdapter extends RecyclerView.Adapter<TimeslotAdapter.SlotViewHolder> {

    Context context;
    List<Timeslot> timeslotList;


    public TimeslotAdapter(Context context, List<Timeslot> timeslotList){
        this.context = context;
        this.timeslotList = timeslotList;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_slot, parent, false);
        SlotViewHolder slotViewHolder = new SlotViewHolder(myView);
        return slotViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class SlotViewHolder extends RecyclerView.ViewHolder {

        public TextView slotText, slotAvailableText;


        public SlotViewHolder(View itemView) {

            super(itemView);
            slotText = itemView.findViewById(R.id.slotText);
            slotAvailableText = itemView.findViewById(R.id.slotAvailableText);

        }

    }
}


