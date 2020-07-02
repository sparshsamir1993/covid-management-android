package com.example.covid_management_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_management_android.R;
import com.example.covid_management_android.model.QuestionOption;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    List<QuestionOption> myQuestions;
    public QuestionAdapter(List<QuestionOption> myQuestions)
    {
        this.myQuestions = myQuestions;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_question,parent,false);
       QuestionViewHolder myviewholder = new QuestionViewHolder(myview);
       return  myviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {

        String question;
        String option1;
        String option2;

        question = myQuestions.get(position).getQuestion().getQuestion();
        option1 = myQuestions.get(position).getOption1();
        option2 = myQuestions.get(position).getOption2();
        holder.myquestion.setText(question);
        holder.option1.setText(option1);
        holder.option2.setText(option2);

    }

    @Override
    public int getItemCount() {
        return myQuestions.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        public TextView myquestion;
        public RadioButton option1;
        public RadioButton option2;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            myquestion = itemView.findViewById(R.id.questionId);
            option1 = itemView.findViewById(R.id.option1);
            option2 = itemView.findViewById(R.id.option2);
        }

    }

}
