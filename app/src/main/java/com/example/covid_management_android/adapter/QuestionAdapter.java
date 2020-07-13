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
import com.example.covid_management_android.model.CovidQuestionResult;
import com.example.covid_management_android.model.QAnswerOption;
import com.example.covid_management_android.model.Question;
import com.example.covid_management_android.model.User;
import com.example.covid_management_android.model.UserAnswerResponse;
import com.example.covid_management_android.service.UserClient;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

   List<Question> myQuestions;
   Context context;
   HashMap<Integer,Integer> myResponses;
    //JSONObject myResponses;
   SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
   Button myResponseButton;
   RetrofitUtil retrofitUtil;
   Retrofit retrofit;
   UserClient userClient;

    public QuestionAdapter(List<Question> myQuestions,Context mycontext,SharedPreferences sharedPreferences,Button myResponseButton,SharedPreferences.Editor editor)
    {
        this.myQuestions = myQuestions;
        this.context = mycontext;
        this.myResponses = new HashMap<>();
        this.sharedPreferences = sharedPreferences;
        this.myResponseButton = myResponseButton;
        this.editor = editor;

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
                  Integer questionId = myQuestions.get(position).getId();
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
                  try {
                      myResponses.put(questionId,optionId);
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
                  Integer m = sharedPreferences.getInt("userId",1);
                 //Log.i("myUserId",m.toString());
                  //myResponses.put(questionId,optionId);
                  Log.i("MyResponses",myResponses.toString());

              }
          }
      });

        myResponseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/admin/");
                retrofit = retrofitUtil.getRetrofit();
                userClient = retrofit.create(UserClient.class);
                Toast.makeText(context,"Hello there",Toast.LENGTH_LONG).show();
                UserAnswerResponse myUserResponse = new UserAnswerResponse();
                Integer userId  = sharedPreferences.getInt("userId",1);
                myUserResponse.setUserId(userId);
                myUserResponse.setUserAnswers(myResponses);

                Call<CovidQuestionResult> call = userClient.sendReport(myUserResponse);

               call.enqueue(new Callback<CovidQuestionResult>() {
                   @Override
                   public void onResponse(Call<CovidQuestionResult> call, Response<CovidQuestionResult> response) {
                       Log.i("My Response",response.body().getResult());
                       Toast.makeText(context,"Survey Submitted",Toast.LENGTH_LONG).show();

                       // To Raise a flag ,indicating user has submitted the questionnaire...
                       editor.putInt("QuestionnaireSubmission",1);
                   }
                   @Override
                   public void onFailure(Call<CovidQuestionResult> call, Throwable t) {
                       Log.i("My Response",t.getMessage());
                   }
               });
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
