package com.example.covid_management_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_management_android.R;
import com.example.covid_management_android.activity.userActivity.HospitalList;
import com.example.covid_management_android.model.HospitalData;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {

    List<HospitalData> myHopitalList;
    OnHospitalCardListener myCustomListener;

    public interface  OnHospitalCardListener {
        void oncardClick(int position);
    }

    public void onHospitalClick(OnHospitalCardListener listener) {
        this.myCustomListener = listener;
    }

    public HospitalAdapter(List<HospitalData> hData) {
        this.myHopitalList = hData;
    }


    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_hospital_list, parent, false);
        HospitalAdapter.HospitalViewHolder myviewholder = new HospitalAdapter.HospitalViewHolder(myview, myCustomListener);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {

        HospitalData data = myHopitalList.get(position);
        holder.hospitalName.setText(data.getName());
        holder.hospitalContact.setText(data.getContact());
        holder.hospitalAddress.setText(data.getDetailedAddress());
    }

    @Override
    public int getItemCount() {
        return myHopitalList.size();
    }


    public static class HospitalViewHolder extends RecyclerView.ViewHolder {

        TextView hospitalName;
        TextView hospitalContact;
        TextView hospitalAddress;

        public HospitalViewHolder(View itemView, final OnHospitalCardListener listener) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.hospitalname);
            hospitalContact = itemView.findViewById(R.id.hospitalcontact);
            hospitalAddress = itemView.findViewById(R.id.hospitalAddress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        listener.oncardClick(position);
                    }

                }
            });
        }

    }
}
