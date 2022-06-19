package com.example.jlpt;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jlpt.model.Question;

import java.util.ArrayList;
import java.util.Locale;

public class QuestionActivity extends AppCompatActivity {
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView texViewQuestionsCount;
    private TextView textViewCategory;
    private TextView textViewCountDown;

    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;

    private CountDownTimer countDownTimer;
    private ArrayList<Question> questionList;
    private long timeLeftMillis;
    private  int questionCounter;
    private int questionSize;

    private int Score;
    private boolean answered;
    private Question currentQuestion;

    private  int count =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_question );

        anhxa();

        //nhan du lieu chu de
        Intent intent = getIntent();
        int categoryID=intent.getIntExtra("idcategories",0);
        String categoryName =intent.getStringExtra("catgoriesname");
        //hiển thị chủ đề
        textViewCategory.setText("chủ đề :"+categoryName);

        Database database= new Database(this);
        //danh sách list chứa câu hỏi
        questionList=database.getQuestions(categoryID);
        //lấy kích cỡ danh sách = tổng số câu hỏi
        questionSize = questionList.size();
        // đảo vị trí các phần tử câu hỏi
        Colections.shuffle(questionList);

        //Show câu hỏi và đáp án
        showNextQuestion();
        //button xác nhận câu tiếp ,  hoàn thành
        buttonConfirmNext.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                if(!answered){
                    //nếu chọn 1 trong 4đáp án
                    if(rb1.isChecked()||rb2.isChecked()||rb3.isChecked()||rb4.isChecked()){
                        //kiểm tra đáp
                        checkAnsewr();
                    }else{
                        Toast.makeText(QuestionActivity.this,"Hãy chọn đáp án", Toast.LENGTH_SHORT).show();
                    }

                }
                //nếu trả lời ,,thực hiện chuyển câu hỏi
                else{
                    showNextQuestion();
                }
            }
        });

    }
    //hiển thị câu hỏi
    private void showNextQuestion(){
        //set lại màu đen cho đáp án
        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);
        //Xóa chọn
        rbGroup.clearCheck();
        //nếu còn câu hỏi
        if(questionCounter<questionSize){
            //lấy dữ liệu ở vị trí counter
            currentQuestion = questionList.get(questionCounter);
            //hiển thị câu hỏi
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());
            //tăng sau mỗi câu hỏi
            questionCounter++;
            //set vị trí câu hỏi đã hiện tại
            texViewQuestionsCount.setText("Câu hỏi :"+questionCounter+"/"+questionSize);
            //giá trị false chưa trả lời,đang show
            answered=false;
            //gán tên cho button
            buttonConfirmNext.setText("Xác nhận");
            //thời gian chạy 30s
            timeLeftMillis =30000;
            //đếm ngược thời gian trả lời
            startCountDown();


        }
        else{
            finishQuestion();
        }
    }
    //phương thức  thời gian đếm ngược
    private void startCountDown(){
        countDownTimer = new CountDownTimer(timeLeftMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = 1;
                //update time
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                //hết giờ
                timeLeftMillis=0;
                updateCountDownText();
                //check đáp án
                checkAnsewr();
            }
        }.start();


    }
    //kiểm tra đáp án
    private void checkAnsewr(){

        answered = true;
        //trả về radiobutton trong fbGroup được check
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        //vị trí của câu đã chọn
        int answer = rbGroup.indexOfChild(rbSelected)+1;

        if(answer== currentQuestion.getAnswer()){
            Score= Score+10;

            textViewScore.setText("Điểm: "+Score);


        }
        //Phương thức hiển thị đáp án
        showSolution();

    }

    private void showSolution(){
        //set màu radioButton đáp án
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);
        //kiểm tra đáp án set màu và hiển thị đáp án nên màn hình
        switch ((currentQuestion.getAnswer())){
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Đáp án là A");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Đáp án là B");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Đáp án là C");
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                textViewQuestion.setText("Đáp án là D");
                break;
        }

        if(questionCounter<questionSize){
            buttonConfirmNext.setText("Câu tiếp");
        }else {
            buttonConfirmNext.setText("Hoàn thành");
        }

        //dừng thời gian lại
        countDownTimer.cancel();

    }

    //update thời gian
    private void updateCountDownText(){
        //tính phút
        int minutes = (int)((timeLeftMillis/1000)/60);
        //tính giây
        int seconds = (int)((timeLeftMillis/1000)%60);
        //định dạng kiểu thời gian
        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        //hiển thị lên màn hình
        textViewCountDown.setText(timeFormatted);
        //nếu thời gian dưới 10s thì chuyển đỏ
        if(timeLeftMillis <10000){
            textViewCountDown.setTextColor(Color.RED);
        }
        else{
            textViewCountDown.setTextColor(Color.BLACK);
        }
    }

    //thoát qua giao diện chính
    private void finishQuestion(){
        //chứa dữ liệu gửi qua activity main
        Intent resultIntent = new Intent();
        resultIntent.putExtra ("score",Score);
        setResult(RESULT_OK,resultIntent);
        finish();
    }
    //back
    public void onBackPressed(){
        count++;
        if(count>=1){
            finishQuestion();
        }
        count=0;
    }

    //anh xa id
    private void anhxa(){
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        texViewQuestionsCount = findViewById(R.id.text_view_question_count);
        textViewCategory = findViewById(R.id.text_view_category);

        textViewCountDown= findViewById(R.id.text_view_countdown);
        rbGroup =findViewById(R.id.radio_group);
        rb1=findViewById(R.id.radio_button1);
        rb2=findViewById(R.id.radio_button2);
        rb3=findViewById(R.id.radio_button3);
        rb4=findViewById(R.id.radio_button4);

        buttonConfirmNext = findViewById(R.id.button_confim_next);

    }
}
