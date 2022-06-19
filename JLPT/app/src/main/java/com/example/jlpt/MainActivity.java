package com.example.jlpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jlpt.model.Category;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textViewHightScore;
    private Spinner spinnerCategory;
    private Button buttonStartQuestion;

    private static final int REQUEST_CODE_QUESTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        AnhXa();
        //load chủ đề
        loadCategories();
        //click bắt đầu
        buttonStartQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuestion();
            }
        });
    }
    //hàm bắt đầu câu hỏi qua activity question
    private void startQuestion() {
        //lấy id,name chủ đề đã chọn
        Category category = (Category) spinnerCategory.getSelectedItem();
        int categoryID=category.getId();
        String categoryName=category.getName();

        //chuyển qua activity question
        Intent intent=new Intent(MainActivity.this,QuestionActivity.class);
        //gửi dữ liệu name,id
        intent.putExtra( "idcategories",categoryID );
        intent.putExtra( "catgoriesname",categoryName );
        // sử dụng startActivityForResult để có thể nhận lại dữ liệu kết quả và trả về thông tin phương thức onActivityResult()
        startActivityForResult(intent,REQUEST_CODE_QUESTION);


    }

    //phương thức ánh xạ id
    private void AnhXa(){
        textViewHightScore = findViewById(R.id.textview_high_score);
        buttonStartQuestion = findViewById(R.id.button_start_question);
        spinnerCategory = findViewById( R.id.spinner_category );
    }
    //load chủ đề
    private void loadCategories(){
        Database database=new Database( this );
        //lấy dữ liệu danh sách chủ đề
        List<Category> categories =database.getDataCategories();
        //tạo adapter
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>( this, android.R.layout.simple_spinner_item,categories);
        //bố cục hiển thị
        categoryArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        //gán chủ đề spinner hiển thị
        spinnerCategory.setAdapter( categoryArrayAdapter );
    }
}