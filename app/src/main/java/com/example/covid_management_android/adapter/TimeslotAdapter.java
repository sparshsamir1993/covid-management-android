package com.example.covid_management_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_management_android.R;
import com.example.covid_management_android.model.appointments.Timeslot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;

import static com.example.covid_management_android.constants.Constants.SLOT_AVAILABLE;
import static com.example.covid_management_android.constants.Constants.SLOT_NOT_AVAILABLE;

public class TimeslotAdapter extends RecyclerView.Adapter<TimeslotAdapter.SlotViewHolder> {

    private static final SparseBooleanArray selectedSlotArray = new SparseBooleanArray();
    static List<CardView> slotCardList;
    final int[] index = {-1};
    Context context;
    List<Timeslot> timeslotList;
    TimeslotAdapter.TimeSlotCardListener myCustomListener;
    private TimeSlotCardListener cardSlotListener;

    public TimeslotAdapter(Context context, List<Timeslot> timeslotList, TimeSlotCardListener listener) {
        this.context = context;
        this.timeslotList = timeslotList;
        slotCardList = new ArrayList<>();
        this.cardSlotListener = listener;
    }

    public void onSlotClick(TimeslotAdapter.TimeSlotCardListener listener) {
        this.myCustomListener = listener;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_slot, parent, false);
        SlotViewHolder slotViewHolder = null;

        slotViewHolder = new SlotViewHolder(myView, cardSlotListener);

        return slotViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final SlotViewHolder holder, int position) {

        Timeslot currentSlot = timeslotList.get(position);

        Long time = currentSlot.getSlot();
        String timeText = "";
        if (time > 12) {
            timeText = (time - 12) + "pm";
        } else if (time == 12) {
            timeText = "12pm";
        } else {
            timeText = time + "am";
        }
        holder.slotText.setText(timeText);
        if (!currentSlot.isAvailable()) {
            holder.currentCard.setVisibility(View.GONE);
        }
        slotCardList.add(holder.currentCard);
        if (index[0] == position) {
            holder.currentCard.setCardBackgroundColor(Color.parseColor("#bdfff4"));
        } else {
            holder.currentCard.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.currentCard.setSelected(index[0] == position);

    }

    @Override
    public int getItemCount() {
        return timeslotList.size();
    }

    public interface TimeSlotCardListener {
        void oncardClick(int position, CardView currentCard, List<CardView> cardList);
    }

    public static class SlotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView slotText;
        CardView currentCard;
        TimeSlotCardListener slotListener;


        public SlotViewHolder(final View itemView, final TimeSlotCardListener listener) {
            super(itemView);
            slotText = itemView.findViewById(R.id.slotText);
            currentCard = itemView.findViewById(R.id.time_slot_card);

            currentCard.setCardBackgroundColor(Color.parseColor("#ffffff"));
            slotListener = listener;
            currentCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (slotListener != null) {
                        slotListener.oncardClick(getAdapterPosition(), currentCard, slotCardList);

                    }
                }
            });

        }

        @Override
        public void onClick(View view) {

        }
    }
}


