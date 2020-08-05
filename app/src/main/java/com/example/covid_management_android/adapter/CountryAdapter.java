package com.example.covid_management_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.covid_management_android.R;
import com.example.covid_management_android.model.Country;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {


    List<Country> myCountries;
    Context context;
    OnCountryCardListener myCustomListener;

    public CountryAdapter(List<Country> data, Context context)
    {
        this.myCountries = data;
        this.context = context;
    }

    public interface OnCountryCardListener
    {
        void onCardClick(int position);
    }

    public void onCountryClick(OnCountryCardListener listener)
    {
        this.myCustomListener = listener;
    }



    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_country_list,parent,false);
        CountryAdapter.CountryViewHolder viewHolder = new CountryAdapter.CountryViewHolder(myview,myCustomListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        String countryname = myCountries.get(position).getCountry();
        String countryflag = myCountries.get(position).getCountryInfo().getFlag();
        holder.countryName.setText(countryname);
        Glide.with(context).load(countryflag).into(holder.countryFlag);


    }

    @Override
    public int getItemCount() {
        return myCountries.size();
    }

    public void filteredCountryList(List<Country> filteredList) {

        myCountries = filteredList;
        notifyDataSetChanged();


    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder {

       ImageView countryFlag;
       TextView countryName;


       public CountryViewHolder(@NonNull final View itemView, final OnCountryCardListener myCustomListener ) {
           super(itemView);
           countryFlag = itemView.findViewById(R.id.countryflag);
           countryName = itemView.findViewById(R.id.countryname);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   if(myCustomListener!=null) {
                       int position = getAdapterPosition();
                       myCustomListener.onCardClick(position);
                   }

               }
           });


       }
   }
}
