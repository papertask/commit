package com.interview.iso.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.interview.iso.models.Answer;
import com.interview.iso.models.Person;
import com.interview.iso.models.Question;

public class DBHelper extends SQLiteOpenHelper {
    
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "commit";
    // Log
    private static final String LOG = "DBcommit";
    
    // Table Names
    private static final String TABLE_PERSON = "person";
    private static final String TABLE_QUESTION = "question";
    private static final String TABLE_INTERVIEW_RESULT = "interview_result";
    
    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_ADD = "address";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_GENDER = "gender";
    
    private static final String KEY_RESULT = "result";
    private static final String KEY_INTERVIEW_DATE = "interview_date";
    // RESULT Table - column names
    private static final String KEY_PERSON_ID = "person_id";
    private static final String KEY_QUESTION_RESULT = "question_result";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_INTERVIEW_TYPE = "interview_type";
    ///////Statement Create Table
    private static final String CREATE_TABLE_PERSON = "CREATE TABLE "
    + TABLE_PERSON + "(" + KEY_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT," + KEY_FIRSTNAME
    + " TEXT," + KEY_LASTNAME + " TEXT," + KEY_AVATAR + " TEXT," + KEY_BIRTHDAY + " TEXT," + KEY_INTERVIEW_DATE
    + " TEXT," + KEY_INTERVIEW_TYPE
    + " INTEGER," + KEY_ADD + " TEXT," + KEY_PHONE + " TEXT," + KEY_GENDER
    + " TEXT" + ")";
    
    
    private static final String CREATE_TABLE_RESULT = "CREATE TABLE "
    + TABLE_INTERVIEW_RESULT + "(" + KEY_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT," + KEY_PERSON_ID
    + " INTEGER," + KEY_QUESTION_RESULT + " TEXT )";
    
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Table ", CREATE_TABLE_PERSON);
        db.execSQL(CREATE_TABLE_PERSON);
        db.execSQL(CREATE_TABLE_RESULT);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERVIEW_RESULT);
        onCreate(db);
        
    }
    
    public void DeleteTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        onCreate(db);
    }
    
    /* Creating Person 	 */
    public long createPerson(Person ans) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, ans.getFirstName());
        values.put(KEY_LASTNAME, ans.getLastName());
        values.put(KEY_BIRTHDAY, ans.getBirthDay());
        values.put(KEY_INTERVIEW_DATE, ans.getTime());
        values.put(KEY_ADD, ans.getAdd());
        values.put(KEY_GENDER, ans.getGender());
        values.put(KEY_AVATAR, ans.getAvatarPath());
        values.put(KEY_INTERVIEW_TYPE, AppData.getInstance().getApptype());
        long ans_id = 0;
        long id = 0;

        ans_id = db.insert(TABLE_PERSON, null, values);
        db.close();
        return ans_id;
    }
    
    public Person getPerson(long ans_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PERSON + " WHERE "
        + KEY_ID + " = " + ans_id;
        
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        
        Person td = new Person();
        td.setID(c.getInt(c.getColumnIndex(KEY_ID)));
        td.setFirstName((c.getString(c.getColumnIndex(KEY_FIRSTNAME))));
        td.setLastName((c.getString(c.getColumnIndex(KEY_LASTNAME))));
        td.setAdd((c.getString(c.getColumnIndex(KEY_ADD))));
        td.setGender((c.getString(c.getColumnIndex(KEY_GENDER))));
        td.setAvatar((c.getString(c.getColumnIndex(KEY_AVATAR))));
        td.setInterview_type((c.getInt(c.getColumnIndex(KEY_INTERVIEW_TYPE))));
        td.setTime(c.getString(c.getColumnIndex(KEY_INTERVIEW_DATE)));
        c.close();
        db.close();
        
        return td;
    }
    
    private long CheckExistPerson(Person person) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  ID FROM " + TABLE_PERSON + " WHERE "
        + KEY_FIRSTNAME + " = '" + person.getFirstName() + "' and "+  KEY_LASTNAME + " = '" + person.getLastName() + "' and "+ KEY_BIRTHDAY +" = '" + person.getBirthDay() + "'" ;
        
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                long id = c.getInt(c.getColumnIndex(KEY_ID));
                c.close();
                return id;
            }
        } else {
            c.close();
            
            return 0L;
        }
        return 0L;
    }
    
    public List<Person> getAllPerson() {
        List<Person> lstAns = new ArrayList<Person>();
        String selectQuery = "SELECT  * FROM " + TABLE_PERSON;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Person td = new Person();
                td.setID(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setFirstName(c.getString(c.getColumnIndex(KEY_FIRSTNAME)));
                td.setLastName(c.getString(c.getColumnIndex(KEY_LASTNAME)));
                td.setAdd(c.getString(c.getColumnIndex(KEY_ADD)));
                td.setGender(c.getString(c.getColumnIndex(KEY_GENDER)));
                td.setAvatar(c.getString(c.getColumnIndex(KEY_AVATAR)));
                td.setInterview_type(c.getInt(c.getColumnIndex(KEY_INTERVIEW_TYPE)));
                td.setTime(c.getString(c.getColumnIndex(KEY_INTERVIEW_DATE)));
                lstAns.add(td);
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return lstAns;
    }
    
    public List<Person> searchPerson(String term) {
        List<Person> lstAns = new ArrayList<Person>();
        String selectQuery = "SELECT  * FROM " + TABLE_PERSON + " WHERE " + KEY_FIRSTNAME + " LIKE '%"
        + term + "%' OR " + KEY_LASTNAME + " LIKE '%" + term + "%' OR " + KEY_INTERVIEW_DATE + " LIKE '%" + term + "%' OR "
        + KEY_ADD + " LIKE '%" + term + "%'";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Person td = new Person();
                td.setID(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setFirstName((c.getString(c.getColumnIndex(KEY_FIRSTNAME))));
                td.setLastName((c.getString(c.getColumnIndex(KEY_LASTNAME))));

                // td.setPhone((c.getString(c.getColumnIndex(KEY_PHONE))));
                td.setAdd((c.getString(c.getColumnIndex(KEY_ADD))));
                // td.setOld((c.getString(c.getColumnIndex(KEY_OLD))));
                td.setGender((c.getString(c.getColumnIndex(KEY_GENDER))));
                td.setAvatar((c.getString(c.getColumnIndex(KEY_AVATAR))));
                lstAns.add(td);
            } while (c.moveToNext());
        }
        return lstAns;
    }
    
    
    /* Updating a Person */
    public int updatePerson(Person ans) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, ans.getFirstName());
        values.put(KEY_LASTNAME, ans.getLastName());
        values.put(KEY_ADD, ans.getAdd());
        values.put(KEY_BIRTHDAY, ans.getBirthDay());
        values.put(KEY_INTERVIEW_DATE, ans.getTime());
        values.put(KEY_GENDER, ans.getGender());
        values.put(KEY_AVATAR, ans.getAvatarPath());
        return db.update(TABLE_PERSON, values, KEY_ID + " = ?",
                         new String[]{String.valueOf(ans.getID())});
    }
    
    public void deletePerson(long ans_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PERSON, KEY_ID + " = ?",
                  new String[]{String.valueOf(ans_id)});
    }
    
    /*
     * Deleting a Question
     */
    public void deleteQuestion(long ans_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTION, KEY_ID + " = ?",
                  new String[]{String.valueOf(ans_id)});
    }
    
    public void createAnswer(long person_id, String json_result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PERSON_ID, person_id);
        values.put(KEY_QUESTION_RESULT,json_result);
        db.insert(TABLE_INTERVIEW_RESULT, null, values);
        db.close();
    }
    
    public Answer getListQuestionByPersion(long person_id) {
        List<Question> lstAns = new ArrayList<Question>();
        String selectQuery = "SELECT  * FROM " + TABLE_INTERVIEW_RESULT + " WHERE " + KEY_PERSON_ID + " = " + person_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        Answer answer = null;
        if (c.moveToFirst()) {
            HashMap<Question, Integer> result = new HashMap<>();
            do {
                answer = new Answer(c.getInt(c.getColumnIndex(KEY_PERSON_ID)),c.getString(c.getColumnIndex(KEY_QUESTION_RESULT)));
                return answer;
            } while (c.moveToNext());
            //            answer = new Answer(person, result);
        }
        return answer;
        
    }
    
}