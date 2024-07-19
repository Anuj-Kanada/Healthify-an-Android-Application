package com.example.firstaid.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ReportDB.db";
    private static final String REPORT_TABLE_NAME = "reportData";
    public static final String REPORT_COLUMN_ID = "id";
    private static final String REPORT_COLUMN_NAME = "name";
    private static final String COLUMN_REPORT_START = "start";
    private static final String COLUMN_REPORT_FINISH = "finish";
    private static final String COLUMN_REPORT_LOCATION = "location";

    private static final String QUESTION_TABLE_NAME = "questionData";
    public static final String QUESTION_COLUMN_ID = "id";
    private static final String QUESTION_COLUMN_REPORT_KEY = "reportkey";
    private static final String QUESTION_COLUMN_QUESTION = "question";
    private static final String QUESTION_COLUMN_ANSWER = "answer";
    private static final String QUESTION_REPORT_START = "qstart";
    private static final String QUESTION_REPORT_FINISH = "qfinish";

    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table reportData " +
                "(id INTEGER PRIMARY KEY autoincrement, name text, start text, finish text, location text)"
        );
        db.execSQL("create table questionData " +
                "(id INTEGER PRIMARY KEY autoincrement, reportkey text, question text, answer text, qstart text, qfinish text)"
        );
        dummyData(db);
    }

    public void dummyData(SQLiteDatabase db) {
        insertLocation(db,"Test","12:55", "13:55", "Hythe, Kent");
        insertLocation(db,"Test2", "11:55", "13:55","Folkestone, Kent");
        insertLocation(db,"Test3", "11:55", "13:55","Folkestone, Kent");
        insertLocation(db,"Test4", "11:55", "13:55","Folkestone, Kent");

        insertQuestion(db,"Test","Safe?", "Yes", "12:55", "12:56");
        insertQuestion(db,"Test","Nahe?", "Yes", "12:55", "12:56");
        insertQuestion(db,"Test2","NoSafe?", "Yes", "12:55", "12:56");
        insertQuestion(db,"Test2","NoSafe?", "Yes", "12:55", "12:56");
        insertQuestion(db,"Test2","NoSafe?", "Yes", "12:55", "12:56");
        insertQuestion(db,"Test3","Bafe?", "No", "12:55", "12:56");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS reportData");
        db.execSQL("DROP TABLE IF EXISTS questionData");
        onCreate(db);
    }

    public void insertLocation(SQLiteDatabase db, String name, String start, String finish, String location){
        //SQLiteDatabase db = this.getWritableDatabase();

        ContentValues reportValues = new ContentValues();
        reportValues.put("name", name);
        reportValues.put("start", start);
        reportValues.put("finish", finish);
        reportValues.put("location", location);
        db.insert("reportData", null, reportValues);
    }

    public void insertQuestion(SQLiteDatabase db, String reportkey, String question, String answer, String start, String finish){
        //SQLiteDatabase db = this.getWritableDatabase();

        ContentValues questionValues = new ContentValues();
        questionValues.put(QUESTION_COLUMN_REPORT_KEY, reportkey);
        questionValues.put("question", question);
        questionValues.put("answer", answer);
        questionValues.put("qstart", start);
        questionValues.put("qfinish", finish);
        db.insert("questionData", null, questionValues);
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from reportData where id="+id+"", null );
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, REPORT_TABLE_NAME);
    }

    public int numberOfQRows(String id){
        String countQuery = "SELECT  * FROM " + QUESTION_TABLE_NAME + " WHERE reportkey ='"+id+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ REPORT_TABLE_NAME);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name ='" + REPORT_TABLE_NAME + "'");
        db.execSQL("delete from "+ QUESTION_TABLE_NAME);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name ='" + QUESTION_TABLE_NAME + "'");
        dummyData(db);
        db.close();
    }

    public ArrayList<String> getSavedReports() {
        return getStrings(REPORT_COLUMN_NAME);
    }

    public ArrayList<String> getStart() {
        return getStrings(COLUMN_REPORT_START);
    }

    public ArrayList<String> getFinish() {
        return getStrings(COLUMN_REPORT_FINISH);
    }

    public ArrayList<String> getLocation() {
        return getStrings(COLUMN_REPORT_LOCATION);
    }

    public ArrayList<String> getQuestion(String id) {
        return getQStrings(QUESTION_COLUMN_QUESTION, id);
    }

    public ArrayList<String> getAnswer(String id) {
        return getQStrings(QUESTION_COLUMN_ANSWER, id);
    }

    public ArrayList<String> getQStart(String id) {
        return getQStrings(QUESTION_REPORT_START, id);
    }

    public ArrayList<String> getQFinish(String id) {
        return getQStrings(QUESTION_REPORT_FINISH, id);
    }

    private ArrayList<String> getStrings(String columnName) {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from reportData", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(columnName)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    private ArrayList<String> getQStrings(String columnName, String id) {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from questionData where reportkey ='"+id+"'", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(columnName)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public Cursor getDataQuery(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }
}