package com.example.covid_management_android.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_management_android.R;
import com.example.covid_management_android.activity.userActivity.QuestionActivity;
import com.example.covid_management_android.model.QAnswerOption;
import com.example.covid_management_android.model.Question;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

   List<Question> myQuestions;
   Context context;
   HashMap<Integer,Integer> myResponses;
   SharedPreferences sharedPreferences;
   Button myResponseButton;

    public QuestionAdapter(List<Question> myQuestions,Context mycontext,SharedPreferences sharedPreferences,Button myResponseButton)
    {
        this.myQuestions = myQuestions;
        this.context = mycontext;
        this.myResponses = new HashMap<>();
        this.sharedPreferences = sharedPreferences;
        this.myResponseButton = myResponseButton;

    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_question,parent,false);
       QuestionViewHolder myviewholder = new QuestionViewHolder(myview);
       return  myviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionViewHolder holder, final int position) {


        holder.myquestion.setText(myQuestions.get(position).getQuestion());
        final int count = myQuestions.get(position).getQAnswerOptions().size();

        for(int i=0;i<count;i++) {
            RadioButton r1 = new RadioButton(context);
            r1.setText(myQuestions.get(position).getQAnswerOptions().get(i).getOptionContent());
            holder.mylayout.removeAllViews();
            holder.myoptions.addView(r1);
            holder.mylayout.addView(holder.myoptions);
        }
        holder.myoptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup group, int checkedId) {

              Integer n = checkedId;
              Log.i("MycheckedId" ,n.toString());
              RadioButton rb = group.findViewById(checkedId);
              Integer optionId = 0;
              if(rb.isChecked())
              {
                  int questionId = myQuestions.get(position).getId();
                  String mySelectedoption = rb.getText().toString();
                  for (Question option : myQuestions)
                  {

                    for(QAnswerOption myoption:option.getQAnswerOptions())
                    {
                       if(myoption.getOptionContent().contains(mySelectedoption))
                       {
                          optionId = myoption.getId();

                       }
                    }
                  }
                  Log.i("My radio button",rb.getText().toString()+myQuestions.get(position).getQuestion()+" " + optionId.toString());
                  myResponses.put(questionId,optionId);

                  setAllData();
                  Integer m = sharedPreferences.getInt("userId",1);
                 //Log.i("myUserId",m.toString());

                  //myResponses.put(questionId,optionId);
                  Log.i("MyResponses",myResponses.toString());

              }
          }
      });


    }

    private void setAllData() {
        myResponseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,myResponses.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myQuestions.size();

    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        public TextView myquestion;
        public RadioGroup myoptions;
        public RadioButton option1;
        public LinearLayout mylayout;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            myquestion = itemView.findViewById(R.id.questionId);
            myoptions = itemView.findViewById(R.id.myradiooptionsgroup);
            mylayout = itemView.findViewById(R.id.myoptionslayout);

        }

    }

}
