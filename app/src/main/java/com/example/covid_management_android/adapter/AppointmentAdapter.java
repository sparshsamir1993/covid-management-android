package com.example.covid_management_android.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_management_android.R;
import com.example.covid_management_android.model.appointments.Appointment;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    Context context;
    List<Appointment> appointmentList;
    private AppointmentCardListener apCardListener;

    public AppointmentAdapter(Context context, List<Appointment> appointmentlist, AppointmentCardListener listener){
        this.context = context;
        this.appointmentList = appointmentlist;
        this.apCardListener = listener;
    }


    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_appointment, parent, false);
        AppointmentViewHolder apviewHolder = new AppointmentViewHolder(myView, apCardListener);
        return apviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment currApp = appointmentList.get(position);
        holder.hospitalNameText.setText(currApp.getHospital().getName());
        holder.appointmentStatusText.setText(currApp.getAppointmentStatus());
        holder.appointmentDTText.setText(currApp.getAppointmentTime() + "  "+ currApp.getAppointmentDate());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public interface AppointmentCardListener{
        void oncardClick(int position);
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder{
        TextView hospitalNameText, appointmentDTText, appointmentStatusText;
        CardView appointmentCard;

        public AppointmentViewHolder(View view, final AppointmentCardListener listener){
            super(view);

            hospitalNameText = view.findViewById(R.id.hospitalNameText);
            appointmentDTText = view.findViewById(R.id.appointmentDateTimeText);
            appointmentStatusText = view.findViewById(R.id.appointmentStatusText);
            appointmentCard = view.findViewById(R.id.appointmentCard);
            appointmentCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Appointment  is -- ", hospitalNameText.getText().toString());
                }
            });
        }

    }
}
