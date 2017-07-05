package kr.anima.xd.s.sqlitedatabasetest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText ed_name, ed_score;
    TextView txt_result;

    SQLiteDatabase db;
    String tableName="Rank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_name= (EditText) findViewById(R.id.ed_name);
        ed_score= (EditText) findViewById(R.id.ed_score);
        txt_result= (TextView) findViewById(R.id.txt_result);

        //data.db 파일명의 Database 파일 생성 또는 열기
        // 같은 이름의 파일이 있을 때 생성하지 않고 그 파일을 열게 됨
        db=openOrCreateDatabase("data.db", MODE_PRIVATE, null);
        // rank 라는 이름의 table 생성 == create
        // 띄어쓰기 구분 필수
        db.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+"("
                            +"id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            +"name TEXT, "
                            +"score INTEGER)");


    } // onCreate


    public void clickInsert(View v){
        String name=ed_name.getText().toString();
        int score=0;
        try{
            // 만약 사용자 입력값이 없을 때, 에러날 수 있음
            // 그래서 스코어 기본값을 넣어두고 try-catch 로 에러 처리
            score=Integer.parseInt(ed_score.getText().toString());
        } catch (Exception e){

        }
        ed_name.setText("");
        ed_score.setText("");
        // 작은 따옴표 필수!!!!! 주의 사항!!!!
        db.execSQL("INSERT INTO "+tableName+"(name, score) VALUES ('"+name+"', "+score+")");

    }

    public void clickSelectAll(View v){
        // db.execSQL("SELECT * FROM "+tableName); //실행만 하는 명령어
        Cursor cursor=db.rawQuery("SELECT * FROM "+tableName, null); // null == where
        if(cursor==null || cursor.getCount()==0) return;
        // cursor 번호는 row
        // column 번호는 말 그대로 세로열
        // txt_result.setText(cursor.getCount()+""); // row == cursor 개수

        StringBuffer buffer=new StringBuffer();

        // 커서가 맨 처음 불려지면 그 위치값은 -1
        // 그러므로 제대로 된 값을 가져오려면 우선 줄 단위(row)로 넘겨서 커서의 위치를 옮겨야함
        while(cursor.moveToNext()){ //다음줄 없을 때까지 반복

            // row 0번째 줄
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String name=cursor.getString(cursor.getColumnIndex("name"));
            int score=cursor.getInt(cursor.getColumnIndex("score"));

            buffer.append("ID : "+id+" NAME : "+name+" SCORE : "+score+"\n");

        } //while

        txt_result.setText(buffer.toString());
    }

    public void clickSelectByName(View v){
        String name=ed_name.getText().toString();
        ed_name.setText("");
        Cursor cursor=db.rawQuery("SELECT * FROM "+tableName+" WHERE name=? and score>?", new String[]{name, 10+""});

        if(cursor==null || cursor.getCount()==0) return;

        StringBuffer buffer=new StringBuffer();
        while (cursor.moveToNext()){
            String name1=cursor.getString(cursor.getColumnIndex("name"));
            int score=cursor.getInt(cursor.getColumnIndex("score"));

            buffer.append("NAME : "+name1+" SCORE : "+score+"\n");
        }

        txt_result.setText(buffer.toString());
    }

    public void clickUpdate(View v){
        String name=ed_name.getText().toString();
        int score=0;
        try{
            // 만약 사용자 입력값이 없을 때, 에러날 수 있음
            // 그래서 스코어 기본값을 넣어두고 try-catch 로 에러 처리
            score=Integer.parseInt(ed_score.getText().toString());
        } catch (Exception e){        }
        ed_name.setText("");
        ed_score.setText("");
        // String 은 무조건 ? 밖에 안됨. 문자열이 단 1개뿐이라도 배열로 써야함
        db.execSQL("UPDATE "+tableName+" SET score="+score+" WHERE name=?", new String[]{name});

    }

    // delete 는 테이블의 내용을 지우는 것 == insert 의 반대
    // 테이블 자체를 지우는 건 DropDown == Create 의 반대
    public void clickDeleteAll(View v){
        db.execSQL("DELETE FROM "+tableName);
    }

    public void clickDeleteName(View v){
        String name=ed_name.getText().toString();
        db.execSQL("DELETE FROM "+tableName+" WHERE name=?", new String[]{name});
    }

} // class Main
