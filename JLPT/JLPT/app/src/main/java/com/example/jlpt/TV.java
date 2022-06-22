package com.example.jlpt;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TV extends AppCompatActivity implements View.OnClickListener {
    private TextView tvQuestion;
    private TextView tvContentQuestion;
    private TextView tvAnswer1,tvAnswer2,tvAnswer3,tvAnswer4;

    private List<Question> mListQuestion;
    private Question mQuestion;
    private int currentQuestion =0;
    private int diem =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuvung_main);

        iniUi();

        mListQuestion=getListQuestion();
        if ((mListQuestion.isEmpty())){
            return;
        }
        setDataQuestion(mListQuestion.get(currentQuestion));
    }

    private void setDataQuestion(Question question) {
        if(question== null){
            return;
        }
        mQuestion=question;

        String titleQuestion = "Question"+question.getNumber();
        tvQuestion.setText(titleQuestion);
        tvContentQuestion.setText(question.getContent());
        tvAnswer1.setText(question.getListAnswer().get(0).getContent());
        tvAnswer2.setText(question.getListAnswer().get(1).getContent());
        tvAnswer3.setText(question.getListAnswer().get(2).getContent());
        tvAnswer4.setText(question.getListAnswer().get(3).getContent());

        tvAnswer1.setOnClickListener(this);
        tvAnswer2.setOnClickListener(this);
        tvAnswer3.setOnClickListener(this);
        tvAnswer4.setOnClickListener(this);
    }

    private void iniUi(){
        tvQuestion=findViewById(R.id.tv_question1);
        tvContentQuestion=findViewById(R.id.tv_content_question1);

        tvAnswer1=findViewById(R.id.tv_answer11);
        tvAnswer2=findViewById(R.id.tv_answer21);
        tvAnswer3=findViewById(R.id.tv_answer31);
        tvAnswer4=findViewById(R.id.tv_answer41);
    }
    private List<Question> getListQuestion(){
        List<Question> list= new ArrayList<>();

        List<Answer> answersList1= new ArrayList<>();
        answersList1.add(new Answer("ゆうべ",false));
        answersList1.add(new Answer("きのう",false));
        answersList1.add(new Answer("あした",true));
        answersList1.add(new Answer("おととい",false));

        List<Answer> answersList2= new ArrayList<>();
        answersList2.add(new Answer("ぺん",false));
        answersList2.add(new Answer("ラジオ",true));
        answersList2.add(new Answer("テーブル",false));
        answersList2.add(new Answer("ストーブ",false));

        list.add(new Question("（　　　）、えいがをみにいきませんか？",1,answersList1));
        list.add(new Question("わたしはいつも（　　　）をききながらべんきょうします",2,answersList2));



        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_answer11:
                tvAnswer1.setBackgroundResource(R.drawable.bg_orange_conner_30);
                checkAnswer(tvAnswer1,mQuestion,mQuestion.getListAnswer().get(0));
                break;
            case R.id.tv_answer21:
                tvAnswer2.setBackgroundResource(R.drawable.bg_orange_conner_30);
                checkAnswer(tvAnswer2,mQuestion,mQuestion.getListAnswer().get(1));
                break;
            case R.id.tv_answer31:
                tvAnswer3.setBackgroundResource(R.drawable.bg_orange_conner_30);
                checkAnswer(tvAnswer3,mQuestion,mQuestion.getListAnswer().get(2));
                break;
            case R.id.tv_answer41:
                tvAnswer4.setBackgroundResource(R.drawable.bg_orange_conner_30);
                checkAnswer(tvAnswer4,mQuestion,mQuestion.getListAnswer().get(3));
                break;

        }
    }
    private void checkAnswer(TextView textView,Question question,Answer answer){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(answer.isCorrect()){
                    diem++;
                    textView.setBackgroundResource(R.drawable.bg_blue_conner_30);
                    nextQuestion();
                }else{
                    textView.setBackgroundResource(R.drawable.bg_blue_conner_30);
                    nextQuestion();
                }
            }
        },1000);
    }

    private void nextQuestion() {
        if(currentQuestion==mListQuestion.size()-1){
            showDialog("Diem:"+diem);
        }else{
            currentQuestion++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setDataQuestion(mListQuestion.get(currentQuestion));
                }
            },1000);
        }

    }
    private void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentQuestion =0;
                setDataQuestion(mListQuestion.get(currentQuestion));
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }
}
