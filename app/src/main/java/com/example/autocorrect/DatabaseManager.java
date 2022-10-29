package com.example.autocorrect;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class DatabaseManager extends SQLiteOpenHelper {
  private static final String DATABASE_CORRECT = "wordDB";
  private static final int DATABASE_VERSION = 1;
  private static final String TABLE_CORRECT = "correct";
  private static final String ID = "id";
  private static final String MISSPELLED = "Misspelled";
  private static final String CORRECT = "Correct";
	
  public DatabaseManager( Context context ) {
    super( context, DATABASE_CORRECT, null, DATABASE_VERSION );
  }
 
  public void onCreate( SQLiteDatabase db ) {
    // build sql create statement
    String sqlCreate = "create table " + TABLE_CORRECT + "( " + ID;
    sqlCreate += " integer primary key autoincrement, " + MISSPELLED;
    sqlCreate += " text, " + CORRECT + " text )" ;

    db.execSQL( sqlCreate );

    String sqlInsert = "insert into " + TABLE_CORRECT;
    sqlInsert += " values( null, '" + "teh";
    sqlInsert += "', '" + "the" + "' )";

    db.execSQL( sqlInsert );

    sqlInsert = "insert into " + TABLE_CORRECT;
    sqlInsert += " values( null, '" + "helloo";
    sqlInsert += "', '" + "hello" + "' )";

    db.execSQL( sqlInsert );

    sqlInsert = "insert into " + TABLE_CORRECT;
    sqlInsert += " values( null, '" + "ther";
    sqlInsert += "', '" + "there" + "' )";

    db.execSQL( sqlInsert );
  }
 
  public void onUpgrade( SQLiteDatabase db,
                         int oldVersion, int newVersion ) {
    // Drop old table if it exists
    db.execSQL( "drop table if exists " + TABLE_CORRECT );
    // Re-create tables
    onCreate( db );
  }
    
  public void insert( Correct corrected ) {
    Log.v("here", "in insert");

    SQLiteDatabase db = this.getWritableDatabase( );
    String sqlInsert = "insert into " + TABLE_CORRECT;
    sqlInsert += " values( null, '" + Correct.getMisspelled( );
    sqlInsert += "', '" + Correct.getCorrect( ) + "' )";
 
    db.execSQL( sqlInsert );
    db.close( );
  }
   
  public void deleteById( int id ) {
    SQLiteDatabase db = this.getWritableDatabase( );
    String sqlDelete = "delete from " + TABLE_CORRECT;
    sqlDelete += " where " + ID + " = " + id;
    
    db.execSQL( sqlDelete );
    db.close( );
  }

  public void updateById( int id, String misspelled, String correct ) {
    SQLiteDatabase db = this.getWritableDatabase();
 
    String sqlUpdate = "update " + TABLE_CORRECT;
    sqlUpdate += " set " + MISSPELLED + " = '" + misspelled + "', ";
    sqlUpdate += CORRECT + " = '" + correct + "'";
    sqlUpdate += " where " + ID + " = " + id;

    db.execSQL( sqlUpdate );
    db.close( );
  }

  public LinkedList<String> selectAll( ) {

    String sqlQuery = "SELECT * FROM " + TABLE_CORRECT;
 
    SQLiteDatabase db = this.getWritableDatabase( );
    Cursor cursor = db.rawQuery( sqlQuery, null );

    LinkedList<String> words = new LinkedList<>( );

    cursor.moveToFirst();
    while( !cursor.isAfterLast()) {
      words.add( cursor.getString( 0 ) + " " +   cursor.getString( 1 ) + " " + cursor.getString( 2 ));
      cursor.moveToNext();
    }

    db.close( );
    return words;
  }
    
  public Correct selectById( int id ) {
    String sqlQuery = "select * from " + TABLE_CORRECT;
    sqlQuery += " where " + ID + " = " + id;
    
    SQLiteDatabase db = this.getWritableDatabase( );
    Cursor cursor = db.rawQuery( sqlQuery, null );

    Correct word = null;
    if( cursor.moveToFirst( ) )
      word = new Correct( Integer.parseInt( cursor.getString( 0 ) ),
		              cursor.getString( 1 ), cursor.getString( 2 ) );
    return word;
  }

  public void deleteAll(SQLiteDatabase db){
    String clearDBQuery = "DELETE FROM " + TABLE_CORRECT;
    db.execSQL(clearDBQuery);
  }
}
