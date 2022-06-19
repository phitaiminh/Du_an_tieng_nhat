package com.example.jlpt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOnpenHelper;

public class Database extends SQLiteOpenHelper {
    //tên database
    private  static final String DATABASE_NAME="Question.db";
    //vertion
    private static final int VERTION=1;

    private SQLiteDatabase db;

    public Database(@Nullable Context context) {
        super( context, DATABASE_NAME, null, VERTION );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db=sqLiteDatabase;

        //biến bảng chuyên mục
        final String CATEGORIES_TABLE ="CREATE TABLE " +
                Table.CategoriesTable.TABLE_NAME+"("+
                Table.CategoriesTable._ID+"INTERGER PRIMARY KEY AUTOINCREMENT,"+
                Table.CategoriesTable.COLUMN_NAME+"TEXT"+")";
        //biến bảng question
        final String QUESTION_TABLE ="CREATE TABLE " +
                Table.QuestionsTable.TABLE_NAME+"("+
                Table.QuestionsTable.COLUMN_QUESTION+"TEXT,"+
                Table.QuestionsTable.COLUMN_OPTION1+"TEXT,"+
                Table.QuestionsTable.COLUMN_OPTION2+"TEXT,"+
                Table.QuestionsTable.COLUMN_OPTION3+"TEXT,"+
                Table.QuestionsTable.COLUMN_OPTION4+"TEXT,"+
                Table.QuestionsTable.COLUMN_ANSWER+"INTEGER,"+
                Table.QuestionsTable.COLUMN_CATEGORY_ID +"INTEGER,"+
                "FOREIGN KEY("+Table.QuestionsTable.COLUMN_CATEGORY_ID+") REFERENCES "+
                Table.QuestionsTable.TABLE_NAME+"("+Table.CategoriesTable._ID+")"+"ON DELETE CASCADE"+
                ")";
        //tạo bảng
        db.execSQL( CATEGORIES_TABLE );
        db.execSQL( QUESTION_TABLE );
        //insert dữ liệu
        CreatCategories();
        CreateQuestions();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL( "DROP TABLE IF EXISTS "+Table.CategoriesTable.COLUMN_NAME );
        db.execSQL( "DROP TABLE IF EXISTS " + Table.QuestionsTable.TABLE_NAME);
        onCreate( db );
    }
    //insert chủ đề vào cơ sở dữ liệu
    private void insertCategories(Category category){
        ContentValues values =new ContentValues();
        values.put(Table.CategoriesTable.COLUMN_NAME,category.getName());
        db.insert( Table.CategoriesTable.TABLE_NAME,nullColumnHack: null,values );
    }
    //giá trị insert
    private void CreatCategories() {
        //từ vựng id=1
        Category c1 = new Category( "Từ vựng" );
        insertCategories( c1 );
        //ngữ pháp id=2
        Category c2 = new Category( "Ngữ Pháp" );
        insertCategories( c2 );
    }
    //insert câu hỏi và đáp án vào csdl
    private void insertQuestion(Question question){
        ContentValues values =new ContentValues();

        values.put( Table.QuestionsTable.COLUMN_QUESTION,question.getQuestion() );
        values.put( Table.QuestionsTable.COLUMN_OPTION1,question.getOption1());
        values.put( Table.QuestionsTable.COLUMN_OPTION2,question.getOption2());
        values.put( Table.QuestionsTable.COLUMN_OPTION3,question.getOption3());
        values.put( Table.QuestionsTable.COLUMN_OPTION4,question.getOption4());
        values.put( Table.QuestionsTable.COLUMN_ANSWER,question.getAnswer());
        values.put( Table.QuestionsTable.COLUMN_CATEGORY_ID,question.getCategoryID());

        db.insert( Table.QuestionsTable.TABLE_NAME,nullColumnHack: null,values );
    }
    //tạo dữ liệu  bảng câu hỏi
    private  void CreateQuestions(){
        Question q1= new Question( "(　　　）、えいがをみにいきませんか？","A.　ゆうべ","B.きのう ","C.あした","D.おととい",3,2);
        insertQuestion( q1 );
        Question q2 = new Question("わたしはいつも（　　　）をききながらべんきょうします。"
                ,"A.　ぺん","B.ラジオ","C.テーブル","D.ストーブ",2,2);
        insertQuestion( q2 );
        Question q3 =new Question("わたしのすきなのみものは（　　　）です","A.おかし","B.こうちゃ","C.みかん","D.ねこ",2,2);
        insertQuestion( q3 );
        Question q4 =new Question("わたしはじてんしゃを（　　　）もっています。","A.にだい","B.にさつ","C.にほん","D.にまい",1,2);
        insertQuestion( q4 );
        Question q5 =new Question("この（　　　）じしょはだれのですか？","A.ほそい","B.まるい","C.みじかい","D.ちいさい",4,2);
        insertQuestion( q5 );
        Question q6 =new Question("ちちはことし80さいですが、（　　　）です。","A.げんき","B.けっこう","C.ざんねん","D.かんたん",1,2);
        insertQuestion( q6 );
        Question q7 =new Question("あめがふっています。みんなかさを（　　　）います","A.あけて","B.あげて","C.さして","D.つけて",3,2);
        insertQuestion( q7 );
        Question q8 =new Question("がくせいたちはきょうしつでやまだせんせいににほんごを（　　　）います。" ,"A.ならって","B.つくって","C.おぼえて","D.べんきょうして",1,2);
        insertQuestion( q8 );
        Question q9 =new Question("やまださんはあかいぼうしを（　　　）います。","A.きて","B.しめて","C.はいって","D.かぶって",4,2);
        insertQuestion( q9 );
        Question q10 =new Question("やまださんはたいていおふろにはいって、（　　　）ねます。","A.ちょうど","B.すぐに ","C.まだ","D.だんだん",2,2);
        insertQuestion( q10 );

    }
    //hàm trả về dữ liệu chủ đề
    @SuppressLint("Range")
    public List<Category> getDataCategories(){
        List<Category> categoryList=new ArrayList<>();
        db=getReadableDatabase();
        Cursor c=db.rawQuery( "SELECT * FROM " +Table.CategoriesTable.TABLE_NAME,selectionArgs: null );
        if(c.moveToFirst()){
            do{
                Category category =new Category();
                category.setId(c.getInt(c.getColumnIndex(Table.CategoriesTable._ID) ) );
                category.setName( c.getString( c.getColumnIndex( Table.CategoriesTable.COLUMN_NAME ) ) );
                categoryList.add( category );
            }while (c.moveToFirst());
        }
        c.close();
        return categoryList;
    }
    //lấy dữ liệu câu hỏi và đáp án có id= id_category theo chủ đề đã chọn
    @SuppressLint("Range")
    public ArrayList<Question> getQuestions(int catgoryID){
        ArrayList<Question> questionArrayList=new ArrayList<>();
        db=getReadableDatabase();
        String selection = Table.QuestionsTable.COLUMN_CATEGORY_ID+"= ?";
        String[] selectionArgs = new String[]{String.valueOf( catgoryID )};
        Cursor c= db.query( Table.QuestionsTable.TABLE_NAME,null,selection,selectionArgs,null,null,null);

        if(c.moveToFirst()){
            do{
                Question question=new Question();

                question.setId( c.getInt( c.getColumnIndex( Table.QuestionsTable._ID) ) );
                question.setQuestion( c.getString( c.getColumnIndex( Table.QuestionsTable.COLUMN_QUESTION) ) );
                question.setOption1( c.getString( c.getColumnIndex( Table.QuestionsTable.COLUMN_OPTION1) ) );
                question.setOption2( c.getString( c.getColumnIndex( Table.QuestionsTable.COLUMN_OPTION2) ) );
                question.setOption3( c.getString( c.getColumnIndex( Table.QuestionsTable.COLUMN_OPTION3) ) );
                question.setOption4( c.getString( c.getColumnIndex( Table.QuestionsTable.COLUMN_OPTION4) ) );
                question.setAnswer( c.getInt( c.getColumnIndex( Table.QuestionsTable.COLUMN_ANSWER) ) );
                question.setCategoryID( c.getInt( c.getColumnIndex( Table.QuestionsTable.COLUMN_CATEGORY_ID) ) );

                questionArrayList.add( question );
            }while (c.moveToFirst());
        }
        c.close();
        return questionArrayList;
    }
}
