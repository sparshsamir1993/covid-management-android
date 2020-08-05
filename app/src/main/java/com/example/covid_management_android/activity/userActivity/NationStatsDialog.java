package com.example.covid_management_android.activity.userActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.covid_management_android.R;
import com.example.covid_management_android.model.Country;


public class NationStatsDialog extends AppCompatDialogFragment {

    private TextView cases,recoverCases;
    private TextView caseCount,recoveryCount;
    private Country countryData;


   public NationStatsDialog(Country countryData)
   {
       this.countryData = countryData;
   }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_country_dialog,null);

        cases =  view.findViewById(R.id.cases);
        caseCount= view.findViewById(R.id.casescount);
        recoverCases = view.findViewById(R.id.recovered);
        recoveryCount = view.findViewById(R.id.recovercount);


        caseCount.setText(countryData.getCases());
        recoveryCount.setText(countryData.getTodayRecovered());

        alertDialog.setView(view)
                    .setTitle("Covid Statistics")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return alertDialog.create();
    }
}
