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
import static com.example.covid_management_android.constants.Constants.SLOT_AVAILABLE;
import static com.example.covid_management_android.constants.Constants.SLOT_NOT_AVAILABLE;

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
        Timeslot currentSlot = timeslotList.get(position);
        Long time = currentSlot.getSlot();
        String timeText = "";
        if(time > 12){
            timeText  = (time - 12) + "pm";
        }else if(time == 12){
            timeText = "12pm";
        }else{
            timeText = time+"am";
        }
        holder.slotText.setText(timeText);
        if(currentSlot.isAvailable()){
            holder.slotAvailableText.setText(SLOT_AVAILABLE);
        }else{
            holder.slotAvailableText.setText(SLOT_NOT_AVAILABLE);
        }
    }

    @Override
    public int getItemCount() {
        return timeslotList.size();
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


