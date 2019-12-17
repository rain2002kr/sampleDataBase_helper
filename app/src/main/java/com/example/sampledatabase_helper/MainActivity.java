package com.example.sampledatabase_helper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button button, button2,button3,button4,button5,button6;
    EditText editText,editText2,editText3,editText4,editText5,editText6;
    TextView textView;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        userfindId();
        userfuction();

    }
    private void  userfindId(){
        button = (Button) findViewById( R.id.button );
        button2 = (Button) findViewById( R.id.button2 );
        button3 = (Button) findViewById( R.id.button3 );
        button4 = (Button) findViewById( R.id.button4 );
        /*
        button5 = (Button) findViewById( R.id.button5 );
        button6 = (Button) findViewById( R.id.button6 );
        */
        editText = (EditText) findViewById( R.id.editText );
        editText2 = (EditText) findViewById( R.id.editText2 );
        editText3 = (EditText) findViewById( R.id.editText3 );
        editText4 = (EditText) findViewById( R.id.editText4 );
        textView = (TextView) findViewById( R.id.textView );
    }
    private void  userfuction(){
        //open or create database
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String databaseName = editText.getText().toString().trim();
                createDatabase( databaseName );
            }
        } );
        //create table
        button2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableName = editText2.getText().toString().trim();
                createTable( tableName );
            }
        } );
        //insert data info
        button3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableName = editText2.getText().toString().trim();
                String name = editText3.getText().toString().trim();
                String age = editText4.getText().toString().trim();
                insertInfo( tableName, name, age );
            }
        } );
        //select data
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tableName = editText2.getText().toString().trim();
                executeQuery(tableName);
            }
        });
    }
    public void createDatabase(String name){
        //println( "createDatabase 호출 됨" );
        /*
        database = openOrCreateDatabase( name, MODE_PRIVATE, null );
        */
        String tableName = editText2.getText().toString().trim();
        DatabaseHelper helper = new DatabaseHelper(this, name , null, 6, tableName );
        database = helper.getWritableDatabase();

        println( "database 생성함 " + name );
    }
    public void createTable(String name){
        println( "create Table 호출 됨" );
        if (database != null){
            String sql ="create table if not exists " + name +"(_id integer PRIMARY KEY autoincrement, name text, age text)";
            try {
                database.execSQL( sql );
                println( "table 생성됨" + name );
            } catch (SQLException e){
                println(" 에러 발생됨 "+ e.getMessage());
            }
        } else {
            println( "database db를 생성 하시오" );
            return ;
        }
    }
    public void insertInfo(String tableName, String name, String age){
        if(database != null) {
            String sql = "insert into " + tableName + "(name, age) " + "values(?,?)";
            Object[] params = {name, age};
            database.execSQL( sql, params );
            println( "데이터 추가함."+ name +" : " + age  );
        } else {
            println( "먼저 데이터베이스를 오픈하세요" );
        }
    }
    public void executeQuery(String tableName){
        println("executeQuery 호출 됨");

        String sql = "select _id, name, age from " + tableName;
        Cursor cursor = database.rawQuery(sql, null);

        println("레코드 개수" + cursor.getCount());

        for(int i=0; i < cursor.getCount(); i++){
            cursor.moveToNext();
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String age = cursor.getString(2);
            println("레코드#" + i + "id : "+ id +" : " + name + " , " + age);
        }
        cursor.close();
    }

    public void println(String data){
        textView.append( data + "\n" );
    }

    class DatabaseHelper extends SQLiteOpenHelper {
        String tableName;
        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version,String tableName) {
            super(context, name, factory, version);
            this.tableName = tableName;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            println( "onCreate 호출 됨" );
            String sql ="create table if not exists " + tableName +"(_id integer PRIMARY KEY autoincrement, name text, age text)";
            db.execSQL( sql );
            println( "table 생성됨" );
        }
        public void onOpen(SQLiteDatabase db){
            println("onOpen 호출됨");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("onUpgrade 호출됨." + oldVersion + "," + newVersion);

            if (newVersion > 1){
                db.execSQL("drop table if exists " + tableName);
                println("table 삭제 함");

                String sql ="create table if not exists " + tableName +"(_id integer PRIMARY KEY autoincrement, name text, age text)";
                db.execSQL(sql);
                println( "table 새로 생성됨" + tableName );
            }
        }
    }
}



